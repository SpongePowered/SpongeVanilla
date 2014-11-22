package org.granitemc.granite.item;

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.item.BlockItemType;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteBlockItemType extends GraniteItemType implements BlockItemType {
    GraniteBlockType type;

    public GraniteBlockItemType(Object parent) {
        super(parent);

        Object block = Mappings.invoke(parent, "getBlock");

        type = (GraniteBlockType) MinecraftUtils.wrap(Mappings.invoke(block, "getDefaultState"));
    }

    @Override
    public BlockType getBlockType() {
        return type;
    }

    @Override
    public String getName() throws IllegalAccessException, InstantiationException {
        return getBlockType().getName();
    }

    // Should only be used by GraniteItemStack.getType() - can't be in the constructor because that would fuck with composite caching
    void setMeta(int meta) {
        Object block = Mappings.invoke(parent, "getBlock");

        type = (GraniteBlockType) MinecraftUtils.wrap(Mappings.invoke(block, "getStateFromMeta", meta));
    }
}
