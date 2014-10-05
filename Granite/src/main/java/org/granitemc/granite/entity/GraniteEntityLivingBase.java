package org.granitemc.granite.entity;

import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.EntityLivingBase;
import org.granitemc.granite.api.utils.Facing;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.util.Collection;

public class GraniteEntityLivingBase extends GraniteEntity implements EntityLivingBase {

    public GraniteEntityLivingBase(Object parent) {
        super(parent);
    }

    public GraniteEntityLivingBase(Object parent, boolean bool) {
        super(parent);
    }

    @Override
    public void killByVoidDamage() {
        invoke("n.m.entity.EntityLivingBase", "killByVoidDamage");
    }

    @Override
    public boolean canBreatheUnderwater() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "canBreathUnderwater");
    }

    @Override
    public boolean isChild() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isChild");
    }

    @Override
    public boolean isAdult() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isAdult");
    }

    @Override
    public int decreaseAirSupply(int amount) {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "decreaseAirSupply", amount);
    }

    @Override
    public int getExperiencePoints(Player player) {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getExperiencePoints", player);
    }

    @Override
    public boolean isPlayer() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPlayer");
    }

    @Override
    public EntityLivingBase getAITarget() {
        return (EntityLivingBase) invoke("n.m.entity.EntityLivingBase", "getAITarget");
    }

    @Override
    public int getRevengeTimer() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getRevengeTimer");
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entity) {
        invoke("n.m.entity.EntityLivingBase", "setRevengeTarget");
    }

    @Override
    public EntityLivingBase getLastAttacker() {
        return (EntityLivingBase) invoke("n.m.entity.EntityLivingBase", "getLastAttacker");
    }

    @Override
    public int getLastAttackerTime() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getLastAttackerTimer");
    }

    @Override
    public void setLastAttacker(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "setLastAttacker", entity);
    }

    @Override
    public int getAge() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getAge");
    }

    @Override
    public void clearActivePotions() {
        invoke("n.m.entity.EntityLivingBase", "clearActivePotions");
    }

    @Override
    public Collection getActivePotionEffects() {
        return (Collection) invoke("n.m.entity.EntityLivingBase", "getActivePotionEffects");
    }

    @Override
    public boolean isPotionActive(int potion) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPotionActive", potion);
    }


    //TODO: Enable after Potion is made
    /*@Override
    public boolean isPotionActive(Potion potion) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isPotionActive", potion);
    }

    @Override
    public PotionEffect getActivePotionEffect(Potion potion) {
        return (PotionEffect) invoke("n.m.entity.EntityLivingBase", "getActivePotionEffect", potion);
    }

    @Override
    public void addPotionEffect(PotionEffect potion) {
        invoke("n.m.entity.EntityLivingBase", "addPotionEffect", potion);
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potion) {
        invoke (boolean) ("n.m.entity.EntityLivingBase", "isPotionApplicable", potion);
    }*/

    @Override
    public boolean isEntityUndead() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isEntityUndead");
    }

    @Override
    public void removePotionEffectClient(int potion) {
        invoke("n.m.entity.EntityLivingBase", "removePotionEffectClient", potion);
    }

    @Override
    public void removePotionEffect(int potion) {
        invoke("n.m.entity.EntityLivingBase", "removePotionEffect", potion);
    }

    @Override
    public void heal(float amount) {
        invoke("n.m.entity.EntityLivingBase", "heal", amount);
    }

    @Override
    public final float getHealth() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getHealth");
    }

    @Override
    public void setHealth(float amount) {
        invoke("n.m.entity.EntityLivingBase", "setHealth", amount);
    }

    //TODO: Enable after DamageSource is made
    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "attackEntityFrom", source, amount);
    }*/

    //TODO: Work out parameters
    @Override
    public void knockBack(Entity entity, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        invoke("n.m.entity.EntityLivingBase", "knockBack", entity, p_70653_2_, p_70653_3_, p_70653_5_);
    }

    @Override
    public String getHurtSound() {
        return (String) invoke("n.m.entity.EntityLivingBase", "getHurtSound");
    }

    @Override
    public String getDeathSound() {
        return (String) invoke("n.m.entity.EntityLivingBase", "getDeathSound");
    }

    @Override
    public boolean isOnLadder() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "inOnLadder");
    }

    @Override
    public boolean isEntityAlive() {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isEntityAlive");
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        invoke("n.m.entity.EntityLivingBase", "fall", distance, damageMultiplier);
    }

    @Override
    public String getFallDamageSound(int blocksFallen) {
        return (String) invoke("n.m.entity.EntityLivingBase", "getFallDamageSound", blocksFallen);
    }

    @Override
    public void performHurtAnimation() {
        invoke("n.m.entity.EntityLivingBase", "preformHurtAnimation");
    }

    @Override
    public int getTotalArmorValue() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getTotalArmorValue");
    }

    //TODO: Work out what the float does on this method & enable after DamageSource has been made
    /*@Override
    public float applyArmorCalculations(DamageSource damageSource, float p_70655_2_) {
        return (float) invoke("n.m.entity.EntityLivingBase", "applyArmorCalculations", damageSource, p_70655_2_);
    }

    @Override
    public float applyPotionDamageCalculations(DamageSource damageSource, float p_70672_2_) {
        return (float) invoke("n.m.entity.EntityLivingBase", "applyPotionDamageCalculations", damageSource, p_70672_2_);
    }

    @Override
    public void damageEntity(DamageSource damageSource, float p_70665_2_) {
        invoke("n.m.entity.EntityLivingBase", "damageEntity", damageSource, p_70665_2_);
    }*/

    @Override
    public final float getMaxHealth() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getMaxHealth");
    }

    @Override
    public final int getArrowCountInEntity() {
        return (Integer) invoke("n.m.entity.EntityLivingBase", "getArrowCountInEntity");
    }

    @Override
    public final void setArrowCountInEntity(int amount) {
        invoke("n.m.entity.EntityLivingBase", "setArrowCountInEntity", amount);
    }

    @Override
    public void swingItem() {
        invoke("n.m.entity.EntityLivingBase", "swingItem");
    }

    @Override
    public void kill() {
        invoke("n.m.entity.EntityLivingBase", "kill");
    }

    @Override
    public IItemStack getHeldItem() {
        return (IItemStack) invoke("n.m.entity.EntityLivingBase", "getHeldItem");
    }

    @Override
    public IItemStack getEquipmentInSlot(int slot) {
        return (IItemStack) invoke("n.m.entity.EntityLivingBase", "getEquiptmentInSlot, slot");
    }

    @Override
    public IItemStack getCurrentArmor(int armotSlot) {
        return (IItemStack) invoke("n.m.entity.EntityLivingBase", "getCurrentArmor", armotSlot);
    }

    @Override
    public void setCurrentItemOrArmor(int slot, IItemStack itemStack) {
        invoke("n.m.entity.EntityLivingBase", "setCurrentItemOrArmot", slot, itemStack);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        invoke("n.m.entity.EntityLivingBase", "setSprinting", sprinting);
    }

    @Override
    public IItemStack[] getInventory() {
        return (IItemStack[]) invoke("n.m.entity.EntityLivingBase", "getInventory");
    }

    @Override
    public void dismountEntity(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "dismountEntity", entity);
    }

    @Override
    public void jump() {
        invoke("n.m.entity.EntityLivingBase", "jump");
    }

    @Override
    public float getAIMoveSpeed() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getAIMoveSpeed");
    }

    @Override
    public void setAIMoveSpeed(float speed) {
        invoke("n.m.entity.EntityLivingBase", "setAIMoveSpeed", speed);
    }

    @Override
    public void mountEntity(Entity entity) {
        invoke("n.m.entity.EntityLivingBase", "mountEntity", entity);
    }

    @Override
    public void setJumping(boolean jumping) {
        invoke("n.m.entity.EntityLivingBase", "setJumping", jumping);
    }

    @Override
    public boolean canEntityBeSeen(Entity entity) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "canEntityBeSeen", entity);
    }

    @Override
    public float getAbsorptionAmount() {
        return (float) invoke("n.m.entity.EntityLivingBase", "getAbsorptionAmount");
    }

    @Override
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
    /*@Override
    public Team getTeam() {
        return (Team) invoke("n.m.entity.EntityLivingBase", "getTeam");
    }

    @Override
    public boolean isOnSameTeam(EntityLivingBase entity) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isOnSameTeam", entity);
    }

    @Override
    public boolean isOnTeam(Team team) {
        return (boolean) invoke("n.m.entity.EntityLivingBase", "isOnTeam", team);
    }*/

}
