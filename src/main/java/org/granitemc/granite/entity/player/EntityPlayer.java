package org.granitemc.granite.entity.player;

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

import org.granitemc.granite.api.chat.ChatComponentText;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.chat.GraniteChatComponentText;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.Composite;

import java.util.UUID;

public class EntityPlayer extends Composite implements CommandSender {

    /**
     * This class provides a mapping wrapper for EntityPlayer.
     * It will have to be updated as the obfuscation changes (or becomes unnecessary).
     */

    public EntityPlayer(Object parent) {
        super(parent, false);
    }

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
        /*Object asLivingEntityBase = Class.forName("wv").cast(parent);
        Class.forName("wv").getDeclaredMethod("b", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE}).invoke(asLivingEntityBase, x, y, z);*/
        invoke("setPosition(Double;Double;Double)", x, y, z);
    }

    public UUID getUUID() {
        return (UUID) invoke("getUUID");
    }

    public double getX() {
        return (double) fieldGet("n.m.entity.Entity", "posX");
    }

    public double getY() {
        return (double) fieldGet("n.m.entity.Entity", "posY");
    }

    public double getZ() {
        return (double) fieldGet("n.m.entity.Entity", "posZ");
    }

    public int getDimension() {
        return (int) invoke("getDimension");
    }

    public boolean isUsingItem() {
        return (boolean) invoke("isUsingItem");
    }

    public void stopUsingItem() {
        invoke("stopUsingItem");
    }

    public void clearItemInUse() {
        invoke("clearItemInUse");
    }

    public String getName() {
        return (String) invoke("getName");
    }

    @Override
    public void sendMessage(String message) {
        ChatComponentText component = new GraniteChatComponentText(message);
        invoke("addChatComponentMessage(String)", component);
    }

    public void heal(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("heal", amount);
    }

    public void setHealth(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setHealth", amount);
    }

    public GraniteItemStack getEquipmentInSlot(int slot) {
        //TODO: Check if this works or need to be invoked differently
        return new GraniteItemStack(invoke("getEquipmentInSlot", slot));
    }

    public GraniteItemStack getHeldItem() {
        //TODO: Check if this works or need to be invoked differently
        return new GraniteItemStack(invoke("getHeldItem"));
    }

    public void setCurrentItemOrArmor(int slot, GraniteItemStack item) {
        invoke("setCurrentItemOrArmor", slot, item);
    }

    /*public void onDeath(DamageSource var1) {
        a(wh var1);
    }*/

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)
}
