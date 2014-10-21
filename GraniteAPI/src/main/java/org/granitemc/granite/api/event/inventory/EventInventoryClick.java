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
import org.granitemc.granite.api.event.Event;
import org.granitemc.granite.api.item.ItemStack;

public class EventInventoryClick extends Event {
    public static enum Action {
        PICK_UP,
        PUT_DOWN,
        CLICK_BLANK,
        COLLECT_STACK,
        SPLIT_STACK,
        HOTBAR_KEY,
        QUICK_MOVE,
        DROP_ITEM,
        DROP_STACK,
        CLICK_OUTSIDE,
        START_DIVIDE_DRAG,
        START_PLACE_DRAG,
        PAINT_DIVIDE_DRAG,
        PAINT_PLACE_DRAG,
        END_DIVIDE_DRAG,
        END_PLACE_DRAG,
        CREATIVE_NEW_FULL_STACK,
        NONE
    }

    public static enum MouseButton {
        LEFT_MOUSE,
        RIGHT_MOUSE,
        MIDDLE_MOUSE,
        NONE
    }

    private Player player;
    private ItemStack stack;
    private int slot;
    private Action action;
    private MouseButton button;

    private ItemStack itemInSlot;
    private ItemStack itemInHand;

    public EventInventoryClick(Player player, ItemStack stack, int slot, Action action, MouseButton button) {
        this.player = player;
        this.stack = stack;
        this.slot = slot;
        this.action = action;
        this.button = button;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getRelatedStack() {
        return stack;
    }

    public int getSlot() {
        return slot;
    }

    public Action getAction() {
        return action;
    }

    public MouseButton getButton() {
        return button;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventInventoryClick that = (EventInventoryClick) o;

        if (slot != that.slot) return false;
        if (action != that.action) return false;
        if (button != that.button) return false;
        if (itemInHand != null ? !itemInHand.equals(that.itemInHand) : that.itemInHand != null) return false;
        if (itemInSlot != null ? !itemInSlot.equals(that.itemInSlot) : that.itemInSlot != null) return false;
        if (!player.equals(that.player)) return false;
        if (stack != null ? !stack.equals(that.stack) : that.stack != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        result = 31 * result + (stack != null ? stack.hashCode() : 0);
        result = 31 * result + slot;
        result = 31 * result + action.hashCode();
        result = 31 * result + button.hashCode();
        result = 31 * result + (itemInSlot != null ? itemInSlot.hashCode() : 0);
        result = 31 * result + (itemInHand != null ? itemInHand.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventInventoryClick{" +
                "player=" + player +
                ", stack=" + stack +
                ", slot=" + slot +
                ", action=" + action +
                ", button=" + button +
                ", itemInSlot=" + itemInSlot +
                ", itemInHand=" + itemInHand +
                '}';
    }

    public ItemStack getItemStackInSlot() {
        return itemInSlot;
    }

    public void setItemStackInSlot(ItemStack itemInSlot) {
        assertCanModify();
        this.itemInSlot = itemInSlot;
    }

    public ItemStack getItemStackInHand() {
        return itemInHand;
    }

    public void setItemStackInHand(ItemStack itemInHand) {
        assertCanModify();
        this.itemInHand = itemInHand;
    }
}
