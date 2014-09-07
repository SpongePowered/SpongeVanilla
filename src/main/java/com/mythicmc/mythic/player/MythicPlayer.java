package com.mythicmc.mythic.player;

import com.mythicmc.mythic.item.MythicItem;
import com.mythicmc.mythic.utils.CompositeHelper;

public class MythicPlayer {
    /*
     * This class provides a mapping wrapper for EntityPlayer.
	 * It will have to be updated as the obfuscation changes (or becomes unnecessary).
	 */

    //set the type of this field to the (obfuscated or not) PlayerEntity type.
    Class<?> VanillaPlayerClass = CompositeHelper.getClass("ahd");
    //Obf: ahd
    Object entityPlayer = null;

    //Obf: ahd
    public MythicPlayer(Object vanillaEntityPlayer) {
        entityPlayer = vanillaEntityPlayer;
    }

    public void sendMessage(String message) {
        //should point at EntityPlayer.addChatComponentMessage(IChatComponent p_146105_1_)
        //Obf: hu
        hu chatComponentMessage = new hu(message);
        //XXX: obfuscation reference
        entityPlayer.a(chatComponentMessage);
    }

    public void teleportToDimension(int dimId) {
        //XXX: obfuscation reference
        ((wv) entityPlayer).c(dimId);
    }

    public void setPosition(double x, double y, double z) {
        //XXX: obfuscation reference
        ((wv) entityPlayer).a(x, y, z);
    }

    public void teleportToPlayer(MythicPlayer mythicPlayer) {
        if (getDimension() != mythicPlayer.getDimension()) {
            teleportToDimension(mythicPlayer.getDimension());
        }
        setPosition(mythicPlayer.getX(), mythicPlayer.getY(), mythicPlayer.getZ());
    }

    public double getX() {
        //XXX: obfuscation reference
        return ((wv) entityPlayer).s;
    }

    public double getY() {
        //XXX: obfuscation reference
        return ((wv) entityPlayer).t;
    }

    public double getZ() {
        //XXX: obfuscation reference
        return ((wv) entityPlayer).u;
    }

    public int getDimension() {
        //XXX: obfuscation reference
        return ((wv) entityPlayer).am;
    }

    public boolean isUsingItem() {
        //XXX: obfuscation reference
        return entityPlayer.bR();
    }

    public void stopUsingItem() {
        //XXX: obfuscation reference
        entityPlayer.bT();
    }

    public void clearItemInUse() {
        //XXX: obfuscation reference
        entityPlayer.bU();
    }

    public String getName() {
        //XXX: obfuscation reference
        return entityPlayer.d_();
    }

    public String getUUIDString() {
        //XXX: obfuscation reference
        return entityPlayer.ao.toString();
    }

    public float getHealth() {
        //TODO: find mapping
        return 0;
    }

    public void setHeath(float amount) {
        //XXX: obfuscation reference
        ((xm) entityPlayer).g(amount);
    }

    public void setMaxHealth(float amount) {
        //XXX: obfuscation reference
        ((xm) entityPlayer).h(amount);
    }

    public float getFoodLevel() {
        return 0;
    }

    public void setFoodLevel(float amount) {
        //TODO:Find mapping
    }

    public MythicItem getEquipmentInSlot(int slot) {
        //XXX: obfuscation reference
        return new MythicItem(entityPlayer.p(slot));
    }

    public MythicItem getHeldItem() {
        //XXX: obfuscation reference
        return new MythicItem(entityPlayer.bz());
    }

    public void setCurrentItemOrArmor(int slot, MythicItem item) {
        //XXX: obfuscation reference
        entityPlayer.c(slot, item.itemStack);
    }

    //void onDeath(DamageSource var1) a(wh var1)

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)
}
