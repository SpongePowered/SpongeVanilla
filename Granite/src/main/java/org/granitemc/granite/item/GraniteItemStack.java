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

package org.granitemc.granite.item;

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GraniteItemStack extends Composite implements ItemStack {
    private static Map<Object, GraniteItemStack> instanceMap = new HashMap<>();

    public GraniteItemStack(Object ïtemStackInstance) {
        super(ïtemStackInstance);
    }

    public GraniteItemStack(Object itemTypeInstance, int size) {
        super(Mappings.getClass("n.m.item.ItemStack"),
                Mappings.getClass("n.m.block.Block").isInstance(itemTypeInstance)
                        ? new Class[]{Mappings.getClass("n.m.block.Block")}
                        : new Class[]{Mappings.getClass("n.m.item.Item"), int.class}, itemTypeInstance, size);
    }

    public GraniteItemStack(ItemType type) {
        this(type, type.getMaxStackSize());
    }

    public GraniteItemStack(BlockType type) {
        this(type, 64);
    }

    public GraniteItemStack(ItemType type, int size) {
        super(Mappings.getClass("n.m.item.ItemStack"), new Class[]{Mappings.getClass("n.m.item.Item"), int.class}, ((GraniteItemType) type).parent, size);

    }

    public GraniteItemStack(BlockType type, int size) {
        this(((GraniteItemStack) ((GraniteItemType) type).createItemStack(size)).parent);
        //super(Mappings.getClass("n.m.item.ItemStack"), new Class[]{Mappings.getClass("n.m.block.Block"), int.class}, ((GraniteBlockType) type).getBlockObject(), size);
    }

    public ItemType getType() {
        return (ItemType) MinecraftUtils.wrap(invoke("n.m.item.ItemStack", "getItem"));
    }

    public void clearCustomName() {
        invoke("n.m.item.ItemStack", "clearCustomName");
    }

    public int getItemDamage() {
        return (int) invoke("n.m.item.ItemStack", "getItemDamage");
    }

    public void setItemDamage(int damage) {
        invoke("n.m.item.ItemStack", "setItemDamage(int)", damage);
    }

    public String[] getItemLore() {
        Object tagCompound = invoke("n.m.item.ItemStack", "getTagCompound");

        if (tagCompound != null) {
            if ((boolean) Mappings.invoke(tagCompound, "n.m.nbt.NBTTagCompound", "hasKey(String;int)", "display", 10)) {
                Object displayCompound = Mappings.invoke(tagCompound, "n.m.nbt.NBTTagCompound", "getCompoundTag(String)", "display");

                if ((boolean) Mappings.invoke(displayCompound, "n.m.nbt.NBTTagCompound", "hasKey(String;int)", "Lore", 9)) {
                    Object loreTagList = Mappings.invoke(displayCompound, "n.m.nbt.NBTTagCompound", "getTagList(String;int)", "Lore", 8);

                    int length = (int) Mappings.invoke(loreTagList, "n.m.nbt.NBTTagList", "tagCount");

                    String[] lore = new String[length];

                    for (int i = 0; i < length; i++) {
                        String loreLine = (String) Mappings.invoke(loreTagList, "n.m.nbt.NBTTagList", "getStringTagAt(int)", i);
                        lore[i] = loreLine;
                    }

                    return lore;
                } else {
                    return new String[]{};
                }
            } else {
                return new String[]{};
            }
        } else {
            return new String[]{};
        }
    }

    public void setItemLore(String... lines) {
        try {
            Object loreList = Mappings.getClass("n.m.nbt.NBTTagList").newInstance();

            for (String line : lines) {
                Object stringTag = Mappings.getClass("n.m.nbt.NBTTagString").getConstructor(String.class).newInstance(line);
                Mappings.invoke(loreList, "n.m.nbt.NBTTagList", "appendTag(n.m.nbt.NBTBase)", stringTag);
            }

            Object tagCompound = invoke("n.m.item.ItemStack", "getTagCompound");

            if (tagCompound == null) {
                tagCompound = Mappings.getClass("n.m.nbt.NBTTagCompound").newInstance();
                invoke("n.m.item.ItemStack", "setTagCompound(n.m.nbt.NBTTagCompound)", tagCompound);
            }

            Object displayCompound;

            if (!(boolean) Mappings.invoke(tagCompound, "n.m.nbt.NBTTagCompound", "hasKey(String;int)", "display", 10)) {
                displayCompound = Mappings.getClass("n.m.nbt.NBTTagCompound").newInstance();
                Mappings.invoke(tagCompound, "n.m.nbt.NBTTagCompound", "setTag(String;n.m.nbt.NBTBase)", "display", displayCompound);
            }

            displayCompound = Mappings.invoke(tagCompound, "n.m.nbt.NBTTagCompound", "getCompoundTag(String)", "display");
            Mappings.invoke(displayCompound, "n.m.nbt.NBTTagCompound", "setTag(String;n.m.nbt.NBTBase)", "Lore", loreList);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getMaxDamage() {
        return getType().getMaxDamage();
    }

    public String getDisplayName() {
        return (String) invoke("n.m.item.ItemStack", "getDisplayName");
    }

    public void setDisplayName(String name) {
        parent = invoke("n.m.item.ItemStack", "setStackDisplayName(String)", name);
    }

    public boolean hasDisplayName() {
        return (boolean) invoke("n.m.item.ItemStack", "hasDisplayName");
    }

    public int getStackSize() {
        return (int) fieldGet("stackSize");
    }

    public void setStackSize(int amount) {
        fieldSet("n.m.item.ItemStack", "stackSize", amount);
    }

    //ItemStack constructors:
    //ItemStack(Block|Item, int stacksize, int meta|damage) amj(atr|alq, int, int)
    //ItemStack(Block|Item, int stacksize) amj(atr|alq, int)
    //ItemStack(Block|Item) amj(atr|alq)

    //ItemStack onUseItemRightClick(World w, EntityPlayer e) amj a(aqu w, ahd e)
}
