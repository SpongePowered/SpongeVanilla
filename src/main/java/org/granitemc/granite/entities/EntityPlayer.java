package org.granitemc.granite.entities;

import org.granitemc.granite.Mappings;
import org.granitemc.granite.item.ItemStack;

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
        invoke("teleportToDimension", dimId);
    }

    public void teleportToPlayer(EntityPlayer player) {
        if (getDimension() != player.getDimension()) {
            teleportToDimension(player.getDimension());
        }
        setPosition(player.getX(), player.getY(), player.getZ());
    }

    public void setPosition(double x, double y, double z) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setPosition", x, y, z);
    }

    public UUID getUUID() {
        return (UUID) invoke("getUUID");
    }

    public double getX() {
        return (double) invoke("getX");
    }

    public double getY() {
        return (double) invoke("getY");
    }

    public double getZ() {
        return (double) invoke("getZ");
    }

    public int getDimension() {
        return (int) invoke("getDimension");
    }

    public boolean isUsingItem() {
        return (boolean) invoke("isUsingItem");
    }

    public void stopUsingItem() {
        invoke("isUsingItem");
    }

    public void clearItemInUse() {
        invoke("clearItemInUse");
    }

    public String getName() {
        return (String) invoke("getName");
    }

    public void heal(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("heal", amount);
    }

    public void setHealth(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setHealth", amount);
    }

    public ItemStack getEquipmentInSlot(int slot) {
        //TODO: Check if this works or need to be invoked differently
        return new ItemStack(invoke("getEquiptmentInSlot", slot));
    }

    public ItemStack getHeldItem() {
        //TODO: Check if this works or need to be invoked differently
        return new ItemStack(invoke("getHeldItem"));
    }

    public void setCurrentItemOrArmor(int slot, ItemStack item) {
        invoke("setCurrentItemOrArmor", slot, item);
    }

    /*public void onDeath(DamageSource var1) {
        a(wh var1);
    }*/

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)

    public Object invoke(String targetMethod, Object... parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaPlayerInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
