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

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.world.GraniteWorld;
import mc.MCEntity;
import mc.MCEntityLiving;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.RelativePositions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.EnumSet;
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
        return obj.onGround;
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
        return new Location(getWorld(), new Vector3d(obj.posX, obj.posY, obj.posZ));
    }

    @Override
    public boolean setLocation(Location location) {
        if (obj.worldObj.provider.dimensionId != ((GraniteWorld) location.getExtent()).getDimension()
                .getDimensionId()) {
            obj.travelToDimension(((GraniteWorld) location.getExtent()).getDimension().getDimensionId());
        }
        obj.setPositionAndUpdate(location.getPosition().getX(), location.getPosition().getY(), location.getPosition().getZ());
        return true;
    }

    @Override
    public boolean setLocationAndRotation(Location location, Vector3f rotation, EnumSet<RelativePositions> enumSet) {
        double xToAdd = enumSet.contains(RelativePositions.X) ? obj.posX : 0;
        double yToAdd = enumSet.contains(RelativePositions.Y) ? obj.posY : 0;
        double zToAdd = enumSet.contains(RelativePositions.Z) ? obj.posZ : 0;

        double yawToAdd = enumSet.contains(RelativePositions.YAW) ? obj.rotationYaw : 0;
        double pitchToAdd = enumSet.contains(RelativePositions.PITCH) ? obj.rotationPitch : 0;

        setLocation(location.add(xToAdd, yToAdd, zToAdd));
        setRotation(rotation.add(pitchToAdd, yawToAdd, 0));
        return true;
    }

    @Override
    public Vector3f getRotation() {
        return new Vector3f(obj.rotationYaw, obj.rotationPitch, 0);
    }

    @Override
    public void setRotation(Vector3f vector2f) {
        obj.rotationYaw = vector2f.getX();
        obj.rotationPitch = vector2f.getY();
    }

    @Override
    public Vector3d getVelocity() {
        return new Vector3d(obj.motionX, obj.motionY, obj.motionZ);
    }

    @Override
    public void setVelocity(Vector3d vector3d) {
        obj.motionX = vector3d.getX();
        obj.motionY = vector3d.getY();
        obj.motionZ = vector3d.getZ();
    }

    @Override
    public World getWorld() {
        return wrap(obj.worldObj);
    }

    @Override
    public Optional<Entity> getPassenger() {
        return Optional.fromNullable((Entity) wrap(obj.riddenByEntity));
    }

    @Override
    public Optional<Entity> getVehicle() {
        return Optional.fromNullable((Entity) wrap(obj.ridingEntity));
    }

    @Override
    public Entity getBaseVehicle() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean setPassenger(Entity entity) {
        obj.riddenByEntity = unwrap(entity);
        return true;
    }

    @Override
    public boolean setVehicle(Entity entity) {
        obj.ridingEntity = unwrap(entity);
        return true;
    }

    @Override
    public float getBase() {
        return obj.width;
    }

    @Override
    public float getHeight() {
        return obj.height;
    }

    @Override
    public float getScale() {
        // TODO: Find out if this is even possible to get
        return 1;
    }

    @Override
    public int getFireTicks() {
        return obj.fire;
    }

    @Override
    public void setFireTicks(int ticks) {
        obj.fire = ticks;
    }

    @Override
    public int getFireDelay() {
        return obj.fireResistance;
    }

    @Override
    public boolean isPersistent() {
        MCEntityLiving entityLiving = (MCEntityLiving) unwrap(this);
        return !entityLiving.persistenceRequired;
    }

    @Override
    public void setPersistent(boolean persistent) {
        MCEntityLiving entityLiving = (MCEntityLiving) unwrap(this);
        entityLiving.persistenceRequired = !persistent;
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
        return obj.entityUniqueID;
    }
}
