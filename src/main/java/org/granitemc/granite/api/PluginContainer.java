package org.granitemc.granite.api;

import org.granitemc.granite.api.events.LoadEvent;
import org.granitemc.granite.api.events.PreloadEvent;
import org.granitemc.granite.utils.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PluginContainer {

    private String name;
    private String id;
    private String version;
    private Class<?> setupClass;
    private Class<?> pluginClass;

    public void loadFromPlugin(Class<?> pluginClass) {
        Plugin classPlugin = GraniteAPI.instance().getClassPlugin(pluginClass);

        setName(classPlugin.name());
        setID(classPlugin.id());
        setVersion(classPlugin.version());
        setSetupClass(classPlugin.setupClass());
        setPluginClass(pluginClass);
    }

    private void setName(String name2) {
        name = name2;
    }

    private void setID(String id2) {
        id = id2;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }


    private void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the setupClass
     */
    public Class<?> getSetupClass() {
        return setupClass;
    }

    private void setSetupClass(Class<?> setupClass) {
        this.setupClass = setupClass;
    }

    public void setup() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        if (getSetupClass() != null) {
            runSetupFunctions(getSetupClass());
        }
        GraniteAPI.eventBus().register(pluginClass);
        GraniteAPI.eventBus().post(new PreloadEvent(this));
        GraniteAPI.eventBus().post(new LoadEvent(this));

    }

    private void runSetupFunctions(Class<?> setupClass2) {
        List<Method> methods = Arrays.asList(setupClass2.getDeclaredMethods());
        List<Method> setupfuncs = new ArrayList<Method>();
        for (Method method : methods) {
            if (method.getAnnotation(SetupMethod.class) != null) {
                setupfuncs.add(method);
            }
        }

        for (Method method : setupfuncs) {
            Class<?>[] params = method.getParameterTypes();
            if (!Arrays.deepEquals(params, new Object[]{GraniteAPI.class})) {
                Logger.info("Found method %s that does not use a single GraniteAPI parameter, instead it uses %s.", method, Arrays.deepToString(params));
                Logger.info("Skipping method %s.", method);
            } else {
                try {
                    method.invoke(setupClass2.newInstance(), GraniteAPI.instance());
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @return the pluginClass
     */
    public Class<?> getPluginClass() {
        return pluginClass;
    }

    /**
     * @param pluginClass the pluginClass to set
     */
    private void setPluginClass(Class<?> pluginClass) {
        this.pluginClass = pluginClass;
    }
}
