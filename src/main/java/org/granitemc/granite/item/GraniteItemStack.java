package org.granitemc.granite.item;

/*****************************************************************************************
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
 ****************************************************************************************/

import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;

public class GraniteItemStack extends Composite implements ItemStack {

    public GraniteItemStack(Object instance) {
        super(instance);
    }

    public GraniteItemStack(Object instance, int size) {
        super(Mappings.getClass("n.m.item.ItemStack"),
                Mappings.getClass("n.m.block.Block").isInstance(instance)
                        ? new Class[]{Mappings.getClass("n.m.block.Block")}
                        : new Class[]{Mappings.getClass("n.m.item.Item"), int.class}, instance, size);
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
        return new GraniteItemType(invoke("n.m.item.ItemStack", "getItem"));
    }

    public int getItemDamage() {
        return (int) invoke("n.m.item.ItemStack", "getItemDamage");
    }

    public void setItemDamage(int damage) {
        invoke("n.m.item.ItemStack", "setItemDamage(int)", damage);
    }

    public String[] getItemLore() {
        return (String[]) invoke("n.m.item.ItemStack", "getItemLore");
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
