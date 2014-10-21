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

package org.granitemc.granite.api.event.inventory;

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;

public class EventInventoryHotbarMove extends EventInventoryClick {
    int hotbarSlot = 0;

    public EventInventoryHotbarMove(Player player, ItemStack stack, int slot, int hotbarSlot) {
        super(player, stack, slot, Action.HOTBAR_KEY, MouseButton.NONE);
        this.hotbarSlot = hotbarSlot;
    }

    public int getHotbarSlot() {
        return hotbarSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EventInventoryHotbarMove that = (EventInventoryHotbarMove) o;

        if (hotbarSlot != that.hotbarSlot) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + hotbarSlot;
        return result;
    }

    @Override
    public String toString() {
        return "EventInventoryHotbarMove{" +
                "hotbarSlot=" + hotbarSlot +
                '}';
    }
}
