package org.granitemc.granite.item;

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

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.item.ItemType;
import org.granitemc.granite.api.item.ItemTypes;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.granitemc.granite.api.nbt.meta.BookMeta;
import org.granitemc.granite.api.nbt.meta.ItemMeta;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteItemStack extends Composite implements ItemStack {

    public GraniteItemStack(Object itemStackInstance) {
        super(itemStackInstance);
    }

    public GraniteItemStack(Object itemTypeInstance, int size) {
        super(Mappings.getClass("ItemStack"),
                Mappings.getClass("Block").isInstance(itemTypeInstance)
                        ? new Class[]{Mappings.getClass("Block")}
                        : new Class[]{Mappings.getClass("Item"), int.class}, itemTypeInstance, size);
    }

    private ItemMeta itemMeta;
    private BookMeta bookMeta;

    public GraniteItemStack(ItemType type) throws InstantiationException, IllegalAccessException {
        this(type, type.getMaxStackSize());
    }

    public GraniteItemStack(BlockType type) {
        this(type, 64);
    }

    public GraniteItemStack(ItemType type, int size) throws IllegalAccessException, InstantiationException {
        super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Item"), int.class}, ((GraniteItemType) type).parent, size);
    }

    public GraniteItemStack(BlockType type, int size) {
        this(((GraniteItemStack) (type).create(size)).parent);
        //super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Block"), int.class}, ((GraniteBlockType) type).getBlockObject(), size);
    }

    public ItemType getType() {
        return (ItemType) MinecraftUtils.wrap(invoke("getItem"));
    }

    public int getItemDamage() {
        return (int) invoke("getItemDamage");
    }

    public void setItemDamage(int damage) {
        invoke("setItemDamage", damage);
    }

    public int getMaxDamage() {
        return getType().getMaxDamage();
    }

    public int getStackSize() {
        return (int) fieldGet("stackSize");
    }

    public void setStackSize(int amount) {
        fieldSet("stackSize", amount);
    }

    public NBTCompound getNBTCompound() throws IllegalAccessException, InstantiationException {
        try {
            return MinecraftUtils.fromMinecraftNBTCompound(invoke("getTagCompound"));
        } catch (Exception ignored) {
            NBTCompound nbtCompound = new NBTCompound();
            this.setNBTCompound(nbtCompound);
            return nbtCompound;
        }
    }

    public void setNBTCompound(NBTCompound NBTCompound) throws InstantiationException, IllegalAccessException {
        invoke("setTagCompound", MinecraftUtils.toMinecraftNBTCompound(NBTCompound));
    }

    public ItemMeta getMetadata() {
        if (this.getType().equals(ItemTypes.written_book) || this.getType().equals(ItemTypes.writable_book)) {
            if (bookMeta == null) {
                bookMeta = new BookMeta(this);
            }
            return bookMeta;
        } else {
            if (itemMeta == null) {
                itemMeta = new ItemMeta(this);
            }
            return itemMeta;
        }
    }

}
