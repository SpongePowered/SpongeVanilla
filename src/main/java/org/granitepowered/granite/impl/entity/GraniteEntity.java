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

package org.granitepowered.granite.impl.entity;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector2f;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.UUID;

public class GraniteEntity<T extends MCEntity> extends Composite<T> implements Entity {

    public GraniteEntity(T obj) {
        super(obj);
    }

    @Override
    public void remove() {
        // TODO: Multiworld (World.removeEntity())
        throw new NotImplementedException("");
    }

    @Override
    public boolean isOnGround() {
        return obj.fieldGet$onGround();
    }

    @Override
    public boolean isRemoved() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isLoaded() {
        throw new NotImplementedException("");
    }

    @Override
    public Location getLocation() {
        return new Location(getWorld(), new Vector3d(obj.fieldGet$posX(), obj.fieldGet$posY(), obj.fieldGet$posZ()));
    }

    @Override
    public boolean setLocation(Location location) {
        obj.setPosition(location.getPosition().getX(), location.getPosition().getY(), location.getPosition().getZ());
        return true;
    }

    @Override
    public Vector2f getRotation() {
        // TODO: Figure out Euler stuff
        throw new NotImplementedException("");
    }

    @Override
    public void setRotation(Vector2f vector2f) {
        // TODO: Figure out Euler stuff
        throw new NotImplementedException("");
    }

    @Override
    public World getWorld() {
        return wrap(obj.fieldGet$worldObj());
    }

    @Override
    public Optional<Entity> getPassenger() {
        return Optional.fromNullable((Entity) wrap(obj.fieldGet$riddenByEntity()));
    }

    @Override
    public Optional<Entity> getVehicle() {
        return Optional.fromNullable((Entity) wrap(obj.fieldGet$ridingEntity()));
    }

    @Override
    public Entity getBaseVehicle() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean setPassenger(Entity entity) {
        obj.fieldSet$riddenByEntity((MCEntity) unwrap(entity));
        return true;
    }

    @Override
    public boolean setVehicle(Entity entity) {
        obj.fieldSet$ridingEntity((MCEntity) unwrap(entity));
        return true;
    }

    @Override
    public float getBase() {
        return obj.fieldGet$width();
    }

    @Override
    public float getHeight() {
        return obj.fieldGet$height();
    }

    @Override
    public float getScale() {
        // TODO: Find out if this is even possible to get
        return 1;
    }

    @Override
    public int getFireTicks() {
        return obj.fieldGet$fire();
    }

    @Override
    public void setFireTicks(int ticks) {
        obj.fieldSet$fire(ticks);
    }

    @Override
    public int getFireDelay() {
        return obj.fieldGet$fireResistance();
    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass) {
        // TODO: Wait for data API
        throw new NotImplementedException("");
    }

    @Override
    public EntityType getType() {
        // TODO: Wait for entity types API
        throw new NotImplementedException("");
    }

    @Override
    public EntitySnapshot getSnapshot() {
        // TODO: Wait for data API
        throw new NotImplementedException("");
    }

    @Override
    public UUID getUniqueId() {
        return obj.fieldGet$entityUniqueID();
    }
}
