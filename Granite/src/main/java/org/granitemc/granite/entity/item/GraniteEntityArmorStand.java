package org.granitemc.granite.entity.item;

import org.granitemc.granite.api.entity.item.EntityArmorStand;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Rotations;
import org.granitemc.granite.entity.GraniteEntityLivingBase;

public class GraniteEntityArmorStand extends GraniteEntityLivingBase implements EntityArmorStand {

    public GraniteEntityArmorStand(Object parent, boolean bool) {
        super(parent, bool);
    }

    public GraniteEntityArmorStand(Object parent) {
        super(parent);
    }

    @Override
    public ItemStack getHeldItem() {
        return (ItemStack) invoke("n.m.entity.item.EntityArmorStand", "getHeldItem");
    }

    @Override
    public ItemStack getEquipmentInSlot(int slot) {
        return (ItemStack) invoke("n.m.entity.item.EntityArmorStand", "getEquipmentInSlot", slot);
    }

    @Override
    public ItemStack getCurrentArmor(int slot) {
        return (ItemStack) invoke("n.m.entity.item.EntityArmorStand", "getCurrentArmor", slot);
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack itemStack) {
        invoke("n.m.entity.item.EntityArmorStand", "setCurrentItemOrArmor", slot, itemStack);
    }

    @Override
    public ItemStack[] getInventory() {
        return (ItemStack[]) invoke("n.m.entity.item.EntityArmorStand", "getInventory");
    }

    @Override
    public boolean canBePushed() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "canBePushed");
    }

    // TODO: Enable when DamageSource has been made
    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "attackEntityFrom", source, amount);
    }*/

    @Override
    public void playParticles() {
        invoke("n.m.entity.item.EntityArmorStand", "playParticles");
    }

    @Override
    public void dropBlock() {
        invoke("n.m.entity.item.EntityArmorStand", "dropBlock");
    }

    @Override
    public void dropContents() {
        invoke("n.m.entity.item.EntityArmorStand", "dropContents");
    }

    @Override
    public void setInvisible(boolean invisible) {
        invoke("n.m.entity.item.EntityArmorStand", "isInvisible", invisible);
    }

    @Override
    public boolean isChild() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "isChild");
    }

    // func_174812_G
    @Override
    public void kill() {
        invoke("n.m.entity.item.EntityArmorStand", "kill");
    }

    // func_180427_aV()
    @Override
    public boolean isInvisible() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "isInvisible");
    }

    @Override
    public void setSmall(boolean size) {
        invoke("n.m.entity.item.EntityArmorStand", "setSmall", size);
    }

    @Override
    public boolean isSmall() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "isSmall");
    }

    @Override
    public void setNoGravity(boolean gravity) {
        invoke("n.m.entity.item.EntityArmorStand", "setNoGravity", gravity);
    }

    @Override
    public boolean hasNoGravity() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "hasNoGravity");
    }

    @Override
    public void setShowArms(boolean showArms) {
        invoke("n.m.entity.item.EntityArmorStand", "setShowArms", showArms);
    }

    @Override
    public boolean getShowArms() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "getShowArms");
    }

    @Override
    public void setNoBasePlate(boolean basePlate) {
        invoke("n.m.entity.item.EntityArmorStand", "setNoBasePlate", basePlate);
    }

    @Override
    public boolean hasNoBasePlate() {
        return (boolean) invoke("n.m.entity.item.EntityArmorStand", "hasNoBasePlate");
    }

    @Override
    public void setHeadRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setHeadRotation", rotations);
    }

    @Override
    public void setBodyRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setBodyRotation", rotations);
    }

    @Override
    public void setLeftArmRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setLeftArmRotation", rotations);
    }

    @Override
    public void setRightArmRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setRightArmRotation", rotations);
    }

    @Override
    public void setLeftLegRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setLeftLegRotation", rotations);
    }

    @Override
    public void setRightLegRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setRightLegRotation", rotations);
    }

    @Override
    public Rotations getHeadRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getHeadRostion");
    }

    @Override
    public Rotations getBodyRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getBodyRotation");
    }

    @Override
    public Rotations getLeftArmRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getLeftArmRotation");
    }

    @Override
    public Rotations getRightArmRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getRightArmRotation");
    }

    @Override
    public Rotations getLeftLegRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getLeftLegRotation");
    }

    @Override
    public Rotations getRightLegRotation() {
        return (Rotations) invoke("n.m.entity.item.EntityArmorStand", "getRightLegRotation");
    }

}
