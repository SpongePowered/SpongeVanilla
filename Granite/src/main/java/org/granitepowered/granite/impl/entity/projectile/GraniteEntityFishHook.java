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

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCEntity;
import org.granitepowered.granite.mc.MCEntityFishHook;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.projectile.FishHook;
import org.spongepowered.api.entity.projectile.source.ProjectileSource;

public class GraniteEntityFishHook extends GraniteEntity<MCEntityFishHook> implements FishHook {

    private double damage = 0;

    public GraniteEntityFishHook(MCEntityFishHook obj) {
        super(obj);
    }

    @Override
    public Optional<Entity> getHookedEntity() {
        return Optional.fromNullable((Entity) MinecraftUtils.wrap(obj.fieldGet$caughtEntity()));
    }

    @Override
    public void setHookedEntity(Entity entity) {
        obj.fieldSet$caughtEntity((MCEntity) MinecraftUtils.unwrap(entity));
    }

    @Override
    public double getDamage() {
        return this.damage;
    }

    @Override
    public void setDamage(double damage) {
        this.damage = damage;
    }

    @Override
    public ProjectileSource getShooter() {
        throw new NotImplementedException("");
    }

    @Override
    public void setShooter(ProjectileSource projectileSource) {
        throw new NotImplementedException("");
    }
}
