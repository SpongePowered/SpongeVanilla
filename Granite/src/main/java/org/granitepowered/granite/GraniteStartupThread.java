package org.granitepowered.granite;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Created by Ash on 02/12/2014.
 */
public class GraniteStartupThread extends Thread {

    String[] args;

    public GraniteStartupThread(String args[]) {
        this.args = args;
        this.setName("Granite Startup");
    }

    public void run() {
        String version = null;

        Properties mavenProp = new Properties();
        InputStream in = java.lang.ClassLoader.getSystemClassLoader().getResourceAsStream("META-INF/maven/org.granitemc/granite/pom.properties");
        if (in != null) {
            try {
                mavenProp.load(in);

                version = mavenProp.getProperty("version");
            } catch (IOException ignored) {
            } finally {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }

        if (version == null) version = "UNKNOWN";

        System.out.println("Starting Granite version " + version);

        System.out.println("Initializing API and logger");

        // GraniteAPI.init();

        //Granite.getLogger().info("Loading Minecraft .jar");

        loadMinecraft();

        //Granite.getLogger().info("Loading libraries");

        loadLibraries();

        //Granite.getLogger().info("Loading plugins");

        loadPlugins();

        //Granite.getLogger().info("Applying bytecode modifications ");

        // Edit stuff before the classes are loaded
        //BytecodeModifier.modify();

        //Granite.getLogger().info("Bootstrapping Minecraft");

        try {
            // Hardcoding bootstrapping here again, this also has to load before the mappings are loaded
            Class.forName("oe").getMethod("c").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Mappings.invoke(null, "Bootstrap", "func_151354_b");

        // Load mappings AFTER editing is done, otherwise it'll break

        //Granite.getLogger().info("Loading mappings");
        //Mappings.load();

        //Granite.getLogger().info("Loading blocks and items");

        loadBlocks();
        loadItems();

        //Granite.getLogger().info("Starting server");

        //GraniteServerComposite.init();
    }

    private void loadLibraries() {
        /*File[] files = GraniteAPI.instance.getServerConfig().getLibrariesDirectory().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        });

        if (files != null) {
            for (File lib : files) {
                Granite.getLogger().info("Loading jarfile lib/%s", lib.getName());
                try {
                    ClassLoader.addFile(lib);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    private void loadPlugins() {
        /*File[] files = GraniteAPI.instance.getServerConfig().getPluginDirectory().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        });

        if (files != null) {
            for (File plugin : files) {
                Granite.getLogger().info("Loading jarfile plugins/%s", plugin.getName());
                Granite.loadPluginFromJar(plugin);
            }
        }*/
    }

    public void loadMinecraft() {
        /*File mcJar = GraniteAPI.instance.getServerConfig().getMinecraftJar();
        Granite.getLogger().info("Loading jar from %s into classpath", mcJar.getAbsolutePath());

        if (!mcJar.exists()) {
            throw new RuntimeException("Could not locate minecraft_server.jar. Is your jar named minecraft_server.1.8.jar?");
        }

        try {
            ClassLoader.addURL(mcJar.toURI().toURL());
            Granite.getLogger().info("Loaded server: " + mcJar.getAbsolutePath());
        } catch (IOException e) {
            Granite.getLogger().error("Failed to read minecraft_server.jar. Please make sure it exists in the same directory.");
            e.printStackTrace();
            System.exit(1);
        }*/
    }

    public void loadBlocks() {
        /*Class<?> blockClass = Mappings.getClass("Block");

        try {
            Field nameField = Mappings.getField(blockClass, "blockWithMetadata");
            nameField.setAccessible(true);

            Field nameMapField = BlockTypes.class.getDeclaredField("nameMap");
            nameMapField.setAccessible(true);
            BiMap<String, BlockType> nameMap = HashBiMap.create();
            nameMapField.set(null, nameMap);

            Field idMapField = BlockTypes.class.getDeclaredField("idMap");
            idMapField.setAccessible(true);
            BiMap<Integer, BlockType> idMap = HashBiMap.create();
            idMapField.set(null, idMap);

            for (Object block : (Iterable) Mappings.getField(blockClass, "blockRegistry").get(null)) {
                String fullName = nameField.get(block).toString();
                String name = fullName.split(":")[1].split("\\[")[0].split(",")[0];

                Object metadata = Mappings.getField(blockClass, "blockMetadata").get(block);
                Collection variants = (Collection) Mappings.getField(metadata.getClass(), "variants").get(metadata);

                GraniteBlockType type = (GraniteBlockType) MinecraftUtils.wrap(variants.iterator().next());
                int id = (int) Mappings.invokeStatic("Block", "getIdFromBlock", block);
                nameMap.put(name, type);
                idMap.put(id, type);

                BlockTypes.class.getDeclaredField(name).set(null, type);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }*/
    }

    public void loadItems() {
        /*Class<?> itemClass = Mappings.getClass("Item");

        try {
            Field nameMapField = ItemTypes.class.getDeclaredField("nameMap");
            nameMapField.setAccessible(true);
            BiMap<String, ItemType> nameMap = HashBiMap.create();
            nameMapField.set(null, nameMap);

            Field idMapField = ItemTypes.class.getDeclaredField("idMap");
            idMapField.setAccessible(true);
            BiMap<Integer, ItemType> idMap = HashBiMap.create();
            idMapField.set(null, idMap);

            Object registry = Mappings.getField(itemClass, "itemRegistry").get(null);
            for (Object item : (Iterable) Mappings.getField(itemClass, "itemRegistry").get(null)) {
                String fullName = Mappings.invoke(registry, "getNameForObject", item).toString();
                String name = fullName.split(":")[1];

                GraniteItemType type = (GraniteItemType) MinecraftUtils.wrap(item);

                int id = (int) Mappings.invokeStatic("Item", "getIdFromItem", item);
                nameMap.put(name, type);
                idMap.put(id, type);

                try {
                    ItemTypes.class.getDeclaredField(name).set(null, type);
                } catch (IllegalAccessException | NoSuchFieldException ignored) {
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }*/
    }


}
