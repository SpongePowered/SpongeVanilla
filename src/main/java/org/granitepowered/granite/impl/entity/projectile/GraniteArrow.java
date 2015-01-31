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

package org.granitepowered.granite.impl.entity.projectile;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import org.granitepowered.granite.mc.MCEntity;
import org.granitepowered.granite.mc.MCEntityArrow;
import org.spongepowered.api.entity.projectile.Arrow;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;

public class GraniteArrow extends GraniteProjectile<MCEntityArrow> implements Arrow {

    public GraniteArrow(MCEntityArrow obj) {
        super(obj);
    }

    @Override
    public boolean isCritical() {
        return (((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject()) & 1) != 0;
    }

    @Override
    public void setCritical(boolean critical) {
        byte prev = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(16, critical ? prev | 1 : prev & -2);
    }

    @Override
    public int getKnockbackStrength() {
        return obj.fieldGet$knockbackStrength();
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        obj.fieldSet$knockbackStrength(knockbackStrength);
    }

    @Override
    public ProjectileSource getShooter() {
        return wrap(obj.fieldGet$shootingEntity());
    }

    @Override
    public void setShooter(ProjectileSource shooter) {
        obj.fieldSet$shootingEntity((MCEntity) unwrap(shooter));
    }

    @Override
    public double getDamage() {
        return obj.fieldGet$damage();
    }

    @Override
    public void setDamage(double damage) {
        obj.fieldSet$damage(damage);
    }
}
