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

import static org.granitepowered.granite.utils.MinecraftUtils.unwrap;
import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCDamageSource;
import org.granitepowered.granite.mc.MCEntityLiving;
import org.granitepowered.granite.mc.MCEntityLivingBase;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.world.Location;

import java.util.Collection;
import java.util.List;

public class GraniteLiving<T extends MCEntityLiving> extends GraniteEntity<T> implements Living {

    public GraniteLiving(T obj) {
        super(obj);
    }

    @Override
    public void damage(double amount) {
        try {
            obj.damageEntity((MCDamageSource) Mappings.getField("DamageSource", "generic").get(null), (float) amount);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getHealth() {
        return obj.getHealth();
    }

    @Override
    public void setHealth(double health) {
        obj.setHealth((float) health);
    }

    @Override
    public double getMaxHealth() {
        return obj.getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        // TODO: Check if possible
        throw new NotImplementedException("");
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect, boolean force) {
        // TODO: Potion effects, see EntityLivingBase.activePotionsMap (xo.g)
        throw new NotImplementedException("");
    }

    @Override
    public void addPotionEffects(Collection<PotionEffect> potionEffects, boolean force) {
        // TODO: Potion effects, see EntityLivingBase.activePotionsMap (xo.g)
        throw new NotImplementedException("");
    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType) {
        // TODO: Potion effects, see EntityLivingBase.activePotionsMap (xo.g)
        throw new NotImplementedException("");
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        // TODO: Potion effects, see EntityLivingBase.activePotionsMap (xo.g)
        throw new NotImplementedException("");
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        // TODO: Potion effects, see EntityLivingBase.activePotionsMap (xo.g)
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Living> getLastAttacker() {
        MCEntityLivingBase living = obj.fieldGet$lastAttacker();
        return Optional.fromNullable((Living) wrap(living));
    }

    @Override
    public void setLastAttacker(Living lastAttacker) {
        obj.fieldSet$lastAttacker((MCEntityLiving) unwrap(lastAttacker));
    }

    @Override
    public boolean isLeashed() {
        return obj.fieldGet$isLeashed();
    }

    @Override
    public void setLeashed(boolean leashed) {
        obj.fieldSet$isLeashed(leashed);
    }

    @Override
    public Optional<Entity> getLeashHolder() {
        // TODO: Not sure what the field "leashedToEntity" in EntityLiving refers to, leaving this unimplemented for now
        throw new NotImplementedException("");
    }

    @Override
    public void setLeashHolder(Entity entity) {
        // TODO: Not sure what the field "leashedToEntity" in EntityLiving refers to, leaving this unimplemented for now
        throw new NotImplementedException("");
    }

    @Override
    public double getEyeHeight() {
        return obj.getEyeHeight();
    }

    @Override
    public Vector3f getEyeLocation() {
        // TODO: Wait for location/position shiz
        throw new NotImplementedException("");
    }

    @Override
    public int getRemainingAir() {
        return obj.getAir();
    }

    @Override
    public void setRemainingAir(int air) {
        obj.setAir(air);
    }

    @Override
    public int getMaxAir() {
        // TODO: I don't think Minecraft has a concept of maximum air
        throw new NotImplementedException("");
    }

    @Override
    public void setMaxAir(int air) {
        // TODO: I don't think Minecraft has a concept of maximum air
        throw new NotImplementedException("");
    }

    @Override
    public double getLastDamage() {
        return obj.fieldGet$lastDamage();
    }

    @Override
    public void setLastDamage(double damage) {
        obj.fieldSet$lastDamage((float) damage);
    }

    @Override
    public int getInvulnerabilityTicks() {
        return obj.fieldGet$hurtResistantTime();
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {
        obj.fieldSet$hurtResistantTime(ticks);
    }

    @Override
    public int getMaxInvulnerabilityTicks() {
        // Minecraft stores this as half-ticks for some reason (default is 20 = 0.5 seconds, wut?)
        return obj.fieldGet$maxHurtResistantTime() / 2;
    }

    @Override
    public void setMaxInvulnerabilityTicks(int ticks) {
        obj.fieldSet$maxHurtResistantTime(ticks * 2);
    }

    @Override
    public boolean getCanPickupItems() {
        return obj.fieldGet$canPickUpLoot();
    }

    @Override
    public void setCanPickupItems(boolean canPickupItems) {
        obj.fieldSet$canPickUpLoot(canPickupItems);
    }

    @Override
    public String getCustomName() {
        return obj.getCustomNameTag();
    }

    @Override
    public void setCustomName(String name) {
        obj.setCustomNameTag(name);
    }

    @Override
    public boolean isCustomNameVisible() {
        return obj.getAlwaysRenderNameTag();
    }

    @Override
    public void setCustomNameVisible(boolean visible) {
        obj.setAlwaysRenderNameTag(visible);
    }

    @Override
    public boolean isPersistent() {
        return obj.fieldGet$persistenceRequired();
    }

    @Override
    public void setPersistent(boolean b) {
        obj.fieldSet$persistenceRequired(b);
    }

    @Override
    public boolean setLocation(Location location) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> getPassenger() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> getVehicle() {
        throw new NotImplementedException("");
    }

    @Override
    public Entity getBaseVehicle() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean setPassenger(Entity entity) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean setVehicle(Entity entity) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isRemoved() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isLoaded() {
        throw new NotImplementedException("");
    }
}
