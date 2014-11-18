package org.granitemc.granite.api.event.player;

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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.event.Event;

public class EventPlayerInteract extends Event {
    public static enum InteractType {
        LEFT_CLICK_AIR,
        LEFT_CLICK_BLOCK,

        /**
         * Stepping onto or into a block, for example:
         * - Jumping on soil
         * - Stepping on a pressure plate
         * - Triggering redstone ore
         * - Triggering tripwire
         */
        PHYSICAL,

        RIGHT_CLICK_AIR,
        RIGHT_CLICK_BLOCK
    }

    private Player player;
    private InteractType interactType;
    private Block block;

    public EventPlayerInteract(Player player, InteractType interactType, Block block) {
        this.player = player;
        this.interactType = interactType;
        this.block = block;
    }

    /**
     * Returns the player that interacted
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the type of interaction event this is
     */
    public InteractType getInteractType() {
        return interactType;
    }

    /**
     * Returns the block that was interacted with, or null for *AIR types
     */
    public Block getBlock() {
        return block;
    }
}
