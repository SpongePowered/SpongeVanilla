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

package org.granitepowered.granite.impl;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.Main;
import org.granitepowered.granite.impl.item.GraniteEnchantment;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStackBuilder;
import org.granitepowered.granite.impl.potion.GranitePotionBuilder;
import org.granitepowered.granite.impl.util.GraniteRotation;
import org.granitepowered.granite.impl.world.GraniteEnvironment;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBlock;
import org.granitepowered.granite.mc.MCEnchantment;
import org.granitepowered.granite.mc.MCGameRules;
import org.granitepowered.granite.mc.MCItem;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.granitepowered.granite.utils.ReflectionUtils;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.hanging.art.Art;
import org.spongepowered.api.entity.living.meta.*;
import org.spongepowered.api.entity.living.villager.Career;
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.Environment;
import org.spongepowered.api.world.Environments;
import org.spongepowered.api.world.biome.BiomeType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class GraniteGameRegistry implements GameRegistry {
    Map<String, BiomeType> biomes = Maps.newHashMap();
    Map<String, BlockType> blockTypes = Maps.newHashMap();
    Map<String, Environment> environments = Maps.newHashMap();
    Map<String, ItemType> itemTypes = Maps.newHashMap();
    Map<String, Enchantment> enchantments = Maps.newHashMap();

    Collection<String> defaultGameRules = new ArrayList<>();

    GraniteItemStackBuilder itemStackBuilder = new GraniteItemStackBuilder();
    GranitePotionBuilder potionBuilder = new GranitePotionBuilder();

    public void register() {
        registerBiomes();
        registerBlocks();
        registerEnchantments();
        registerEnvironments();
        registerGameRules();
        registerItems();
        registerRotations();
    }

    private void registerBiomes() {
        // TODO: Do this later when we see how sponge/mixin goes about doing this :P
        /*Granite.instance.getLogger().info("Registering Biomes");

        try {
            Class biomeGenBaseClass = Mappings.getClass("BiomeGenBase");
            Field biomeList = Mappings.getField(biomeGenBaseClass, "biomeList");
            MCBiomeGenBase[] biomesGenBase = (MCBiomeGenBase[]) biomeList.get(biomeGenBaseClass);

            Field[] fields = BiomeTypes.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = "minecraft:" + fields[i].getName().toLowerCase();
                BiomeType biomeType = new GraniteBiomeType(biomesGenBase[i]);
                fields[i].set(null, biomeType);
                biomes.put(name, biomeType);
                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Biome" + name);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }*/
    }

    private void registerBlocks() {
        Granite.instance.getLogger().info("Registering Blocks");

        for (Field field : BlockTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCBlock mcBlock = (MCBlock) Mappings.invokeStatic("Blocks", "getRegisteredBlock", name);

                BlockType block = wrap(mcBlock);
                field.set(null, block);
                blockTypes.put(name, block);

                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Block minecraft:" + block.getId());

                // TODO: remove if not effecting anything (30/12/14)
                /*if (name.equals("redstone_wire")) {
                    block.getDefaultState().cycleProperty(block.getDefaultState().getPropertyByName("power").get());
                }*/
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEnchantments() {
        Granite.instance.getLogger().info("Registering Enchantments");

        for (Field field : Enchantments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCEnchantment mcEnchantment = (MCEnchantment) Mappings.invokeStatic("Enchantment", "getEnchantmentByLocation", name);

                Enchantment enchantment = new GraniteEnchantment(mcEnchantment);
                field.set(null, enchantment);
                enchantments.put("minecraft:" + name, enchantment);

                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Enchantment minecraft:" + enchantment.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private void registerEnvironments() {
        Granite.instance.getLogger().info("Registering Environments");

        for (Field field : Environments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Environment environment = new GraniteEnvironment(name);
                field.set(null, environment);
                environments.put("minecraft:" + environment.getName(), environment);
                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Environment " + environment.getName() + " dimId: " + environment.getDimensionId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerGameRules() {
        Granite.instance.getLogger().info("Registering default GameRules");
        MCGameRules gameRules = MinecraftUtils.instantiate(Mappings.getClass("GameRules"), new Class[]{});
        String[] rules = gameRules.getRules();
        for (String rule : rules) {
            defaultGameRules.add(rule);
            if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered default GameRule " + rule);
        }
    }

    private void registerItems() {
        Granite.instance.getLogger().info("Registering Items");

        for (Field field : ItemTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcItem = Mappings.invokeStatic("Items", "getRegisteredItem", name);

                ItemType item = wrap((MCItem) mcItem);
                field.set(null, item);
                itemTypes.put("minecraft:" + name, item);

                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Item minecraft:" + item.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerRotations() {
        Granite.instance.getLogger().info("Registering Rotations");

        int angle = 0;
        List<Rotation> rotations = new ArrayList<>();
        Field[] fields = Rotations.class.getDeclaredFields();

        for (Field field : fields) {
            ReflectionUtils.forceAccessible(field);

            try {
                Rotation rotation = new GraniteRotation(angle);
                field.set(null, rotation);
                rotations.add(rotation);
                angle += 45;
                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Rotation angle:" + rotation.getAngle());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Optional<BlockType> getBlock(String id) {
        return Optional.fromNullable(blockTypes.get(id));
    }

    @Override
    public List<BlockType> getBlocks() {
        return (List<BlockType>) blockTypes.values();
    }

    @Override
    public Optional<ItemType> getItem(String id) {
        return Optional.fromNullable(itemTypes.get(id));
    }

    @Override
    public List<ItemType> getItems() {
        return (List<ItemType>) itemTypes.values();
    }

    @Override
    public Optional<BiomeType> getBiome(String id) {
        return Optional.fromNullable(biomes.get(id));
    }

    @Override
    public List<BiomeType> getBiomes() {
        return (List<BiomeType>) biomes.values();
    }

    @Override
    public ItemStackBuilder getItemBuilder() {
        return itemStackBuilder;
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        // TODO: TradeOfferBuilder API
        throw new NotImplementedException("");
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        return potionBuilder;
    }

    @Override
    public Optional<ParticleType> getParticleType(String name) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public List<ParticleType> getParticleTypes() {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<EntityType> getEntity(String id) {
        // TODO: EntityType API
        throw new NotImplementedException("");
    }

    @Override
    public List<EntityType> getEntities() {
        // TODO: EntityType API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Art> getArt(String id) {
        // TODO: Art API
        throw new NotImplementedException("");
    }

    @Override
    public List<Art> getArts() {
        // TODO: Art API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<DyeColor> getDye(String id) {
        // TODO: Dye API
        throw new NotImplementedException("");
    }

    @Override
    public List<DyeColor> getDyes() {
        // TODO: Dye API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id) {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public List<HorseColor> getHorseColors() {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id) {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public List<HorseStyle> getHorseStyles() {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id) {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public List<HorseVariant> getHorseVariants() {
        // TODO: Horse API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id) {
        // TODO: Ocelot API
        throw new NotImplementedException("");
    }

    @Override
    public List<OcelotType> getOcelotTypes() {
        // TODO: Ocelot API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        // TODO: Rabbit API
        throw new NotImplementedException("");
    }

    @Override
    public List<RabbitType> getRabbitTypes() {
        // TODO: Rabbit API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        // TODO: Spooky Skary Skellingtons API
        throw new NotImplementedException("");
    }

    @Override
    public List<SkeletonType> getSkeletonTypes() {
        // TODO: Spooky Skary Skellingtons API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Career> getCareer(String id) {
        // TODO: Career API
        throw new NotImplementedException("");
    }

    @Override
    public List<Career> getCareers() {
        // TODO: Career API
        throw new NotImplementedException("");
    }

    @Override
    public List<Career> getCareers(Profession profession) {
        // TODO: Career API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Profession> getProfession(String id) {
        // TODO: Profession API
        throw new NotImplementedException("");
    }

    @Override
    public List<Profession> getProfessions() {
        // TODO: Profession API
        throw new NotImplementedException("");
    }

    @Override
    public List<GameMode> getGameModes() {
        // TODO: GameMode API
        throw new NotImplementedException("");
    }

    @Override
    public List<PotionEffectType> getPotionEffects() {
        // TODO: Potion Effects API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Enchantment> getEnchantment(String id) {
        return Optional.fromNullable(enchantments.get(id));
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return (List<Enchantment>) enchantments.values();
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        return defaultGameRules;
    }

    @Override
    public Optional<Environment> getEnvironment(String id) {
        return Optional.fromNullable(environments.get(id));
    }

    @Override
    public Optional<Environment> getEnvironment(int dimensionId) {
        for (Environment environment : environments.values()) {
            if (environment.getDimensionId() == dimensionId) {
                return Optional.fromNullable(environment);
            }
        }
        return Optional.absent();
    }

    @Override
    public List<Environment> getEnvironments() {
        return (List<Environment>) environments.values();
    }
}
