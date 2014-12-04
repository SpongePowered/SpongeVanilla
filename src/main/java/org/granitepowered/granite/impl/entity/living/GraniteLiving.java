/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.List;

public class GraniteLiving extends GraniteEntity implements Living {
    public GraniteLiving(Object parent) {
        super(parent);
    }

    @Override
    public void damage(double amount) {
        try {
            invoke("damageEntity", Mappings.getField("DamageSource", "generic").get(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getHealth() {
        return (double) invoke("getHealth");
    }

    @Override
    public void setHealth(double health) {
        invoke("setHealth", health);
    }

    @Override
    public double getMaxHealth() {
        return (double) invoke("getMaxHealth");
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
        Object living = fieldGet("lastAttacker");
        if (living != null) {
            return Optional.of((Living) MinecraftUtils.wrapComposite(living));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public void setLastAttacker(Living lastAttacker) {
        fieldSet("lastAttacker", ((GraniteLiving) lastAttacker).parent);
    }

    @Override
    public boolean isLeashed() {
        return (boolean) fieldGet("isLeashed");
    }

    @Override
    public void setLeashed(boolean leashed) {
        fieldSet("isLeashed", leashed);
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
        return (double) invoke("getEyeHeight");
    }

    @Override
    public Vector3f getEyeLocation() {
        // TODO: Wait for location/position shiz
        throw new NotImplementedException("");
    }

    @Override
    public int getRemainingAir() {
        return (int) invoke("getAir");
    }

    @Override
    public void setRemainingAir(int air) {
        invoke("setAir", air);
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
        return (double) fieldGet("lastDamage");
    }

    @Override
    public void setLastDamage(double damage) {
        fieldSet("lastDamage", damage);
    }

    @Override
    public int getInvulnerabilityTicks() {
        // TODO: Where are invulnerability ticks in Minecraft?
        throw new NotImplementedException("");
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {
        // TODO: Where are invulnerability ticks in Minecraft?
        throw new NotImplementedException("");
    }

    @Override
    public int getMaxInvulnerabilityTicks() {
        // TODO: Where are invulnerability ticks in Minecraft?
        throw new NotImplementedException("");
    }

    @Override
    public void setMaxInvulnerabilityTicks(int ticks) {
        // TODO: Where are invulnerability ticks in Minecraft?
        throw new NotImplementedException("");
    }

    @Override
    public boolean getCanPickupItems() {
        return (boolean) fieldGet("canPickUpLoot");
    }

    @Override
    public void setCanPickupItems(boolean canPickupItems) {
        fieldSet("canPickUpLoot", canPickupItems);
    }

    @Override
    public String getCustomName() {
        return (String) invoke("getCustomNameTag");
    }

    @Override
    public void setCustomName(String name) {
        invoke("setCustomNameTag", name);
    }

    @Override
    public boolean isCustomNameVisible() {
        return (boolean) invoke("getAlwaysRenderNameTag");
    }

    @Override
    public void setCustomNameVisible(boolean visible) {
        invoke("setAlwaysRenderNameTag", visible);
    }
}
