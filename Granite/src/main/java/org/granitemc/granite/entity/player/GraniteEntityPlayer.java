package org.granitemc.granite.entity.player;

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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.TextComponent;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.inventory.Inventory;
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.entity.GraniteEntityLivingBase;
import org.granitemc.granite.inventory.GraniteInventory;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.PlayServerComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class GraniteEntityPlayer extends GraniteEntityLivingBase implements Player {

    public GraniteEntityPlayer(Object parent) {
        super(parent);
    }

    @Override
    public void setLocation(Location location) {
        // Yes, this is supposed to be overridden
        PlayServerComposite ps = (PlayServerComposite) MinecraftUtils.wrap(fieldGet("playerNetServerHandler"));

        ps.invoke("setPlayerLocation", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
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
    public boolean isBlocking() {
        return (boolean) invoke("isBlocking");
    }

    @Override
    public boolean isMovementBlocked() {
        return (boolean) invoke("isMovementBlocked");
    }

    @Override
    public int getScore() {
        return (Integer) invoke("getScore");
    }

    @Override
    public void setScore(int score) {
        invoke("setScore", score);
    }

    @Override
    public void addScore(int amount) {
        invoke("addScore", amount);
    }

    @Override
    public EntityItem dropOneItem(boolean dropOne) {
        return (EntityItem) MinecraftUtils.wrap(invoke("dropOneItem", dropOne));
    }

    @Override
    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean random) {
        return (EntityItem) MinecraftUtils.wrap(invoke("dropPlayerItemWithRandomChoice", ((GraniteItemStack) itemStack).parent, random));
    }

    @Override
    public EntityItem dropItem(ItemStack itemStack, boolean par2, boolean par3) {
        return (EntityItem) MinecraftUtils.wrap(invoke("dropItem", ((GraniteItemStack) itemStack).parent, par2, par3));
    }

    @Override
    public float getToolDigEfficiency(BlockType blockType) {
        return (float) invoke("getToolDigEfficiency", ((GraniteBlockType) blockType).parent);
    }

    @Override
    public boolean canHarvestBlock(BlockType blockType) {
        return (boolean) invoke("canHarvestBlock", ((GraniteBlockType) blockType).parent);
    }

    /*@Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        return (boolean) invoke("attackEntityFrom", ((GraniteDamageSource) damageSource).parent, amount);
    }*/

    @Override
    public void damageArmor(float damage) {
        invoke("damageArmor", damage);
    }

    @Override
    public float getArmorVisibility() {
        return (float) invoke("getArmorVisibility");
    }

    /*@Override
    public void damageEntity(DamageSource damageSource, float damage) {
        invoke("damageEntity", ((GraniteDamageSource) damageSource).parent, damage);
    }*/

    @Override
    public boolean interactWith(Entity entity) {
        return (boolean) invoke("interactWith", ((GraniteEntity) entity).parent);
    }

    @Override
    public ItemStack getCurrentEquippedItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("getCurrentEquippedItem"));
    }

    @Override
    public void destroyCurrentEquippedItem() {
        invoke("destroyCurrentEquippedItem");
    }

    public Object getGameProfile() {
        return invoke("getGameProfile");
    }

    @Override
    public boolean isInBed() {
        return (boolean) invoke("isInBed");
    }

    @Override
    public Location getBedSpawnLocation(World world, Location location, boolean noBedSpawn) {
        return MinecraftUtils.fromMinecraftLocation(world, invoke("getBedSpawnLocation", ((GraniteWorld) world).parent, MinecraftUtils.toMinecraftLocation(location), noBedSpawn));
    }

    @Override
    public boolean isPlayerFullyAsleep() {
        return (boolean) invoke("isPlayerFullyAsleep");
    }

    @Override
    public Location getBedLocation() {
        return MinecraftUtils.fromMinecraftLocation(getWorld(), invoke("getBedLocation"));
    }

    @Override
    public void setSpawnPoint(Location location, boolean p_180473_2_) {
        invoke("setSpawnPoint", MinecraftUtils.toMinecraftLocation(location), p_180473_2_);
    }

    /*@Override
    public void triggerAchievement(StatBase statBase) {
        invoke("triggerAchievement", ((GaniteStatBase) statBase).parent);
    }*/

    @Override
    public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
        invoke("addMovementStat", p_71000_1_, p_71000_3_, p_71000_5_);
    }

    @Override
    public void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
        invoke("addMountedMovementStat", p_71015_1_, p_71015_3_, p_71015_5_);
    }

    @Override
    public String getFallSoundString(int fallDistance) {
        return (String) invoke("getFallSoundString", fallDistance);
    }

    @Override
    public void addExperience(int amount) {
        invoke("addExperience", amount);
    }

    @Override
    public int getXPSeed() {
        return (Integer) invoke("getXPSeed");
    }

    @Override
    public void removeExperienceLevel(int amount) {
        invoke("removeExperienceLevel", amount);
    }

    @Override
    public int xpBarCap() {
        return (Integer) invoke("xpBarCap");
    }

    @Override
    public void addExhaustion(float amount) {
        invoke("addExhaustion", amount);
    }

    @Override
    public boolean canEat(boolean ignoreHunger) {
        return (boolean) invoke("canEat", ignoreHunger);
    }

    @Override
    public boolean shouldHeal() {
        return (boolean) invoke("shouldHeal");
    }

    @Override
    public boolean isAllowEdit() {
        return (boolean) invoke("isAllowEdit");
    }

    // TODO: Facing to Mineacraft Facing Enum
    /*@Override
    public boolean canPlayerEdit(Location location, Facing facing, ItemStack itemStack) {
        return (boolean) invoke("canPlayerEdit", MinecraftUtils.toMinecraftLocation(location), facing, ((GraniteItemStack) itemStack).parent);
    }*/

    @Override
    public int getExperiencePoints(Player player) {
        return (Integer) invoke("getExperiencePoints", ((GraniteEntityPlayer) player).parent);
    }

    // TODO: Complete when inventories are made in the API
    /*@Override
    public InventoryEnderChest getInventoryEnderChest() {
        return null;
    }*/

    @Override
    public boolean isSpectator() {
        return (boolean) invoke("isSpectator");
    }

    @Override
    public boolean isPushedByWater() {
        return (boolean) invoke("isPushedByWater");
    }

    // TODO: Complete when scoreboards are made in the API
    /*@Override
    public Scoreboard getWorldScoreboard() {
    }*/

    // TODO: Complete when teams are made in the API
    /*@Override
    public Team getTeam() {
    }*/

    // TODO: Complete when chatcomponent is finished
    /*@Override
    public IChatComponent getDisplayName() {
    }*/

    @Override
    public UUID getUUID() {
        return (UUID) invoke("getUUID", getGameProfile());
    }

    @Override
    public UUID getOfflineUUID(String playerName) {
        return (UUID) invoke("getOfflineUUID", playerName);
    }

    // TODO: do this some time later, not urgent
    /*@Override
    public boolean canOpen(LockCode p_175146_1_) {
    }*/

    @Override
    public void addExperienceLevel(int amount) {
        invoke("addExperienceLevel", amount);
    }

    @Override
    public void addSelfToInternalCraftingInventory() {
        invoke("addSelfToInternalCraftingInventory");
    }

    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }*/

    @Override
    public boolean canAttackPlayer(Player player) {
        return (boolean) invoke("canAttackPlayer", ((GraniteEntityPlayer) player).parent);
    }

    @Override
    public void travelToDimension(int dimensionId) {
        invoke("travelToDimension", dimensionId);
    }

    @Override
    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn) {
        invoke("wakeUpPlayer", p_70999_1_, updateWorldFlag, setSpawn);
    }

    @Override
    public void mountEntity(Entity entity) {
        invoke("mountEntity", ((GraniteEntity) entity).parent);
    }

    /*@Override
    public void openEditSign(TileEntitySign p_175141_1_) {
    }*/

    /*@Override
    public void displayGui(IInteractionObject guiOwner) {
    }*/

    @Override
    public void displayGUIChest(Inventory chestInventory) {
        invoke("displayGUIChest", ((GraniteInventory) chestInventory).parent);
    }

    /*@Override
    public void displayVillagerTradeGui(IMerchant villager) {
    }*/

    /*@Override
    public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {
    }*/

    @Override
    public void displayGUIBook(ItemStack bookItemStack) {
        invoke("displayGUIBook", ((GraniteItemStack) bookItemStack).parent);
    }

    /*@Override
    public void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_) {
    }*/

    /*@Override
    public void sendContainerToPlayer(Container p_71120_1_) {
    }*/

    /*@Override
    public void updateCraftingInventory(Container p_71110_1_, List p_71110_2_) {
    }*/

    // TODO: could be interesting
    /*@Override
    public void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_) {
    }*/

    @Override
    public void closeScreen() {
        invoke("closeScreen");
    }

    @Override
    public void updateHeldItem() {
        invoke("updateHeldItem");
    }

    @Override
    public void closeContainer() {
        invoke("closeContainer");
    }

    @Override
    public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean p_110430_4_) {
        invoke("setEntityActionState", p_110430_1_, p_110430_2_, p_110430_3_, p_110430_4_);
    }

    @Override
    public void mountEntityAndWakeUp() {
        invoke("mountEntityAndWakeUp");
    }

    @Override
    public void setPlayerHealthUpdated() {
        invoke("setPlayerHealthUpdated");
    }

    @Override
    public void setItemInUse(ItemStack itemStack, int slot) {
        invoke("setItemInUse", ((GraniteItemStack) itemStack).parent, slot);
    }

    @Override
    public void clonePlayer(Player player, boolean copyEveryThing) {
        invoke("clonePlayer", ((GraniteEntityPlayer) player).parent, copyEveryThing);
    }

    /*@Override
    public void setGameType(WorldSettings.GameType gameType) {
    }*/

    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return (boolean) invoke("canCommandSenderUseCommand", permissionLevel, command);
    }

    @Override
    public String getPlayerIP() {
        return (String) invoke("getPlayerIP");
    }

    /*@Override
    public EntityPlayer.EnumChatVisibility getChatVisibility() {
        return null;
    }*/

    @Override
    public void loadResourcePack(String url, String hash) {
        invoke("loadResourcePack", url, hash);
    }

    @Override
    public Location getPosition() {
        return MinecraftUtils.fromMinecraftLocation(getWorld(), invoke("getPosition"));
    }

    @Override
    public void markPlayerActive() {
        invoke("markPlayerActive");
    }

    @Override
    public void attackTargetEntityWithCurrentItem(Entity entity) {
        invoke("attackTargetEntityWithCurrentItem", ((GraniteEntity) entity).parent);
    }

    @Override
    public long getLastActiveTime() {
        return (long) invoke("getLastActiveTime");
    }

    @Override
    public PlayerInventory getPlayerInventory() {
        return (PlayerInventory) MinecraftUtils.wrap(fieldGet("inventory"));
    }

    @Override
    public void sendPacket(Object packet) {
        Mappings.invoke(fieldGet("playerNetServerHandler"), "sendPacket", packet);
    }

    @Override
    public void sendBlockUpdate(Block block) {
        try {
            sendPacket(Mappings.getClass("S23PacketBlockChange").getConstructor(
                    Mappings.getClass("World"),
                    Mappings.getClass("BlockPos")
            ).newInstance(
                    ((GraniteWorld) getWorld()).parent,
                    MinecraftUtils.toMinecraftLocation(new Location(getWorld(), block.getX(), block.getY(), block.getZ()))
            ));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendBlockUpdate(Block block, BlockType type) {
        try {
            Object packet = Mappings.getClass("S23PacketBlockChange").getConstructor(
                    Mappings.getClass("World"),
                    Mappings.getClass("BlockPos")
            ).newInstance(
                    ((GraniteWorld) getWorld()).parent,
                    MinecraftUtils.toMinecraftLocation(new Location(getWorld(), block.getX(), block.getY(), block.getZ()))
            );
            fieldSet(packet, "field_148883_d", ((GraniteBlockType) type).parent);
            sendPacket(packet);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(String message) {
        invoke("addChatComponentMessage", MinecraftUtils.toMinecraftChatComponent(new TextComponent(message)));
    }

    @Override
    public void sendMessage(ChatComponent component) {
        invoke("addChatComponentMessage", MinecraftUtils.toMinecraftChatComponent(component));
    }

    public boolean hasPermission(String node) {
        return Granite.getPermissionsHook().hasPermission(this, node);
    }

}
