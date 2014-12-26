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
import org.granitepowered.granite.impl.item.inventory.GraniteItemStackBuilder;
import org.granitepowered.granite.impl.potion.GranitePotionBuilder;
import org.granitepowered.granite.impl.world.GraniteEnvironment;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBlock;
import org.granitepowered.granite.mc.MCEnchantment;
import org.granitepowered.granite.mc.MCItem;
import org.granitepowered.granite.utils.ReflectionUtils;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.effect.Particle;
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
import org.spongepowered.api.world.Environment;
import org.spongepowered.api.world.Environments;
import org.spongepowered.api.world.biome.BiomeType;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class GraniteGameRegistry implements GameRegistry {
    Map<String, BlockType> blockTypes = Maps.newHashMap();
    Map<String, ItemType> itemTypes = Maps.newHashMap();
    Map<String, Enchantment> enchantments = Maps.newHashMap();

    GraniteItemStackBuilder itemStackBuilder = new GraniteItemStackBuilder();
    GranitePotionBuilder potionBuilder = new GranitePotionBuilder();

    List<Environment> environments;

    public void register() {
        registerBlocks();
        registerItems();
        registerEnchantments();
        registerEnvironments();
    }

    private void registerBlocks() {
        Granite.instance.getLogger().info("Registering blocks");

        for (Field field : BlockTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCBlock mcBlock = (MCBlock) Mappings.invokeStatic("Blocks", "getRegisteredBlock", name);

                BlockType block = wrap(mcBlock);
                field.set(null, block);
                blockTypes.put("minecraft:" + name, block);

                Granite.getInstance().getLogger().info("Registered block " + block.getId());

                if (name.equals("redstone_wire")) {
                    block.getDefaultState().cycleProperty(block.getDefaultState().getPropertyByName("power").get());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerItems() {
        Granite.instance.getLogger().info("Registering items");

        for (Field field : ItemTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcItem = Mappings.invokeStatic("Items", "getRegisteredItem", name);

                ItemType item = wrap((MCItem) mcItem);
                field.set(null, item);
                itemTypes.put("minecraft:" + name, item);

                Granite.getInstance().getLogger().info("Registered item " + item.getId());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerEnchantments() {
        Granite.instance.getLogger().info("Registering enchantments");

        for (Field field : Enchantments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcEnchantment = Mappings.invokeStatic("Enchantment", "getEnchantmentByLocation", name);

                Enchantment enchantment = wrap((MCEnchantment) mcEnchantment);
                field.set(null, enchantment);
                enchantments.put("minecraft:" + name, enchantment);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    private void registerEnvironments() {
        Granite.instance.getLogger().info("Registering environments");

        for (Field field : Environments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                if (name.equals("overworld")) {
                    field.set(null, new GraniteEnvironment(0, "Overworld"));
                } else if (name.equals("nether")) {
                    field.set(null, new GraniteEnvironment(1, "Nether"));
                } else if (name.equals("end")) {
                    field.set(null, new GraniteEnvironment(-1, "The End"));
                }
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
        // TODO: Biome API
        throw new NotImplementedException("");
    }

    @Override
    public List<BiomeType> getBiomes() {
        // TODO: Biome API
        throw new NotImplementedException("");
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
    public Optional<Particle> getParticle(String id) {
        // TODO: Particle API
        throw new NotImplementedException("");
    }

    @Override
    public List<Particle> getParticles() {
        // TODO: Particle API
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
        return (Optional<Enchantment>) enchantments.get(id);
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return (List<Enchantment>) enchantments.values();
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        // TODO: Game Rules API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Environment> getEnvironment(String name) {
        for (Environment environment : environments) {
            if (environment.getName().equals(name)) {
                return (Optional<Environment>) environment;
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<Environment> getEnvironment(int dimensionId) {
        for (Environment environment : environments) {
            if (environment.getDimensionId() == dimensionId) {
                return (Optional<Environment>) environment;
            }
        }
        return Optional.absent();
    }

    @Override
    public List<Environment> getEnvironments() {
        return environments;
    }
}
