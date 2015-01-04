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
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.Main;
import org.granitepowered.granite.impl.entity.hanging.art.GraniteArt;
import org.granitepowered.granite.impl.entity.living.meta.GraniteDyeColor;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseColor;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseStyle;
import org.granitepowered.granite.impl.entity.living.meta.GraniteHorseVariant;
import org.granitepowered.granite.impl.entity.living.meta.GraniteOcelotType;
import org.granitepowered.granite.impl.entity.living.meta.GraniteRabbitType;
import org.granitepowered.granite.impl.entity.living.meta.GraniteSkeletonType;
import org.granitepowered.granite.impl.entity.living.villager.GraniteCareer;
import org.granitepowered.granite.impl.entity.living.villager.GraniteProfession;
import org.granitepowered.granite.impl.item.GraniteEnchantment;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStackBuilder;
import org.granitepowered.granite.impl.potion.GranitePotionBuilder;
import org.granitepowered.granite.impl.potion.GranitePotionEffectType;
import org.granitepowered.granite.impl.util.GraniteRotation;
import org.granitepowered.granite.impl.world.GraniteDimension;
import org.granitepowered.granite.impl.world.GraniteDimensionType;
import org.granitepowered.granite.impl.world.biome.GraniteBiomeType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBiomeGenBase;
import org.granitepowered.granite.mc.MCBlock;
import org.granitepowered.granite.mc.MCEnchantment;
import org.granitepowered.granite.mc.MCEnumArt;
import org.granitepowered.granite.mc.MCGameRules;
import org.granitepowered.granite.mc.MCItem;
import org.granitepowered.granite.mc.MCPotion;
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
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraniteGameRegistry implements GameRegistry {

    Map<String, Art> arts = Maps.newHashMap();
    Map<String, BiomeType> biomes = Maps.newHashMap();
    Map<String, BlockType> blockTypes = Maps.newHashMap();
    Map<String, Career> careers = Maps.newHashMap();
    Map<String, DimensionType> dimensions = Maps.newHashMap();
    Map<String, DyeColor> dyeColors = Maps.newHashMap();
    Map<String, Enchantment> enchantments = Maps.newHashMap();
    Map<String, HorseColor> horseColors = Maps.newHashMap();
    Map<String, HorseStyle> horseStyles = Maps.newHashMap();
    Map<String, HorseVariant> horseVariants = Maps.newHashMap();
    Map<String, ItemType> itemTypes = Maps.newHashMap();
    Map<String, Profession> professions = Maps.newHashMap();
    Map<String, OcelotType> ocelots = Maps.newHashMap();
    Map<Profession, List<Career>> professionCareers = Maps.newHashMap();
    Map<String, RabbitType> rabbits = Maps.newHashMap();
    Map<Integer, Rotation> rotations = Maps.newHashMap();
    Map<String, SkeletonType> skeletons = Maps.newHashMap();
    Map<String, PotionEffectType> potionEffects = Maps.newHashMap();

    Collection<String> defaultGameRules = new ArrayList<>();

    GraniteItemStackBuilder itemStackBuilder = new GraniteItemStackBuilder();
    GranitePotionBuilder potionBuilder = new GranitePotionBuilder();

    public void register() {
        registerArts();
        registerBiomes();
        registerBlocks();
        registerDimensions();
        registerDyes();
        registerEnchantments();
        registerGameRules();
        registerHorseColors();
        registerHorseStyles();
        registerHorseVariants();
        registerItems();
        registerOcelots();
        registerProfessionsAndCareers();
        registerRabbits();
        registerRotations();
        registerSkeletons();
        registerPotionEffects();
    }

    private void registerArts() {
        Granite.instance.getLogger().info("Registering Arts");

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
    }

    private void registerBiomes() {
        Granite.instance.getLogger().info("Registering Biomes");

        try {
            Class biomeGenBaseClass = Mappings.getClass("BiomeGenBase");
            Field biomeList = Mappings.getField(biomeGenBaseClass, "biomeList");
            ArrayList<MCBiomeGenBase> biomesGenBase = Lists.newArrayList((MCBiomeGenBase[]) biomeList.get(biomeGenBaseClass));

            // Minecraft mutated biomes are placed into the biomeList with an index equal to the original plus 128, so there are around 70 null biomes in the list... Lets remove all of these
            biomesGenBase.removeAll(Collections.singleton(null));

            // Grab all fields from BiomeTypes
            for (Field f : BiomeTypes.class.getDeclaredFields()) {

                String name = "minecraft:" + f.getName().toLowerCase();

                // Run loop over all Minecraft biomes
                for (MCBiomeGenBase biome : biomesGenBase) {
                    String fieldName = f.getName().toLowerCase();

                    // Some Sponge biome names aren't equal to the names defined in the Minecraft class, rename them to their correct name
                    if (fieldName.equals("sky")) {
                        fieldName = "the_end";
                    } else if (fieldName.equals("extreme_hills_plus")) {
                        fieldName = "Extreme_Hills+";
                    } else if (fieldName.equals("frozen_ocean") || f.getName().equals("frozen_river") || f.getName().equals("Mushroom_Island") || f.getName().equals("Mushroom_Island_shore") || f.getName().equals("desert_hills") || f.getName().equals("forest_hills") || f.getName().equals("taiga_hills") || f.getName().equals("Jungle_Hills") || f.getName().equals("Jungle_Edge")) {
                        fieldName = fieldName.replace("_", "");
                    } else if (fieldName.equals("mesa_plateau_forest")) {
                        fieldName = "Mesa_Plateau_F";
                    }

                    String biomeName = biome.fieldGet$biomeName().toLowerCase().replace(" ", "_");

                    /**
                     * Here the magic happens, if the sponge biome name is equal to the minecraft biome name, place that biome into their correct field
                     * The biome is found so break the loop and continue to find the next biome
                     */
                    if (biomeName.equals(fieldName)) {
                        BiomeType biomeType = new GraniteBiomeType(biome);
                        ReflectionUtils.forceAccessible(f);
                        f.set(null, biomeType);
                        biomes.put(name, biomeType);
                        break;
                    }

                }

                if ( Main.debugLog ) Granite.getInstance().getLogger().info("Registered Biome " + name);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

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

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Block minecraft:" + block.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerDimensions() {
        Granite.instance.getLogger().info("Registering Dimensions");

        for (Field field : DimensionTypes.class.getDeclaredFields()) {
            ReflectionUtils.forceAccessible(field);

            String name = field.getName().toLowerCase();
            DimensionType dimensionType;
            boolean registered = false;
            try {
                switch (name) {
                    case "overworld":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(Mappings.getClass("WorldProviderSurface").newInstance()));
                        field.set(null, dimensionType);
                        dimensions.put("minecraft:" + name, dimensionType);
                        registered = true;
                        break;
                    case "nether":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(Mappings.getClass("WorldProviderHell").newInstance()));
                        field.set(null, dimensionType);
                        dimensions.put("minecraft:" + name, dimensionType);
                        registered = true;
                        break;
                    case "end":
                        dimensionType = new GraniteDimensionType(new GraniteDimension(Mappings.getClass("WorldProviderEnd").newInstance()));
                        field.set(null, dimensionType);
                        dimensions.put("minecraft:" + name, dimensionType);
                        registered = true;
                        break;
                }
                if (Main.debugLog && registered) {
                    Granite.getInstance().getLogger().info("Registered Dimension minecraft:" + name);
                }
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerDyes() {
        Granite.instance.getLogger().info("Registering Dyes");

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

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Enchantment " + enchantment.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }

        }
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
    }

    private void registerHorseStyles() {
        Granite.instance.getLogger().info("Registering Horse Styles");

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
    }

    private void registerHorseVariants() {
        Granite.instance.getLogger().info("Registering Horse Variants");

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

                if (Main.debugLog) {
                    Granite.getInstance().getLogger().info("Registered Item minecraft:" + item.getId());
                }
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
        }
    }

    private void registerOcelots() {
        Granite.instance.getLogger().info("Registering Ocelots");

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
    }

    // TODO: THIS IS BIG, FAT AND UGLY. And need redoing if possible.
    private void registerProfessionsAndCareers() {
        Granite.instance.getLogger().info("Registering Professions");

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

        Granite.instance.getLogger().info("Registering Careers");

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
                    registered = true;
                } else if (i == 4) {
                    Career career = new GraniteCareer(librarianProfession, name);
                    fields[i].set(null, career);
                    librarians.add(career);
                    registered = true;
                } else if (i == 5) {
                    Career career = new GraniteCareer(priestProfession, name);
                    fields[i].set(null, career);
                    priests.add(career);
                    registered = true;
                } else if (i > 5 && i <= 7) {
                    Career career = new GraniteCareer(blacksmithProfession, name);
                    fields[i].set(null, career);
                    blacksmiths.add(career);
                    registered = true;
                } else if (i >= 8 && i <= 10) {
                    Career career = new GraniteCareer(butcherProfession, name);
                    fields[i].set(null, career);
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
        Granite.instance.getLogger().info("Registering Rabbits");

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
    }

    private void registerRotations() {
        Granite.instance.getLogger().info("Registering Rotations");

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
        Granite.instance.getLogger().info("Registering Skeletons");

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
    }

    private void registerPotionEffects() {
        Granite.instance.getLogger().info("Registering PotionEffects");

        try {
            Class biomeGenBaseClass = Mappings.getClass("Potion");
            Field biomeList = Mappings.getField(biomeGenBaseClass, "potionTypes");
            ArrayList<MCPotion> mcPotions = Lists.newArrayList((MCPotion[]) biomeList.get(biomeGenBaseClass));
            mcPotions.removeAll(Collections.singleton(null));

            for (Field field : PotionEffectTypes.class.getDeclaredFields()) {
                ReflectionUtils.forceAccessible(field);

                String name = field.getName().toLowerCase();

                for (MCPotion p : mcPotions) {
                    HashMap<Object, MCPotion> field_180150_I = (HashMap) Mappings.getField(p.getClass(), "field_180150_I").get(p.getClass());
                    Object o = null;
                    for (Map.Entry entry : field_180150_I.entrySet()) {
                        if (p.equals(entry.getValue())) {
                            o = entry.getKey();
                            break;
                        }
                    }
                    String potionName = (String) Mappings.getField(o.getClass(), "resourcePath").get(o);
                    if (name.equals(potionName)) {
                        boolean isInstant = p.isInstant();

                        PotionEffectType potionEffectType = new GranitePotionEffectType(isInstant);
                        field.set(null, potionEffectType);
                        potionEffects.put("minecraft:" + name, potionEffectType);
                        if (Main.debugLog) {
                            Granite.getInstance().getLogger().info("Registered Potion Effect minecraft:" + potionName);
                        }

                        break;
                    }

                }
            }

        }catch (IllegalAccessException e){
            Throwables.propagate(e);
        }

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
        return Optional.fromNullable(biomes.get(id));
    }

    @Override
    public List<BiomeType> getBiomes() {
        return ImmutableList.copyOf(biomes.values());
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
        return Optional.fromNullable(ocelots.get(id));
    }

    @Override
    public List<OcelotType> getOcelotTypes() {
        return ImmutableList.copyOf(ocelots.values());
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        return Optional.fromNullable(rabbits.get(id));
    }

    @Override
    public List<RabbitType> getRabbitTypes() {
        return ImmutableList.copyOf(rabbits.values());
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        return Optional.fromNullable(skeletons.get(id));
    }

    @Override
    public List<SkeletonType> getSkeletonTypes() {
        return ImmutableList.copyOf(skeletons.values());
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
        // TODO: GameMode API
        throw new NotImplementedException("");
    }

    @Override
    public List<PotionEffectType> getPotionEffects() {
        return ImmutableList.copyOf(potionEffects.values());
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
        return Optional.fromNullable(dimensions.get(id));
    }

    @Override
    public List<DimensionType> getDimensionTypes() {
        return ImmutableList.copyOf(dimensions.values());
    }
}
