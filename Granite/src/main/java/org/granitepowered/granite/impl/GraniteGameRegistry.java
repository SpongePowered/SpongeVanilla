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

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import mc.MC;
import mc.MCEnumArt;
import mc.MCEnumBannerPattern;
import mc.MCGameRules;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.Main;
import org.granitepowered.granite.impl.effect.particle.GraniteParticleEffectBuilder;
import org.granitepowered.granite.impl.effect.particle.GraniteParticleType;
import org.granitepowered.granite.impl.entity.hanging.art.GraniteArt;
import org.granitepowered.granite.impl.entity.player.gamemode.GraniteGameMode;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStackBuilder;
import org.granitepowered.granite.impl.item.merchant.GraniteTradeOfferBuilder;
import org.granitepowered.granite.impl.meta.GraniteCareer;
import org.granitepowered.granite.impl.meta.GraniteHorseColor;
import org.granitepowered.granite.impl.meta.GraniteHorseStyle;
import org.granitepowered.granite.impl.meta.GraniteHorseVariant;
import org.granitepowered.granite.impl.meta.GraniteNotePitch;
import org.granitepowered.granite.impl.meta.GraniteOcelotType;
import org.granitepowered.granite.impl.meta.GraniteProfession;
import org.granitepowered.granite.impl.meta.GraniteRabbitType;
import org.granitepowered.granite.impl.meta.GraniteSkeletonType;
import org.granitepowered.granite.impl.meta.GraniteSkullType;
import org.granitepowered.granite.impl.potion.GranitePotionBuilder;
import org.granitepowered.granite.impl.status.GraniteFavicon;
import org.granitepowered.granite.impl.util.GraniteRotation;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.ReflectionUtils;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.attribute.Operation;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.meta.BannerPatternShape;
import org.spongepowered.api.block.meta.BannerPatternShapes;
import org.spongepowered.api.block.meta.NotePitch;
import org.spongepowered.api.block.meta.NotePitches;
import org.spongepowered.api.block.meta.SkullType;
import org.spongepowered.api.block.meta.SkullTypes;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.hanging.art.Art;
import org.spongepowered.api.entity.hanging.art.Arts;
import org.spongepowered.api.entity.living.animal.HorseColor;
import org.spongepowered.api.entity.living.animal.HorseColors;
import org.spongepowered.api.entity.living.animal.HorseStyle;
import org.spongepowered.api.entity.living.animal.HorseStyles;
import org.spongepowered.api.entity.living.animal.HorseVariant;
import org.spongepowered.api.entity.living.animal.HorseVariants;
import org.spongepowered.api.entity.living.animal.OcelotType;
import org.spongepowered.api.entity.living.animal.OcelotTypes;
import org.spongepowered.api.entity.living.animal.RabbitType;
import org.spongepowered.api.entity.living.animal.RabbitTypes;
import org.spongepowered.api.entity.living.monster.SkeletonType;
import org.spongepowered.api.entity.living.monster.SkeletonTypes;
import org.spongepowered.api.entity.living.villager.Career;
import org.spongepowered.api.entity.living.villager.Careers;
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.living.villager.Professions;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.item.CoalType;
import org.spongepowered.api.item.CookedFish;
import org.spongepowered.api.item.DyeColor;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.Fish;
import org.spongepowered.api.item.GoldenApple;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.difficulty.Difficulty;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

public class GraniteGameRegistry implements GameRegistry {

