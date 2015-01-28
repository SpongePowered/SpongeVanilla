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

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

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
import org.granitepowered.granite.impl.world.biome.GraniteBiomeType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.*;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.BlockLoc;
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
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.storage.WorldStorage;
import org.spongepowered.api.world.weather.Weather;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
        String name = getMCWorldInfo().fieldGet$levelName();
        if (name == null) {
            MCWorldInfo parentWorldInfo = ((MCDerivedWorldInfo) getMCWorldInfo()).fieldGet$theWorldInfo();
            return parentWorldInfo.fieldGet$levelName() + (getDimension().getDimensionId() == 1 ? "_the_end" : "_nether");
        }
        return name;
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
        for (String rule : rules) {
            map.put(rule, String.valueOf(getGameRule(rule)));
        }
        return map;
    }

    @Override
    public Dimension getDimension() {
        return new GraniteDimension(obj.fieldGet$provider());
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
    public BiomeType getBiome(Vector3i position) {
        return new GraniteBiomeType(obj.getBiomeGenForCoords(MinecraftUtils.graniteToMinecraftBlockPos(position.toInt())));
    }

    @Override
    public void setBiome(Vector3i vector3i, BiomeType biomeType) {
        Optional<Chunk> chunk = getChunk(vector3i);

        if (chunk.isPresent()) {
            chunk.get().setBiome(new Vector3i(vector3i.getX() % 16, 0, vector3i.getZ() % 16), biomeType);
        }
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
        entity.setPositionAndUpdate(position.getX(), position.getY(), position.getZ());
        boolean ret = obj.spawnEntityInWorld(entity);

        return ret ? Optional.of((Entity) wrap(entity)) : Optional.<Entity>absent();
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
        // Can't find method to set minVolume?
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
        return obj.fieldGet$worldInfo();
    }

    public MCGameRules getMCGameRules() {
        return getMCWorldInfo().fieldGet$gameRules();
    }

    @Override
    public Context getContext() {
        return new Context("world", getName());
    }
}
