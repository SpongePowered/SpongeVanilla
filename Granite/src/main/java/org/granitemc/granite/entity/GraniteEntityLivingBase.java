package org.granitemc.granite.entity;

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.EntityLivingBase;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
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
    public int getExperiencePoints(Player entityPlayer) {
        return (int) invoke("getExperiencePoints", ((GraniteEntityPlayer) entityPlayer).parent);
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

    @Override
    public boolean isOnLadder() {
        return (boolean) invoke("isOnLadder");
    }

    @Override
    public String getFallDamageSound(int blocksFallen) {
        return (String) invoke("getFallDamageSound", blocksFallen);
    }

    @Override
    public int getTotalArmorValue() {
        return (Integer) invoke("getTotalArmorValue");
    }

    /*@Override
    public void damageEntity(DamageSource p_70665_1_, float p_70665_2_) {
    }*/

    @Override
    public float getMaxHealth() {
        return (float) invoke("getMaxHeath");
    }

    @Override
    public int getArrowCountInEntity() {
        return (Integer) invoke("getArrowCountInEntity");
    }

    @Override
    public void setArrowCountInEntity(int amount) {
        invoke("setArrowCountInEntity", amount);
    }

    @Override
    public void swingItem() {
        invoke("swingItem");
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

    @Override
    public void dismountEntity(Entity entity) {
        invoke("dismountEntity", ((GraniteEntity) entity).parent);
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
    public float getAIMoveSpeed() {
        return (float) invoke("getAIMoveSpeed");
    }

    @Override
    public void setAIMoveSpeed(float speed) {
        invoke("setAIMoveSpeed", speed);
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return (boolean) invoke("attackEntityAsMob", ((GraniteEntity) entity).parent);
    }

    @Override
    public boolean isPlayerSleeping() {
        return (boolean) invoke("isPlayerSleeping");
    }

    @Override
    public void setJumping(boolean jumping) {
        invoke("setJumping", jumping);
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
}
