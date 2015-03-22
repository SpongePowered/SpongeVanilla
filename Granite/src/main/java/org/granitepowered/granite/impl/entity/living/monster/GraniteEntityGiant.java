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

package org.granitepowered.granite.impl.entity.living.monster;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import mc.MCEntityGiantZombie;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.monster.Giant;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.api.item.inventory.types.CarriedInventory;

public class GraniteEntityGiant extends GraniteEntityMonster<MCEntityGiantZombie> implements Giant {

    public GraniteEntityGiant(MCEntityGiantZombie obj) {
        super(obj);
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.equipment[4]));
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        obj.equipment[4] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.equipment[3]));
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        obj.equipment[3] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.equipment[2]));
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        obj.equipment[2] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.equipment[1]));
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        obj.equipment[1] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.equipment[0]));
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {
        obj.equipment[0] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public boolean canEquip(EquipmentType equipmentType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean canEquip(EquipmentType equipmentType, ItemStack itemStack) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<ItemStack> getEquipped(EquipmentType equipmentType) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean equip(EquipmentType equipmentType, ItemStack itemStack) {
        throw new NotImplementedException("");
    }

    @Override
    public CarriedInventory<? extends Carrier> getInventory() {
        throw new NotImplementedException("");
    }
}
