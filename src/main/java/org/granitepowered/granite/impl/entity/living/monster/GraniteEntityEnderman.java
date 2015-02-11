package org.granitepowered.granite.impl.entity.living.monster;

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.mc.MCEntityEnderman;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.monster.Enderman;

public class GraniteEntityEnderman extends GraniteEntityMonster<MCEntityEnderman> implements Enderman {

    public GraniteEntityEnderman(MCEntityEnderman obj) {
        super(obj);
    }

    @Override
    public Optional<BlockState> getCarriedBlock() {
        GraniteBlockType blockType = (GraniteBlockType) Granite.instance.getGameRegistry().blockTypes.get("air");
        return Optional.fromNullable((BlockState) MinecraftUtils.wrap(blockType.obj.getStateFromId((short) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 65535)));
    }

    @Override
    public void setCarriedBlock(BlockState carriedBlock) {
        GraniteBlockType blockType = (GraniteBlockType) Granite.instance.getGameRegistry().blockTypes.get("air");
        obj.fieldGet$dataWatcher().updateObject(16, (short) (blockType.obj.getStateId(MinecraftUtils.unwrap((GraniteBlockState) carriedBlock).fieldGet$block().fieldGet$defaultBlockState()) & 65535));

    }

    @Override
    public boolean isScreaming() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(18).fieldGet$watchedObject() > 0;
    }

    @Override
    public void setScreaming(boolean screaming) {
        obj.fieldGet$dataWatcher().updateObject(18, (byte) (screaming ? 1 : 0));
    }
}
