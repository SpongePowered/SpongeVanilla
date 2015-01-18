/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.impl.entity.living;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.granitepowered.granite.mc.MCEntityArmorStand;
import org.granitepowered.granite.mc.MCItemStack;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteArmorStand extends GraniteLivingBase<MCEntityArmorStand> implements ArmorStand {

    public GraniteArmorStand(MCEntityArmorStand obj) {
        super(obj);
    }

    @Override
    public Vector3f getHeadDirection() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$headRotation());
    }

    @Override
    public void setHeadDirection(Vector3f vector3f) {
        obj.setHeadRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getBodyRotation() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$bodyRotation());
    }

    @Override
    public void setBodyDirection(Vector3f vector3f) {
        obj.setBodyRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getLeftArmDirection() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$leftArmRotation());
    }

    @Override
    public void setLeftArmDirection(Vector3f vector3f) {
        obj.setLeftArmRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getRightArmDirection() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$rightArmRotation());
    }

    @Override
    public void setRightArmDirection(Vector3f vector3f) {
        obj.setRightArmRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getLeftLegDirection() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$leftLegRotation());
    }

    @Override
    public void setLeftLegDirection(Vector3f vector3f) {
        obj.setLeftLegRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getRightLegDirection() {
        return MinecraftUtils.minecraftToGraniteRotations(obj.fieldGet$rightLegRotation());
    }

    @Override
    public void setRightLegDirection(Vector3f vector3f) {
        obj.setRightLegRotation(MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public boolean isSmall() {
        return obj.isSmall();
    }

    @Override
    public void setSmall(boolean small) {
        obj.setSmall(small);
    }

    @Override
    public boolean hasGravity() {
        return !obj.hasNoGravity();
    }

    @Override
    public void setGravity(boolean gravity) {
        obj.setNoGravity(!gravity);
    }

    @Override
    public void setInvisible(boolean invisible) {
        obj.setInvisible(invisible);
    }

    @Override
    public boolean doesShowArms() {
        return obj.getShowArms();
    }

    @Override
    public void setShowArms(boolean showArms) {
        obj.setShowArms(showArms);
    }

    @Override
    public boolean hasBasePlate() {
        return !obj.hasNoBasePlate();
    }

    @Override
    public void setHasBasePlate(boolean hasBasePlate) {
        obj.setNoBasePlate(!hasBasePlate);
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.getEquipmentInSlot(1)));
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        obj.setCurrentItemOrArmor(1, (MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.getEquipmentInSlot(2)));
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        obj.setCurrentItemOrArmor(2, (MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.getEquipmentInSlot(3)));
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        obj.setCurrentItemOrArmor(3, (MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.getEquipmentInSlot(4)));
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        obj.setCurrentItemOrArmor(4, (MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.getEquipmentInSlot(0)));
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {
        obj.setCurrentItemOrArmor(0, (MCItemStack) MinecraftUtils.unwrap(itemStack));
    }
}
