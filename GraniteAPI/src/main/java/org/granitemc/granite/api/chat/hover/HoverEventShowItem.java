package org.granitemc.granite.api.chat.hover;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.chat.HoverEvent;
import org.granitemc.granite.api.item.ItemStack;

public class HoverEventShowItem extends HoverEvent {
    ItemStack itemStack;

    public HoverEventShowItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    protected String getAction() {
        return "show_item";
    }

    @Override
    public Object getValue() {
        return Granite.getAPIHelper().getItemNBTCompoundString(itemStack);
    }
}
