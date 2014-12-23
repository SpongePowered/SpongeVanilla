package org.granitepowered.granite.impl.event.player;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.event.block.GraniteBlockEvent;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.player.PlayerBreakBlockEvent;

public class GranitePlayerBreakBlockEvent extends GraniteBlockEvent implements PlayerBreakBlockEvent {
    Player player;

    public GranitePlayerBreakBlockEvent(BlockLoc loc, Player player) {
        super(loc);
        this.player = player;
    }

    @Override
    public BlockSnapshot getReplacementBlock() {
        // TODO: Block Snapshot stuff
        throw new NotImplementedException("");
    }

    @Override
    public int getExp() {
        throw new NotImplementedException("");
    }

    @Override
    public void setExp(int exp) {
        throw new NotImplementedException("");
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Entity getEntity() {
        return player;
    }
}
