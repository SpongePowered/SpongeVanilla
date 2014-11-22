package org.granitemc.granite.api.item.meta;

import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.nbt.NBTCompound;

public class ItemMeta {
    private ItemStack itemStack;

    private ItemMetaDisplayComponent displayComponent;
    private ItemMetaEnchantmentsComponent enchantmentsComponent;
    private ItemMetaBookComponent bookComponent;

    public ItemMeta(ItemStack itemStack) {
        this.itemStack = itemStack;

        displayComponent = new ItemMetaDisplayComponent(this);
        if (!displayComponent.appliesTo(itemStack)) displayComponent = null;

        enchantmentsComponent = new ItemMetaEnchantmentsComponent(this);
        if (!enchantmentsComponent.appliesTo(itemStack)) enchantmentsComponent = null;

        bookComponent = new ItemMetaBookComponent(this);
        if (!bookComponent.appliesTo(itemStack)) bookComponent = null;

        load();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemMetaDisplayComponent getDisplayComponent() {
        return displayComponent;
    }

    public ItemMetaEnchantmentsComponent getEnchantmentsComponent() {
        return enchantmentsComponent;
    }

    public ItemMetaBookComponent getBookComponent() {
        return bookComponent;
    }

    public void save() {
        NBTCompound nbt = itemStack.getNBTCompound();

        if (displayComponent != null) getDisplayComponent().save(nbt);
        if (enchantmentsComponent != null) getEnchantmentsComponent().save(nbt);
        if (bookComponent != null) getBookComponent().save(nbt);

        itemStack.setNBTCompound(nbt);
    }

    public void load() {
        NBTCompound nbt = itemStack.getNBTCompound();

        if (displayComponent != null) getDisplayComponent().load(nbt);
        if (enchantmentsComponent != null) getEnchantmentsComponent().load(nbt);
        if (bookComponent != null) getBookComponent().load(nbt);
    }
}
