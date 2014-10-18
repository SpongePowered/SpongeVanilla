package org.granitemc.granite.entity;

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.EntityLivingBase;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.entity.player.GraniteEntityPlayer;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.utils.MinecraftUtils;

import java.util.Collection;

public class GraniteEntityLivingBase extends GraniteEntity implements EntityLivingBase {

    public GraniteEntityLivingBase(Object parent) {
        super(parent);
    }

    @Override
    public void killByVoid() {
        invoke("killByVoid");
    }

    @Override
    public boolean canBreatheUnderwater() {
        return (boolean) invoke("canBreatheUnderwater");
    }

    @Override
    public boolean isChild() {
        return (boolean) invoke("isChild");
    }

    @Override
    public boolean isAdult() {
        return (boolean) invoke("isAdult");
    }

    @Override
    public int decreaseAirSupply(int amount) {
        return (int) invoke("decreaseAirSupply", amount);
    }

    @Override
    public int getExperiencePoints(Player player) {
        return (Integer) invoke("getExperiencePoints", player);
    }

    @Override
    public boolean isPlayer() {
        return (boolean) invoke("isPlayer");
    }

    @Override
    public EntityLivingBase getAITarget() {
        return (EntityLivingBase) MinecraftUtils.wrap(invoke("getAITarget"));
    }

    @Override
    public int getRevengeTimer() {
        return (Integer) invoke("getRevengeTimer");
    }

    @Override
    public void setRevengeTarget(EntityLivingBase entityLivingBase) {
        invoke("setRevengeTarget", ((GraniteEntityLivingBase) entityLivingBase).parent);
    }

    @Override
    public EntityLivingBase getLastAttacker() {
        return (EntityLivingBase) MinecraftUtils.wrap(invoke("getLastAttacker"));
    }

    @Override
    public int getLastAttackerTime() {
        return (Integer) invoke("getLastAttackerTime");
    }

    @Override
    public void setLastAttacker(Entity entity) {
        invoke("setLastAttacker", ((GraniteEntity) entity).parent);
    }

    @Override
    public int getAge() {
        return (Integer) invoke("getAge");
    }

    @Override
    public void clearActivePotions() {
        invoke("clearActivePotions");
    }

    @Override
    public Collection getActivePotionEffects() {
        return (Collection) invoke("getActivePotionEffects");
    }

    @Override
    public boolean isPotionActive(int potion) {
        return (boolean) invoke("isPotionActive", potion);
    }

    @Override
    public boolean isEntityUndead() {
        return (boolean) invoke("isEntityUndead");
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

    public void removePotionEffectClient(int potion) {
        invoke("removePotionEffectClient", potion);
    }

    @Override
    public void removePotionEffect(int potion) {
        invoke("removePotionEffect", potion);
    }

    @Override
    public void heal(float amount) {
        invoke("heal", amount);
    }

    @Override
    public float getHealth() {
        return (float) invoke("getHealth");
    }

    @Override
    public void setHealth(float amount) {
        invoke("setHealth", amount);
    }

    @Override
    public void renderBrokenItemStack(ItemStack itemStack) {
        invoke("renderBrokenItemStack", ((GraniteItemStack) itemStack).parent);
    }

    @Override
    public void knockBack(Entity entity, float par1, double par2, double par3) {
        invoke("knockBack", ((GraniteEntity) entity).parent, par1, par2, par3);
    }

    //TODO: Enable after DamageSource is made
    /*
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("attackEntityFrom", source, amount);
    }*/

    @Override
    public String getHurtSound() {
        return (String) invoke("getHurtSound");
    }

    @Override
    public String getDeathSound() {
        return (String) invoke("getDeathSound");
    }

    @Override
    public void addRandomArmor() {
        invoke("addRandomArmor");
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

    @Override
    public String getFallDamageSound(int blocksFallen) {
        return (String) invoke("getFallDamageSound", blocksFallen);
    }

    public void performHurtAnimation() {
        invoke("preformHurtAnimation");
    }

    @Override
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

    @Override
    public void swingItem() {
        invoke("swingItem");
    }

    public void kill() {
        invoke("kill");
    }

    @Override
    public ItemStack getHeldItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("getHeldItem"));
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return (ItemStack) MinecraftUtils.wrap(invoke("getEquipmentInSlot", slot));
    }

    @Override
    public float getSoundVolume() {
        return (float) invoke("getSoundVolume");
    }

    @Override
    public float getSoundPitch() {
        return (float) invoke("getSoundPitch");
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

    @Override
    public void dismountEntity(Entity entity) {
        invoke("dismountEntity", ((GraniteEntity) entity).parent);
        invoke("dismountEntity", entity);
    }

    @Override
    public void jump() {
        invoke("jump");
    }

    @Override
    public void moveEntityWithHeading(float yaw, float pitch) {
        invoke("moveEntityWithHeading", yaw, pitch);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return (boolean) invoke("attackEntityAsMob", ((GraniteEntity) entity).parent);
    }

    @Override
    public boolean isPlayerSleeping() {
        return (boolean) invoke("isPlayerSleeping");
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

    @Override
    public Vector getLook(float par1) {
        return MinecraftUtils.fromMinecraftVector(invoke("getLook", par1));
    }

    @Override
    public boolean canEntityBeSeen(Entity entity) {
        return (boolean) invoke("canEntityBeSeen", ((GraniteEntity) entity).parent);
    }

    @Override
    public float getAbsorptionAmount() {
        return (float) invoke("getAbsorptionAmount");
    }

    @Override
    public void setAbsorptionAmount(float amount) {
        invoke("setAbsorptionAmount", amount);
    }

    @Override
    public boolean isOnSameTeam(EntityLivingBase entityLivingBase) {
        return (boolean) invoke("isOnTheSameTeam", ((GraniteEntityLivingBase) entityLivingBase).parent);
    }

    @Override
    public RayTraceResult rayTrace(double maxDistance, boolean stopOnLiquid) {
        Location location = getLocation();
        Vector eyeVector = new Vector(location.getX(), location.getY(), location.getZ());
        eyeVector.setY(eyeVector.getY() + getEyeHeight());

        return getWorld().rayTrace(eyeVector, getLook(1.0f), maxDistance, stopOnLiquid);
    }


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
