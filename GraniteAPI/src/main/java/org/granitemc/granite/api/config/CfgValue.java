package org.granitemc.granite.api.config;

import com.typesafe.config.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CfgValue {
    static Class<?> originClass;
    static Class<?> originTypeClass;
    static Class<?> configListClass;
    static Class<?> configObjectClass;

    static Constructor<?> configOriginConstructor;
    static Constructor<?> configListConstructor;
    static Constructor<?> configObjectConstructor;

    static {
        try {
            Class<?> originInterfaceClass = Class.forName("com.typesafe.config.ConfigOrigin");
            originClass = Class.forName("com.typesafe.config.impl.SimpleConfigOrigin");
            originTypeClass = Class.forName("com.typesafe.config.impl.OriginType");
            configListClass = Class.forName("com.typesafe.config.impl.SimpleConfigList");
            configObjectClass = Class.forName("com.typesafe.config.impl.SimpleConfigObject");

            configOriginConstructor = originClass.getDeclaredConstructor(
                    String.class, int.class, int.class, originTypeClass, String.class, List.class);
            configListConstructor = configListClass.getDeclaredConstructor(originInterfaceClass, List.class);
            configObjectConstructor = configObjectClass.getDeclaredConstructor(originInterfaceClass, Map.class);

            configOriginConstructor.setAccessible(true);
            configListConstructor.setAccessible(true);
            configObjectConstructor.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    List<String> comments;

    public abstract Object unwrap();

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    private static CfgValue fromHoconValue(ConfigValue val) {
        CfgValue value;
        switch (val.valueType()) {
            case OBJECT:
                value = new CfgObject();
                for (Map.Entry<String, ConfigValue> entry : ((ConfigObject) val).entrySet()) {
                    ((CfgObject) value).putSegment(entry.getKey(), fromHoconValue(entry.getValue()));
                }
                break;
            case LIST:
                value = new CfgList();
                for (ConfigValue value1 : (ConfigList) val) {
                    ((CfgList) value).add(fromHoconValue(value1));
                }
                break;
            case NULL:
                value = CfgNull.instance;
                break;
            default:
                value = new CfgPrimitive((Serializable) val.unwrapped());
        }

        value.comments = new ArrayList<>(val.origin().comments());

        for (int i = 0; i < value.comments.size(); i++) {
            value.comments.set(i, value.comments.get(i).trim());
        }

        return value;
    }

    private static ConfigValue toHoconValue(CfgValue val) {
        ConfigValue value = null;
        try {
            ConfigOrigin origin = (ConfigOrigin) configOriginConstructor
                    .newInstance("", -1, -1, originTypeClass.getEnumConstants()[0], null, val.getComments());


            if (val instanceof CfgList) {
                List<ConfigValue> raw = new ArrayList<>();

                for (CfgValue value1 : (CfgList) val) {
                    raw.add(toHoconValue(value1));
                }

                value = (ConfigValue) configListConstructor.newInstance(origin, raw);
            } else if (val instanceof CfgObject) {
                Map<String, ConfigValue> raw = new HashMap<>();

                Map<String, CfgValue> merged = new HashMap<>();
                merged.putAll(deepMerge(((CfgObject) val).defaults, ((CfgObject) val).value));
                /*if (((CfgObject) val).defaults != null) {
                    merged.putAll(((CfgObject) val).defaults);
                }

                merged.putAll(((CfgObject) val).value)*/
                for (Map.Entry<String, CfgValue> entry : merged.entrySet()) {
                    raw.put(entry.getKey(), toHoconValue(entry.getValue()));
                }

                value = (ConfigValue) configObjectConstructor.newInstance(origin, raw);
            } else if (val instanceof CfgPrimitive) {
                value = ConfigValueFactory.fromAnyRef(val.unwrap(), "");
            } else if (val instanceof CfgNull) {
                value = ConfigValueFactory.fromAnyRef(null);
            }

        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static CfgValue fromObject(Object obj) {
        CfgValue cfgValue;
        if (obj instanceof Map) {
            cfgValue = new CfgObject((Map<String, CfgValue>) obj);
        } else if (obj instanceof List) {
            cfgValue = new CfgList((List<CfgValue>) obj);
        } else {
            cfgValue = new CfgPrimitive((Serializable) obj);
        }

        return cfgValue;
    }

    public static CfgValue read(Reader reader) {
        ConfigObject root = ConfigFactory.parseReader(reader).root();

        return fromHoconValue(root);
    }

    public static CfgValue read(InputStream stream) {
        return read(new InputStreamReader(stream));
    }

    public static CfgValue read(String string) {
        return read(new StringReader(string));
    }

    public static CfgValue read(File file) throws FileNotFoundException {
        return read(new FileReader(file));
    }

    public static CfgValue read(ClassLoader loader, String resource) {
        return read(loader.getResourceAsStream(resource));
    }

    public String writeToString() {
        return toHoconValue(this).render(ConfigRenderOptions.defaults().setOriginComments(false).setJson(false).setFormatted(true).setComments(true));
    }

    public void write(File file) throws IOException {
        FileWriter f = new FileWriter(file);
        write(f);
        f.close();
    }

    public void write(Writer writer) throws IOException {
        writer.write(writeToString());
    }

    public abstract CfgValueType getType();

    protected static Map<String, CfgValue> deepMerge(Map<String, CfgValue> a, Map<String, CfgValue> b) {
        Map<String, CfgValue> res = new HashMap<>();

        if (a != null) {
            res.putAll(a);
        }

        if (b != null) {
            for (Map.Entry<String, CfgValue> e : b.entrySet()) {
                if (res.get(e.getKey()) instanceof CfgObject && e.getValue() instanceof CfgObject) {
                    res.put(e.getKey(), new CfgObject(deepMerge(((CfgObject) res.get(e.getKey())), ((CfgObject) e.getValue()))));
                } else {
                    res.put(e.getKey(), e.getValue());
                }
            }
        }

        return res;
    }
}
