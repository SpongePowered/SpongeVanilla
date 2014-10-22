package org.granitemc.granite.api.entity.player;

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.command.CommandSender;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.EntityLivingBase;
import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.inventory.Inventory;
import org.granitemc.granite.api.inventory.PlayerInventory;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.world.World;

import java.util.UUID;

public interface Player extends CommandSender, EntityLivingBase {

    boolean isUsingItem();

    void stopUsingItem();

    void clearItemInUse();

    boolean isBlocking();

    boolean isMovementBlocked();

    int getScore();

    void setScore(int score);

    void addScore(int amount);

    EntityItem dropOneItem(boolean p_71040_1_);

    EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean p_71019_2_);

    EntityItem dropItem(ItemStack itemStack, boolean p_146097_2_, boolean p_146097_3_);

    float getToolDigEfficiency(BlockType blockType);

    boolean canHarvestBlock(BlockType blockType);

    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    void damageArmor(float amount);

    float getArmorVisibility();

    /*void damageEntity(DamageSource p_70665_1_, float damage);*/

    boolean interactWith(Entity entity);

    ItemStack getCurrentEquippedItem();

    void destroyCurrentEquippedItem();

    boolean isInBed();

    Location getBedSpawnLocation(World world, Location location, boolean noBedSpawn);

    boolean isPlayerFullyAsleep();

    Location getBedLocation();

    void setSpawnPoint(Location location, boolean p_180473_2_);

    void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_);

    void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_);

    String getFallSoundString(int blocksFallen);

    ItemStack getCurrentArmor(int slot);

    void addExperience(int amount);

    int getXPSeed();

    void removeExperienceLevel(int amount);

    int xpBarCap();

    void addExhaustion(float amount);

    boolean canEat(boolean ignoreHunger);

    boolean shouldHeal();

    boolean isAllowEdit();

    /*boolean canPlayerEdit(Location location, Facing facing, ItemStack itemStack);*/

    /*InventoryEnderChest getInventoryEnderChest();*/

    boolean isSpectator();

    boolean isPushedByWater();

    /*Scoreboard getWorldScoreboard();*/

    /*Team getTeam();*/

    /*IChatComponent getDisplayName();*/

    UUID getUUID();

    UUID getOfflineUUID(String name);

    /*boolean canOpen(LockCode p_175146_1_);*/

    boolean replaceItemInInventory(int slot, ItemStack itemStack);

    void addExperienceLevel(int amount);

    void addSelfToInternalCraftingInventory();

    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    boolean canAttackPlayer(Player player);

    void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn);

    /*void openEditSign(TileEntitySign p_175141_1_);*/

    /*void displayGui(IInteractionObject guiOwner);*/

    void displayGUIChest(Inventory chestInventory);

    /*void displayVillagerTradeGui(IMerchant villager);*/

    /*void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_);*/

    void displayGUIBook(ItemStack bookStack);

    /*void sendSlotContents(Container p_71111_1_, int p_71111_2_, ItemStack p_71111_3_);*/

    /*void sendContainerToPlayer(Container p_71120_1_);*/

    /*void updateCraftingInventory(Container p_71110_1_, List p_71110_2_);*/

    /*void sendProgressBarUpdate(Container p_71112_1_, int p_71112_2_, int p_71112_3_);*/

    void closeScreen();

    void updateHeldItem();

    void closeContainer();

    void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean p_110430_4_);

    void mountEntityAndWakeUp();

    void setPlayerHealthUpdated();

    /*void addChatComponentMessage(IChatComponent p_146105_1_);*/

    void setItemInUse(ItemStack itemStack, int p_71008_2_);

    void clonePlayer(Player player, boolean p_71049_2_);

    /*void setGameType(WorldSettings.GameType gameType);*/

    /*void addChatMessage(IChatComponent message);*/

    String getPlayerIP();

    /*EntityPlayer.EnumChatVisibility getChatVisibility();*/

    void loadResourcePack(String url, String hash);

    Location getPosition();

    void markPlayerActive();

    void attackTargetEntityWithCurrentItem(Entity targetEntity);

    long getLastActiveTime();

    void sendBlockUpdate(Block block, BlockType type);

    PlayerInventory getPlayerInventory();

    void sendPacket(Object packet);

    void sendBlockUpdate(Block block);

    void sendMessage(String message);

    void sendMessage(ChatComponent component);

    boolean hasPermission(String node);
}
