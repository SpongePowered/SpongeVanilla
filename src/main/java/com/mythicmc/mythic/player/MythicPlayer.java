package com.mythicmc.mythic.player;

import com.mythicmc.mythic.Mappings;
import com.mythicmc.mythic.item.MythicItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class MythicPlayer {

    /**
     * This class provides a mapping wrapper for EntityPlayer.
	 * It will have to be updated as the obfuscation changes (or becomes unnecessary).
	*/

    //set the type of this field to the (obfuscated or not) PlayerEntity type.
    Object vanillaPlayerInstance = null;
    String targetClass = "net.minecraft.entity.player.EntityPlayer";

    public MythicPlayer(Object instance) {
        instance = vanillaPlayerInstance;
    }

    /*public void sendChatMessage(String message) {
        //should point at EntityPlayer.addChatComponentMessage(IChatComponent p_146105_1_)
        //obf: hu chatComponentMessage = new hu(message);
        hu chatComponentMessage = new hu(message);
        //XXX: obfuscation reference
        vanillaPlayerInstance.a(chatComponentMessage);
    }*/

    public void teleportToDimension(int dimId) {
        //TODO: Check if this works or need to be invoked differently
        //Obf: wv
        invoke("teleportToDimension", new Object[]{dimId});
    }

    public void setPosition(double x, double y, double z) {
        //TODO: Check if this works or need to be invoked differently
        //Obf: a
        invoke("setPosition", new Object[]{x, y, z});
    }

    public void teleportToPlayer(MythicPlayer mythicPlayer) {
        if (getDimension() != mythicPlayer.getDimension()) {
            teleportToDimension(mythicPlayer.getDimension());
        }
        setPosition(mythicPlayer.getX(), mythicPlayer.getY(), mythicPlayer.getZ());
    }

    public double getX() {
        //Obf: wv.s
        return (double) invoke("getX", new Object[]{});
    }

    public double getY() {
        //Obf: wv.s
        return (double) invoke("getY", new Object[]{});
    }

    public double getZ() {
        //Obf: wv.s
        return (double) invoke("getZ", new Object[]{});
    }

    public int getDimension() {
        //Obf: wv.am
        return (int) invoke("getDimension", new Object[]{});
    }

    public boolean isUsingItem() {
        //Obf: br
        return (boolean) invoke("isUsingItem", new Object[]{});
    }

    public void stopUsingItem() {
        //Obf: bT
        invoke("isUsingItem", new Object[]{});
    }

    public void clearItemInUse() {
        //Obf: bU
        invoke("clearItemInUse", new Object[]{});
    }

    public String getName() {
        //Obf: d_
        return (String) invoke("isUsingItem", new Object[]{});
    }

    public UUID getUUID() {
        //Obf: ao
        return (UUID) invoke("getUUID", new Object[]{});
    }

    public void heal(int amount) {
        //TODO: Check if this works or need to be invoked differently
        //Obf: (xm) g
        invoke("heal", new Object[]{amount});
    }

    public void setHealth(int amount) {
        //TODO: Check if this works or need to be invoked differently
        //Obf: (xm) h
        invoke("setHealth", new Object[]{amount});
    }

    public MythicItemStack getEquipmentInSlot(int slot) {
        //TODO: Check if this works or need to be invoked differently
        //Obf: p
        return new MythicItemStack(invoke("getEquiptmentInSlot", new Object[]{slot}));
    }

    public MythicItemStack getHeldItem() {
        //TODO: Check if this works or need to be invoked differently
        //XXX: obfuscation reference
        return new MythicItemStack(invoke("getHeldItem", new Object[]{}));
    }

    public void setCurrentItemOrArmor(int slot, MythicItemStack item) {
        //Obf: c
        invoke("setCurrentItemOrArmor", new Object[]{slot, item});
    }

    /*public void onDeath(DamageSource var1) {
        a(wh var1);
    }*/

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)

    public Object invoke(String targetMethod, Object[] parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaPlayerInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
