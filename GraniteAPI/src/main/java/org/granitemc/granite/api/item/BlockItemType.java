package org.granitemc.granite.api.item;

import org.granitemc.granite.api.block.BlockType;

public interface BlockItemType extends ItemType {
    BlockType getBlockType();
}