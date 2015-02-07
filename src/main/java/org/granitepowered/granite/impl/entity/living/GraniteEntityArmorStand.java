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
import org.granitepowered.granite.mc.MCRotations;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntityArmorStand extends GraniteEntityLivingBase<MCEntityArmorStand> implements ArmorStand {

    public GraniteEntityArmorStand(MCEntityArmorStand obj) {
        super(obj);
    }

    @Override
    public Vector3f getHeadDirection() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(11).fieldGet$watchedObject());
    }

    @Override
    public void setHeadDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(11, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getBodyRotation() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(12).fieldGet$watchedObject());
    }

    @Override
    public void setBodyDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(12, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getLeftArmDirection() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(13).fieldGet$watchedObject());
    }

    @Override
    public void setLeftArmDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(13, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getRightArmDirection() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(14).fieldGet$watchedObject());
    }

    @Override
    public void setRightArmDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(14, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getLeftLegDirection() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(15).fieldGet$watchedObject());
    }

    @Override
    public void setLeftLegDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(15, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public Vector3f getRightLegDirection() {
        return MinecraftUtils.minecraftToGraniteRotations((MCRotations) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject());
    }

    @Override
    public void setRightLegDirection(Vector3f vector3f) {
        obj.fieldGet$dataWatcher().updateObject(16, MinecraftUtils.graniteToMinecraftRotations(vector3f));
    }

    @Override
    public boolean isSmall() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject() & 1) != 0;
    }

    @Override
    public void setSmall(boolean small) {
        byte object = (byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(10, (byte) (small ? (object | 1) : (object & -2)));
    }

    @Override
    public boolean hasGravity() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject() & 2) == 0;
    }

    @Override
    public void setGravity(boolean gravity) {
        byte object = (byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(10, (byte) (gravity ? (object & -3) : (object | 2)));
    }

    @Override
    public boolean doesShowArms() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject() & 4) != 0;
    }

    @Override
    public void setShowArms(boolean showArms) {
        byte object = (byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(10, (byte) (showArms ? (object | 4) : (object & -5)));
    }

    @Override
    public boolean hasBasePlate() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject() & 8) == 0;
    }

    @Override
    public void setHasBasePlate(boolean hasBasePlate) {
        byte object = (byte) obj.fieldGet$dataWatcher().getWatchedObject(10).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(10, (byte) (hasBasePlate ? (object & -9) : (object | 8)));
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
