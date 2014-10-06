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

package org.granitemc.granite.entity;

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.EntityLivingBase;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.utils.MinecraftUtils;

import java.util.Collection;

public class GraniteEntityLivingBase extends GraniteEntity implements EntityLivingBase {

    public GraniteEntityLivingBase(Object parent) {
        super(parent);
    }

    public GraniteEntityLivingBase(Object parent, boolean bool) {
        super(parent);
    }

    public void killByVoidDamage() {
        invoke("n.m.entity.EntityLivingBase", "killByVoidDamage");
    }

    public boolean canBreatheUnderwater() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "canBreathUnderwater");
    }

    public boolean isChild() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isChild");
    }

    public boolean isAdult() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isAdult");
    }

    public int decreaseAirSupply(int amount) {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "decreaseAirSupply", amount);
    }

    public int getExperiencePoints(Player player) {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getExperiencePoints", player);
    }

    public boolean isPlayer() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPlayer");
    }

    public EntityLivingBase getAITarget() {
        return (EntityLivingBase) invoke("n.m.entity.EntityLivingBase", "getAITarget");
    }

    public int getRevengeTimer() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getRevengeTimer");
    }

    public void setRevengeTarget(EntityLivingBase entity) {
        invoke("n.m.entity.EntityLivingBase", "setRevengeTarget");
    }

    public EntityLivingBase getLastAttacker() {
        return (EntityLivingBase) invoke("n.m.entity.EntityLivingBase", "getLastAttacker");
    }

    public int getLastAttackerTime() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getLastAttackerTimer");
    }

    public void setLastAttacker(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "setLastAttacker", entity);
    }

    public int getAge() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getAge");
    }

    public void clearActivePotions() {
        invoke("n.m.entity.EntityLivingBase", "clearActivePotions");
    }

    public Collection getActivePotionEffects() {
        return (Collection) invoke("n.m.entity.EntityLivingBase", "getActivePotionEffects");
    }

    public boolean isPotionActive(int potion) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPotionActive", potion);
    }

    //TODO: Enable after Potion is made
    /*
    public boolean isPotionActive(Potion potion) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPotionActive", potion);
    }

    public PotionEffect getActivePotionEffect(Potion potion) {
        return (PotionEffect) invoke("n.m.entity.EntityLivingBase", "getActivePotionEffect", potion);
    }


    public void addPotionEffect(PotionEffect potion) {
        invoke("n.m.entity.EntityLivingBase", "addPotionEffect", potion);
    }

    public boolean isPotionApplicable(PotionEffect potion) {
        invoke (boolean) ("n.m.entity.EntityLivingBase", "isPotionApplicable", potion);
    }*/

    public boolean isEntityUndead() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isEntityUndead");
    }

    public void removePotionEffectClient(int potion) {
        invoke("n.m.entity.EntityLivingBase", "removePotionEffectClient", potion);
    }

    public void removePotionEffect(int potion) {
        invoke("n.m.entity.EntityLivingBase", "removePotionEffect", potion);
    }

    public void heal(float amount) {
        invoke("n.m.entity.EntityLivingBase", "heal", amount);
    }

    public final float getHealth() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getHealth");
    }

    public void setHealth(float amount) {
        invoke("n.m.entity.EntityLivingBase", "setHealth", amount);
    }

    //TODO: Enable after DamageSource is made
    /*
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "attackEntityFrom", source, amount);
    }*/

    //TODO: Work out parameters
    public void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        invoke("n.m.entity.EntityLivingBase", "knockBack", entity, p_70653_2_, p_70653_3_, p_70653_5_);
    }

    public String getHurtSound() {
        return (String) invoke("n.m.entity.EntityLivingBase", "getHurtSound");
    }

    public String getDeathSound() {
        return (String) invoke("n.m.entity.EntityLivingBase", "getDeathSound");
    }

    public boolean isOnLadder() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "inOnLadder");
    }

    public boolean isEntityAlive() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isEntityAlive");
    }

    public void fall(float distance, float damageMultiplier) {
        invoke("n.m.entity.EntityLivingBase", "fall", distance, damageMultiplier);
    }

    public String getFallDamageSound(int blocksFallen) {
        return (String) invoke("n.m.entity.EntityLivingBase", "getFallDamageSound", blocksFallen);
    }

    public void performHurtAnimation() {
        invoke("n.m.entity.EntityLivingBase", "preformHurtAnimation");
    }

    public int getTotalArmorValue() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getTotalArmorValue");
    }

    //TODO: Work out what the float does on this method & enable after DamageSource has been made
    /*
    public float applyArmorCalculations(DamageSource damageSource, float p_70655_2_) {
        return (float) invoke("n.m.entity.EntityLivingBase", "applyArmorCalculations", damageSource, p_70655_2_);
    }

    public float applyPotionDamageCalculations(DamageSource damageSource, float p_70672_2_) {
        return (float) invoke("n.m.entity.EntityLivingBase", "applyPotionDamageCalculations", damageSource, p_70672_2_);
    }

    public void damageEntity(DamageSource damageSource, float p_70665_2_) {
        invoke("n.m.entity.EntityLivingBase", "damageEntity", damageSource, p_70665_2_);
    }*/

    public final float getMaxHealth() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getMaxHealth");
    }

    public final int getArrowCountInEntity() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getArrowCountInEntity");
    }

    public final void setArrowCountInEntity(int amount) {
        invoke("n.m.entity.EntityLivingBase", "setArrowCountInEntity", amount);
    }

    public void swingItem() {
        invoke("n.m.entity.EntityLivingBase", "swingItem");
    }

    public void kill() {
        invoke("n.m.entity.EntityLivingBase", "kill");
    }

    public ItemStack getHeldItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("n.m.entity.EntityLivingBase", "getHeldItem"));
    }

    public ItemStack getEquipmentInSlot(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("n.m.entity.EntityLivingBase", "getEquipmentInSlot, slot"));
    }

    public ItemStack getCurrentArmor(int armorSlot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("n.m.entity.EntityLivingBase", "getCurrentArmor", armorSlot));
    }

    public void setCurrentItemOrArmor(int slot, IItemStack itemStack) {
        invoke("n.m.entity.EntityLivingBase", "setCurrentItemOrArmot", slot, itemStack);
    }

    public void setSprinting(boolean sprinting) {
        invoke("n.m.entity.EntityLivingBase", "setSprinting", sprinting);
    }

    public ItemStack[] getInventory() {
        return (ItemStack[]) MinecraftUtils.wrap(invoke("n.m.entity.EntityLivingBase", "getInventory"));
    }

    public void dismountEntity(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "dismountEntity", entity);
    }

    public void jump() {
        invoke("n.m.entity.EntityLivingBase", "jump");
    }

    public float getAIMoveSpeed() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getAIMoveSpeed");
    }

    public void setAIMoveSpeed(float speed) {
        invoke("n.m.entity.EntityLivingBase", "setAIMoveSpeed", speed);
    }

    public void mountEntity(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "mountEntity", entity);
    }

    public void setJumping(boolean jumping) {
        invoke("n.m.entity.EntityLivingBase", "setJumping", jumping);
    }

    public boolean canEntityBeSeen(Entity entity) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "canEntityBeSeen", entity);
    }

    public float getAbsorptionAmount() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getAbsorptionAmount");
    }

    public void setAbsorptionAmount(float amount) {
        invoke("n.m.entity.EntityLivingBase", "setAbsorptionAmount", amount);
    }

    @Override
    public Vector getLookDirection() {
        return MinecraftUtils.fromMinecraftVector(invoke("n.m.entity.Entity", "getLook", 1.0F));
    }

    @Override
    public RayTraceResult rayTrace(double maxDistance, boolean stopOnLiquid) {
        Vector eyeVector = new Vector(getX(), getY(), getZ());
        eyeVector.setY(eyeVector.getY() + getEyeHeight());

        return getWorld().rayTrace(eyeVector, getLookDirection(), maxDistance, stopOnLiquid);
    }

    @Override
    public float getEyeHeight() {
        return (float) invoke("n.m.entity.Entity", "getEyeHeight");
    }



    //TODO: Enable after Team has been made
    /*
    public Team getTeam() {
        return (Team) invoke("n.m.entity.EntityLivingBase", "getTeam");
    }

    public boolean isOnSameTeam(EntityLivingBase entity) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isOnSameTeam", entity);
    }

    public boolean isOnTeam(Team team) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isOnTeam", team);
    }*/

}
