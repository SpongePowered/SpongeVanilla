package org.granitemc.granite.api.entity;

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.api.item.ItemStack;

import java.util.Collection;

public interface EntityLivingBase extends Entity {

    void killByVoidDamage();

    boolean canBreatheUnderwater();

    boolean isChild();

    boolean isAdult();

    int decreaseAirSupply(int amount);

    int getExperiencePoints(Player player);

    boolean isPlayer();

    EntityLivingBase getAITarget();

    int getRevengeTimer();

    void setRevengeTarget(EntityLivingBase entity);

    EntityLivingBase getLastAttacker();
    
    int getLastAttackerTime();

    void setLastAttacker(Entity entity);

    int getAge();

    void clearActivePotions();

    Collection getActivePotionEffects();

    boolean isPotionActive(int potion);

    //TODO: Enable after Potion is made
    /*boolean isPotionActive(Potion potion);

    PotionEffect getActivePotionEffect(Potion potion);

    void addPotionEffect(PotionEffect potion);

    boolean isPotionApplicable(PotionEffect potion);*/

    boolean isEntityUndead();

    void removePotionEffectClient(int potion);

    void removePotionEffect(int potion);

    void heal(float amount);

    float getHealth();

    void setHealth(float amount);

    //TODO: Enable after DamageSource is made
    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    //TODO: Work out parameters
    void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_) ;

    String getHurtSound();

    String getDeathSound();

    boolean isOnLadder();

    boolean isEntityAlive();

    void fall(float distance, float damageMultiplier);

    String getFallDamageSound(int blocksFallen);

    void performHurtAnimation();

    int getTotalArmorValue();

    //TODO: Work out what the float does on this method & enable after DamageSource has been made
    /*float applyArmorCalculations(DamageSource damageSource, float p_70655_2_);

    float applyPotionDamageCalculations(DamageSource damageSource, float p_70672_2_);

    void damageEntity(DamageSource damageSource, float p_70665_2_);*/

    float getMaxHealth();

    int getArrowCountInEntity();

    void setArrowCountInEntity(int amount);

    void swingItem();

    void kill();

    ItemStack getHeldItem();

    ItemStack getEquipmentInSlot(int slot);

    ItemStack getCurrentArmor(int armotSlot);

    void setCurrentItemOrArmor(int slot, IItemStack itemStack);

    void setSprinting(boolean sprinting);

    ItemStack[] getInventory();

    void dismountEntity(Entity entity);

    void jump();

    float getAIMoveSpeed();

    void setAIMoveSpeed(float speed);

    void mountEntity(Entity entity);

    void setJumping(boolean jumping);

    boolean canEntityBeSeen(Entity entity);

    float getAbsorptionAmount();

    void setAbsorptionAmount(float amount);

    //TODO: Enable after Team has been made
    /*Team getTeam();

    boolean isOnSameTeam(EntityLivingBase entity);

    boolean isOnTeam(Team team);*/
    
}
