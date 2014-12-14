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

package org.granitepowered.granite.impl.entity;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.EulerDirection;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.UUID;

import static org.granitepowered.granite.utils.MinecraftUtils.unwrap;
import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

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
    public void interact(EntityInteractionType interactionType) {
        // TODO: figure out how to fake events from players which necessarily do not exist
        throw new NotImplementedException("");
    }

    @Override
    public void interactWith(ItemStack itemStack, EntityInteractionType interactionType) {
        // TODO: see above
        throw new NotImplementedException("");
    }

    @Override
    public boolean isOnGround() {
        return obj.fieldGet$onGround();
    }

    @Override
    public Location getLocation() {
        // TODO: Figure out Extent
        throw new NotImplementedException("");
    }

    @Override
    public void teleport(Location location) {
        // TODO: Figure out Extent
        throw new NotImplementedException("");
    }

    @Override
    public void teleport(Extent extent, Vector3d position) {
        // TODO: Figure out Extent
        throw new NotImplementedException("");
    }

    @Override
    public Vector3d getPosition() {
        return new Vector3d(getX(), getY(), getZ());
    }

    @Override
    public void setPosition(Vector3d position) {
        obj.setPosition(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public World getWorld() {
        return wrap(obj.fieldGet$worldObj());
    }

    @Override
    public void teleport(double x, double y, double z, World world) {
        // TODO: Multiworld
        throw new NotImplementedException("");
    }

    @Override
    public double getX() {
        return obj.fieldGet$posX();
    }

    @Override
    public double getY() {
        return obj.fieldGet$posY();
    }

    @Override
    public double getZ() {
        return obj.fieldGet$posZ();
    }

    @Override
    public Vector3f getVectorRotation() {
        return getRotation().toVector();
    }

    @Override
    public void setVectorRotation(Vector3f rotation) {
        // TODO: Figure out Euler things
        throw new NotImplementedException("");
    }

    @Override
    public EulerDirection getRotation() {
        // TODO: Figure out Euler things
        throw new NotImplementedException("");
    }

    @Override
    public void setRotation(EulerDirection rotation) {
        // TODO: Figure out Euler things
        throw new NotImplementedException("");
    }

    @Override
    public void mount(Entity entity) {
        obj.mountEntity(entity == null ? null : (MCEntity) unwrap(entity));
    }

    @Override
    public void dismount() {
        mount(null);
    }

    @Override
    public void eject() {
        // 99% sure these two use the same internal mechanism
        dismount();
    }

    @Override
    public Optional<Entity> getRider() {
        Entity rider = wrap(obj.fieldGet$riddenByEntity());
        return Optional.fromNullable(rider);
    }

    @Override
    public Optional<Entity> getRiding() {
        Entity riding = wrap(obj.fieldGet$ridingEntity());
        return Optional.fromNullable(riding);
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
    public boolean isDead() {
        return obj.fieldGet$isDead();
    }

    @Override
    public boolean isValid() {
        // TODO: What is this?
        throw new NotImplementedException("");
    }

    @Override
    public int getFireTicks() {
        return obj.fieldGet$fire();
    }

    @Override
    public void setFireTicks(int ticks) {
        // TODO: This might need to check enchantments first
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

    @Override
    public void teleport(Vector3d position, World world) {
        // TODO: Multiworld
        throw new NotImplementedException("");
    }
}
