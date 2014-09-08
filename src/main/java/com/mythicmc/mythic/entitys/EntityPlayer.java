package com.mythicmc.mythic.entitys;

import com.mythicmc.mythic.Mappings;
import com.mythicmc.mythic.item.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class EntityPlayer {

    /**
     * This class provides a mapping wrapper for EntityPlayer.
     * It will have to be updated as the obfuscation changes (or becomes unnecessary).
     */

    //set the type of this field to the (obfuscated or not) PlayerEntity type.
    Object vanillaPlayerInstance = null;
    String targetClass = "net.minecraft.entity.EntityPlayer";

    public EntityPlayer(Object instance) {
        instance = vanillaPlayerInstance;
    }

    /*public void sendChatMessage(String message) {
        //should point at EntityPlayer.addChatComponentMessage(IChatComponent p_146105_1_)
        //obf: hu chatComponentMessage = new hu(message);
        hu chatComponentMessage = new hu(message);
        //XXX: obfuscation reference
        vanillaEntityInstance.a(chatComponentMessage);
    }*/

    public void teleportToDimension(int dimId) {
        //TODO: Check if this works or need to be invoked differently
        invoke("teleportToDimension", new Object[]{dimId});
    }


    public void teleportToPlayer(EntityPlayer player) {
        if (getDimension() != player.getDimension()) {
            teleportToDimension(player.getDimension());
        }
        setPosition(player.getX(), player.getY(), player.getZ());
    }

    public void setPosition(double x, double y, double z) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setPosition", new Object[]{x, y, z});
    }

    public UUID getUUID() {
        return (UUID) invoke("getUUID", new Object[]{});
    }

    public double getX() {
        return (double) invoke("getX", new Object[]{});
    }

    public double getY() {
        return (double) invoke("getY", new Object[]{});
    }

    public double getZ() {
        return (double) invoke("getZ", new Object[]{});
    }

    public int getDimension() {
        return (int) invoke("getDimension", new Object[]{});
    }

    public boolean isUsingItem() {
        return (boolean) invoke("isUsingItem", new Object[]{});
    }

    public void stopUsingItem() {
        invoke("isUsingItem", new Object[]{});
    }

    public void clearItemInUse() {
        invoke("clearItemInUse", new Object[]{});
    }

    public String getName() {
        return (String) invoke("getName", new Object[]{});
    }

    public void heal(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("heal", new Object[]{amount});
    }

    public void setHealth(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setHealth", new Object[]{amount});
    }

    public ItemStack getEquipmentInSlot(int slot) {
        //TODO: Check if this works or need to be invoked differently
        return new ItemStack(invoke("getEquiptmentInSlot", new Object[]{slot}));
    }

    public ItemStack getHeldItem() {
        //TODO: Check if this works or need to be invoked differently
        return new ItemStack(invoke("getHeldItem", new Object[]{}));
    }

    public void setCurrentItemOrArmor(int slot, ItemStack item) {
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
