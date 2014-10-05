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

package org.granitemc.granite.entity.item;

import org.granitemc.granite.api.entity.item.EntityArmorStand;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Rotations;
import org.granitemc.granite.entity.GraniteEntityLivingBase;
import org.granitemc.granite.utils.MinecraftUtils;

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
    public ItemStack getCurrentArmor(int armorSlot) {
        return (ItemStack) invoke("n.m.entity.item.EntityArmorStand", "getCurrentArmor", armorSlot);
    }

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

    public void playParticles() {
        invoke("n.m.entity.item.EntityArmorStand", "playParticles");
    }

    public void dropBlock() {
        invoke("n.m.entity.item.EntityArmorStand", "dropBlock");
    }

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
        invoke("n.m.entity.item.EntityArmorStand", "setHeadRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public void setBodyRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setBodyRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public void setLeftArmRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setLeftArmRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public void setRightArmRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setRightArmRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public void setLeftLegRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setLeftLegRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public void setRightLegRotation(Rotations rotations) {
        invoke("n.m.entity.item.EntityArmorStand", "setRightLegRotation", MinecraftUtils.toMinecraftRotations(rotations));
    }

    @Override
    public Rotations getHeadRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getHeadRostion"));
    }

    @Override
    public Rotations getBodyRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getBodyRotation"));
    }

    @Override
    public Rotations getLeftArmRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getLeftArmRotation"));
    }

    @Override
    public Rotations getRightArmRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getRightArmRotation"));
    }

    @Override
    public Rotations getLeftLegRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getLeftLegRotation"));
    }

    @Override
    public Rotations getRightLegRotation() {
        return MinecraftUtils.fromMinecraftRotations(invoke("n.m.entity.item.EntityArmorStand", "getRightLegRotation"));
    }

}
