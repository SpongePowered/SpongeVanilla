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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.*;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntitySnapshot;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.Dimension;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.storage.WorldStorage;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class GraniteWorld extends Composite<MCWorld> implements World {

    public GraniteWorld(MCWorld obj) {
        super(obj);
    }

    @Override
    public Difficulty getDifficulty() {
        throw new NotImplementedException("");
    }

    @Override
    public void setDifficulty(Difficulty difficulty) {
        throw new NotImplementedException("");
    }

    @Override
    public String getName() {
        /*String name = getMCWorldInfo().fieldGet$levelName();
        if (name == null) {
            MCWorldInfo parentWorldInfo = ((MCDerivedWorldInfo) getMCWorldInfo()).fieldGet$theWorldInfo();
            return parentWorldInfo.fieldGet$levelName() + (getDimension().getDimensionId() == 1 ? "_the_end" : "_nether");
        }
        return name;*/
        throw new NotImplementedException("");
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
    public boolean unloadChunk(Chunk chunk) {
        throw new NotImplementedException("");
    }

    @Override
    public Iterable<Chunk> getLoadedChunks() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> getEntity(UUID uuid) {
        final UUID finalUUID = uuid;
        return Iterables.tryFind(getEntities(), new Predicate<Entity>() {
            @Override
            public boolean apply(Entity input) {
                return input.getUniqueId().equals(finalUUID);
            }
        });
    }

    @Override
    public WorldBorder getWorldBorder() {
        return new GraniteWorldBorder(obj.worldBorder);
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
        for (String rule : rules) {
            map.put(rule, String.valueOf(getGameRule(rule)));
        }
        return map;
    }

    @Override
    public Dimension getDimension() {
        return new GraniteDimension(obj.provider);
    }

    @Override
    public long getWorldSeed() {
        return getMCWorldInfo().randomSeed;
    }

    @Override
    public void setSeed(long seed) {
        getMCWorldInfo().randomSeed = seed;
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
    public boolean doesKeepSpawnLoaded() {
        throw new NotImplementedException("");
    }

    @Override
    public void setKeepSpawnLoaded(boolean b) {
        throw new NotImplementedException("");
    }

    @Override
    public WorldStorage getWorldStorage() {
        /// TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public BlockState getBlock(Vector3i vector3i) {
        throw new NotImplementedException("");
    }

    @Override
    public BlockState getBlock(int x, int y, int z) {
        return getBlock(new Vector3i(x, y, z));
    }

    @Override
    public void setBlock(Vector3i vector3i, BlockState blockState) {
        throw new NotImplementedException("");
    }

    @Override
    public void setBlock(int i, int i1, int i2, BlockState blockState) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Entity> getEntities() {
        return Lists.<Entity>newArrayList(Iterables.transform(obj.loadedEntityList, new MinecraftUtils.WrapFunction<GraniteEntity>()));
    }

    @Override
    public Collection<Entity> getEntities(Predicate<Entity> predicate) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntityType type, Vector3d position) {
        MCEntity entity = (MCEntity) Mappings.invokeStatic("createEntityByName", type.getId(), obj);
        entity.setPositionAndUpdate(position.getX(), position.getY(), position.getZ());
        boolean ret = obj.spawnEntityInWorld(entity);

        return ret ? Optional.of((Entity) wrap(entity)) : Optional.<Entity>absent();
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot entitySnapshot, Vector3i vector3i) {
        // TODO: Entity Snapshot API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntityType entityType, Vector3i vector3i) {
        /// TODO: EntityType API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(EntitySnapshot snapshot, Vector3d position) {
        // TODO: Entity Snapshot API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Entity> createEntity(DataContainer entityContainer) {
        /// TODO: Persistence API
        throw new NotImplementedException("");
    }

    @Override
    public boolean spawnEntity(Entity entity) {
        return obj.spawnEntityInWorld((MCEntity) unwrap((GraniteEntity) entity));
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
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position) {
        for (Entity entity : getEntities()) {
            if (entity instanceof Player) {
                ((Player) entity).spawnParticles(particleEffect, position);
            }
        }
    }

    @Override
    public void spawnParticles(ParticleEffect particleEffect, Vector3d position, int radius) {
        for (Entity entity : getEntities()) {
            if (entity instanceof Player) {
                if (entity.getLocation().getPosition().distanceSquared(position) < radius * radius) {
                    ((Player) entity).spawnParticles(particleEffect, position);
                }
            }
        }
    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume) {
        this.playSound(sound, position, volume, 1.0f);
    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch) {
        this.obj.playSoundEffect(position.getX(), position.getY(), position.getZ(), sound.getName(), (float) volume, (float) pitch);
    }

    @Override
    public void playSound(SoundType sound, Vector3d position, double volume, double pitch, double minVolume) {
        // TODO: Can't find method to set minVolume?
        this.obj.playSoundEffect(position.getX(), position.getY(), position.getZ(), sound.getName(), (float) volume, (float) pitch);
    }

    public boolean isPowered(MCBlockPos blockPos) {
        return obj.isPowered(blockPos);
    }

    public boolean isFacePowered(MCBlockPos blockPos, MCEnumFacing enumFacing) {
        return obj.isFacePowered(blockPos, enumFacing);
    }

    public boolean isIndirectlyPowered(MCBlockPos blockPos) {
        return obj.isIndirectlyPowered(blockPos) > 0;
    }

    public MCWorldInfo getMCWorldInfo() {
        return obj.worldInfo;
    }

    public MCGameRules getMCGameRules() {
        return getMCWorldInfo().gameRules;
    }

    @Override
    public Context getContext() {
        return new Context("world", getName());
    }

    @Override
    public UUID getUniqueId() {
        // TODO: UUID
        throw new NotImplementedException("");
    }

    @Override
    public BlockLoc getFullBlock(Vector3i vector3i) {
        return null;
    }

    @Override
    public BlockLoc getFullBlock(int i, int i1, int i2) {
        return null;
    }

    @Override
    public BiomeType getBiome(Vector2i vector2i) {
        return null;
    }

    @Override
    public BiomeType getBiome(int i, int i1) {
        return null;
    }

    @Override
    public void setBiome(Vector2i vector2i, BiomeType biomeType) {

    }

    @Override
    public void setBiome(int i, int i1, BiomeType biomeType) {

    }
}
