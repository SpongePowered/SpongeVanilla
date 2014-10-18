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

package org.granitemc.granite.block;

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GraniteBlockType extends Composite implements BlockType {

    public GraniteBlockType(Object parent) {
        super(parent);

        minecraftMetadataValuePossibilities = new HashMap<>();

        MethodHandle setValue = Mappings.getMethod("BlockState$StateImplementation", "setValue");
        Field values = Mappings.getField("BlockState$StateImplementation", "values");

        try {
            Map valuesMap = (Map) values.get(parent);
            for (Object v : valuesMap.keySet()) {
                String name = (String) Mappings.invoke(v, "getName");
                minecraftMetadataValuePossibilities.put(name, v);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> minecraftMetadataValuePossibilities;

    GraniteBlockType(Object parent, Map<String, Comparable> metadataValues) {
        this(parent);

        for (Map.Entry<String, Comparable> entry : metadataValues.entrySet()) {
            parent = setValue(parent, entry.getKey(), entry.getValue());
        }
    }

    private Object setValue(Object blockWithMetadata, String key, Comparable value) {
        try {
            MethodHandle setValue = Mappings.getMethod("BlockState$StateImplementation", "setValue");

            Object valueType = minecraftMetadataValuePossibilities.get(key);

            Object actualValue = value;

            if (valueType.getClass().isAssignableFrom(Mappings.getClass("BlockMetadataEnumValue"))) {
                Map map = (Map) Mappings.getField("BlockMetadataEnumValue", "valuesMap").get(valueType);
                actualValue = map.get(value);
            }

            return setValue.invoke(blockWithMetadata, valueType, actualValue);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public float getSlipperiness() {
        return (float) fieldGet("slipperiness");
    }

    public int getLightOpacity() {
        return (int) fieldGet("lightOpacity");
    }

    public int getLightValue() {
        return (int) fieldGet("lightValue");
    }

    public float getHardness() {
        return (float) fieldGet("blockHardness");
    }

    public float getBlastResistance() {
        return (float) fieldGet("blockResistance");
    }

    public boolean isOpaque() {
        return getLightOpacity() == 0;
    }

    public boolean isTransparent() {
        return !isOpaque();
    }

    public boolean canBlockGrass() {
        return (boolean) fieldGet("canBlockGrass");
    }

    public String getTechnicalName() {
        // Hacky hack
        return parent.toString().split(":")[1].split("\\[")[0].split(",")[0];
    }

    public ItemStack create(int amount) {
        return getItemStack(amount);
    }

    public ItemStack getItemStack(int amount) {
        // Super messy, it works, don't touch (pls)

        if (typeEquals(BlockTypes.air)) return null;

        Object itemStackObject = Mappings.invoke(invoke("getBlock"), "createStackedBlock", parent);

        if (Mappings.invoke(itemStackObject, "getItem") == null) {
            Object itemObject = Mappings.invoke(null, "getItemFromBlock", invoke("getBlock"));

            if (itemObject == null) {
                itemObject = Mappings.invoke(null, "getItemFromName", "minecraft:" + getTechnicalName());
            }

            if (itemObject == null) {
                itemObject = Mappings.invoke(getBlockObject(), "Block", "getItemDropped", parent, new Random(), 1);
            }

            if (itemObject == null) {
                try {
                    itemObject = Mappings.getClass("ItemBlock")
                            .getConstructor(Mappings.getClass("Block"))
                            .newInstance(getBlockObject());
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            return new GraniteItemStack(itemObject, 1);
        } else {
            GraniteItemStack graniteItemStack = (GraniteItemStack) MinecraftUtils.wrap(itemStackObject);
            graniteItemStack.setStackSize(amount);
            return graniteItemStack;
        }
    }

    public String getName() {
        return getItemStack(1).getDisplayName();
    }

    public String getUnlocalizedName() {
        return (String) fieldGet(invoke("getBlock"), "unlocalizedName");
    }

    public int getNumericId() {
        return BlockTypes.getIdFromBlock(this);
    }

    public Comparable getMetadata(String key) {
        return (Comparable) invoke("getValue", minecraftMetadataValuePossibilities.get(key));
    }

    public BlockType setMetadata(String key, Comparable value) {
        return (BlockType) MinecraftUtils.wrap(setValue(parent, key, value));
    }

    public boolean equals(BlockType that) {
        GraniteBlockType thatGbt = ((GraniteBlockType) that);
        if (!thatGbt.getUnlocalizedName().equals(thatGbt.getUnlocalizedName())) return false;
        for (String key : thatGbt.minecraftMetadataValuePossibilities.keySet()) {
            if (!getMetadata(key).equals(thatGbt.getMetadata(key))) return false;
        }
        return true;
    }

    public boolean typeEquals(BlockType that) {
        GraniteBlockType thatGbt = ((GraniteBlockType) that);
        return getUnlocalizedName().equals(thatGbt.getUnlocalizedName());
    }

    public Object getBlockObject() {
        return invoke("BlockState$StateImplementation", "getBlock");
    }
}