    public Map<String, Art> arts = Maps.newHashMap();
    public Map<String, BannerPatternShape> bannerPatternShapes = Maps.newHashMap();
    public Map<String, BiomeType> biomeTypes = Maps.newHashMap();
    public Map<String, BlockType> blockTypes = Maps.newHashMap();
    public Map<String, Career> careers = Maps.newHashMap();
    public Map<String, DimensionType> dimensionTypes = Maps.newHashMap();
    public Map<String, DyeColor> dyeColors = Maps.newHashMap();
    public Map<String, Enchantment> enchantments = Maps.newHashMap();
    public Map<String, GameMode> gameModes = Maps.newHashMap();
    public Map<String, HorseColor> horseColors = Maps.newHashMap();
    public Map<String, HorseStyle> horseStyles = Maps.newHashMap();
    public Map<String, HorseVariant> horseVariants = Maps.newHashMap();
    public Map<String, ItemType> itemTypes = Maps.newHashMap();
    public Map<String, NotePitch> notePitches = Maps.newHashMap();
    public Map<String, OcelotType> ocelotTypes = Maps.newHashMap();
    public Map<String, ParticleType> particleTypes = Maps.newHashMap();
    public Map<String, PotionEffectType> potionEffectTypes = Maps.newHashMap();
    public Map<String, Profession> professions = Maps.newHashMap();
    public Map<Profession, List<Career>> professionCareers = Maps.newHashMap();
    public Map<String, RabbitType> rabbitTypes = Maps.newHashMap();
    public Map<Integer, Rotation> rotations = Maps.newHashMap();
    public Map<String, SkeletonType> skeletonTypes = Maps.newHashMap();
    public Map<String, SkullType> skullTypes = Maps.newHashMap();
    public Map<String, SoundType> soundTypes = Maps.newHashMap();

    Collection<String> defaultGameRules = new ArrayList<>();

    ItemStackBuilder itemStackBuilder = new GraniteItemStackBuilder();
    PotionEffectBuilder potionEffectBuilder = new GranitePotionBuilder();
    TradeOfferBuilder tradeOfferBuilder = new GraniteTradeOfferBuilder();

    public void register() {
        registerArts();
        registerBannerPatternShapes();
        registerBiomes();
        registerBlocks();
        registerDimensions();
        registerDyeColors();
        registerEnchantments();
        registerGameModes();
        registerGameRules();
        registerHorseColors();
        registerHorseStyles();
        registerHorseVariants();
        registerItems();
        registerNotePitches();
        registerOcelots();
        registerParticleTypes();
        registerPotionEffects();
        registerProfessionsAndCareers();
        registerRabbits();
        registerRotations();
        registerSkeletons();
        registerSkullTypes();
        registerSounds();
    }

    private void registerArts() {
        Granite.getInstance().getLogger().info("Registering Arts");

        List<MCEnumArt> enumArts = Arrays.asList(MCEnumArt.class.getEnumConstants());
        for (Field field : Arts.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase().replace("_", "");
            for (MCEnumArt enumArt : enumArts) {
                if (name.equals(enumArt.name.toLowerCase())) {
                    try {
                        Art art = new GraniteArt(enumArt);
                        field.set(null, art);
                        arts.put(name, art);
                        if (Main.debugLog) {
                            Granite.getInstance().getLogger().info("Registered Art minecraft:" + art.getName());
                        }
                    } catch (IllegalAccessException e) {
                        Throwables.propagate(e);
                    }
                }
            }
        }
    }

