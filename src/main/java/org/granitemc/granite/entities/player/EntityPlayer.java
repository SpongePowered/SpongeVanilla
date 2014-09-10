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

package org.granitemc.granite.entities.player;

import org.granitemc.granite.item.ItemStack;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class EntityPlayer {

    /**
     * This class provides a mapping wrapper for EntityPlayer.
     * It will have to be updated as the obfuscation changes (or becomes unnecessary).
     */

    //set the type of this field to the (obfuscated or not) PlayerEntity type.
    Object entityPlayerInstance = null;
    String targetClass = "net.minecraft.entity.EntityPlayer";

    public EntityPlayer(Object instance) {
        instance = entityPlayerInstance;
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
        try {
            Object asLivingEntityBase = Class.forName("wv").cast(entityPlayerInstance);
            Class.forName("wv").getDeclaredMethod("b", new Class[]{Double.TYPE, Double.TYPE, Double.TYPE}).invoke(asLivingEntityBase, x, y, z);
        } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public UUID getUUID() {
        return (UUID) invoke("getUUID");
    }

    public double getX() {
        try {
            return Class.forName("wv").getDeclaredField("s").getDouble(Class.forName("wv").cast(entityPlayerInstance));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0D;
    }

    public double getY() {
        try {
            return Class.forName("wv").getDeclaredField("t").getDouble(Class.forName("wv").cast(entityPlayerInstance));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0D;
    }

    public double getZ() {
        try {
            return Class.forName("wv").getDeclaredField("u").getDouble(Class.forName("wv").cast(entityPlayerInstance));
        } catch (IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0D;
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
            return Mappings.getMethod(targetClass, targetMethod).invoke(entityPlayerInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
