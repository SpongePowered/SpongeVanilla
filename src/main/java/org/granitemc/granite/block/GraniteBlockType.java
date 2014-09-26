package org.granitemc.granite.block;

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.chat.GraniteChatComponentText;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * **************************************************************************************
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
 * **************************************************************************************
 */

public class GraniteBlockType extends Composite implements BlockType {
    public GraniteBlockType(Object parent) {
        super(parent);

        minecraftMetadataValuePossibilities = new HashMap<>();

        Method setValue = Mappings.getMethod("n.m.block.BlockWithMetadata", "setValue(n.m.block.BlockMetadataValue;Comparable)");
        Field values = Mappings.getField("n.m.block.BlockWithMetadata", "values");

        try {
            Map valuesMap = (Map) values.get(parent);
            for (Object v : valuesMap.keySet()) {
                String name = (String) Mappings.invoke(v, "n.m.block.BlockMetadataValue", "getName()");
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
            Method setValue = Mappings.getMethod("n.m.block.BlockWithMetadata", "setValue(n.m.block.BlockMetadataValue;Comparable)");
            setValue.setAccessible(true);

            Object valueType = minecraftMetadataValuePossibilities.get(key);

            Object actualValue = value;

            if (valueType.getClass().isAssignableFrom(Mappings.getClass("n.m.block.BlockMetadataEnumValue"))) {
                Map map = (Map) Mappings.getField("n.m.block.BlockMetadataEnumValue", "valuesMap").get(valueType);
                actualValue = map.get(value);
            }

            return setValue.invoke(blockWithMetadata, valueType, actualValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public float getSlipperiness() {
        return (float) fieldGet("slipperiness");
    }

    @Override
    public int getLightOpacity() {
        return (int) fieldGet("lightOpacity");
    }

    @Override
    public int getLightValue() {
        return (int) fieldGet("lightValue");
    }

    @Override
    public float getHardness() {
        return (float) fieldGet("blockHardness");
    }

    @Override
    public float getBlastResistance() {
        return (float) fieldGet("blockResistance");
    }

    @Override
    public boolean isOpaque() {
        return getLightOpacity() == 0;
    }

    @Override
    public boolean isTransparent() {
        return !isOpaque();
    }

    @Override
    public boolean canBlockGrass() {
        return (boolean) fieldGet("canBlockGrass");
    }

    @Override
    public String getTechnicalName() {
        // Hacky hack
        return parent.toString().split(":")[1].split("\\[")[0].split(",")[0];
    }

    @Override
    public String getName() {
        Object blockType = Mappings.invoke(invoke("getBlock"), "n.m.block.Block", "createStackedBlock(n.m.block.IBlockWithMetadata)", parent);
        return (String) Mappings.invoke(blockType, "n.m.item.ItemStack", "getName()");
    }

    public String getUnlocalizedName() {
        return (String) fieldGet(invoke("getBlock"), Mappings.getClass("n.m.block.Block"), "unlocalizedName");
    }

    @Override
    public int getBlockId() {
        return BlockTypes.getIdFromBlock(this);
    }

    @Override
    public Comparable getMetadata(String key) {
        return (Comparable) invoke("getValue(n.m.block.BlockMetadataValue)", minecraftMetadataValuePossibilities.get(key));
    }

    @Override
    public BlockType setMetadata(String key, Comparable value) {
        return new GraniteBlockType(setValue(parent, key, value));
    }

    @Override
    public boolean equals(BlockType that) {
        GraniteBlockType thatGbt = ((GraniteBlockType) that);
        if (!thatGbt.getUnlocalizedName().equals(thatGbt.getUnlocalizedName())) return false;
        for (String key : thatGbt.minecraftMetadataValuePossibilities.keySet()) {
            if (!getMetadata(key).equals(thatGbt.getMetadata(key))) return false;
        }
        return true;
    }

    @Override
    public boolean typeEquals(BlockType that) {
        GraniteBlockType thatGbt = ((GraniteBlockType) that);
        return getUnlocalizedName().equals(thatGbt.getUnlocalizedName());
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    // TODO: wtf is this?
    @Override
    public int getMaxDamage() {
        return 0;
    }

    public Object getBlockObject() {
        return invoke("n.m.block.BlockWithMetadata", "getBlock");
    }
}
