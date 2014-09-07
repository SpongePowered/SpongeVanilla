package com.mythicmc.mythic.item;

import com.mythicmc.mythic.utils.CompositeHelper;

public class MythicItem {

    //Obf: amj
    Object itemStack = null;
    Class<?> VanillaItemClass = CompositeHelper.getClass("amj");

    //XXX: obfuscation reference
    alq test;

    //Obf: amj
    public MythicItem(Object item) {
        itemStack = item;
    }

    public static MythicItem copyItemStack(MythicItem item) {
        //XXX: obfuscation reference
        return new MythicItem(amj.b(item.itemStack));
    }

    public alq getItem() {
        //XXX: obfuscation reference
        return itemStack.b();
    }

    public int getItemDamage() {
        //XXX: obfuscation reference
        return itemStack.i();
    }

    public void setItemDamage(int damage) {
        //XXX: obfuscation reference
        itemStack.b(damage);
    }

    public int getMaxDamage() {
        //XXX: obfuscation reference
        return itemStack.j();
    }

    public String getDisplayName() {
        //XXX: obfuscation reference
        return itemStack.q();
    }

    public MythicItem setStackDisplayName(String name) {
        //XXX: obfuscation reference
        return new MythicItem(itemStack.c(name));
    }

    public boolean hasDisplayName() {
        //XXX: obfuscation reference
        return itemStack.s();
    }

    //ItemStack constructors:
    //ItemStack(Block|Item, int stacksize, int meta|damage) amj(atr|alq, int, int)
    //ItemStack(Block|Item, int stacksize) amj(atr|alq, int)
    //ItemStack(Block|Item) amj(atr|alq)

    //ItemStack onUseItemRightClick(World w, EntityPlayer e) amj a(aqu w, ahd e)

}
