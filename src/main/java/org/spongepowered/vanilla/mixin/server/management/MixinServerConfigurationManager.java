/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.vanilla.mixin.server.management;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.Logger;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.entity.player.PlayerJoinEvent;
import org.spongepowered.api.event.entity.player.PlayerRespawnEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.text.SpongeTexts;

import java.util.Iterator;

@Mixin(ServerConfigurationManager.class)
public abstract class MixinServerConfigurationManager {

    @Shadow private MinecraftServer mcServer;
    @Shadow private static Logger logger;
    @Shadow abstract NBTTagCompound readPlayerDataFromFile(EntityPlayerMP playerIn);
    @Shadow abstract void setPlayerGameTypeBasedOnOther(EntityPlayerMP playerIn1, EntityPlayerMP playerIn2, World world);
    @Shadow abstract MinecraftServer getServerInstance();
    @Shadow abstract void func_96456_a(ServerScoreboard scoreboardIn, EntityPlayerMP playerIn);
    @Shadow abstract int getMaxPlayers();
    @Shadow abstract void sendChatMsg(IChatComponent component);
    @Shadow abstract void playerLoggedIn(EntityPlayerMP playerIn);
    @Shadow abstract void updateTimeAndWeatherForPlayer(EntityPlayerMP playerIn, WorldServer worldIn);

    /**
     * @Author Zidane
     *
     * Purpose: To fire the {@link PlayerJoinEvent} before instructing the player's client where the join will occur at.
     * Reasoning: We want to be able to control the location and the message. I moved up the logic for constructing the message
     * before the sending of the chat message regarding where they are spawning at. This also has the added benefit of letting a plugin cleanly
     * set the location before all logic applies (in sending the packets to prepare a client for spawn)
     * @param netManager Injected network manager param
     * @param playerIn Injected player param
     */
    @Overwrite
    public void initializeConnectionToPlayer(NetworkManager netManager, EntityPlayerMP playerIn) {
        GameProfile gameprofile = playerIn.getGameProfile();
        PlayerProfileCache playerprofilecache = this.mcServer.getPlayerProfileCache();
        GameProfile gameprofile1 = playerprofilecache.getProfileByUUID(gameprofile.getId());
        String s = gameprofile1 == null ? gameprofile.getName() : gameprofile1.getName();
        playerprofilecache.addEntry(gameprofile);
        NBTTagCompound nbttagcompound = this.readPlayerDataFromFile(playerIn);
        playerIn.setWorld(this.mcServer.worldServerForDimension(playerIn.dimension));
        playerIn.theItemInWorldManager.setWorld((WorldServer)playerIn.worldObj);
        String s1 = "local";

        if (netManager.getRemoteAddress() != null)
        {
            s1 = netManager.getRemoteAddress().toString();
        }

        // Sponge Start

        // Move logic for creating join message up here
        ChatComponentTranslation chatcomponenttranslation;

        if (!playerIn.getCommandSenderName().equalsIgnoreCase(s))
        {
            chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[] {playerIn.getDisplayName(), s});
        }
        else
        {
            chatcomponenttranslation = new ChatComponentTranslation("multiplayer.player.joined", new Object[] {playerIn.getDisplayName()});
        }

        chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.YELLOW);

        // Create the handler here (so the player's gets set)
        NetHandlerPlayServer nethandlerplayserver = new NetHandlerPlayServer(this.mcServer, netManager, playerIn);

        // Fire PlayerJoinEvent
        final PlayerJoinEvent event = SpongeEventFactory.createPlayerJoin(Sponge.getGame(), (Player) playerIn, ((Player) playerIn).getLocation(),
                SpongeTexts.toText(chatcomponenttranslation));
        System.out.println(event);
        Sponge.getGame().getEventManager().post(event);
        // Set the resolved location of the event onto the player
        ((Player) playerIn).setLocation(event.getLocation());

        // Sponge End

        logger.info(playerIn.getCommandSenderName() + "[" + s1 + "] logged in with entity id " + playerIn.getEntityId() + " at (" + playerIn
                .posX + ", " + playerIn.posY + ", " + playerIn.posZ + ")");
        WorldServer worldserver = this.mcServer.worldServerForDimension(playerIn.dimension);
        WorldInfo worldinfo = worldserver.getWorldInfo();
        BlockPos blockpos = worldserver.getSpawnPoint();
        this.setPlayerGameTypeBasedOnOther(playerIn, (EntityPlayerMP)null, worldserver);
        nethandlerplayserver.sendPacket(new S01PacketJoinGame(playerIn.getEntityId(), playerIn.theItemInWorldManager.getGameType(), worldinfo.isHardcoreModeEnabled(), worldserver.provider.getDimensionId(), worldserver.getDifficulty(), this.getMaxPlayers(), worldinfo.getTerrainType(), worldserver.getGameRules().getGameRuleBooleanValue("reducedDebugInfo")));
        nethandlerplayserver.sendPacket(new S3FPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
        nethandlerplayserver.sendPacket(new S41PacketServerDifficulty(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
        nethandlerplayserver.sendPacket(new S05PacketSpawnPosition(blockpos));
        nethandlerplayserver.sendPacket(new S39PacketPlayerAbilities(playerIn.capabilities));
        nethandlerplayserver.sendPacket(new S09PacketHeldItemChange(playerIn.inventory.currentItem));
        playerIn.getStatFile().func_150877_d();
        playerIn.getStatFile().func_150884_b(playerIn);
        this.func_96456_a((ServerScoreboard)worldserver.getScoreboard(), playerIn);
        this.mcServer.refreshStatusNextTick();

        this.sendChatMsg(SpongeTexts.toComponent(event.getNewMessage()));
        this.playerLoggedIn(playerIn);
        nethandlerplayserver.setPlayerLocation(playerIn.posX, playerIn.posY, playerIn.posZ, playerIn.rotationYaw, playerIn.rotationPitch);
        this.updateTimeAndWeatherForPlayer(playerIn, worldserver);

        if (this.mcServer.getResourcePackUrl().length() > 0)
        {
            playerIn.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
        }

        Iterator iterator = playerIn.getActivePotionEffects().iterator();

        while (iterator.hasNext())
        {
            PotionEffect potioneffect = (PotionEffect)iterator.next();
            nethandlerplayserver.sendPacket(new S1DPacketEntityEffect(playerIn.getEntityId(), potioneffect));
        }

        playerIn.addSelfToInternalCraftingInventory();

        if (nbttagcompound != null && nbttagcompound.hasKey("Riding", 10))
        {
            Entity entity = EntityList.createEntityFromNBT(nbttagcompound.getCompoundTag("Riding"), worldserver);

            if (entity != null)
            {
                entity.forceSpawn = true;
                worldserver.spawnEntityInWorld(entity);
                playerIn.mountEntity(entity);
                entity.forceSpawn = false;
            }
        }
    }

    /**
     * Invoke before <code>return entityplayermp1;</code> (line 513 of source) to fire {@link org.spongepowered.api.event.entity.player.PlayerRespawnEvent}.
     * @param playerIn Injected player param
     * @param dimension Injected dimension param
     * @param conqueredEnd Injected conquered end flag
     * @param ci Info to provide mixin on how to handle the callback
     */
    @Inject(method = "recreatePlayerEntity", at = @At("RETURN"))
    public void onRecreatePlayerEntityEnd(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd, CallbackInfoReturnable<EntityPlayerMP> ci) {
        final PlayerRespawnEvent event = SpongeEventFactory.createPlayerRespawn(Sponge.getGame(), (Player) playerIn, playerIn.getBedLocation() != null, ((Player) playerIn).getLocation());
        Sponge.getGame().getEventManager().post(event);
        ((Player) playerIn).setLocation(event.getRespawnLocation());
    }

}
