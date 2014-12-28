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

package org.granitepowered.granite.impl.world;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCChunk;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.biome.Biome;

import java.util.Collection;

public class GraniteChunk extends Composite<MCChunk> implements Chunk {
    public GraniteChunk(Object obj) {
        super(obj);
    }

    @Override
    public Vector3i getPosition() {
        return new Vector3i(obj.fieldGet$xPosition(), 0, obj.fieldGet$zPosition());
    }

    @Override
    public boolean isLoaded() {
        return obj.fieldGet$isChunkLoaded();
    }

    @Override
    public boolean loadChunk(boolean b) {
        // TODO: What does the boolean mean?
        throw new NotImplementedException("");
    }

    @Override
    public boolean unloadChunk() {
        throw new NotImplementedException("");
    }

    @Override
    public Biome getBiome(Vector3d vector3d) {
        throw new NotImplementedException("");
    }

    @Override
    public BlockLoc getBlock(Vector3d vector3d) {
        return getBlock((int) vector3d.getX(), (int) vector3d.getY(), (int) vector3d.getZ());
    }

    @Override
    public BlockLoc getBlock(int i, int i1, int i2) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Entity> getEntities() {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> predicate) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntityType entityType, Vector3d vector3d) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot entitySnapshot, Vector3d vector3d) {
        throw new NotImplementedException("");
    }
}
