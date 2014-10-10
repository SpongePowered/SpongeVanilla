/*
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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.entity.player;

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.chat.ChatComponentText;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.chat.GraniteChatComponentText;
import org.granitemc.granite.entity.GraniteEntityLivingBase;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;

public class GranitePlayer extends GraniteEntityLivingBase implements Player {
    public GranitePlayer(Object parent) {
        super(parent, false);
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
        invoke("addChatComponentMessage", ((GraniteChatComponentText) component).parent);
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("getEquipmentInSlot", slot));
    }

    @Override
    public ItemStack getHeldItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("getHeldItem"));
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack item) {
        invoke("setCurrentItemOrArmor", slot, ((GraniteItemStack) item).parent);
    }

    public PlayerInventory getPlayerInventory() {
        return (PlayerInventory) MinecraftUtils.wrap(fieldGet("EntityPlayer", "inventory"));
    }

    public void sendPacket(Object packet) {
        Mappings.invoke(fieldGet("playerNetServerHandler"), "sendPacket", packet);
    }

    public void sendBlockUpdate(Block block) {
        try {
            sendPacket(Mappings.getClass("S23PacketBlockChange").getConstructor(
                    Mappings.getClass("World"),
                    Mappings.getClass("BlockPos")
            ).newInstance(
                    ((GraniteWorld) getWorld()).parent,
                    MinecraftUtils.createBlockPos(block.getX(), block.getY(), block.getZ())
            ));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void sendBlockUpdate(Block block, BlockType type) {
        try {
            Object packet = Mappings.getClass("S23PacketBlockChange").getConstructor(
                    Mappings.getClass("World"),
                    Mappings.getClass("BlockPos")
            ).newInstance(
                    ((GraniteWorld) getWorld()).parent,
                    MinecraftUtils.createBlockPos(block.getX(), block.getY(), block.getZ())
            );
            fieldSet(packet, "field_148883_d", ((GraniteBlockType) type).parent);
            sendPacket(packet);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /*public void onDeath(DamageSource var1) {
        a(wh var1);
    }*/

    //void EntityLivingBase.onItemPickup(Entity itemToBePickedUp, int unused) xm.a(wv itemToBePickedUp, int unused)

    /*************************************************
     *                                               *
     *              Granite Methods                  *
     *                                               *
     *************************************************/

}
