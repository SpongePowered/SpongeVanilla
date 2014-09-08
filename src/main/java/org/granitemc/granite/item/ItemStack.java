package org.granitemc.granite.item;

import org.granitemc.granite.Mappings;

import java.lang.reflect.InvocationTargetException;

public class ItemStack {
    Object vanillaItemInstance = null;
    String targetClass = "net.minecraft.item.ItemStack";

    public ItemStack(Object instance) {
        vanillaItemInstance = instance;
    }

    //need more constructors here that are capable of creating vanilla item instances

    public Object getItem() {
        return invoke("getItem");
    }

    public int getItemDamage() {
        return (int) invoke("getItemDamage");
    }

    public String[] getItemLore() {
        return (String[]) invoke("getItemLore");
    }

    public void setItemDamage(int damage) {
        invoke("setItemDamage", damage);
    }

    public int getMaxDamage() {
        return (int) invoke("getMaxDamage");
    }

    public String getDisplayName() {
        return (String) invoke("getDisplayName");
    }

    public ItemStack setDisplayName(String name) {
        return new ItemStack(invoke("setDisplayName", name));
    }

    public boolean hasDisplayName() {
        return (boolean) invoke("hasDisplayName");
    }

    public Object invoke(String targetMethod, Object... parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaItemInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    //ItemStack constructors:
    //ItemStack(Block|Item, int stacksize, int meta|damage) amj(atr|alq, int, int)
    //ItemStack(Block|Item, int stacksize) amj(atr|alq, int)
    //ItemStack(Block|Item) amj(atr|alq)

    //ItemStack onUseItemRightClick(World w, EntityPlayer e) amj a(aqu w, ahd e)
}
