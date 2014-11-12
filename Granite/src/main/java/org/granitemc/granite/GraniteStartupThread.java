/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.block.ItemTypes;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.item.GraniteItemType;
import org.granitemc.granite.reflect.BytecodeModifier;
import org.granitemc.granite.reflect.GraniteServerComposite;
import org.granitemc.granite.utils.ClassLoader;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Properties;

public class GraniteStartupThread extends Thread {

    private String[] args;

    public GraniteStartupThread(String[] args) {
        setName("Granite Startup");
        this.args = args;
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

        GraniteAPI.init();

        Granite.getLogger().info("Loading Minecraft .jar");

        loadMinecraft();

        Granite.getLogger().info("Loading libraries");

        loadLibraries();

        Granite.getLogger().info("Loading plugins");

        loadPlugins();

        Granite.getLogger().info("Applying bytecode modifications ");

        // Edit stuff before the classes are loaded
        BytecodeModifier.modify();

        Granite.getLogger().info("Bootstrapping Minecraft");

        try {
            Class.forName("od").getMethod("c").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Mappings.invoke(null, "Bootstrap", "func_151354_b");

        // Load mappings AFTER editing is done, otherwise it'll break

        Granite.getLogger().info("Loading mappings");
        Mappings.load();

        Granite.getLogger().info("Loading blocks and items");

        loadBlocks();
        loadItems();

        Granite.getLogger().info("Starting server");

        GraniteServerComposite.init();
    }

    private void loadLibraries() {
        File[] files = GraniteAPI.instance.getServerConfig().getLibrariesDirectory().listFiles(new FilenameFilter() {
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
        }
    }

    private void loadPlugins() {
        File[] files = GraniteAPI.instance.getServerConfig().getPluginDirectory().listFiles(new FilenameFilter() {
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
        }
    }

    public void loadMinecraft() {
        File mcJar = GraniteAPI.instance.getServerConfig().getMinecraftJar();
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
        }
    }

    public void loadBlocks() {
        Class<?> blockClass = Mappings.getClass("Block");

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
        }
    }

    public void loadItems() {
        Class<?> itemClass = Mappings.getClass("Item");

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
        }
    }
}
