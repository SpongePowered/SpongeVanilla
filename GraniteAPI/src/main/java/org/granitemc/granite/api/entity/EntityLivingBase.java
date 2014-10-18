package org.granitemc.granite.api.entity;

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Vector;

import java.util.Collection;

public interface EntityLivingBase extends Entity {

    void killByVoid();

    boolean canBreatheUnderwater();

    boolean isChild();

    boolean isAdult();

    int decreaseAirSupply(int amount);

    int getExperiencePoints(Player player);

    boolean isPlayer();

    EntityLivingBase getAITarget();

    int getRevengeTimer();

    void setRevengeTarget(EntityLivingBase entityLivingBase);

    EntityLivingBase getLastAttacker();

    int getLastAttackerTime();

    void setLastAttacker(Entity entity);

    int getAge();

    void clearActivePotions();

    Collection getActivePotionEffects();

    boolean isPotionActive(int potion);

    /*boolean isPotionActive(Potion potion)*/

    /*PotionEffect getActivePotionEffect(Potion potion)*/

    /*void addPotionEffect(PotionEffect potionEffect)*/

    /*boolean isPotionApplicable(PotionEffect PotionEffect)*/

    boolean isEntityUndead();

    void removePotionEffect(int potionID);

    void heal(float amount);

    float getHealth();

    void setHealth(float health);

    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    void renderBrokenItemStack(ItemStack itemStack);

    void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_);

    String getHurtSound();

    String getDeathSound();

    void addRandomArmor();

    boolean isOnLadder();

    boolean isEntityAlive();

    void fall(float distance, float damageMultiplier);

    String getFallDamageSound(int blockFallen);

    int getTotalArmorValue();

    /*void damageEntity(DamageSource p_70665_1_, float p_70665_2_);*/

    float getMaxHealth();

    int getArrowCountInEntity();

    void setArrowCountInEntity(int amount);

    void swingItem();

    ItemStack getHeldItem();

    ItemStack getEquipmentInSlot(int slot);

    void setCurrentItemOrArmor(int slot, ItemStack itemStack);

    void setSprinting(boolean sprinting);

    ItemStack[] getInventory();

    float getSoundVolume();

    float getSoundPitch();

    void dismountEntity(Entity entity);

    void jump();

    void moveEntityWithHeading(float yaw, float pitch);

    float getAIMoveSpeed();

    void setAIMoveSpeed(float speed);

    boolean attackEntityAsMob(Entity entity);

    boolean isPlayerSleeping();

    void mountEntity(Entity entityIn);

    void setJumping(boolean jumping);

    boolean canEntityBeSeen(Entity entity);

    Vector getLook(float p_70676_1_);

    boolean canBeCollidedWith();

    boolean canBePushed();

    void setBeenAttacked();

    float getAbsorptionAmount();

    void setAbsorptionAmount(float amount);

    /*Team getTeam();*/

    boolean isOnSameTeam(EntityLivingBase entityLivingBase);

    /*boolean isOnTeam(Team p_142012_1_);*/

}
