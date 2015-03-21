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

package org.granitepowered.granite.impl.world;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCChunk;
import org.granitepowered.granite.mc.MCChunkProvider;
import org.granitepowered.granite.mc.MCEntity;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.Set;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class GraniteChunk extends Composite<MCChunk> implements Chunk {

    boolean loaded;
    int x;
    int z;

    public GraniteChunk(int x, int z) {
        super(null);
        this.x = x;
        this.z = z;
        loaded = false;
    }

    public GraniteChunk(MCChunk obj) {
        super(obj);
        x = obj.xPosition;
        z = obj.zPosition;
    }

    @Override
    public Vector3i getPosition() {
        return new Vector3i(x, 0, z);
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public boolean isPopulated() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean loadChunk(boolean generate) {
        // TODO: Check if it's possible to load a chunk without generating it
        MCChunk ret = getChunkProvider().provideChunk(x, z);

        if (ret != null) {
            obj = ret;
            loaded = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean unloadChunk() {
        getChunkProvider().dropChunk(x, z);
        return getChunkProvider().droppedChunksSet.contains(obj);
    }

    @Override
    public BlockState getBlock(Vector3i vector3i) {
        return null;
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return getBlock(new Vector3i(x, y, z));
    }

    @Override
    public void setBlock(Vector3i vector3i, BlockState blockState) {

    }

    @Override
    public void setBlock(int i, int i1, int i2, BlockState blockState) {

    }

    @Override
    public Collection<Entity> getEntities() {
        ImmutableSet.Builder<Entity> entities = ImmutableSet.builder();
        for (Set mcEntitySet : obj.entityLists) {
            for (Object entity : mcEntitySet) {
                entities.add((Entity) wrap((MCEntity) entity));
            }
        }

        return (Collection<Entity>) entities;
    }

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> predicate) {
        return Sets.filter((Set<Entity>) getEntities(), predicate);
    }

    @Override
    public Optional<Entity> createEntity(EntityType entityType, Vector3d vector3d) {
        return getWorld().createEntity(entityType, toWorldCoordinates(vector3d));
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot entitySnapshot, Vector3i vector3i) {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntityType entityType, Vector3i vector3i) {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot entitySnapshot, Vector3d vector3d) {
        // TODO: EntitySnapshot API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(DataContainer dataContainer) {
        // TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public boolean spawnEntity(Entity entity) {
        return false;
    }

    public GraniteWorld getWorld() {
        return wrap(obj.worldObj);
    }

    public Vector3d toWorldCoordinates(Vector3d chunkCoordinates) {
        Vector3i worldCoordinates = getPosition().mul(16);
        return new Vector3d(worldCoordinates.getX(), worldCoordinates.getY(), worldCoordinates.getZ()).add(chunkCoordinates);
    }

    public MCChunkProvider getChunkProvider() {
        return getWorld().obj.chunkProvider;
    }

    @Override
    public BlockLoc getFullBlock(Vector3i vector3i) {
        throw new NotImplementedException("");
    }

    @Override
    public BlockLoc getFullBlock(int i, int i1, int i2) {
        throw new NotImplementedException("");
    }

    @Override
    public BiomeType getBiome(Vector2i vector2i) {
        throw new NotImplementedException("");
    }

    @Override
    public BiomeType getBiome(int i, int i1) {
        throw new NotImplementedException("");
    }

    @Override
    public void setBiome(Vector2i vector2i, BiomeType biomeType) {
        throw new NotImplementedException("");
    }

    @Override
    public void setBiome(int i, int i1, BiomeType biomeType) {
        throw new NotImplementedException("");
    }

    @Override
    public Weather getWeather() {
        throw new NotImplementedException("");
    }

    @Override
    public long getRemainingDuration() {
        throw new NotImplementedException("");
    }

    @Override
    public long getRunningDuration() {
        throw new NotImplementedException("");
    }

    @Override
    public void forecast(Weather weather) {
        throw new NotImplementedException("");
    }

    @Override
    public void forecast(Weather weather, long l) {
        throw new NotImplementedException("");
    }
}
