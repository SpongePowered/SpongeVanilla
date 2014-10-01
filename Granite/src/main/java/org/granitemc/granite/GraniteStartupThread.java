package org.granitemc.granite;

/*****************************************************************************************
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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

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
import org.granitemc.granite.utils.Config;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;

public class GraniteStartupThread extends Thread {

    private String[] args;

    public GraniteStartupThread(String[] args) {
        setName("Granite Startup");
        this.args = args;
    }

    public void run() {
        GraniteAPI.init();

        Granite.getLogger().info("Starting Granite version @VERSION@");

        Config.initDirs();

        loadMinecraft();

        loadPlugins();

        // Edit stuff before the classes are loaded
        BytecodeModifier.modify();

        // Load mappings AFTER editing is done, otherwise it'll break
        Mappings.load();

        Mappings.invoke(null, "n.m.init.Bootstrap", "func_151354_b");
        loadBlocks();
        loadItems();

        GraniteServerComposite.init();
    }

    private void loadPlugins() {
        for (File plugin : Config.pluginsFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File arg0, String arg1) {
                return arg1.endsWith(".jar");
            }
        })) {
            Granite.getLogger().info("Loading jarfile plugins/%s.", plugin.getName());
            Granite.loadPluginFromJar(plugin);
        }
    }

    public void loadMinecraft() {
        Granite.getLogger().info("Loading jar from %s into classpath.", Config.mcJar.getAbsolutePath());

        if (!Config.mcJar.exists()) {
            throw new RuntimeException("Could not locate minecraft_server.jar. Is your jar named minecraft_server.1.8.jar?");
        }

        try {
            ClassLoader.addURL(Config.mcJar.toURI().toURL());
            Granite.getLogger().info("Loaded server: " + Config.mcJar.getAbsolutePath());
        } catch (IOException e) {
            Granite.getLogger().error("Failed to load minecraft_server.jar. Please make sure it exists in the same directory.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadBlocks() {
        Class<?> blockClass = Mappings.getClass("n.m.block.Block");

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
                int id = (int) Mappings.invoke(null, "n.m.block.Block", "getIdFromBlock(n.m.block.Block)", block);
                nameMap.put(name, type);
                idMap.put(id, type);

                BlockTypes.class.getDeclaredField(name).set(null, type);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void loadItems() {
        Class<?> itemClass = Mappings.getClass("n.m.item.Item");

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
                String fullName = (String) Mappings.invoke(registry, "n.m.util.RegistryNamespaced", "getNameForObject(Object)", item).toString();
                String name = fullName.split(":")[1];

                GraniteItemType type = (GraniteItemType) MinecraftUtils.wrap(item);

                int id = (int) Mappings.invoke(null, "n.m.item.Item", "getIdFromItem(n.m.item.Item)", item);
                nameMap.put(name, type);
                idMap.put(id, type);

                try {
                    ItemTypes.class.getDeclaredField(name).set(null, type);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                }
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
