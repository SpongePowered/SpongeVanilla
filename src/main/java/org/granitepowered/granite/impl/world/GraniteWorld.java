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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.block.GraniteBlockLoc;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCEntity;
import org.granitepowered.granite.mc.MCGameRules;
import org.granitepowered.granite.mc.MCWorld;
import org.granitepowered.granite.mc.MCWorldInfo;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.effect.Particle;
import org.spongepowered.api.effect.Sound;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Environment;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.biome.Biome;
import org.spongepowered.api.world.biome.BiomeManager;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class GraniteWorld extends Composite<MCWorld> implements World {
    public GraniteWorld(MCWorld obj) {
        super(obj);
    }

    @Override
    public UUID getUniqueID() {
        throw new NotImplementedException("");
    }

    @Override
    public String getName() {
        return getMCWorldInfo().fieldGet$levelName();
    }

    @Override
    public Optional<Chunk> getChunk(Vector3i vector3i) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Chunk> loadChunk(Vector3i vector3i, boolean b) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean deleteChunk(Chunk chunk) {
        throw new NotImplementedException("");
    }

    @Override
    public Iterable<Chunk> getLoadedChunks() {
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
    public WorldBorder getWorldBorder() {
        return new GraniteWorldBorder(obj.fieldGet$worldBorder());
    }

    @Override
    public Optional<String> getGameRule(String gameRule) {
        return Optional.fromNullable(getMCGameRules().getGameRuleStringValue(gameRule));
    }

    @Override
    public void setGameRule(String gameRule, String value) {
        getMCGameRules().setOrCreateGameRule(gameRule, value);
    }

    @Override
    public Map<String, String> getGameRules() {
        Map<String, String> map = new HashMap<>();
        String[] rules = getMCGameRules().getRules();
        for (int i = 0; i < rules.length; i++) {
            map.put(rules[i], String.valueOf(getGameRule(rules[i])));
        }
        return map;
    }

    @Override
    public Environment getEnvironment() {
        // TODO: Environment API
        throw new NotImplementedException("");
    }

    @Override
    public long getWorldSeed() {
        return getMCWorldInfo().fieldGet$randomSeed();
    }

    @Override
    public void setSeed(long seed) {
        getMCWorldInfo().fieldSet$randomSeed(seed);
    }

    @Override
    public WorldGenerator getWorldGenerator() {
        throw new NotImplementedException("");
    }

    @Override
    public void setWorldGenerator(WorldGenerator worldGenerator) {
        throw new NotImplementedException("");
    }

    @Override
    public BiomeManager getBiomeManager() {
        throw new NotImplementedException("");
    }

    @Override
    public Biome getBiome(Vector3d position) {
        // TODO: Biome API
        throw new NotImplementedException("");
    }

    @Override
    public BlockLoc getBlock(Vector3d position) {
        return new GraniteBlockLoc(new Location(this, position));
    }

    @Override
    public BlockLoc getBlock(int x, int y, int z) {
        return getBlock(new Vector3d(x, y, z));
    }

    @Override
    public Collection<Entity> getEntities() {
        return Lists.<Entity>newArrayList(Iterables.transform(obj.fieldGet$loadedEntityList(), new MinecraftUtils.WrapFunction<GraniteEntity>()));
    }

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> predicate) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position) {
        MCEntity entity = (MCEntity) Mappings.invokeStatic("createEntityByName", type.getId(), obj);
        entity.setPosition(position.getX(), position.getY(), position.getZ());
        boolean ret = obj.spawnEntityInWorld(entity);

        return ret ? Optional.of((Entity) wrap(entity)) : Optional.<Entity>absent();
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

    public MCWorldInfo getMCWorldInfo() {
        return obj.fieldGet$worldInfo();
    }

    public MCGameRules getMCGameRules() {
        return getMCWorldInfo().fieldGet$gameRules();
    }
}
