package com.mythicmc.mythic.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.mythicmc.mythic.Mappings;

public class MythicItemStack {
    Object vanillaItemInstance = null;
    String targetClass = "net.minecraft.item.ItemStack";

    public MythicItemStack(Object instance) {
        vanillaItemInstance = instance;
    }

    //need more constructors here that are capable of creating vanilla item instances

    public Object getItem() {
        return invoke("getItem", new Object[]{});
    }

    public int getItemDamage() {
        return (int) invoke("getItemDamage", new Object[]{});
    }

    public void setItemDamage(int damage) {
        invoke("setItemDamage", new Object[]{damage});
    }

    public int getMaxDamage() {
        return (int) invoke("getMaxDamage", new Object[]{});
    }

    public String getDisplayName() {
        return (String) invoke("getDisplayName", new Object[]{});
    }

    public MythicItemStack setDisplayName(String name) {
        return new MythicItemStack(invoke("setDisplayName", new Object[]{name}));
    }

    public boolean hasDisplayName() {
        return (boolean) invoke("hasDisplayName", new Object[]{});
    }

    public Object invoke(String targetMethod, Object[] parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaItemInstance, parameters);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
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
