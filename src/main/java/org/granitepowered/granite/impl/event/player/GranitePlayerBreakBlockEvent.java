/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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
 */

package org.granitepowered.granite.impl.event.player;

import org.granitepowered.granite.impl.event.block.GraniteBlockEvent;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.entity.living.player.PlayerBreakBlockEvent;
import org.spongepowered.api.util.Direction;

import java.util.Collection;

public class GranitePlayerBreakBlockEvent extends GraniteBlockEvent implements PlayerBreakBlockEvent {

    Player player;
    BlockSnapshot nextBlock;

    public GranitePlayerBreakBlockEvent(BlockLoc loc, Player player, BlockSnapshot nextBlock) {
        super(loc);
        this.player = player;
        this.nextBlock = nextBlock;
    }

    @Override
    public BlockSnapshot getReplacementBlock() {
        return nextBlock;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Player getHuman() {
        return null;
    }

    @Override
    public Player getLiving() {
        return null;
    }

    @Override
    public Player getEntity() {
        return player;
    }

    @Override
    public Direction getBlockFaceDirection() {
        return null;
    }

    @Override
    public double getExp() {
        return 0;
    }

    @Override
    public void setExp(double v) {

    }

    @Override
    public Collection<Item> getDroppedItems() {
        return null;
    }
}
