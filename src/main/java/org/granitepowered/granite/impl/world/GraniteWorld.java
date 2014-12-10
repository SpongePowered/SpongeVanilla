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

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.effect.Particle;
import org.spongepowered.api.effect.Sound;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.Biome;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.UUID;

public class GraniteWorld extends Composite implements World {
    public GraniteWorld(Object parent) {
        super(parent);
    }

    @Override
    public UUID getUniqueID() {
        throw new NotImplementedException("");
    }

    @Override
    public String getName() {
        return (String) fieldGet(getWorldInfo(), "levelName");
    }

    @Override
    public Optional<Chunk> getChunk(Vector2i position) {
        // TODO: Chunk API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Chunk> loadChunk(Vector2i position, boolean shouldGenerate) {
        // TODO: Chunk API
        throw new NotImplementedException("");
    }

    @Override
    public Chunk loadChunk(Vector2i position) {
        // TODO: Chunk API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> getEntityFromUUID(final UUID uuid) {
        return Iterables.tryFind(getEntities(), new Predicate<Entity>() {
            @Override
            public boolean apply(Entity input) {
                return input.getUniqueId().equals(uuid);
            }
        });
    }

    @Override
    public Biome getBiome(Vector3d position) {
        // TODO: Biome API
        throw new NotImplementedException("");
    }

    @Override
    public BlockLoc getBlock(Vector3d position) {
        return null;
    }

    @Override
    public BlockLoc getBlock(int x, int y, int z) {
        return null;
    }

    @Override
    public Collection<Entity> getEntities() {
        return Lists.newArrayList(Iterables.transform((Iterable<Object>) fieldGet("loadedEntityList"), new MinecraftUtils.WrapFunction<Entity>()));
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position) {
        GraniteEntity entity = (GraniteEntity) MinecraftUtils.wrapComposite(Mappings.invokeStatic("createEntityByName", type.getId(), parent));
        entity.setPosition(position);
        boolean ret = (boolean) invoke("spawnEntityInWorld", entity);

        return ret ? Optional.<Entity>of(entity) : Optional.<Entity>absent();
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot snapshot, Vector3d position) {
        // TODO: Snapshot API
        throw new NotImplementedException("");
    }

    @Override
    public Weather getWeather() {
        // TODO: Weather API
        throw new NotImplementedException("");
    }

    @Override
    public long getRemainingDuration() {
        // TODO: Figure out where these are
        throw new NotImplementedException("");
    }

    @Override
    public long getRunningDuration() {
        throw new NotImplementedException("");
    }

    @Override
    public void forecast(Weather weather) {
        // TODO: Weather API
        throw new NotImplementedException("");
    }

    @Override
    public void forecast(Weather weather, long duration) {
        // TODO: Weather API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, ItemType itemType) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius, ItemType itemType) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch, double minVolume) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    public Object getWorldInfo() {
        return fieldGet("worldInfo");
    }
}
