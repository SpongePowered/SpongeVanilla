package org.granitemc.granite.api.item.meta;

import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.item.ItemType;
import org.granitemc.granite.api.nbt.NBTCompound;

public abstract class ItemMetaComponent {
    private ItemMeta meta;

    public ItemMetaComponent(ItemMeta meta) {
        this.meta = meta;
    }

    public ItemStack getItemStack() {
        return meta.getItemStack();
    }

    public ItemMeta getMeta() {
        return meta;
    }

    public void save() {
        NBTCompound nbt = getItemStack().getNBTCompound();

        save(nbt);

        getItemStack().setNBTCompound(nbt);
    }

    public void load() {
        NBTCompound nbt = getItemStack().getNBTCompound();

        load(nbt);
    }

    public abstract void save(NBTCompound nbt);

    public abstract void load(NBTCompound nbt);

    public abstract boolean appliesTo(ItemStack itemStack);
}
