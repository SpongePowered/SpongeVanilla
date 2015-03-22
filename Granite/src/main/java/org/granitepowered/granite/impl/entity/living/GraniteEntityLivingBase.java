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

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import mc.MCEntityLivingBase;
import mc.MCPotion;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.potion.GranitePotionEffect;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeModifier;
import org.spongepowered.api.entity.living.ArmorStand;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.potion.PotionEffect;
import org.spongepowered.api.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GraniteEntityLivingBase<T extends MCEntityLivingBase> extends GraniteEntity<T> implements Living {

    private int maxAir = 300;

    public GraniteEntityLivingBase(T obj) {
        super(obj);
    }

    @Override
    public void damage(double amount) {
        /*try {
            obj.damageEntity((MCDamageSource) Mappings.getField("DamageSource", "generic").get(null), (float) amount);
        } catch (IllegalAccessException e) {
            Granite.error(e);
        }*/
    }

    @Override
    public double getHealth() {
        return (float) obj.dataWatcher.getWatchedObject(6).watchedObject;
    }

    @Override
    public void setHealth(double health) {
        if (health < 0) {
            health = 0;
        } else if (health > 20) {
            health = 20;
        }
        obj.dataWatcher.updateObject(6, (float) health);
    }

    @Override
    public double getMaxHealth() {
        return obj.getMaxHealth();
    }

    @Override
    public void setMaxHealth(double maxHealth) {
        // TODO: need to look at accessing getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
        throw new NotImplementedException("");
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect, boolean force) {
        if (hasPotionEffect(potionEffect.getType())) {
            if (!force) {
                return;
            }
            removePotionEffect(potionEffect.getType());
        }
        obj.addPotionEffect(unwrap((GranitePotionEffect) potionEffect));
    }

    @Override
    public void addPotionEffects(Collection<PotionEffect> potionEffects, boolean force) {
        for (PotionEffect potionEffect : potionEffects) {
            addPotionEffect(potionEffect, force);
        }
    }

    @Override
    public void removePotionEffect(PotionEffectType potionEffectType) {
        obj.removePotionEffect(((MCPotion) unwrap(potionEffectType)).id);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType potionEffectType) {
        return getPotionEffects().contains(potionEffectType);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        Map potions = obj.activePotions;
        List<PotionEffect> potionEffects = new ArrayList<>();
        for (Object object : potions.values()) {
            potionEffects.add(new GranitePotionEffect(object));
        }
        return potionEffects;
    }

    @Override
    public Optional<Living> getLastAttacker() {
        MCEntityLivingBase living = obj.lastAttacker;
        return Optional.fromNullable((Living) wrap(living));
    }

    @Override
    public void setLastAttacker(Living lastAttacker) {
        obj.lastAttacker = unwrap(lastAttacker);
    }

    @Override
    public double getEyeHeight() {
        return obj.getEyeHeight();
    }

    @Override
    public Vector3f getEyeLocation() {
        Vector3d pos = getLocation().getPosition();
        return new Vector3f(pos.getX(), pos.getY() + getEyeHeight(), pos.getZ());
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
        return this.maxAir;
    }

    @Override
    public void setMaxAir(int air) {
        this.maxAir = air;
    }

    @Override
    public double getLastDamage() {
        return obj.lastDamage;
    }

    @Override
    public void setLastDamage(double damage) {
        obj.lastDamage = ((float) damage);
    }

    @Override
    public int getInvulnerabilityTicks() {
        return obj.hurtResistantTime;
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {
        obj.hurtResistantTime = ticks;
    }

    @Override
    public int getMaxInvulnerabilityTicks() {
        return obj.maxHurtResistantTime / 2;
    }

    @Override
    public void setMaxInvulnerabilityTicks(int ticks) {
        obj.maxHurtResistantTime = (ticks * 2);
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
    public boolean isInvisible() {
        return ((byte) obj.dataWatcher.getWatchedObject(0).watchedObject & 1 << 5) != 0;
    }

    @Override
    public void setInvisible(boolean invisible) {
        if (this instanceof ArmorStand) {
            GraniteEntityArmorStand armorStand = (GraniteEntityArmorStand) this;
            armorStand.obj.canInteract = invisible;
        }
        byte object = (byte) obj.dataWatcher.getWatchedObject(0).watchedObject;
        obj.dataWatcher.updateObject(0, (byte) (invisible ? (object | 1 << 5) : (object & ~(1 << 5))));
    }

    @Override
    public boolean isInvisibleTo(Player player) {
        throw new NotImplementedException("");
    }

    @Override
    public void setInvisibleTo(Player player, boolean b) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Double> getAttributeValue(Attribute attribute) {
        throw new NotImplementedException("");
    }

    @Override
    public double getBase(Attribute attribute) {
        throw new NotImplementedException("");
    }

    @Override
    public void setBase(Attribute attribute, double v) throws IllegalArgumentException {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<AttributeModifier> getAllModifiers() {
        throw new NotImplementedException("");
    }

    @Override
    public void addModifier(AttributeModifier attributeModifier) throws IllegalArgumentException {
        throw new NotImplementedException("");
    }

    @Override
    public void addModifiers(Iterable<AttributeModifier> iterable) throws IllegalArgumentException {
        throw new NotImplementedException("");
    }

    @Override
    public List<AttributeModifier> getModifiersFor(Attribute attribute) {
        throw new NotImplementedException("");
    }

    @Override
    public void removeModifier(AttributeModifier attributeModifier) {
        throw new NotImplementedException("");
    }
}
