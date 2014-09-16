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

import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.chat.ChatComponentText;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.chat.GraniteChatComponentText;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.item.GraniteItemStack;

import java.util.UUID;

public class GranitePlayer extends GraniteEntity implements Player {

    public GranitePlayer(Object parent) {
        super(parent, false);
    }

    @Override
    public void teleportToDimension(int dimId) {
        //TODO: Check if this works or need to be invoked differently
        invoke("teleportToDimension", dimId);
    }

    @Override
    public void teleportToPlayer(Player player) {
        if (getDimension() != player.getDimension()) {
            teleportToDimension(player.getDimension());
        }
        setPosition(player.getX(), player.getY(), player.getZ());
    }

    @Override
    public void setPosition(double x, double y, double z) {
        /*Object asLivingEntityBase = Class.forName("wv").cast(parent);
        Class.forName("wv").getDeclaredMethod("b", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE}).invoke(asLivingEntityBase, x, y, z);*/
        invoke("setPosition(Double;Double;Double)", x, y, z);
    }

    @Override
    public void onCollideWithPlayer(GranitePlayer entityPlayer) {

    }

    @Override
    public float getCollisionBorderSize() {
        return 0;
    }

    @Override
    public UUID getUUID() {
        return (UUID) invoke("getUUID");
    }

    @Override
    public double getX() {
        return (double) fieldGet("n.m.entity.Entity", "posX");
    }

    @Override
    public double getY() {
        return (double) fieldGet("n.m.entity.Entity", "posY");
    }

    @Override
    public double getZ() {
        return (double) fieldGet("n.m.entity.Entity", "posZ");
    }

    @Override
    public int getDimension() {
        return (int) invoke("getDimension");
    }

    @Override
    public boolean isUsingItem() {
        return (boolean) invoke("isUsingItem");
    }

    @Override
    public void stopUsingItem() {
        invoke("stopUsingItem");
    }

    @Override
    public void clearItemInUse() {
        invoke("clearItemInUse");
    }

    @Override
    public String getName() {
        return (String) invoke("getName");
    }

    @Override
    public void sendMessage(String message) {
        ChatComponentText component = new GraniteChatComponentText(message);
        invoke("addChatComponentMessage(String)", ((GraniteChatComponentText) component).parent);
    }

    @Override
    public void heal(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("heal", amount);
    }

    @Override
    public void setHealth(int amount) {
        //TODO: Check if this works or need to be invoked differently
        invoke("setHealth", amount);
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        //TODO: Check if this works or need to be invoked differently
        return new GraniteItemStack(invoke("getEquipmentInSlot", slot));
    }

    @Override
    public ItemStack getHeldItem() {
        //TODO: Check if this works or need to be invoked differently
        return new GraniteItemStack(invoke("getHeldItem"));
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack item) {
        invoke("setCurrentItemOrArmor", slot, ((GraniteItemStack) item).parent);
    }

    /*public void onDeath(DamageSource var1) {
        a(wh var1);
    }*/

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)
}
