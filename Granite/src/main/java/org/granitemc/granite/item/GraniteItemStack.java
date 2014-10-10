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
        super(Mappings.getClass("ItemStack"),
                Mappings.getClass("Block").isInstance(itemTypeInstance)
                        ? new Class[]{Mappings.getClass("Block")}
                        : new Class[]{Mappings.getClass("Item"), int.class}, itemTypeInstance, size);
    }

    public GraniteItemStack(ItemType type) {
        this(type, type.getMaxStackSize());
    }

    public GraniteItemStack(BlockType type) {
        this(type, 64);
    }

    public GraniteItemStack(ItemType type, int size) {
        super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Item"), int.class}, ((GraniteItemType) type).parent, size);

    }

    public GraniteItemStack(BlockType type, int size) {
        this(((GraniteItemStack) ((GraniteItemType) type).create(size)).parent);
        //super(Mappings.getClass("ItemStack"), new Class[]{Mappings.getClass("Block"), int.class}, ((GraniteBlockType) type).getBlockObject(), size);
    }

    public ItemType getType() {
        return (ItemType) MinecraftUtils.wrap(invoke("ItemStack", "getItem"));
    }

    public void clearCustomName() {
        invoke("ItemStack", "clearCustomName");
    }

    public int getItemDamage() {
        return (int) invoke("ItemStack", "getItemDamage");
    }

    public void setItemDamage(int damage) {
        invoke("ItemStack", "setItemDamage", damage);
    }

    public String[] getItemLore() {
        Object tagCompound = invoke("ItemStack", "getTagCompound");

        if (tagCompound != null) {
            if ((boolean) Mappings.invoke(tagCompound, "NBTTagCompound", "hasKey", "display", 10)) {
                Object displayCompound = Mappings.invoke(tagCompound, "NBTTagCompound", "getCompoundTag", "display");

                if ((boolean) Mappings.invoke(displayCompound, "NBTTagCompound", "hasKey", "Lore", 9)) {
                    Object loreTagList = Mappings.invoke(displayCompound, "NBTTagCompound", "getTagList", "Lore", 8);

                    int length = (int) Mappings.invoke(loreTagList, "NBTTagList", "tagCount");

                    String[] lore = new String[length];

                    for (int i = 0; i < length; i++) {
                        String loreLine = (String) Mappings.invoke(loreTagList, "NBTTagList", "getStringTagAt", i);
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
            Object loreList = Mappings.getClass("NBTTagList").newInstance();

            for (String line : lines) {
                Object stringTag = Mappings.getClass("NBTTagString").getConstructor(String.class).newInstance(line);
                Mappings.invoke(loreList, "NBTTagList", "appendTag", stringTag);
            }

            Object tagCompound = invoke("ItemStack", "getTagCompound");

            if (tagCompound == null) {
                tagCompound = Mappings.getClass("NBTTagCompound").newInstance();
                invoke("ItemStack", "setTagCompound", tagCompound);
            }

            Object displayCompound;

            if (!(boolean) Mappings.invoke(tagCompound, "NBTTagCompound", "hasKey", "display", 10)) {
                displayCompound = Mappings.getClass("NBTTagCompound").newInstance();
                Mappings.invoke(tagCompound, "NBTTagCompound", "setTag", "display", displayCompound);
            }

            displayCompound = Mappings.invoke(tagCompound, "NBTTagCompound", "getCompoundTag", "display");
            Mappings.invoke(displayCompound, "NBTTagCompound", "setTag", "Lore", loreList);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public int getMaxDamage() {
        return getType().getMaxDamage();
    }

    public String getDisplayName() {
        return (String) invoke("getDisplayName");
    }

    public void setDisplayName(String name) {
        parent = invoke("ItemStack", "setStackDisplayName", name);
    }

    public boolean hasDisplayName() {
        return (boolean) invoke("ItemStack", "hasDisplayName");
    }

    public int getStackSize() {
        return (int) fieldGet("stackSize");
    }

    public void setStackSize(int amount) {
        fieldSet("stackSize", amount);
    }

    //ItemStack constructors:
    //ItemStack(Block|Item, int stacksize, int meta|damage) amj(atr|alq, int, int)
    //ItemStack(Block|Item, int stacksize) amj(atr|alq, int)
    //ItemStack(Block|Item) amj(atr|alq)

    //ItemStack onUseItemRightClick(World w, EntityPlayer e) amj a(aqu w, ahd e)
}
