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
        invoke("killByVoidDamage");
    }

    public boolean canBreatheUnderwater() {
        return (boolean) invoke("canBreathUnderwater");
    }

    public boolean isChild() {
        return (boolean) invoke("isChild");
    }

    public boolean isAdult() {
        return (boolean) invoke("isAdult");
    }

    public int decreaseAirSupply(int amount) {
        return (int) invoke("decreaseAirSupply", amount);
    }

    public int getExperiencePoints(Player player) {
        return (Integer) invoke("getExperiencePoints", player);
    }

    public boolean isPlayer() {
        return (boolean) invoke("isPlayer");
    }

    public EntityLivingBase getAITarget() {
        return (EntityLivingBase) invoke("getAITarget");
    }

    public int getRevengeTimer() {
        return (Integer) invoke("getRevengeTimer");
    }

    public void setRevengeTarget(EntityLivingBase entity) {
        invoke("setRevengeTarget");
    }

    public EntityLivingBase getLastAttacker() {
        return (EntityLivingBase) invoke("getLastAttacker");
    }

    public int getLastAttackerTime() {
        return (Integer) invoke("getLastAttackerTimer");
    }

    public void setLastAttacker(Entity entity) {
        invoke("setLastAttacker", entity);
    }

    public int getAge() {
        return (Integer) invoke("getAge");
    }

    public void clearActivePotions() {
        invoke("clearActivePotions");
    }

    public Collection getActivePotionEffects() {
        return (Collection) invoke("getActivePotionEffects");
    }

    public boolean isPotionActive(int potion) {
        return (boolean) invoke("isPotionActive", potion);
    }

    //TODO: Enable after Potion is made
    /*
    public boolean isPotionActive(Potion potion) {
        return (boolean) invoke("isPotionActive", potion);
    }

    public PotionEffect getActivePotionEffect(Potion potion) {
        return (PotionEffect) invoke("getActivePotionEffect", potion);
    }


    public void addPotionEffect(PotionEffect potion) {
        invoke("addPotionEffect", potion);
    }

    public boolean isPotionApplicable(PotionEffect potion) {
        invoke (boolean) ("isPotionApplicable", potion);
    }*/

    public boolean isEntityUndead() {
        return (boolean) invoke("isEntityUndead");
    }

    public void removePotionEffectClient(int potion) {
        invoke("removePotionEffectClient", potion);
    }

    public void removePotionEffect(int potion) {
        invoke("removePotionEffect", potion);
    }

    public void heal(float amount) {
        invoke("heal", amount);
    }

    public final float getHealth() {
        return (float) invoke("getHealth");
    }

    public void setHealth(float amount) {
        invoke("setHealth", amount);
    }

    //TODO: Enable after DamageSource is made
    /*
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("attackEntityFrom", source, amount);
    }*/

    //TODO: Work out parameters
    public void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        invoke("knockBack", entity, p_70653_2_, p_70653_3_, p_70653_5_);
    }

    public String getHurtSound() {
        return (String) invoke("getHurtSound");
    }

    public String getDeathSound() {
        return (String) invoke("getDeathSound");
    }

    public boolean isOnLadder() {
        return (boolean) invoke("inOnLadder");
    }

    public boolean isEntityAlive() {
        return (boolean) invoke("isEntityAlive");
    }

    public void fall(float distance, float damageMultiplier) {
        invoke("fall", distance, damageMultiplier);
    }

    public String getFallDamageSound(int blocksFallen) {
        return (String) invoke("getFallDamageSound", blocksFallen);
    }

    public void performHurtAnimation() {
        invoke("preformHurtAnimation");
    }

    public int getTotalArmorValue() {
        return (Integer) invoke("getTotalArmorValue");
    }

    //TODO: Work out what the float does on this method & enable after DamageSource has been made
    /*
    public float applyArmorCalculations(DamageSource damageSource, float p_70655_2_) {
        return (float) invoke("applyArmorCalculations", damageSource, p_70655_2_);
    }

    public float applyPotionDamageCalculations(DamageSource damageSource, float p_70672_2_) {
        return (float) invoke("applyPotionDamageCalculations", damageSource, p_70672_2_);
    }

    public void damageEntity(DamageSource damageSource, float p_70665_2_) {
        invoke("damageEntity", damageSource, p_70665_2_);
    }*/

    public final float getMaxHealth() {
        return (float) invoke("getMaxHealth");
    }

    public final int getArrowCountInEntity() {
        return (Integer) invoke("getArrowCountInEntity");
    }

    public final void setArrowCountInEntity(int amount) {
        invoke("setArrowCountInEntity", amount);
    }

    public void swingItem() {
        invoke("swingItem");
    }

    public void kill() {
        invoke("kill");
    }

    public ItemStack getHeldItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("getHeldItem"));
    }

    public ItemStack getEquipmentInSlot(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("getEquipmentInSlot, slot"));
    }

    public ItemStack getCurrentArmor(int armorSlot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("getCurrentArmor", armorSlot));
    }

    public void setCurrentItemOrArmor(int slot, ItemStack itemStack) {
        invoke("setCurrentItemOrArmot", slot, itemStack);
    }

    public void setSprinting(boolean sprinting) {
        invoke("setSprinting", sprinting);
    }

    public ItemStack[] getInventory() {
        return (ItemStack[]) MinecraftUtils.wrap(invoke("getInventory"));
    }

    public void dismountEntity(Entity entity) {
        invoke("dismountEntity", entity);
    }

    public void jump() {
        invoke("jump");
    }

    public float getAIMoveSpeed() {
        return (float) invoke("getAIMoveSpeed");
    }

    public void setAIMoveSpeed(float speed) {
        invoke("setAIMoveSpeed", speed);
    }

    public void mountEntity(Entity entity) {
        invoke("mountEntity", entity);
    }

    public void setJumping(boolean jumping) {
        invoke("setJumping", jumping);
    }

    public boolean canEntityBeSeen(Entity entity) {
        return (boolean) invoke("canEntityBeSeen", entity);
    }

    public float getAbsorptionAmount() {
        return (float) invoke("getAbsorptionAmount");
    }

    public void setAbsorptionAmount(float amount) {
        invoke("setAbsorptionAmount", amount);
    }

    @Override
    public Vector getLookDirection() {
        return MinecraftUtils.fromMinecraftVector(invoke("getLook", 1.0F));
    }

    @Override
    public RayTraceResult rayTrace(double maxDistance, boolean stopOnLiquid) {
        Vector eyeVector = new Vector(getX(), getY(), getZ());
        eyeVector.setY(eyeVector.getY() + getEyeHeight());

        return getWorld().rayTrace(eyeVector, getLookDirection(), maxDistance, stopOnLiquid);
    }

    @Override
    public float getEyeHeight() {
        return (float) invoke("getEyeHeight");
    }



    //TODO: Enable after Team has been made
    /*
    public Team getTeam() {
        return (Team) invoke("getTeam");
    }

    public boolean isOnSameTeam(EntityLivingBase entity) {
        return (boolean) invoke("isOnSameTeam", entity);
    }

    public boolean isOnTeam(Team team) {
        return (boolean) invoke("isOnTeam", team);
    }*/

}