    private void registerBannerPatternShapes() {
        Granite.getInstance().getLogger().info("Registering Banner Shapes");

        Object[] enums = MCEnumBannerPattern.class.getEnumConstants();
        Field[] fields = BannerPatternShapes.class.getFields();
        for (int i = 0; i < enums.length; i++) {
            ReflectionUtils.forceAccessible(fields[i]);

            try {
                BannerPatternShape bannerPatternShape = wrap((MC) enums[i]);
                fields[i].set(null, bannerPatternShape);
                bannerPatternShapes.put(bannerPatternShape.getName(), bannerPatternShape);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Banner Pattern minecraft:" + bannerPatternShape.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerBiomes() {
        /*Granite.getInstance().getLogger().info("Registering Biomes");

        try {
            Class biomeGenBaseClass = MCBiomeGenBase.class;
            Field biomeList = Mappings.getField(biomeGenBaseClass, "biomeList");
            ArrayList<BiomeGenBase> biomesGenBase = Lists.newArrayList((BiomeGenBase[]) biomeList.get(biomeGenBaseClass));
            biomesGenBase.removeAll(Collections.singleton(null));

            for (Field field : BiomeTypes.class.getDeclaredFields()) {
                ReflectionUtils.forceAccessible(field);

                String name = field.getName().toLowerCase();
                for (BiomeGenBase biome : biomesGenBase) {

                    if (name.equals("sky")) {
                        name = "the_end";
                    } else if (name.equals("extreme_hills_plus")) {
                        name = "extreme_hills+";
                    } else if (name.equals("frozen_ocean") || field.getName().equals("frozen_river") || field.getName().equals("mushroom_island")
                            || field.getName().equals("mushroom_island_shore") || field.getName().equals("desert_hills") || field.getName()
                            .equals("forest_hills") || field.getName().equals("taiga_hills") || field.getName().equals("jungle_hills") || field
                            .getName().equals("jungle_edge")) {
                        name = name.replace("_", "");
                    } else if (name.equals("mesa_plateau_forest")) {
                        name = "mesa_plateau_f";
                    }

                    String biomeName = biome.biomeName.toLowerCase().replace(" ", "_");

                    if (biomeName.equals(name)) {
                        BiomeType biomeType = new GraniteBiomeType(biome);
                        field.set(null, biomeType);
                        biomeTypes.put(name, biomeType);
                        if (Main.debugLog) {
                            Granite.getInstance().getLogger().info("Registered Biome minecarft:" + name);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            Granite.error(e);
        }*/
    }

    private void registerBlocks() {
        /*Granite.getInstance().getLogger().info("Registering Blocks");

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
        }*/
    }

    private void registerDimensions() {
        /*Granite.getInstance().getLogger().info("Registering Dimensions");

        for (Field field : DimensionTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            DimensionType dimensionType = null;
            boolean registered = false;
            try {
                switch (name) {
                    case "overworld":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(WorldProviderSurface.class.newInstance()));
                        registered = true;
                        break;
                    case "nether":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(WorldProviderHell.class.newInstance()));
                        registered = true;
                        break;
                    case "end":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(WorldProviderEnd.class.newInstance()));
                        registered = true;
                        break;
                }
                if (Main.debugLog && registered) {
                    field.set(null, dimensionType);
                    dimensionTypes.put(name, dimensionType);
                    Granite.getInstance().getLogger().info("Registered Dimension minecraft:" + name);
                }
            } catch (IllegalAccessException | InstantiationException e) {
                Granite.error(e);
            }
        }*/
    }

    private void registerDyeColors() {
        /*Granite.getInstance().getLogger().info("Registering Dye Colors");

        Object[] enums = EnumDyeColor.class.getEnumConstants();
        Field[] fields = DyeColors.class.getFields();
        for (int i = 0; i < enums.length; i++) {
            ReflectionUtils.forceAccessible(fields[i]);

            try {
                DyeColor dyeColor = wrap((MC) enums[i]);
                fields[i].set(null, dyeColor);
                dyeColors.put(dyeColor.getName(), dyeColor);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Dye Color minecraft:" + dyeColor.getName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }*/
    }

    private void registerEnchantments() {
        /*Granite.getInstance().getLogger().info("Registering Enchantments");

        for (Field field : Enchantments.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                MCEnchantment mcEnchantment = (MCEnchantment) Mappings.invokeStatic("Enchantment", "getEnchantmentByLocation", name);

                Enchantment enchantment = new GraniteEnchantment(mcEnchantment);
                field.set(null, enchantment);
                enchantments.put(name, enchantment);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Enchantment " + enchantment.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }

        }*/
    }

    private void registerGameModes() {
        Granite.getInstance().getLogger().info("Registering default GameModes");

        for (Field field : GameModes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                GameMode gameMode = new GraniteGameMode(name);
                field.set(null, gameMode);
                gameModes.put(name, gameMode);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered GameMode minecraft:" + name);
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }

        }
    }

    private void registerGameRules() {
        Granite.getInstance().getLogger().info("Registering default GameRules");
        MCGameRules gameRules = Instantiator.get().newGameRules();
        String[] rules = gameRules.getRules();
        for (String rule : rules) {
            defaultGameRules.add(rule);
            if (Main.debugLog) {
                Granite.getInstance().getLogger().info("Registered default GameRule minecraft:" + rule);
            }
        }
    }

    private void registerHorseColors() {
        Granite.getInstance().getLogger().info("Registering Horse Colors");

        for (int i = 0; i < HorseColors.class.getDeclaredFields().length; i++) {
            Field field = HorseColors.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseColor horseColor = new GraniteHorseColor(i, name);
                field.set(null, horseColor);
                horseColors.put(name, horseColor);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Color minecraft:" + horseColor.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerHorseStyles() {
        Granite.getInstance().getLogger().info("Registering Horse Styles");

        for (int i = 0; i < HorseStyles.class.getDeclaredFields().length; i++) {
            Field field = HorseStyles.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseStyle horseStyle = new GraniteHorseStyle(i, name);
                field.set(null, horseStyle);
                horseStyles.put(name, horseStyle);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Style minecraft:" + horseStyle.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerHorseVariants() {
        Granite.getInstance().getLogger().info("Registering Horse Variants");

        for (int i = 0; i < HorseVariants.class.getDeclaredFields().length; i++) {
            Field field = HorseVariants.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                HorseVariant horseVariant = new GraniteHorseVariant(i, name);
                field.set(null, horseVariant);
                horseVariants.put(name, horseVariant);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Horse Variant minecraft:" + horseVariant.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerItems() {
        /*Granite.getInstance().getLogger().info("Registering Items");

        for (Field field : ItemTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Object mcItem = Mappings.invokeStatic("Items", "getRegisteredItem", name);

                ItemType item = wrap((MCItem) mcItem);
                field.set(null, item);
                itemTypes.put(name, item);

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Item minecraft:" + item.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }*/
    }

    private void registerNotePitches() {
        Granite.getInstance().getLogger().info("Registering Ocelots");

        for (int i = 0; i < NotePitches.class.getDeclaredFields().length; i++) {
            Field field = NotePitches.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                NotePitch notePitch = new GraniteNotePitch((byte) i, name);
                field.set(null, notePitch);
                notePitches.put(name, notePitch);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered NotePitch minecraft:" + notePitch.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerOcelots() {
        Granite.getInstance().getLogger().info("Registering Ocelots");

        for (int i = 0; i < OcelotTypes.class.getDeclaredFields().length; i++) {
            Field field = OcelotTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                OcelotType ocelotType = new GraniteOcelotType(i, name);
                field.set(null, ocelotType);
                ocelotTypes.put(name, ocelotType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Ocelot minecraft:" + ocelotType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerParticleTypes() {
        Granite.getInstance().getLogger().info("Registering ParticleTypes");

        List<GraniteParticleType> types = new ArrayList<>();
        types.add(new GraniteParticleType("EXPLOSION_NORMAL", true));
        types.add(new GraniteParticleType.GraniteResizable("EXPLOSION_LARGE", 1f));
        types.add(new GraniteParticleType("EXPLOSION_HUGE", false));
        types.add(new GraniteParticleType("FIREWORKS_SPARK", true));
        types.add(new GraniteParticleType("WATER_BUBBLE", true));
        types.add(new GraniteParticleType("WATER_SPLASH", true));
        types.add(new GraniteParticleType("WATER_WAKE", true));
        types.add(new GraniteParticleType("SUSPENDED", false));
        types.add(new GraniteParticleType("SUSPENDED_DEPTH", false));
        types.add(new GraniteParticleType("CRIT", true));
        types.add(new GraniteParticleType("CRIT_MAGIC", true));
        types.add(new GraniteParticleType("SMOKE_NORMAL", true));
        types.add(new GraniteParticleType("SMOKE_LARGE", true));
        types.add(new GraniteParticleType("SPELL", false));
        types.add(new GraniteParticleType("SPELL_INSTANT", false));
        types.add(new GraniteParticleType.GraniteColorable("SPELL_MOB", Color.BLACK));
        types.add(new GraniteParticleType.GraniteColorable("SPELL_MOB_AMBIENT", Color.BLACK));
        types.add(new GraniteParticleType("SPELL_WITCH", false));
        types.add(new GraniteParticleType("DRIP_WATER", false));
        types.add(new GraniteParticleType("DRIP_LAVA", false));
        types.add(new GraniteParticleType("VILLAGER_ANGRY", false));
        types.add(new GraniteParticleType("VILLAGER_HAPPY", true));
        types.add(new GraniteParticleType("TOWN_AURA", true));
        types.add(new GraniteParticleType.GraniteNote("NOTE", 0f));
        types.add(new GraniteParticleType("PORTAL", true));
        types.add(new GraniteParticleType("ENCHANTMENT_TABLE", true));
        types.add(new GraniteParticleType("FLAME", true));
        types.add(new GraniteParticleType("LAVA", false));
        types.add(new GraniteParticleType("FOOTSTEP", false));
        types.add(new GraniteParticleType("CLOUD", true));
        types.add(new GraniteParticleType.GraniteColorable("REDSTONE", Color.RED));
        types.add(new GraniteParticleType("SNOWBALL", false));
        types.add(new GraniteParticleType("SNOW_SHOVEL", true));
        types.add(new GraniteParticleType("SLIME", false));
        types.add(new GraniteParticleType("HEART", false));
        types.add(new GraniteParticleType("BARRIER", false));
        types.add(new GraniteParticleType.GraniteMaterial("ITEM_CRACK", true, getItemBuilder().itemType(ItemTypes.STONE).build()));
        types.add(new GraniteParticleType.GraniteMaterial("BLOCK_CRACK", true, getItemBuilder().itemType(ItemTypes.STONE).build()));
        types.add(new GraniteParticleType.GraniteMaterial("BLOCK_DUST", true, getItemBuilder().itemType(ItemTypes.STONE).build()));
        types.add(new GraniteParticleType("WATER_DROP", false));
        types.add(new GraniteParticleType("ITEM_TAKE", false));
        types.add(new GraniteParticleType("MOB_APPEARANCE", false));

        for (int i = 0; i < types.size(); i++) {
            GraniteParticleType type = types.get(i);
            type.setId(i);
            particleTypes.put(type.getName(), type);

            Field field = ParticleTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);
            try {
                field.set(null, type);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (Main.debugLog) {
                Granite.getInstance().getLogger().info("Registered Particle minecraft:" + type.getName());
            }
        }
    }

    private void registerPotionEffects() {
        /*Granite.getInstance().getLogger().info("Registering PotionEffects");

        try {
            Class potionClass = Potion.class;
            Field potionTypes = Mappings.getField(potionClass, "potionTypes");
            ArrayList<MCPotion> mcPotions = Lists.newArrayList((MCPotion[]) potionTypes.get(potionClass));
            mcPotions.removeAll(Collections.singleton(null));

            for (Field field : PotionEffectTypes.class.getDeclaredFields()) {
                ReflectionUtils.forceAccessible(field);

                String name = field.getName().toLowerCase();
                for (MCPotion potion : mcPotions) {
                    HashMap<Object, MCPotion>
                            resourceToPotion =
                            (HashMap) Mappings.getField(potion.getClass(), "resourceToPotion").get(potion.getClass());

                    Object resourceLocation = null;
                    for (Map.Entry entry : resourceToPotion.entrySet()) {
                        if (potion.equals(entry.getValue())) {
                            resourceLocation = entry.getKey();
                        }
                    }

                    String potionName = (String) Mappings.getField(resourceLocation.getClass(), "resourcePath").get(resourceLocation);
                    if (name.equals(potionName)) {
                        PotionEffectType potionEffectType = new GranitePotionEffectType(potion);
                        field.set(null, potionEffectType);
                        potionEffectTypes.put(name, potionEffectType);
                        if (Main.debugLog) {
                            Granite.getInstance().getLogger().info("Registered Potion Effect minecraft:" + potionName);
                        }
                    }

                }
            }

        } catch (IllegalAccessException e) {
            Throwables.propagate(e);
        }*/

    }

    private void registerProfessionsAndCareers() {
        Granite.getInstance().getLogger().info("Registering Professions and Careers");

        for (int i = 0; i < Professions.class.getDeclaredFields().length; i++) {
            Field field = Professions.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                Profession profession = new GraniteProfession(i, name);
                field.set(null, profession);
                professions.put(name, profession);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Profession minecraft:" + profession.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }

        List<Career> farmers = new ArrayList<>();
        List<Career> librarians = new ArrayList<>();
        List<Career> priests = new ArrayList<>();
        List<Career> blacksmiths = new ArrayList<>();
        List<Career> butchers = new ArrayList<>();

        Profession farmerProfession = professions.get("farmer");
        Profession librarianProfession = professions.get("librarian");
        Profession priestProfession = professions.get("priest");
        Profession blacksmithProfession = professions.get("blacksmith");
        Profession butcherProfession = professions.get("butcher");

        for (int i = 0; i < Careers.class.getDeclaredFields().length; i++) {
            Field field = Careers.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                boolean registered = false;
                if (i < 4) {
                    Career career = new GraniteCareer(i, name, farmerProfession);
                    field.set(null, career);
                    farmers.add(career);
                    registered = true;
                } else if (i == 4) {
                    Career career = new GraniteCareer(i, name, librarianProfession);
                    field.set(null, career);
                    librarians.add(career);
                    registered = true;
                } else if (i == 5) {
                    Career career = new GraniteCareer(i, name, priestProfession);
                    field.set(null, career);
                    priests.add(career);
                    registered = true;
                } else if (i > 5 && i <= 7) {
                    Career career = new GraniteCareer(i, name, blacksmithProfession);
                    field.set(null, career);
                    blacksmiths.add(career);
                    registered = true;
                } else if (i >= 8 && i <= 10) {
                    Career career = new GraniteCareer(i, name, butcherProfession);
                    field.set(null, career);
                    butchers.add(career);
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
    }

    private void registerRabbits() {
        Granite.getInstance().getLogger().info("Registering Rabbits");

        for (int i = 0; i < RabbitTypes.class.getDeclaredFields().length; i++) {
            Field field = RabbitTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                RabbitType rabbitType = new GraniteRabbitType(RabbitTypes.class.getDeclaredFields()[i].toString().equals("KILLER") ? 99 : i, name);
                field.set(null, rabbitType);
                rabbitTypes.put(name, rabbitType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Rabbit minecraft:" + rabbitType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerRotations() {
        Granite.getInstance().getLogger().info("Registering Rotations");

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
    }

    private void registerSkeletons() {
        Granite.getInstance().getLogger().info("Registering Skeletons");

        for (int i = 0; i < SkeletonTypes.class.getDeclaredFields().length; i++) {
            Field field = SkeletonTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                SkeletonType skeletonType = new GraniteSkeletonType(i, name);
                field.set(null, skeletonType);
                skeletonTypes.put(name, skeletonType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Skeleton minecraft:" + skeletonType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerSkullTypes() {
        Granite.getInstance().getLogger().info("Registering Skulls");

        for (int i = 0; i < SkullTypes.class.getDeclaredFields().length; i++) {
            Field field = SkullTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            try {
                SkullType skullType = new GraniteSkullType((byte) i, name);
                field.set(null, skullType);
                skullTypes.put(name, skullType);
                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Skull minecraft:" + skullType.getName());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerSounds() {
        // TODO: Register all soundTypes to list in order they are in the SpongeAPI Class
        /*Granite.instance.getLogger().info("Registering Sounds");

        List<String> minecraftSoundNames = new ArrayList<>();
        minecraftSoundNames.add("");

        for (int i = 0; i < minecraftSoundNames.size(); i++) {
            SoundType soundType = new GraniteSoundType(minecraftSoundNames.get(i));
            soundTypes.put(minecraftSoundNames.get(i), soundType);
            Field field = SoundTypes.class.getDeclaredFields()[i];
            ReflectionUtils.forceAccessible(field);
            try {
                field.set(null, soundType);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (Main.debugLog) {
                Granite.getInstance().getLogger().info("Registered Sound minecraft:" + soundType.getName());
            }
        }*/
    }

    @Override
    public Optional<BlockType> getBlock(String id) {
        return Optional.fromNullable(blockTypes.get(id));
    }

    @Override
    public List<BlockType> getBlocks() {
        return ImmutableList.copyOf(blockTypes.values());
    }

    @Override
    public Optional<ItemType> getItem(String id) {
        return Optional.fromNullable(itemTypes.get(id));
    }

    @Override
    public List<ItemType> getItems() {
        return ImmutableList.copyOf(itemTypes.values());
    }

    @Override
    public Optional<BiomeType> getBiome(String id) {
        return Optional.fromNullable(biomeTypes.get(id));
    }

    @Override
    public List<BiomeType> getBiomes() {
        return ImmutableList.copyOf(biomeTypes.values());
    }

    @Override
    public ItemStackBuilder getItemBuilder() {
        return itemStackBuilder;
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        return tradeOfferBuilder;
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        return potionEffectBuilder;
    }

    @Override
    public Optional<ParticleType> getParticleType(String name) {
        return Optional.fromNullable(particleTypes.get(name));
    }

    @Override
    public List<ParticleType> getParticleTypes() {
        return ImmutableList.copyOf(particleTypes.values());
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        if (particle instanceof ParticleType.Resizable) {
            return new GraniteParticleEffectBuilder.GraniteResizable((GraniteParticleType.GraniteResizable) particle);
        }
        if (particle instanceof ParticleType.Colorable) {
            return new GraniteParticleEffectBuilder.GraniteColorable((GraniteParticleType.GraniteColorable) particle);
        }
        if (particle instanceof ParticleType.Note) {
            return new GraniteParticleEffectBuilder.GraniteNote((GraniteParticleType.GraniteNote) particle);
        }
        if (particle instanceof ParticleType.Material) {
            return new GraniteParticleEffectBuilder.GraniteMaterial((GraniteParticleType.GraniteMaterial) particle);
        }
        return new GraniteParticleEffectBuilder((GraniteParticleType) particle);
    }

    @Override
    public Optional<SoundType> getSound(String id) {
        return Optional.fromNullable(soundTypes.get(id));
    }

    @Override
    public List<SoundType> getSounds() {
        return ImmutableList.copyOf(soundTypes.values());
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
        return Optional.fromNullable(arts.get(id));
    }

    @Override
    public List<Art> getArts() {
        return ImmutableList.copyOf(arts.values());
    }

    @Override
    public Optional<DyeColor> getDye(String id) {
        return Optional.fromNullable(dyeColors.get(id));
    }

    @Override
    public List<DyeColor> getDyes() {
        return ImmutableList.copyOf(dyeColors.values());
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id) {
        return Optional.fromNullable(horseColors.get(id));
    }

    @Override
    public List<HorseColor> getHorseColors() {
        return ImmutableList.copyOf(horseColors.values());
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id) {
        return Optional.fromNullable(horseStyles.get(id));
    }

    @Override
    public List<HorseStyle> getHorseStyles() {
        return ImmutableList.copyOf(horseStyles.values());
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id) {
        return Optional.fromNullable(horseVariants.get(id));
    }

    @Override
    public List<HorseVariant> getHorseVariants() {
        return ImmutableList.copyOf(horseVariants.values());
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id) {
        return Optional.fromNullable(ocelotTypes.get(id));
    }

    @Override
    public List<OcelotType> getOcelotTypes() {
        return ImmutableList.copyOf(ocelotTypes.values());
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        return Optional.fromNullable(rabbitTypes.get(id));
    }

    @Override
    public List<RabbitType> getRabbitTypes() {
        return ImmutableList.copyOf(rabbitTypes.values());
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        return Optional.fromNullable(skeletonTypes.get(id));
    }

    @Override
    public List<SkeletonType> getSkeletonTypes() {
        return ImmutableList.copyOf(skeletonTypes.values());
    }

    @Override
    public Optional<Career> getCareer(String id) {
        return Optional.fromNullable(careers.get(id));
    }

    @Override
    public List<Career> getCareers() {
        return ImmutableList.copyOf(careers.values());
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
    public List<Profession> getProfessions() {
        return ImmutableList.copyOf(professions.values());
    }

    @Override
    public List<GameMode> getGameModes() {
        return ImmutableList.copyOf(gameModes.values());
    }

    @Override
    public List<PotionEffectType> getPotionEffects() {
        return ImmutableList.copyOf(potionEffectTypes.values());
    }

    @Override
    public Optional<Enchantment> getEnchantment(String id) {
        return Optional.fromNullable(enchantments.get(id));
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return ImmutableList.copyOf(enchantments.values());
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        return defaultGameRules;
    }

    @Override
    public Optional<DimensionType> getDimensionType(String id) {
        return Optional.fromNullable(dimensionTypes.get(id));
    }

    @Override
    public List<DimensionType> getDimensionTypes() {
        return ImmutableList.copyOf(dimensionTypes.values());
    }

    @Override
    public Optional<Rotation> getRotationFromDegree(int degrees) {
        return Optional.fromNullable(rotations.get(degrees));
    }

    @Override
    public List<Rotation> getRotations() {
        return ImmutableList.copyOf(rotations.values());
    }

    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return wrap(Instantiator.get().newGameProfile(uuid, name));
    }

    @Override
    public Favicon loadFavicon(String image) throws IOException {
        return new GraniteFavicon(image);
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        return new GraniteFavicon(ImageIO.read(file));
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        return new GraniteFavicon(ImageIO.read(url));
    }

    @Override
    public Favicon loadFavicon(InputStream inputStream) throws IOException {
        return new GraniteFavicon(ImageIO.read(inputStream));
    }

    @Override
    public Favicon loadFavicon(BufferedImage bufferedImage) throws IOException {
        return new GraniteFavicon(bufferedImage);
    }

    @Override
    public Optional<NotePitch> getNotePitch(String id) {
        return Optional.fromNullable(notePitches.get(id));
    }

    @Override
    public List<NotePitch> getNotePitches() {
        return ImmutableList.copyOf(notePitches.values());
    }

    @Override
    public Optional<SkullType> getSkullType(String id) {
        return Optional.fromNullable(skullTypes.get(id));
    }

    @Override
    public List<SkullType> getSkullTypes() {
        return ImmutableList.copyOf(skullTypes.values());
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShape(String id) {
        for (BannerPatternShape bannerPatternShape : bannerPatternShapes.values()) {
            if (bannerPatternShape.getName().equals(id)) {
                return Optional.fromNullable(bannerPatternShape);
            }
        }
        return Optional.absent();
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShapeById(String id) {
        for (BannerPatternShape bannerPatternShape : bannerPatternShapes.values()) {
            if (bannerPatternShape.getId().equals(id)) {
                return Optional.fromNullable(bannerPatternShape);
            }
        }
        return Optional.absent();
    }

    @Override
    public List<BannerPatternShape> getBannerPatternShapes() {
        return ImmutableList.copyOf(bannerPatternShapes.values());
    }

    @Override
    public GameDictionary getGameDictionary() {
        throw new NotImplementedException("");
    }

    @Override
    public RecipeRegistry getRecipeRegistry() {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Difficulty> getDifficulties() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Difficulty> getDifficulty(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<EntityInteractionType> getEntityInteractionTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<EntityInteractionType> getEntityInteractionType(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Attribute> getAttribute(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Attribute> getAttributes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Operation> getOperation(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Operation> getOperations() {
        throw new NotImplementedException("");
    }

    @Override
    public AttributeModifierBuilder getAttributeModifierBuilder() {
        throw new NotImplementedException("");
    }

    @Override
    public AttributeBuilder getAttributeBuilder() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<CoalType> getCoalType(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<CoalType> getCoalTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Fish> getFishType(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Fish> getFishTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<CookedFish> getCookedFishType(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<CookedFish> getCookedFishTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<GoldenApple> getGoldenAppleType(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<GoldenApple> getGoldenAppleTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public FireworkEffectBuilder getFireworkEffectBuilder() {
        throw new NotImplementedException("");
    }
}
