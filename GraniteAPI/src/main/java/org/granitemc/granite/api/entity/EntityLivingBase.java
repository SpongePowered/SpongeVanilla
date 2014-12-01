package org.granitemc.granite.api.entity;

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

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.RayTraceResult;

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

    String getFallDamageSound(int blockFallen);

    int getTotalArmorValue();

    /*void damageEntity(DamageSource p_70665_1_, float p_70665_2_);*/

    float getMaxHealth();

    int getArrowCountInEntity();

    void setArrowCountInEntity(int amount);

    void swingItem();

    ItemStack getHeldItem();

    ItemStack getEquipmentInSlot(int slot);

    float getSoundVolume();

    float getSoundPitch();

    void dismountEntity(Entity entity);

    void jump();

    void moveEntityWithHeading(float yaw, float pitch);

    float getAIMoveSpeed();

    void setAIMoveSpeed(float speed);

    boolean attackEntityAsMob(Entity entity);

    boolean isPlayerSleeping();

    void setJumping(boolean jumping);

    boolean canEntityBeSeen(Entity entity);

    float getAbsorptionAmount();

    void setAbsorptionAmount(float amount);

    public RayTraceResult rayTrace(double maxDistance, boolean stopOnLiquid);

    /*Team getTeam();*/

    boolean isOnSameTeam(EntityLivingBase entityLivingBase);

    /*boolean isOnTeam(Team p_142012_1_);*/
}
