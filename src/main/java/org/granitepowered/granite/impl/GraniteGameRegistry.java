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

package org.granitepowered.granite.impl;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.Main;
import org.granitepowered.granite.impl.entity.living.meta.GraniteArt;
import org.granitepowered.granite.impl.entity.living.meta.GraniteDyeColor;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseColor;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseStyle;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseVariant;
import org.granitepowered.granite.impl.entity.living.meta.GraniteOcelotType;
import org.granitepowered.granite.impl.entity.living.meta.GraniteRabbitType;
import org.granitepowered.granite.impl.entity.living.meta.GraniteSkeletonType;
import org.granitepowered.granite.impl.entity.living.villager.GraniteCareer;
import org.granitepowered.granite.impl.entity.living.villager.GraniteProfession;
import org.granitepowered.granite.impl.entity.player.gamemode.GraniteGameMode;
import org.granitepowered.granite.impl.item.GraniteEnchantment;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStackBuilder;
import org.granitepowered.granite.impl.potion.GranitePotionBuilder;
import org.granitepowered.granite.impl.util.GraniteRotation;
import org.granitepowered.granite.impl.world.GraniteEnvironment;
import org.granitepowered.granite.impl.world.biome.GraniteBiomeType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBiomeGenBase;
import org.granitepowered.granite.mc.MCBlock;
import org.granitepowered.granite.mc.MCEnchantment;
import org.granitepowered.granite.mc.MCEnumArt;
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
import org.spongepowered.api.entity.hanging.art.Arts;
import org.spongepowered.api.entity.living.meta.DyeColor;
import org.spongepowered.api.entity.living.meta.DyeColors;
import org.spongepowered.api.entity.living.meta.HorseColor;
import org.spongepowered.api.entity.living.meta.HorseColors;
import org.spongepowered.api.entity.living.meta.HorseStyle;
import org.spongepowered.api.entity.living.meta.HorseStyles;
import org.spongepowered.api.entity.living.meta.HorseVariant;
import org.spongepowered.api.entity.living.meta.HorseVariants;
import org.spongepowered.api.entity.living.meta.OcelotType;
import org.spongepowered.api.entity.living.meta.OcelotTypes;
import org.spongepowered.api.entity.living.meta.RabbitType;
import org.spongepowered.api.entity.living.meta.RabbitTypes;
import org.spongepowered.api.entity.living.meta.SkeletonType;
import org.spongepowered.api.entity.living.meta.SkeletonTypes;
import org.spongepowered.api.entity.living.villager.Career;
import org.spongepowered.api.entity.living.villager.Careers;
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.living.villager.Professions;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
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
import org.spongepowered.api.world.biome.BiomeTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GraniteGameRegistry implements GameRegistry {

    ImmutableMap<String, Art> arts;
    ImmutableMap<String, BiomeType> biomes;
    ImmutableMap<String, BlockType> blockTypes;
    ImmutableMap<String, Career> careers;
    ImmutableMap<String, DyeColor> dyeColors;
    ImmutableMap<String, Enchantment> enchantments;
    ImmutableMap<String, Environment> environments;
    ImmutableMap<String, HorseColor> horseColors;
    ImmutableMap<String, HorseStyle> horseStyles;
    ImmutableMap<String, HorseVariant> horseVariants;
    ImmutableMap<String, ItemType> itemTypes;
    ImmutableMap<String, Profession> professions;
    ImmutableMap<String, OcelotType> ocelots;
    ImmutableMap<String, GameMode> gamemodes;
    ImmutableMap<Profession, List<Career>> professionCareers;
    ImmutableMap<String, RabbitType> rabbits;
    ImmutableMap<Integer, Rotation> rotations;
    ImmutableMap<String, SkeletonType> skeletons;
    
    Collection<String> defaultGameRules = new ArrayList<>();

    GraniteItemStackBuilder itemStackBuilder = new GraniteItemStackBuilder();
    GranitePotionBuilder potionBuilder = new GranitePotionBuilder();

    public void register() {
        registerArts();
        registerBiomes();
        registerBlocks();
        registerDyes();
        registerEnchantments();
        registerEnvironments();
        registerGameRules();
        registerHorseColors();
        registerHorseStyles();
        registerHorseVariants();
        registerItems();
        registerOcelots();
        registerProfessions();
        registerCareers();
        registerRabbits();
        registerRotations();
        registerSkeletons();
        registerGamemodes();

        Granite.getInstance().getLogger().info("Registered everything!");
    }

    private void registerArts() {
        Granite.instance.getLogger().info("Registering Arts");
        Map<String, Art> arts = Maps.newHashMap();
        List<MCEnumArt> mcEnumArts = Arrays.asList((MCEnumArt[]) Mappings.getClass("EnumArt").getEnumConstants());
        for (Field field : Arts.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase().replace("_", "");
            for (MCEnumArt mcEnumArt : mcEnumArts) {
                if (name.equals(mcEnumArt.fieldGet$name().toLowerCase())) {
                    try {
                        Art art = new GraniteArt(mcEnumArt);
                        field.set(null, art);
                        arts.put("minecraft:" + name, art);
                        if (Main.debugLog) {
                            Granite.getInstance().getLogger().info("Registered Art minecraft:" + art.getName());
                        }
                    } catch (IllegalAccessException e) {
                        Throwables.propagate(e);
                    }
                }
            }
        }
        
        this.arts = ImmutableMap.copyOf(arts);
    }

    private void registerBiomes() {
        
        Granite.instance.getLogger().info("Registering Biomes");

        Map<String, BiomeType> biomes = Maps.newHashMap();
        
        try {
            Class biomeGenBaseClass = Mappings.getClass("BiomeGenBase");
            Field biomeList = Mappings.getField(biomeGenBaseClass, "biomeList");
            MCBiomeGenBase[] biomesGenBase = (MCBiomeGenBase[]) biomeList.get(biomeGenBaseClass);

            Field[] fields = BiomeTypes.class.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                String name = "minecraft:" + fields[i].getName().toLowerCase();
                BiomeType biomeType = new GraniteBiomeType(biomesGenBase[i]);

                fields[i].setAccessible(true);

                Field modifiers = fields[i].getClass().getDeclaredField("modifiers");
                modifiers.setAccessible(true);
                modifiers.setInt(fields[i], fields[i].getModifiers() & ~Modifier.FINAL);

                fields[i].set(null, biomeType);
                biomes.put(name, biomeType);
                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Biome " + name);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Throwables.propagate(e);
        }
        
        this.biomes = ImmutableMap.copyOf(biomes);

    }

    private void registerBlocks() {
        Granite.instance.getLogger().info("Registering Blocks");

        Map<String, BlockType> blockTypes = Maps.newHashMap();
        
        for (Field field : BlockTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCBlock mcBlock = (MCBlock) Mappings.invokeStatic("Blocks", "getRegisteredBlock", name);

                BlockType block = wrap(mcBlock);
                field.set(null, block);
                blockTypes.put(name, block);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Block minecraft:" + block.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
        
        this.blockTypes = ImmutableMap.copyOf(blockTypes);
    }

    private void registerDyes() {
        Granite.instance.getLogger().info("Registering Dyes");

        Map<String, DyeColor> dyeColors = Maps.newHashMap();

        for (Field field : DyeColors.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                DyeColor dyeColor = new GraniteDyeColor(name);
                field.set(null, dyeColor);
                dyeColors.put("minecraft:" + name, dyeColor);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Dye Color minecraft:" + dyeColor.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.dyeColors = ImmutableMap.copyOf(dyeColors);
    }

    private void registerEnchantments() {
        Granite.instance.getLogger().info("Registering Enchantments");

        Map<String, Enchantment> enchantments = Maps.newHashMap();

        for (Field field : Enchantments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCEnchantment mcEnchantment = (MCEnchantment) Mappings.invokeStatic("Enchantment", "getEnchantmentByLocation", name);

                Enchantment enchantment = new GraniteEnchantment(mcEnchantment);
                field.set(null, enchantment);
                enchantments.put("minecraft:" + name, enchantment);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Enchantment " + enchantment.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }

        }

        this.enchantments = ImmutableMap.copyOf(enchantments);
    }

    private void registerEnvironments() {
        Granite.instance.getLogger().info("Registering Environments");

        Map<String, Environment> environments = Maps.newHashMap();

        for (Field field : Environments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Environment environment = new GraniteEnvironment(name);
                field.set(null, environment);
                environments.put("minecraft:" + environment.getName(), environment);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger()
                            .info("Registered Environment " + environment.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.environments = ImmutableMap.copyOf(environments);
    }

    private void registerGameRules() {
        Granite.instance.getLogger().info("Registering default GameRules");
        MCGameRules gameRules = MinecraftUtils.instantiate(Mappings.getClass("GameRules"), new Class[]{});
        String[] rules = gameRules.getRules();
        for (String rule : rules) {
            defaultGameRules.add(rule);
            if (Main.debugLog) {
                Granite.getInstance().getLogger().info("Registered default GameRule minecraft:" + rule);
            }
        }
    }

    private void registerHorseColors() {
        Granite.instance.getLogger().info("Registering Horse Colors");

        Map<String, HorseColor> horseColors = Maps.newHashMap();

        for (Field field : HorseColors.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseColor horseColor = new GraniteHorseColor(name);
                field.set(null, horseColor);
                horseColors.put("minecraft:" + name, horseColor);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Color minecraft:" + horseColor.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.horseColors = ImmutableMap.copyOf(horseColors);
    }

    private void registerHorseStyles() {
        Granite.instance.getLogger().info("Registering Horse Styles");

        Map<String, HorseStyle> horseStyles = Maps.newHashMap();

        for (Field field : HorseStyles.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseStyle horseStyle = new GraniteHorseStyle(name);
                field.set(null, horseStyle);
                horseStyles.put("minecraft:" + name, horseStyle);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Style minecraft:" + horseStyle.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.horseStyles = ImmutableMap.copyOf(horseStyles);
    }

    private void registerHorseVariants() {
        Granite.instance.getLogger().info("Registering Horse Variants");

        Map<String, HorseVariant> horseVariants = Maps.newHashMap();

        for (Field field : HorseVariants.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseVariant horseVariant = new GraniteHorseVariant(name);
                field.set(null, horseVariant);
                horseVariants.put("minecraft:" + name, horseVariant);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Variant minecraft:" + horseVariant.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.horseVariants = ImmutableMap.copyOf(horseVariants);
    }

    private void registerItems() {
        Granite.instance.getLogger().info("Registering Items");

        Map<String, ItemType> itemTypes = Maps.newHashMap();

        for (Field field : ItemTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcItem = Mappings.invokeStatic("Items", "getRegisteredItem", name);

                ItemType item = wrap((MCItem) mcItem);
                field.set(null, item);
                itemTypes.put("minecraft:" + name, item);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Item minecraft:" + item.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.itemTypes = ImmutableMap.copyOf(itemTypes);
    }

    private void registerOcelots() {
        Granite.instance.getLogger().info("Registering Ocelots");

        Map<String, OcelotType> ocelots = Maps.newHashMap();

        for (Field field : OcelotTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                OcelotType ocelotType = new GraniteOcelotType(name);
                field.set(null, ocelotType);
                ocelots.put("minecraft:" + name, ocelotType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Ocelot minecraft:" + ocelotType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.ocelots = ImmutableMap.copyOf(ocelots);
    }

    private void registerGamemodes(){
        Granite.getInstance().getLogger().info("Registering Gamemodes");

        Map<String, GameMode> gamemodes = Maps.newHashMap();

        Object[] gamemodeEnum = Mappings.getClass("GameType").getEnumConstants();

        Field[] fields = GameModes.class.getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
            ReflectionUtils.forceAccessible(fields[i]);

            String name = fields[i].getName().toLowerCase();
            try {
                GameMode gameMode = new GraniteGameMode();
                fields[i].set(null, gameMode);
                gamemodes.put("minecraft:" + name, gameMode);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Gamemode:" + gamemodeEnum[i]);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        this.gamemodes = ImmutableMap.copyOf(gamemodes);
    }

    // TODO: THIS IS BIG, FAT AND UGLY. And need redoing if possible. <-- Still?
    private void registerProfessions() {
        Granite.instance.getLogger().info("Registering Professions");

        Map<String, Profession> professions = Maps.newHashMap();

        for (Field field : Professions.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Profession profession = new GraniteProfession(name);
                field.set(null, profession);
                professions.put("minecraft:" + name, profession);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Profession minecraft:" + profession.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.professions = ImmutableMap.copyOf(professions);
    }

    public void registerCareers(){
        Granite.instance.getLogger().info("Registering Careers");

        Map<Profession, List<Career>> professionCareers = Maps.newHashMap();
        Map<String, Career> careers = Maps.newHashMap();

        List<Career> farmers = new ArrayList<>();
        List<Career> librarians = new ArrayList<>();
        List<Career> priests = new ArrayList<>();
        List<Career> blacksmiths = new ArrayList<>();
        List<Career> butchers = new ArrayList<>();

        Profession farmerProfession = professions.get("minecraft:farmer");
        Profession librarianProfession = professions.get("minecraft:librarian");
        Profession priestProfession = professions.get("minecraft:priest");
        Profession blacksmithProfession = professions.get("minecraft:blacksmith");
        Profession butcherProfession = professions.get("minecraft:butcher");

        Field[] fields = Careers.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            ReflectionUtils.forceAccessible(fields[i]);

            String name = fields[i].getName().toLowerCase();
            try {
                boolean registered = false;
                if (i < 4) {
                    Career career = new GraniteCareer(farmerProfession, name);
                    fields[i].set(null, career);
                    farmers.add(career);
                    careers.put("minecraft:" + name, career);
                    registered = true;
                } else if (i == 4) {
                    Career career = new GraniteCareer(librarianProfession, name);
                    fields[i].set(null, career);
                    librarians.add(career);
                    careers.put("minecraft:" + name, career);
                    registered = true;
                } else if (i == 5) {
                    Career career = new GraniteCareer(priestProfession, name);
                    fields[i].set(null, career);
                    priests.add(career);
                    careers.put("minecraft:" + name, career);
                    registered = true;
                } else if (i > 5 && i <= 7) {
                    Career career = new GraniteCareer(blacksmithProfession, name);
                    fields[i].set(null, career);
                    blacksmiths.add(career);
                    careers.put("minecraft:" + name, career);
                    registered = true;
                } else if (i >= 8 && i <= 10) {
                    Career career = new GraniteCareer(butcherProfession, name);
                    fields[i].set(null, career);
                    butchers.add(career);
                    careers.put("minecraft:" + name, career);
                    registered = true;
                }
                if (Main.debugLog && registered) {
                    Granite.getInstance().getLogger().info("Registered Career minecraft:" + name);
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        professionCareers.put(farmerProfession, farmers);
        professionCareers.put(librarianProfession, librarians);
        professionCareers.put(priestProfession, priests);
        professionCareers.put(blacksmithProfession, blacksmiths);
        professionCareers.put(butcherProfession, butchers);

        this.professionCareers = ImmutableMap.copyOf(professionCareers);
        this.careers = ImmutableMap.copyOf(careers);
    }

    private void registerRabbits() {
        Granite.instance.getLogger().info("Registering Rabbits");

        Map<String, RabbitType> rabbits = Maps.newHashMap();

        for (Field field : RabbitTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                RabbitType rabbitType = new GraniteRabbitType(name);
                field.set(null, rabbitType);
                rabbits.put("minecraft:" + name, rabbitType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Rabbit minecraft:" + rabbitType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.rabbits = ImmutableMap.copyOf(rabbits);
    }

    private void registerRotations() {
        Granite.instance.getLogger().info("Registering Rotations");

        Map<Integer, Rotation> rotations = Maps.newHashMap();

        int angle = 0;
        Field[] fields = Rotations.class.getDeclaredFields();

        for (Field field : fields) {
            ReflectionUtils.forceAccessible(field);

            try {
                Rotation rotation = new GraniteRotation(angle);
                field.set(null, rotation);
                rotations.put(angle, rotation);
                angle += 45;
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Rotation degrees:" + rotation.getAngle());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.rotations = ImmutableMap.copyOf(rotations);
    }

    private void registerSkeletons() {
        Granite.instance.getLogger().info("Registering Skeletons");

        Map<String, SkeletonType> skeletons = Maps.newHashMap();

        for (Field field : SkeletonTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                SkeletonType skeletonType = new GraniteSkeletonType(name);
                field.set(null, skeletonType);
                skeletons.put("minecraft:" + name, skeletonType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Skeleton minecraft:" + skeletonType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        this.skeletons = ImmutableMap.copyOf(skeletons);
    }

    @Override
    public Optional<BlockType> getBlock(String id) {
        return Optional.fromNullable(blockTypes.get(id));
    }

    @Override
    public ImmutableList<BlockType> getBlocks() {
        return blockTypes.values().asList();
    }

    @Override
    public Optional<ItemType> getItem(String id) {
        return Optional.fromNullable(itemTypes.get(id));
    }

    @Override
    public ImmutableList<ItemType> getItems() {
        return itemTypes.values().asList();
    }

    @Override
    public Optional<BiomeType> getBiome(String id) {
        return Optional.fromNullable(biomes.get(id));
    }

    @Override
    public ImmutableList<BiomeType> getBiomes() {
        return biomes.values().asList();
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
    public ImmutableList<ParticleType> getParticleTypes() {
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
    public ImmutableList<EntityType> getEntities() {
        // TODO: EntityType API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Art> getArt(String id) {
        return Optional.fromNullable(arts.get(id));
    }

    @Override
    public ImmutableList<Art> getArts() {
        return arts.values().asList();
    }

    @Override
    public Optional<DyeColor> getDye(String id) {
        return Optional.fromNullable(dyeColors.get(id));
    }

    @Override
    public ImmutableList<DyeColor> getDyes() {
        return dyeColors.values().asList();
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id) {
        return Optional.fromNullable(horseColors.get(id));
    }

    @Override
    public ImmutableList<HorseColor> getHorseColors() {
        return horseColors.values().asList();
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id) {
        return Optional.fromNullable(horseStyles.get(id));
    }

    @Override
    public ImmutableList<HorseStyle> getHorseStyles() {
        return horseStyles.values().asList();
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id) {
        return Optional.fromNullable(horseVariants.get(id));
    }

    @Override
    public ImmutableList<HorseVariant> getHorseVariants() {
        return horseVariants.values().asList();
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id) {
        return Optional.fromNullable(ocelots.get(id));
    }

    @Override
    public ImmutableList<OcelotType> getOcelotTypes() {
        return ocelots.values().asList();
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        return Optional.fromNullable(rabbits.get(id));
    }

    @Override
    public ImmutableList<RabbitType> getRabbitTypes() {
        return rabbits.values().asList();
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        return Optional.fromNullable(skeletons.get(id));
    }

    @Override
    public ImmutableList<SkeletonType> getSkeletonTypes() {
        return skeletons.values().asList();
    }

    @Override
    public Optional<Career> getCareer(String id) {
        return Optional.fromNullable(careers.get(id));
    }

    @Override
    public ImmutableList<Career> getCareers() {
        return careers.values().asList();
    }

    @Override
    public List<Career> getCareers(Profession profession) {
        return professionCareers.get(profession);
    }

    @Override
    public Optional<Profession> getProfession(String id) {
        return Optional.fromNullable(professions.get(id));
    }

    @Override
    public ImmutableList<Profession> getProfessions() {
        return professions.values().asList();
    }

    @Override
    public ImmutableList<GameMode> getGameModes() {
        return ImmutableList.copyOf(gamemodes.values());
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
    public ImmutableList<Enchantment> getEnchantments() {
        return enchantments.values().asList();
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
    public ImmutableList<Environment> getEnvironments() {
        return environments.values().asList();
    }
}
