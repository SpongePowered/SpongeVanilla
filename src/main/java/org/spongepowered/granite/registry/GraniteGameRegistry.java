/*
 * This file is part of Granite, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <http://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.granite.registry;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.BiomeGenBase;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.GameDictionary;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.attribute.Attribute;
import org.spongepowered.api.attribute.AttributeBuilder;
import org.spongepowered.api.attribute.AttributeCalculator;
import org.spongepowered.api.attribute.AttributeModifierBuilder;
import org.spongepowered.api.attribute.Operation;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.tile.TileEntityType;
import org.spongepowered.api.block.tile.data.BannerPatternShape;
import org.spongepowered.api.block.tile.data.BannerPatternShapes;
import org.spongepowered.api.block.tile.data.NotePitch;
import org.spongepowered.api.block.tile.data.SkullType;
import org.spongepowered.api.effect.particle.ParticleEffectBuilder;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.effect.sound.SoundType;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.EntityInteractionType;
import org.spongepowered.api.entity.EntityInteractionTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
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
import org.spongepowered.api.entity.living.villager.Profession;
import org.spongepowered.api.entity.living.villager.Professions;
import org.spongepowered.api.entity.player.gamemode.GameMode;
import org.spongepowered.api.entity.player.gamemode.GameModes;
import org.spongepowered.api.item.CoalType;
import org.spongepowered.api.item.CoalTypes;
import org.spongepowered.api.item.CookedFish;
import org.spongepowered.api.item.CookedFishes;
import org.spongepowered.api.item.DyeColor;
import org.spongepowered.api.item.DyeColors;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.FireworkEffectBuilder;
import org.spongepowered.api.item.Fish;
import org.spongepowered.api.item.Fishes;
import org.spongepowered.api.item.GoldenApple;
import org.spongepowered.api.item.GoldenApples;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackBuilder;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;
import org.spongepowered.api.item.recipe.RecipeRegistry;
import org.spongepowered.api.potion.PotionEffectBuilder;
import org.spongepowered.api.potion.PotionEffectType;
import org.spongepowered.api.potion.PotionEffectTypes;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.selector.ArgumentType;
import org.spongepowered.api.text.selector.SelectorType;
import org.spongepowered.api.text.selector.SelectorTypes;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.text.translation.locale.Locales;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.api.util.rotation.Rotation;
import org.spongepowered.api.util.rotation.Rotations;
import org.spongepowered.api.world.DimensionType;
import org.spongepowered.api.world.DimensionTypes;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.difficulty.Difficulties;
import org.spongepowered.api.world.difficulty.Difficulty;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.granite.effect.particle.GraniteParticleType;
import org.spongepowered.granite.effect.sound.GraniteSoundType;
import org.spongepowered.granite.entity.GraniteEntityInteractionType;
import org.spongepowered.granite.entity.GraniteEntityType;
import org.spongepowered.granite.entity.living.animal.GraniteHorseColor;
import org.spongepowered.granite.entity.living.animal.GraniteHorseStyle;
import org.spongepowered.granite.entity.living.animal.GraniteHorseVariant;
import org.spongepowered.granite.entity.living.animal.GraniteOcelotType;
import org.spongepowered.granite.entity.living.animal.GraniteRabbitType;
import org.spongepowered.granite.entity.living.monster.GraniteSkeletonType;
import org.spongepowered.granite.entity.living.villager.GraniteCareer;
import org.spongepowered.granite.entity.living.villager.GraniteProfession;
import org.spongepowered.granite.entity.player.gamemode.GraniteGameMode;
import org.spongepowered.granite.item.GraniteCoalType;
import org.spongepowered.granite.item.GraniteGoldenApple;
import org.spongepowered.granite.potion.GranitePotionEffectType;
import org.spongepowered.granite.rotation.GraniteRotation;
import org.spongepowered.granite.text.GraniteTextFactory;
import org.spongepowered.granite.text.chat.GraniteChatType;
import org.spongepowered.granite.text.format.GraniteTextColor;
import org.spongepowered.granite.text.format.GraniteTextStyle;
import org.spongepowered.granite.text.selector.GraniteSelectorType;
import org.spongepowered.granite.world.GraniteDimensionType;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Singleton;

@NonnullByDefault
@Singleton
public class GraniteGameRegistry implements GameRegistry {

    private final Map<String, Art> arts = Maps.newHashMap();
    private final Map<String, BannerPatternShape> bannerPatternShapes = Maps.newHashMap();
    private final Map<String, BiomeType> biomeTypes = Maps.newHashMap();
    private final Map<String, BlockType> blockTypes = Maps.newHashMap();
    private final Map<String, Career> careers = Maps.newHashMap();
    private final Map<String, ChatType> chatTypes = Maps.newHashMap();
    private final Map<String, CoalType> coalTypes = Maps.newHashMap();
    private final Map<String, CookedFish> cookedFishes = Maps.newHashMap();
    private final Map<String, String> defaultGameRules = Maps.newHashMap();
    private final Map<String, Difficulty> difficulties = Maps.newHashMap();
    private final Map<String, DimensionType> dimensionTypes = Maps.newHashMap();
    private final Map<Direction, EnumFacing> directions = Maps.newHashMap();
    private final Map<String, DyeColor> dyeColors = Maps.newHashMap();
    private final Map<String, Enchantment> enchantments = Maps.newHashMap();
    private final Map<String, EntityInteractionType> entityInteractionTypes = Maps.newHashMap();
    private final Map<String, EntityType> entityTypes = Maps.newHashMap();
    private final Map<String, Fish> fishes = Maps.newHashMap();
    private final Map<String, GameMode> gameModes = Maps.newHashMap();
    private final Map<String, GoldenApple> goldenApples = Maps.newHashMap();
    private final Map<String, HorseColor> horseColors = Maps.newHashMap();
    private final Map<String, HorseStyle> horseStyles = Maps.newHashMap();
    private final Map<String, HorseVariant> horseVariants = Maps.newHashMap();
    private final Map<String, ItemType> itemTypes = Maps.newHashMap();
    private final Map<String, Locale> locales = Maps.newHashMap();
    private final Map<String, NotePitch> notePitches = Maps.newHashMap();
    private final Map<String, OcelotType> ocelotTypes = Maps.newHashMap();
    private final Map<String, ParticleType> particleTypes = Maps.newHashMap();
    private final Map<String, PotionEffectType> potionEffectTypes = Maps.newHashMap();
    private final Map<String, Profession> professions = Maps.newHashMap();
    private final Map<String, RabbitType> rabbitTypes = Maps.newHashMap();
    private final Map<String, Rotation> rotations = Maps.newHashMap();
    private final Map<String, SelectorType> selectorTypes = Maps.newHashMap();
    private final Map<String, SkeletonType> skeletonTypes = Maps.newHashMap();
    private final Map<String, SkullType> skullTypes = Maps.newHashMap();
    private final Map<String, SoundType> soundTypes = Maps.newHashMap();
    private final Map<String, TextColor> textColors = Maps.newHashMap();
    private final Map<String, TextStyle> textStyles = Maps.newHashMap();
    private final Map<String, Translation> translations = Maps.newHashMap();

    public EnumFacing getNotchDirection(Direction direction) {
        return this.directions.get(direction);
    }

    @Override
    public Optional<BlockType> getBlock(String id) {
        return Optional.fromNullable(this.blockTypes.get(id));
    }

    @Override
    public Collection<BlockType> getBlocks() {
        return ImmutableList.copyOf(this.blockTypes.values());
    }

    @Override
    public Optional<ItemType> getItem(String id) {
        return Optional.fromNullable(this.itemTypes.get(id));
    }

    @Override
    public Collection<ItemType> getItems() {
        return ImmutableList.copyOf(this.itemTypes.values());
    }

    @Override
    public Optional<TileEntityType> getTileEntityType(String id) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<TileEntityType> getTileEntityTypes() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<BiomeType> getBiome(String id) {
        return Optional.fromNullable(this.biomeTypes.get(id));
    }

    @Override
    public Collection<BiomeType> getBiomes() {
        return ImmutableList.copyOf(this.biomeTypes.values());
    }

    @Override
    public ItemStackBuilder getItemBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public TradeOfferBuilder getTradeOfferBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public PotionEffectBuilder getPotionEffectBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<ParticleType> getParticleType(String name) {
        return Optional.fromNullable(this.particleTypes.get(name));
    }

    @Override
    public Collection<ParticleType> getParticleTypes() {
        return ImmutableList.copyOf(this.particleTypes.values());
    }

    @Override
    public ParticleEffectBuilder getParticleEffectBuilder(ParticleType particle) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<SoundType> getSound(String name) {
        return Optional.fromNullable(this.soundTypes.get(name));
    }

    @Override
    public Collection<SoundType> getSounds() {
        return ImmutableList.copyOf(this.soundTypes.values());
    }

    @Override
    public Optional<EntityType> getEntity(String id) {
        return Optional.fromNullable(this.entityTypes.get(id));
    }

    @Override
    public Collection<EntityType> getEntities() {
        return ImmutableList.copyOf(this.entityTypes.values());
    }

    @Override
    public Optional<Art> getArt(String id) {
        return Optional.fromNullable(this.arts.get(id));
    }

    @Override
    public Collection<Art> getArts() {
        return ImmutableList.copyOf(this.arts.values());
    }

    @Override
    public Optional<DyeColor> getDye(String id) {
        return Optional.fromNullable(this.dyeColors.get(id));
    }

    @Override
    public Collection<DyeColor> getDyes() {
        return ImmutableList.copyOf(this.dyeColors.values());
    }

    @Override
    public Optional<HorseColor> getHorseColor(String id) {
        return Optional.fromNullable(this.horseColors.get(id));
    }

    @Override
    public Collection<HorseColor> getHorseColors() {
        return ImmutableList.copyOf(this.horseColors.values());
    }

    @Override
    public Optional<HorseStyle> getHorseStyle(String id) {
        return Optional.fromNullable(this.horseStyles.get(id));
    }

    @Override
    public Collection<HorseStyle> getHorseStyles() {
        return ImmutableList.copyOf(this.horseStyles.values());
    }

    @Override
    public Optional<HorseVariant> getHorseVariant(String id) {
        return Optional.fromNullable(this.horseVariants.get(id));
    }

    @Override
    public Collection<HorseVariant> getHorseVariants() {
        return ImmutableList.copyOf(this.horseVariants.values());
    }

    @Override
    public Optional<OcelotType> getOcelotType(String id) {
        return Optional.fromNullable(this.ocelotTypes.get(id));
    }

    @Override
    public Collection<OcelotType> getOcelotTypes() {
        return ImmutableList.copyOf(this.ocelotTypes.values());
    }

    @Override
    public Optional<RabbitType> getRabbitType(String id) {
        return Optional.fromNullable(this.rabbitTypes.get(id));
    }

    @Override
    public Collection<RabbitType> getRabbitTypes() {
        return ImmutableList.copyOf(this.rabbitTypes.values());
    }

    @Override
    public Optional<SkeletonType> getSkeletonType(String id) {
        return Optional.fromNullable(this.skeletonTypes.get(id));
    }

    @Override
    public Collection<SkeletonType> getSkeletonTypes() {
        return ImmutableList.copyOf(this.skeletonTypes.values());
    }

    @Override
    public Optional<Career> getCareer(String id) {
        return Optional.fromNullable(this.careers.get(id));
    }

    @Override
    public Collection<Career> getCareers() {
        return ImmutableList.copyOf(this.careers.values());
    }

    @Override
    public Collection<Career> getCareers(Profession profession) {
        return getCareers().stream().filter(career -> career.getProfession() == profession).collect(Collectors.toList());
    }

    @Override
    public Optional<Profession> getProfession(String id) {
        return Optional.fromNullable(this.professions.get(id));
    }

    @Override
    public Collection<Profession> getProfessions() {
        return ImmutableList.copyOf(this.professions.values());
    }

    @Override
    public Collection<GameMode> getGameModes() {
        return ImmutableList.copyOf(this.gameModes.values());
    }

    @Override
    public Collection<PotionEffectType> getPotionEffects() {
        return ImmutableList.copyOf(this.potionEffectTypes.values());
    }

    @Override
    public Optional<Enchantment> getEnchantment(String id) {
        return Optional.fromNullable(this.enchantments.get(id));
    }

    @Override
    public Collection<Enchantment> getEnchantments() {
        return ImmutableList.copyOf(this.enchantments.values());
    }

    @Override
    public Collection<String> getDefaultGameRules() {
        return ImmutableList.copyOf(this.defaultGameRules.keySet());
    }

    @Override
    public Optional<DimensionType> getDimensionType(String name) {
        return Optional.fromNullable(this.dimensionTypes.get(name));
    }

    @Override
    public Collection<DimensionType> getDimensionTypes() {
        return ImmutableList.copyOf(this.dimensionTypes.values());
    }

    @Override
    public Optional<Rotation> getRotationFromDegree(int degrees) {
        for (Rotation rotation : getRotations()) {
            if (rotation.getAngle() == degrees) {
                return Optional.fromNullable(rotation);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<Rotation> getRotations() {
        return ImmutableList.copyOf(this.rotations.values());
    }

    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return (GameProfile) new com.mojang.authlib.GameProfile(uuid, name);
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Favicon loadFavicon(InputStream in) throws IOException {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Favicon loadFavicon(BufferedImage image) throws IOException {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<NotePitch> getNotePitch(String name) {
        return Optional.fromNullable(this.notePitches.get(name));
    }

    @Override
    public Collection<NotePitch> getNotePitches() {
        return ImmutableList.copyOf(this.notePitches.values());
    }

    @Override
    public Optional<SkullType> getSkullType(String name) {
        return Optional.fromNullable(this.skullTypes.get(name));
    }

    @Override
    public Collection<SkullType> getSkullTypes() {
        return ImmutableList.copyOf(this.skullTypes.values());
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShape(String name) {
        return Optional.fromNullable(this.bannerPatternShapes.get(name));
    }

    @Override
    public Optional<BannerPatternShape> getBannerPatternShapeById(String id) {
        for (BannerPatternShape bannerPatternShape : getBannerPatternShapes()) {
            if (bannerPatternShape.getId().equals(id)) {
                return Optional.fromNullable(bannerPatternShape);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<BannerPatternShape> getBannerPatternShapes() {
        return ImmutableList.copyOf(this.bannerPatternShapes.values());
    }

    @Override
    public GameDictionary getGameDictionary() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public RecipeRegistry getRecipeRegistry() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Collection<Difficulty> getDifficulties() {
        return ImmutableList.copyOf(this.difficulties.values());
    }

    @Override
    public Optional<Difficulty> getDifficulty(String name) {
        return Optional.fromNullable(this.difficulties.get(name));
    }

    @Override
    public Collection<EntityInteractionType> getEntityInteractionTypes() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<EntityInteractionType> getEntityInteractionType(String name) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<Attribute> getAttribute(String name) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Collection<Attribute> getAttributes() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<Operation> getOperation(String name) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Collection<Operation> getOperations() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public AttributeModifierBuilder getAttributeModifierBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public AttributeCalculator getAttributeCalculator() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public AttributeBuilder getAttributeBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<CoalType> getCoalType(String name) {
        return Optional.fromNullable(this.coalTypes.get(name));
    }

    @Override
    public Collection<CoalType> getCoalTypes() {
        return ImmutableList.copyOf(this.coalTypes.values());
    }

    @Override
    public Optional<Fish> getFishType(String name) {
        return Optional.fromNullable(this.fishes.get(name));
    }

    @Override
    public Collection<Fish> getFishTypes() {
        return ImmutableList.copyOf(this.fishes.values());
    }

    @Override
    public Optional<CookedFish> getCookedFishType(String name) {
        return Optional.fromNullable(this.cookedFishes.get(name));
    }

    @Override
    public Collection<CookedFish> getCookedFishTypes() {
        return ImmutableList.copyOf(this.cookedFishes.values());
    }

    @Override
    public Optional<GoldenApple> getGoldenAppleType(String name) {
        return Optional.fromNullable(this.goldenApples.get(name));
    }

    @Override
    public Collection<GoldenApple> getGoldenAppleTypes() {
        return ImmutableList.copyOf(this.goldenApples.values());
    }

    @Override
    public FireworkEffectBuilder getFireworkEffectBuilder() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<TextColor> getTextColor(String name) {
        return Optional.fromNullable(this.textColors.get(name));
    }

    @Override
    public Collection<TextColor> getTextColors() {
        return ImmutableList.copyOf(this.textColors.values());
    }

    @Override
    public Optional<TextStyle> getTextStyle(String name) {
        return Optional.fromNullable(this.textStyles.get(name));
    }

    @Override
    public Collection<TextStyle> getTextStyles() {
        return ImmutableList.copyOf(this.textStyles.values());
    }

    @Override
    public Optional<ChatType> getChatType(String name) {
        return Optional.fromNullable(this.chatTypes.get(name));
    }

    @Override
    public Collection<ChatType> getChatTypes() {
        return ImmutableList.copyOf(this.chatTypes.values());
    }

    @Override
    public Optional<SelectorType> getSelectorType(String name) {
        return Optional.fromNullable(this.selectorTypes.get(name));
    }

    @Override
    public Collection<SelectorType> getSelectorTypes() {
        return ImmutableList.copyOf(this.selectorTypes.values());
    }

    @Override
    public Optional<ArgumentType<?>> getArgumentType(String name) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Collection<ArgumentType<?>> getArgumentTypes() {
        throw new NotImplementedException("TODO");
    }

    @Override
    public Optional<Locale> getLocale(String name) {
        return Optional.fromNullable(this.locales.get(name));
    }

    @Override
    public Optional<Locale> getLocaleById(String id) {
        for (Locale locale : getLocales()) {
            String localeString = locale.getLanguage() + "_" + locale.getCountry();
            if (localeString.equals(id)) {
                return Optional.fromNullable(locale);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<Locale> getLocales() {
        return ImmutableList.copyOf(this.locales.values());
    }

    @Override
    public Optional<Translation> getTranslationById(String id) {
        // TODO: this.translations.values() is temporary until getTranslations is added.
        for (Translation translation : this.translations.values()) {
            if (translation.getId().equals(id)) {
                return Optional.fromNullable(translation);
            }
        }
        return Optional.absent();
    }

    private void registerArts() {
        RegistryHelper.mapFields(Arts.class, new Function<String, Art>() {
            @Override
            public Art apply(String fieldName) {
                Art art = (Art) (Object) EntityPainting.EnumArt.valueOf(fieldName);
                GraniteGameRegistry.this.arts.put(art.getName(), art);
                return art;
            }
        });
    }

    private void registerBannerPatterns() {
        RegistryHelper.mapFields(BannerPatternShapes.class, new Function<String, BannerPatternShape>() {
            @Override
            public BannerPatternShape apply(String input) {
                BannerPatternShape bannerPattern = BannerPatternShape.class.cast(TileEntityBanner.EnumBannerPattern.valueOf(input));
                GraniteGameRegistry.this.bannerPatternShapes.put(bannerPattern.getName(), bannerPattern);
                return bannerPattern;
            }
        });
    }

    private void registerBiomes() {
        BiomeGenBase[] biomeArray = BiomeGenBase.getBiomeGenArray();
        this.biomeTypes.put("OCEAN", (BiomeType) BiomeGenBase.ocean);
        this.biomeTypes.put("PLAINS", (BiomeType) BiomeGenBase.plains);
        this.biomeTypes.put("DESERT", (BiomeType) BiomeGenBase.desert);
        this.biomeTypes.put("EXTREME_HILLS", (BiomeType) BiomeGenBase.extremeHills);
        this.biomeTypes.put("FOREST", (BiomeType) BiomeGenBase.forest);
        this.biomeTypes.put("TAIGA", (BiomeType) BiomeGenBase.taiga);
        this.biomeTypes.put("SWAMPLAND", (BiomeType) BiomeGenBase.swampland);
        this.biomeTypes.put("RIVER", (BiomeType) BiomeGenBase.river);
        this.biomeTypes.put("HELL", (BiomeType) BiomeGenBase.hell);
        this.biomeTypes.put("SKY", (BiomeType) BiomeGenBase.sky);
        this.biomeTypes.put("FROZEN_OCEAN", (BiomeType) BiomeGenBase.frozenOcean);
        this.biomeTypes.put("FROZEN_RIVER", (BiomeType) BiomeGenBase.frozenRiver);
        this.biomeTypes.put("ICE_PLAINS", (BiomeType) BiomeGenBase.icePlains);
        this.biomeTypes.put("ICE_MOUNTAINS", (BiomeType) BiomeGenBase.iceMountains);
        this.biomeTypes.put("MUSHROOM_ISLAND", (BiomeType) BiomeGenBase.mushroomIsland);
        this.biomeTypes.put("MUSHROOM_ISLAND_SHORE", (BiomeType) BiomeGenBase.mushroomIslandShore);
        this.biomeTypes.put("BEACH", (BiomeType) BiomeGenBase.beach);
        this.biomeTypes.put("DESERT_HILLS", (BiomeType) BiomeGenBase.desertHills);
        this.biomeTypes.put("FOREST_HILLS", (BiomeType) BiomeGenBase.forestHills);
        this.biomeTypes.put("TAIGA_HILLS", (BiomeType) BiomeGenBase.taigaHills);
        this.biomeTypes.put("EXTREME_HILLS_EDGE", (BiomeType) BiomeGenBase.extremeHillsEdge);
        this.biomeTypes.put("JUNGLE", (BiomeType) BiomeGenBase.jungle);
        this.biomeTypes.put("JUNGLE_HILLS", (BiomeType) BiomeGenBase.jungleHills);
        this.biomeTypes.put("JUNGLE_EDGE", (BiomeType) BiomeGenBase.jungleEdge);
        this.biomeTypes.put("DEEP_OCEAN", (BiomeType) BiomeGenBase.deepOcean);
        this.biomeTypes.put("STONE_BEACH", (BiomeType) BiomeGenBase.stoneBeach);
        this.biomeTypes.put("COLD_BEACH", (BiomeType) BiomeGenBase.coldBeach);
        this.biomeTypes.put("BIRCH_FOREST", (BiomeType) BiomeGenBase.birchForest);
        this.biomeTypes.put("BIRCH_FOREST_HILLS", (BiomeType) BiomeGenBase.birchForestHills);
        this.biomeTypes.put("ROOFED_FOREST", (BiomeType) BiomeGenBase.roofedForest);
        this.biomeTypes.put("COLD_TAIGA", (BiomeType) BiomeGenBase.coldTaiga);
        this.biomeTypes.put("COLD_TAIGA_HILLS", (BiomeType) BiomeGenBase.coldTaigaHills);
        this.biomeTypes.put("MEGA_TAIGA", (BiomeType) BiomeGenBase.megaTaiga);
        this.biomeTypes.put("MEGA_TAIGA_HILLS", (BiomeType) BiomeGenBase.megaTaigaHills);
        this.biomeTypes.put("EXTREME_HILLS_PLUS", (BiomeType) BiomeGenBase.extremeHillsPlus);
        this.biomeTypes.put("SAVANNA", (BiomeType) BiomeGenBase.savanna);
        this.biomeTypes.put("SAVANNA_PLATEAU", (BiomeType) BiomeGenBase.savannaPlateau);
        this.biomeTypes.put("MESA", (BiomeType) BiomeGenBase.mesa);
        this.biomeTypes.put("MESA_PLATEAU_FOREST", (BiomeType) BiomeGenBase.mesaPlateau_F);
        this.biomeTypes.put("MESA_PLATEAU", (BiomeType) BiomeGenBase.mesaPlateau);
        this.biomeTypes.put("SUNFLOWER_PLAINS", (BiomeType) biomeArray[BiomeGenBase.plains.biomeID + 128]);
        this.biomeTypes.put("DESERT_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.desert.biomeID + 128]);
        this.biomeTypes.put("FLOWER_FOREST", (BiomeType) biomeArray[BiomeGenBase.forest.biomeID + 128]);
        this.biomeTypes.put("TAIGA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.taiga.biomeID + 128]);
        this.biomeTypes.put("SWAMPLAND_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.swampland.biomeID + 128]);
        this.biomeTypes.put("ICE_PLAINS_SPIKES", (BiomeType) biomeArray[BiomeGenBase.icePlains.biomeID + 128]);
        this.biomeTypes.put("JUNGLE_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.jungle.biomeID + 128]);
        this.biomeTypes.put("JUNGLE_EDGE_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.jungleEdge.biomeID + 128]);
        this.biomeTypes.put("COLD_TAIGA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.coldTaiga.biomeID + 128]);
        this.biomeTypes.put("SAVANNA_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.savanna.biomeID + 128]);
        this.biomeTypes.put("SAVANNA_PLATEAU_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.savannaPlateau.biomeID + 128]);
        this.biomeTypes.put("MESA_BRYCE", (BiomeType) biomeArray[BiomeGenBase.mesa.biomeID + 128]);
        this.biomeTypes.put("MESA_PLATEAU_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau_F.biomeID + 128]);
        this.biomeTypes.put("MESA_PLATEAU_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.mesaPlateau.biomeID + 128]);
        this.biomeTypes.put("BIRCH_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.birchForest.biomeID + 128]);
        this.biomeTypes.put("BIRCH_FOREST_HILLS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.birchForestHills.biomeID + 128]);
        this.biomeTypes.put("ROOFED_FOREST_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.roofedForest.biomeID + 128]);
        this.biomeTypes.put("MEGA_SPRUCE_TAIGA", (BiomeType) biomeArray[BiomeGenBase.megaTaiga.biomeID + 128]);
        this.biomeTypes.put("EXTREME_HILLS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.extremeHills.biomeID + 128]);
        this.biomeTypes.put("EXTREME_HILLS_PLUS_MOUNTAINS", (BiomeType) biomeArray[BiomeGenBase.extremeHillsPlus.biomeID + 128]);
        this.biomeTypes.put("MEGA_SPRUCE_TAIGA_HILLS", (BiomeType) biomeArray[BiomeGenBase.megaTaigaHills.biomeID + 128]);
        RegistryHelper.mapFields(BiomeTypes.class, this.biomeTypes);
    }

    private void registerBlocks() {
        while (Block.blockRegistry.iterator().hasNext()) {
            Block block = (Block) Block.blockRegistry.iterator().next();
            this.blockTypes.put(block.getLocalizedName(), (BlockType) block);
        }
        RegistryHelper.mapFields(ItemTypes.class, this.itemTypes);
    }

    private void registerCareersAndProfessions() {
        this.professions.put("FARMER", new GraniteProfession("farmer"));
        this.professions.put("LIBRARIAN", new GraniteProfession("librarian"));
        this.professions.put("PRIEST", new GraniteProfession("priest"));
        this.professions.put("BLACKSMITH", new GraniteProfession("blacksmith"));
        this.professions.put("BUTCHER", new GraniteProfession("butcher"));
        RegistryHelper.mapFields(Professions.class, this.professions);

        this.careers.put("FARMER", new GraniteCareer("farmer", this.professions.get("FARMER")));
        this.careers.put("FISHERMAN", new GraniteCareer("fisherman", this.professions.get("FARMER")));
        this.careers.put("SHEPHERD", new GraniteCareer("shepherd", this.professions.get("FARMER")));
        this.careers.put("FLETCHER", new GraniteCareer("fletcher", this.professions.get("FARMER")));
        this.careers.put("LIBRARIAN", new GraniteCareer("librarian", this.professions.get("LIBRARIAN")));
        this.careers.put("CLERIC", new GraniteCareer("cleric", this.professions.get("PRIEST")));
        this.careers.put("ARMORER", new GraniteCareer("armor", this.professions.get("BLACKSMITH")));
        this.careers.put("WEAPON_SMITH", new GraniteCareer("weapon", this.professions.get("BLACKSMITH")));
        this.careers.put("TOOL_SMITH", new GraniteCareer("tool", this.professions.get("BLACKSMITH")));
        this.careers.put("BUTCHER", new GraniteCareer("butcher", this.professions.get("BUTCHER")));
        this.careers.put("LEATHERWORKER", new GraniteCareer("leatherworker", this.professions.get("BUTCHER")));
        RegistryHelper.mapFields(Professions.class, this.careers);
    }

    private void registerCoalTypes() {
        this.coalTypes.put("COAL", new GraniteCoalType("COAL"));
        this.coalTypes.put("CHARCOAL", new GraniteCoalType("CHARCOAL"));
        RegistryHelper.mapFields(CoalTypes.class, this.coalTypes);
    }

    private void registerChatTypes() {
        this.chatTypes.put("CHAT", new GraniteChatType((byte) 0));
        this.chatTypes.put("SYSTEM", new GraniteChatType((byte) 1));
        this.chatTypes.put("ACTION_BAR", new GraniteChatType((byte) 2));
        RegistryHelper.mapFields(ChatTypes.class, this.chatTypes);
    }

    private void registerDefaultGameRules() {
        this.defaultGameRules.put("COMMAND_BLOCK_OUTPUT", "commandBlockOutput");
        this.defaultGameRules.put("DO_DAYLIGHT_CYCLE", "doDaylightCycle");
        this.defaultGameRules.put("DO_ENTITY_DROPS", "doEntityDrops");
        this.defaultGameRules.put("DO_FIRE_TICK", "doFireTick");
        this.defaultGameRules.put("DO_MOB_LOOT", "doMobLoot");
        this.defaultGameRules.put("DO_MOB_SPAWNING", "doMobSpawning");
        this.defaultGameRules.put("DO_TILE_DROPS", "doTileDrops");
        this.defaultGameRules.put("KEEP_INVENTORY", "keepInventory");
        this.defaultGameRules.put("LOG_ADMIN_COMMANDS", "logAdminCommands");
        this.defaultGameRules.put("MOB_GRIEFING", "mobGriefing");
        this.defaultGameRules.put("NATURAL_REGENERATION", "naturalRegeneration");
        this.defaultGameRules.put("RANDOM_TICK_SPEED", "randomTickSpeed");
        this.defaultGameRules.put("REDUCED_DEBUG_INFO", "reducedDebugInfo");
        this.defaultGameRules.put("SEND_COMMAND_FEEDBACK", "sendCommandFeedback");
        this.defaultGameRules.put("SHOW_DEATH_MESSAGES", "showDeathMessages");
        RegistryHelper.mapFields(DefaultGameRules.class, this.defaultGameRules);
    }

    private void registerDifficalties() {
        this.difficulties.put("PEACEFUL", (Difficulty) (Object) EnumDifficulty.PEACEFUL);
        this.difficulties.put("EASY", (Difficulty) (Object) EnumDifficulty.EASY);
        this.difficulties.put("NORMAL", (Difficulty) (Object) EnumDifficulty.NORMAL);
        this.difficulties.put("HARD", (Difficulty) (Object) EnumDifficulty.HARD);
        RegistryHelper.mapFields(Difficulties.class, this.difficulties);
    }

    private void registerDimensionTypes() {
        this.dimensionTypes.put("NETHER", new GraniteDimensionType("NETHER", true, WorldProviderHell.class));
        this.dimensionTypes.put("OVERWORLD", new GraniteDimensionType("OVERWORLD", true, WorldProviderSurface.class));
        this.dimensionTypes.put("END", new GraniteDimensionType("END", true, WorldProviderEnd.class));
        RegistryHelper.mapFields(DimensionTypes.class, this.dimensionTypes);
    }

    private void registerDirections() {
        this.directions.put(Direction.NORTH, EnumFacing.NORTH);
        this.directions.put(Direction.EAST, EnumFacing.EAST);
        this.directions.put(Direction.SOUTH, EnumFacing.SOUTH);
        this.directions.put(Direction.WEST, EnumFacing.WEST);
        this.directions.put(Direction.UP, EnumFacing.UP);
        this.directions.put(Direction.DOWN, EnumFacing.DOWN);
    }

    private void registerDyeColors() {
        RegistryHelper.mapFields(DyeColors.class, new Function<String, DyeColor>() {

            @Override
            public DyeColor apply(String input) {
                DyeColor dyeColor = DyeColor.class.cast(EnumDyeColor.valueOf(input));
                GraniteGameRegistry.this.dyeColors.put(dyeColor.getName(), dyeColor);
                return dyeColor;
            }

        });
    }

    private void registerEnchantments() {
        this.enchantments.put("PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.protection);
        this.enchantments.put("FIRE_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.fireProtection);
        this.enchantments.put("FEATHER_FALLING", (Enchantment) net.minecraft.enchantment.Enchantment.featherFalling);
        this.enchantments.put("BLAST_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.blastProtection);
        this.enchantments.put("PROJECTILE_PROTECTION", (Enchantment) net.minecraft.enchantment.Enchantment.projectileProtection);
        this.enchantments.put("RESPIRATION", (Enchantment) net.minecraft.enchantment.Enchantment.respiration);
        this.enchantments.put("AQUA_AFFINITY", (Enchantment) net.minecraft.enchantment.Enchantment.aquaAffinity);
        this.enchantments.put("THORNS", (Enchantment) net.minecraft.enchantment.Enchantment.thorns);
        this.enchantments.put("DEPTH_STRIDER", (Enchantment) net.minecraft.enchantment.Enchantment.depthStrider);
        this.enchantments.put("SHARPNESS", (Enchantment) net.minecraft.enchantment.Enchantment.sharpness);
        this.enchantments.put("SMITE", (Enchantment) net.minecraft.enchantment.Enchantment.smite);
        this.enchantments.put("BANE_OF_ARTHROPODS", (Enchantment) net.minecraft.enchantment.Enchantment.baneOfArthropods);
        this.enchantments.put("KNOCKBACK", (Enchantment) net.minecraft.enchantment.Enchantment.knockback);
        this.enchantments.put("FIRE_ASPECT", (Enchantment) net.minecraft.enchantment.Enchantment.fireAspect);
        this.enchantments.put("LOOTING", (Enchantment) net.minecraft.enchantment.Enchantment.looting);
        this.enchantments.put("EFFICIENCY", (Enchantment) net.minecraft.enchantment.Enchantment.efficiency);
        this.enchantments.put("SILK_TOUCH", (Enchantment) net.minecraft.enchantment.Enchantment.silkTouch);
        this.enchantments.put("UNBREAKING", (Enchantment) net.minecraft.enchantment.Enchantment.unbreaking);
        this.enchantments.put("FORTUNE", (Enchantment) net.minecraft.enchantment.Enchantment.fortune);
        this.enchantments.put("POWER", (Enchantment) net.minecraft.enchantment.Enchantment.power);
        this.enchantments.put("PUNCH", (Enchantment) net.minecraft.enchantment.Enchantment.punch);
        this.enchantments.put("FLAME", (Enchantment) net.minecraft.enchantment.Enchantment.flame);
        this.enchantments.put("INFINITY", (Enchantment) net.minecraft.enchantment.Enchantment.infinity);
        this.enchantments.put("LUCK_OF_THE_SEA", (Enchantment) net.minecraft.enchantment.Enchantment.luckOfTheSea);
        this.enchantments.put("LURE", (Enchantment) net.minecraft.enchantment.Enchantment.lure);
        RegistryHelper.mapFields(Enchantments.class, this.enchantments);
    }

    private void registerEntityInteractionTypes() {
        this.entityInteractionTypes.put("ATTACK", new GraniteEntityInteractionType("Attack"));
        this.entityInteractionTypes.put("PICK_BLOCK", new GraniteEntityInteractionType("Pick Block"));
        this.entityInteractionTypes.put("USE", new GraniteEntityInteractionType("Use"));
        RegistryHelper.mapFields(EntityInteractionTypes.class, this.entityInteractionTypes);
    }

    private void registerEntityTypes() {
        this.entityTypes.put("DROPPED_ITEM", new GraniteEntityType("Item"));
        this.entityTypes.put("EXPERIENCE_ORB", new GraniteEntityType("XPOrb"));
        this.entityTypes.put("LEASH_HITCH", new GraniteEntityType("LeashKnot"));
        this.entityTypes.put("PAINTING", new GraniteEntityType("Painting"));
        this.entityTypes.put("ARROW", new GraniteEntityType("Arrow"));
        this.entityTypes.put("SNOWBALL", new GraniteEntityType("Snowball"));
        this.entityTypes.put("FIREBALL", new GraniteEntityType("Fireball"));
        this.entityTypes.put("SMALL_FIREBALL", new GraniteEntityType("SmallFireball"));
        this.entityTypes.put("ENDER_PEARL", new GraniteEntityType("ThrownEnderpearl"));
        this.entityTypes.put("EYE_OF_ENDER", new GraniteEntityType("EyeOfEnderSignal"));
        this.entityTypes.put("SPLASH_POTION", new GraniteEntityType("ThrownPotion"));
        this.entityTypes.put("THROWN_EXP_BOTTLE", new GraniteEntityType("ThrownExpBottle"));
        this.entityTypes.put("ITEM_FRAME", new GraniteEntityType("ItemFrame"));
        this.entityTypes.put("WITHER_SKULL", new GraniteEntityType("WitherSkull"));
        this.entityTypes.put("PRIMED_TNT", new GraniteEntityType("PrimedTnt"));
        this.entityTypes.put("FALLING_BLOCK", new GraniteEntityType("FallingSand"));
        this.entityTypes.put("FIREWORK", new GraniteEntityType("FireworksRocketEntity"));
        this.entityTypes.put("ARMOR_STAND", new GraniteEntityType("ArmorStand"));
        this.entityTypes.put("BOAT", new GraniteEntityType("Boat"));
        this.entityTypes.put("RIDEABLE_MINECART", new GraniteEntityType("MinecartRideable"));
        this.entityTypes.put("CHESTED_MINECART", new GraniteEntityType("MinecartChest"));
        this.entityTypes.put("FURNACE_MINECART", new GraniteEntityType("MinecartFurnace"));
        this.entityTypes.put("TNT_MINECART", new GraniteEntityType("MinecartTNT"));
        this.entityTypes.put("HOPPER_MINECART", new GraniteEntityType("MinecartHopper"));
        this.entityTypes.put("MOB_SPAWNER_MINECART", new GraniteEntityType("MinecartSpawner"));
        this.entityTypes.put("COMMANDBLOCK_MINECART", new GraniteEntityType("MinecartCommandBlock"));
        this.entityTypes.put("CREEPER", new GraniteEntityType("Creeper"));
        this.entityTypes.put("SKELETON", new GraniteEntityType("Skeleton"));
        this.entityTypes.put("SPIDER", new GraniteEntityType("Spider"));
        this.entityTypes.put("GIANT", new GraniteEntityType("Giant"));
        this.entityTypes.put("ZOMBIE", new GraniteEntityType("Zombie"));
        this.entityTypes.put("SLIME", new GraniteEntityType("Slime"));
        this.entityTypes.put("GHAST", new GraniteEntityType("Ghast"));
        this.entityTypes.put("PIG_ZOMBIE", new GraniteEntityType("PigZombie"));
        this.entityTypes.put("ENDERMAN", new GraniteEntityType("Enderman"));
        this.entityTypes.put("CAVE_SPIDER", new GraniteEntityType("CaveSpider"));
        this.entityTypes.put("SILVERFISH", new GraniteEntityType("Silverfish"));
        this.entityTypes.put("BLAZE", new GraniteEntityType("Blaze"));
        this.entityTypes.put("MAGMA_CUBE", new GraniteEntityType("LavaSlime"));
        this.entityTypes.put("ENDER_DRAGON", new GraniteEntityType("EnderDragon"));
        this.entityTypes.put("WITHER", new GraniteEntityType("WitherBoss"));
        this.entityTypes.put("BAT", new GraniteEntityType("Bat"));
        this.entityTypes.put("WITCH", new GraniteEntityType("Witch"));
        this.entityTypes.put("ENDERMITE", new GraniteEntityType("Endermite"));
        this.entityTypes.put("GUARDIAN", new GraniteEntityType("Guardian"));
        this.entityTypes.put("PIG", new GraniteEntityType("Pig"));
        this.entityTypes.put("SHEEP", new GraniteEntityType("Sheep"));
        this.entityTypes.put("COW", new GraniteEntityType("Cow"));
        this.entityTypes.put("CHICKEN", new GraniteEntityType("Chicken"));
        this.entityTypes.put("SQUID", new GraniteEntityType("Squid"));
        this.entityTypes.put("WOLF", new GraniteEntityType("Wolf"));
        this.entityTypes.put("MUSHROOM_COW", new GraniteEntityType("MushroomCow"));
        this.entityTypes.put("SNOWMAN", new GraniteEntityType("SnowMan"));
        this.entityTypes.put("OCELOT", new GraniteEntityType("Ozelot"));
        this.entityTypes.put("IRON_GOLEM", new GraniteEntityType("VillagerGolem"));
        this.entityTypes.put("HORSE", new GraniteEntityType("EntityHorse"));
        this.entityTypes.put("RABBIT", new GraniteEntityType("Rabbit"));
        this.entityTypes.put("VILLAGER", new GraniteEntityType("Villager"));
        this.entityTypes.put("ENDER_CRYSTAL", new GraniteEntityType("EnderCrystal"));
        this.entityTypes.put("EGG", new GraniteEntityType("Egg", EntityEgg.class));
        this.entityTypes.put("FISHING_HOOK", new GraniteEntityType("FishingHook", EntityFishHook.class));
        this.entityTypes.put("LIGHTNING", new GraniteEntityType("Lightning", EntityLightningBolt.class));
        this.entityTypes.put("WEATHER", new GraniteEntityType("Weather", EntityWeatherEffect.class));
        this.entityTypes.put("PLAYER", new GraniteEntityType("Player", EntityPlayerMP.class));
        this.entityTypes.put("COMPLEX_PART", new GraniteEntityType("ComplexPart", EntityDragonPart.class));
        RegistryHelper.mapFields(EntityTypes.class, this.entityTypes);
    }

    private void registerFactories() {
        //RegistryHelper.setFactory(Selectors.class, new SpongeSelectorFactory());
        //RegistryHelper.setFactory(SelectorTypes.class, new SpongeSelectorTypeFactory());
        RegistryHelper.setFactory(Texts.class, new GraniteTextFactory());
        //RegistryHelper.setFactory(TextActions.class, new SpongeTextActionFactory());
        //RegistryHelper.setFactory(Titles.class, new SpongeTitleFactory());
    }

    private void registerFishes() {
        RegistryHelper.mapFields(Fishes.class, new Function<String, Fish>() {
            @Override
            public Fish apply(String input) {
                Fish fish = Fish.class.cast(ItemFishFood.FishType.valueOf(input));
                if (fish != null) {
                    GraniteGameRegistry.this.fishes.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });

        RegistryHelper.mapFields(CookedFishes.class, new Function<String, CookedFish>() {
            @Override
            public CookedFish apply(String input) {
                CookedFish fish = CookedFish.class.cast(ItemFishFood.FishType.valueOf(input));
                if (fish != null) {
                    GraniteGameRegistry.this.cookedFishes.put(fish.getId(), fish);
                    return fish;
                } else {
                    return null;
                }
            }
        });
    }

    private void registerGameModes() {
        this.gameModes.put("SURVIVAL", new GraniteGameMode("Survival"));
        this.gameModes.put("CREATIVE", new GraniteGameMode("Creative"));
        this.gameModes.put("ADVENTURE", new GraniteGameMode("Adventure"));
        this.gameModes.put("SPECTATOR", new GraniteGameMode("Spectator"));
        RegistryHelper.mapFields(GameModes.class, this.gameModes);
    }

    private void registerGoldenApples() {
        this.goldenApples.put("GOLDEN_APPLE", new GraniteGoldenApple("Golden Apple"));
        this.goldenApples.put("ENCHANTED_GOLDEN_APPLE", new GraniteGoldenApple("Enchanted Golden Apple"));
        RegistryHelper.mapFields(GoldenApples.class, this.goldenApples);
    }

    private void registerHorseColors() {
        this.horseColors.put("CREAMY", new GraniteHorseColor("CREAMY"));
        this.horseColors.put("CHESTNUT", new GraniteHorseColor("CHESTNUT"));
        this.horseColors.put("BROWN", new GraniteHorseColor("BROWN"));
        this.horseColors.put("BLACK", new GraniteHorseColor("BLACK"));
        this.horseColors.put("GRAY", new GraniteHorseColor("GRAY"));
        this.horseColors.put("DARK_BROWN", new GraniteHorseColor("DARK_BROWN"));
        RegistryHelper.mapFields(HorseColors.class, this.horseColors);
    }

    private void registerHorseStyles() {
        this.horseStyles.put("NONE", new GraniteHorseStyle("NONE"));
        this.horseStyles.put("WHITE", new GraniteHorseStyle("WHITE"));
        this.horseStyles.put("WHITEFIELD", new GraniteHorseStyle("WHITEFIELD"));
        this.horseStyles.put("WHITE_DOTS", new GraniteHorseStyle("WHITE_DOTS"));
        this.horseStyles.put("BLACK_DOTS", new GraniteHorseStyle("BLACK_DOTS"));
        RegistryHelper.mapFields(HorseStyles.class, this.horseStyles);
    }

    private void registerHorseVariants() {
        this.horseVariants.put("HORSE", new GraniteHorseVariant("HORSE"));
        this.horseVariants.put("DONKEY", new GraniteHorseVariant("DONKEY"));
        this.horseVariants.put("MULE", new GraniteHorseVariant("MULE"));
        this.horseVariants.put("UNDEAD_HORSE", new GraniteHorseVariant("UNDEAD_HORSE"));
        this.horseVariants.put("SKELETON_HORSE", new GraniteHorseVariant("SKELETON_HORSE"));
        RegistryHelper.mapFields(HorseVariants.class, this.horseVariants);
    }

    private void registerItems() {
        while (Item.itemRegistry.iterator().hasNext()) {
            Item item = (Item) Item.itemRegistry.iterator().next();
            this.itemTypes.put(item.getItemStackDisplayName(new ItemStack(item)), (ItemType) item);
        }
        RegistryHelper.mapFields(ItemTypes.class, this.itemTypes);
    }

    private void registerLocales() {
        this.locales.put("AFRIKAANS", new Locale("af", "ZA"));
        this.locales.put("ARABIC", new Locale("ar", "SA"));
        this.locales.put("ASTURIAN", new Locale("ast", "ES"));
        this.locales.put("AZERBAIJANI", new Locale("az", "AZ"));
        this.locales.put("BULGARIAN", new Locale("bg", "BG"));
        this.locales.put("CATALAN", new Locale("ca", "ES"));
        this.locales.put("CZECH", new Locale("cs", "CZ"));
        this.locales.put("WELSH", new Locale("cy", "GB"));
        this.locales.put("DANISH", new Locale("da", "DK"));
        this.locales.put("GERMAN", new Locale("de", "DE"));
        this.locales.put("GREEK", new Locale("el", "GR"));
        this.locales.put("AUSTRALIAN_ENGLISH", new Locale("en", "AU"));
        this.locales.put("CANADIAN_ENGLISH", new Locale("en", "CA"));
        this.locales.put("BRITISH_ENGLISH", new Locale("en", "GB"));
        this.locales.put("PIRATE_ENGLISH", new Locale("en", "PT"));
        this.locales.put("ENGLISH", new Locale("en", "US"));
        this.locales.put("ESPERANTO", new Locale("eo", "UY"));
        this.locales.put("ARGENTINIAN_SPANISH", new Locale("es", "AR"));
        this.locales.put("SPANISH", new Locale("es", "ES"));
        this.locales.put("MEXICAN_SPANISH", new Locale("es", "MX"));
        this.locales.put("URUGUAYAN_SPANISH", new Locale("es", "UY"));
        this.locales.put("VENEZUELAN_SPANISH", new Locale("es", "VE"));
        this.locales.put("ESTONIAN", new Locale("et", "EE"));
        this.locales.put("BASQUE", new Locale("eu", "ES"));
        this.locales.put("PERSIAN", new Locale("fa", "IR"));
        this.locales.put("FINNISH", new Locale("fi", "FI"));
        this.locales.put("FILIPINO", new Locale("fil", "PH"));
        this.locales.put("CANADIAN_FRENCH", new Locale("fr", "CA"));
        this.locales.put("FRENCH", new Locale("fr", "FR"));
        this.locales.put("IRISH", new Locale("ga", "IE"));
        this.locales.put("GALICIAN", new Locale("gl", "ES"));
        this.locales.put("MANX", new Locale("gv", "IM"));
        this.locales.put("HEBREW", new Locale("he", "IL"));
        this.locales.put("HINDI", new Locale("hi", "IN"));
        this.locales.put("CROATIAN", new Locale("hr", "HR"));
        this.locales.put("HUNGARIAN", new Locale("hu", "HU"));
        this.locales.put("ARMENIAN", new Locale("hy", "AM"));
        this.locales.put("INDONESIAN", new Locale("id", "ID"));
        this.locales.put("ICELANDIC", new Locale("is", "IS"));
        this.locales.put("ITALIAN", new Locale("it", "IT"));
        this.locales.put("JAPANESE", new Locale("ja", "JP"));
        this.locales.put("GEORGIAN", new Locale("ka", "GE"));
        this.locales.put("KOREAN", new Locale("ko", "KR"));
        this.locales.put("CORNISH", new Locale("kw", "GB"));
        this.locales.put("LATIN", new Locale("la", "LA"));
        this.locales.put("LUXEMBOURGISH", new Locale("lb", "LU"));
        this.locales.put("LITHUANIAN", new Locale("lt", "LT"));
        this.locales.put("LATVIAN", new Locale("lv", "LV"));
        this.locales.put("MAORI", new Locale("mi", "NZ"));
        this.locales.put("MALAY", new Locale("ms", "MY"));
        this.locales.put("MALTESE", new Locale("mt", "MT"));
        this.locales.put("LOW_GERMAN", new Locale("nds", "DE"));
        this.locales.put("DUTCH", new Locale("nl", "NL"));
        this.locales.put("NORWEGIAN_NYNORSK", new Locale("nn", "NO"));
        this.locales.put("NORWEGIAN", new Locale("no", "NO"));
        this.locales.put("OCCITAN", new Locale("oc", "FR"));
        this.locales.put("POLISH", new Locale("pl", "PL"));
        this.locales.put("BRAZILIAN_PORTUGUESE", new Locale("pt", "BR"));
        this.locales.put("PORTUGUESE", new Locale("pt", "PT"));
        this.locales.put("QUENYA", new Locale("qya", "AA"));
        this.locales.put("ROMANIAN", new Locale("ro", "RO"));
        this.locales.put("RUSSIAN", new Locale("ru", "RU"));
        this.locales.put("NORTHERN_SAMI", new Locale("se", "NO"));
        this.locales.put("SLOVAK", new Locale("sk", "SK"));
        this.locales.put("SLOVENE", new Locale("sl", "SI"));
        this.locales.put("SERBIAN", new Locale("sr", "SP"));
        this.locales.put("SWEDISH", new Locale("sv", "SE"));
        this.locales.put("THAI", new Locale("th", "TH"));
        this.locales.put("KLINGON", new Locale("tlh", "AA"));
        this.locales.put("TURKISH", new Locale("tr", "TR"));
        this.locales.put("UKRAINIAN", new Locale("uk", "UA"));
        this.locales.put("VALENCIAN", new Locale("val", "ES"));
        this.locales.put("VIETNAMESE", new Locale("vi", "VN"));
        this.locales.put("SIMPLIFIED_CHINESE", new Locale("zh", "CN"));
        this.locales.put("TRADITIONAL_CHINESE", new Locale("zh", "TW"));
        RegistryHelper.mapFields(Locales.class, this.locales);
    }

    private void registerOcelotTypes() {
        this.ocelotTypes.put("BROWN", new GraniteOcelotType("WILD_OCELOT"));
        this.ocelotTypes.put("BLACK", new GraniteOcelotType("BLACK_CAT"));
        this.ocelotTypes.put("GRAY", new GraniteOcelotType("RED_CAT"));
        this.ocelotTypes.put("DARK_BROWN", new GraniteOcelotType("SIAMESE_CAT"));
        RegistryHelper.mapFields(OcelotTypes.class, this.ocelotTypes);
    }

    private void registerParticleTypes() {
        this.particleTypes.put("EXPLOSION_NORMAL", new GraniteParticleType(EnumParticleTypes.EXPLOSION_NORMAL, true));
        this.particleTypes.put("EXPLOSION_LARGE", new GraniteParticleType.Resizable(EnumParticleTypes.EXPLOSION_LARGE, 1f));
        this.particleTypes.put("EXPLOSION_HUGE", new GraniteParticleType(EnumParticleTypes.EXPLOSION_HUGE, false));
        this.particleTypes.put("FIREWORKS_SPARK", new GraniteParticleType(EnumParticleTypes.FIREWORKS_SPARK, true));
        this.particleTypes.put("WATER_BUBBLE", new GraniteParticleType(EnumParticleTypes.WATER_BUBBLE, true));
        this.particleTypes.put("WATER_SPLASH", new GraniteParticleType(EnumParticleTypes.WATER_SPLASH, true));
        this.particleTypes.put("WATER_WAKE", new GraniteParticleType(EnumParticleTypes.WATER_WAKE, true));
        this.particleTypes.put("SUSPENDED", new GraniteParticleType(EnumParticleTypes.SUSPENDED, false));
        this.particleTypes.put("SUSPENDED_DEPTH", new GraniteParticleType(EnumParticleTypes.SUSPENDED_DEPTH, false));
        this.particleTypes.put("CRIT", new GraniteParticleType(EnumParticleTypes.CRIT, true));
        this.particleTypes.put("CRIT_MAGIC", new GraniteParticleType(EnumParticleTypes.CRIT_MAGIC, true));
        this.particleTypes.put("SMOKE_NORMAL", new GraniteParticleType(EnumParticleTypes.SMOKE_NORMAL, true));
        this.particleTypes.put("SMOKE_LARGE", new GraniteParticleType(EnumParticleTypes.SMOKE_LARGE, true));
        this.particleTypes.put("SPELL", new GraniteParticleType(EnumParticleTypes.SPELL, false));
        this.particleTypes.put("SPELL_INSTANT", new GraniteParticleType(EnumParticleTypes.SPELL_INSTANT, false));
        this.particleTypes.put("SPELL_MOB", new GraniteParticleType.Colorable(EnumParticleTypes.SPELL_MOB, Color.BLACK));
        this.particleTypes.put("SPELL_MOB_AMBIENT", new GraniteParticleType.Colorable(EnumParticleTypes.SPELL_MOB_AMBIENT, Color.BLACK));
        this.particleTypes.put("SPELL_WITCH", new GraniteParticleType(EnumParticleTypes.SPELL_WITCH, false));
        this.particleTypes.put("DRIP_WATER", new GraniteParticleType(EnumParticleTypes.DRIP_WATER, false));
        this.particleTypes.put("DRIP_LAVA", new GraniteParticleType(EnumParticleTypes.DRIP_LAVA, false));
        this.particleTypes.put("VILLAGER_ANGRY", new GraniteParticleType(EnumParticleTypes.VILLAGER_ANGRY, false));
        this.particleTypes.put("VILLAGER_HAPPY", new GraniteParticleType(EnumParticleTypes.VILLAGER_HAPPY, true));
        this.particleTypes.put("TOWN_AURA", new GraniteParticleType(EnumParticleTypes.TOWN_AURA, true));
        this.particleTypes.put("NOTE", new GraniteParticleType.Note(EnumParticleTypes.NOTE, 0f));
        this.particleTypes.put("PORTAL", new GraniteParticleType(EnumParticleTypes.PORTAL, true));
        this.particleTypes.put("ENCHANTMENT_TABLE", new GraniteParticleType(EnumParticleTypes.ENCHANTMENT_TABLE, true));
        this.particleTypes.put("FLAME", new GraniteParticleType(EnumParticleTypes.FLAME, true));
        this.particleTypes.put("LAVA", new GraniteParticleType(EnumParticleTypes.LAVA, false));
        this.particleTypes.put("FOOTSTEP", new GraniteParticleType(EnumParticleTypes.FOOTSTEP, false));
        this.particleTypes.put("CLOUD", new GraniteParticleType(EnumParticleTypes.CLOUD, true));
        this.particleTypes.put("REDSTONE", new GraniteParticleType.Colorable(EnumParticleTypes.REDSTONE, Color.RED));
        this.particleTypes.put("SNOWBALL", new GraniteParticleType(EnumParticleTypes.SNOWBALL, false));
        this.particleTypes.put("SNOW_SHOVEL", new GraniteParticleType(EnumParticleTypes.SNOW_SHOVEL, true));
        this.particleTypes.put("SLIME", new GraniteParticleType(EnumParticleTypes.SLIME, false));
        this.particleTypes.put("HEART", new GraniteParticleType(EnumParticleTypes.HEART, false));
        this.particleTypes.put("BARRIER", new GraniteParticleType(EnumParticleTypes.BARRIER, false));
        this.particleTypes.put("ITEM_CRACK",
                new GraniteParticleType.Material(EnumParticleTypes.ITEM_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.particleTypes.put("BLOCK_CRACK",
                new GraniteParticleType.Material(EnumParticleTypes.BLOCK_CRACK, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.particleTypes.put("BLOCK_DUST",
                new GraniteParticleType.Material(EnumParticleTypes.BLOCK_DUST, new net.minecraft.item.ItemStack(Blocks.air), true));
        this.particleTypes.put("WATER_DROP", new GraniteParticleType(EnumParticleTypes.WATER_DROP, false));
        this.particleTypes.put("ITEM_TAKE", new GraniteParticleType(EnumParticleTypes.ITEM_TAKE, false));
        this.particleTypes.put("MOB_APPEARANCE", new GraniteParticleType(EnumParticleTypes.MOB_APPEARANCE, false));
        RegistryHelper.mapFields(ParticleTypes.class, this.particleTypes);
    }

    private void registerPotionTypes() {
        this.potionEffectTypes.put("SPEED", new GranitePotionEffectType(Potion.moveSpeed));
        this.potionEffectTypes.put("SLOWNESS", new GranitePotionEffectType(Potion.moveSlowdown));
        this.potionEffectTypes.put("HASTE", new GranitePotionEffectType(Potion.digSpeed));
        this.potionEffectTypes.put("MINING_FATIGUE", new GranitePotionEffectType(Potion.digSlowdown));
        this.potionEffectTypes.put("STRENGTH", new GranitePotionEffectType(Potion.damageBoost));
        this.potionEffectTypes.put("INSTANT_HEALTH", new GranitePotionEffectType(Potion.heal));
        this.potionEffectTypes.put("INSTANT_DAMAGE", new GranitePotionEffectType(Potion.harm));
        this.potionEffectTypes.put("JUMP_BOOST", new GranitePotionEffectType(Potion.jump));
        this.potionEffectTypes.put("NAUSEA", new GranitePotionEffectType(Potion.confusion));
        this.potionEffectTypes.put("REGENERATION", new GranitePotionEffectType(Potion.regeneration));
        this.potionEffectTypes.put("RESISTANCE", new GranitePotionEffectType(Potion.resistance));
        this.potionEffectTypes.put("FIRE_RESISTANCE", new GranitePotionEffectType(Potion.fireResistance));
        this.potionEffectTypes.put("WATER_BREATHING", new GranitePotionEffectType(Potion.waterBreathing));
        this.potionEffectTypes.put("INVISIBILITY", new GranitePotionEffectType(Potion.invisibility));
        this.potionEffectTypes.put("BLINDNESS", new GranitePotionEffectType(Potion.blindness));
        this.potionEffectTypes.put("NIGHT_VISION", new GranitePotionEffectType(Potion.nightVision));
        this.potionEffectTypes.put("HUNGER", new GranitePotionEffectType(Potion.hunger));
        this.potionEffectTypes.put("WEAKNESS", new GranitePotionEffectType(Potion.weakness));
        this.potionEffectTypes.put("POISON", new GranitePotionEffectType(Potion.poison));
        this.potionEffectTypes.put("WITHER", new GranitePotionEffectType(Potion.wither));
        this.potionEffectTypes.put("HEALTH_BOOST", new GranitePotionEffectType(Potion.healthBoost));
        this.potionEffectTypes.put("ABSORPTION", new GranitePotionEffectType(Potion.absorption));
        this.potionEffectTypes.put("SATURATION", new GranitePotionEffectType(Potion.saturation));
        RegistryHelper.mapFields(PotionEffectTypes.class, this.potionEffectTypes);
    }

    private void registerRabbitTypes() {
        this.rabbitTypes.put("BROWN_RABBIT", new GraniteRabbitType("BROWN"));
        this.rabbitTypes.put("WHITE_RABBIT", new GraniteRabbitType("WHITE"));
        this.rabbitTypes.put("BLACK_RABBIT", new GraniteRabbitType("BLACK"));
        this.rabbitTypes.put("BLACK_AND_WHITE_RABBIT", new GraniteRabbitType("BLACK_AND_WHITE"));
        this.rabbitTypes.put("GOLD_RABBIT", new GraniteRabbitType("GOLD"));
        this.rabbitTypes.put("SALT_AND_PEPPER_RABBIT", new GraniteRabbitType("SALT_AND_PEPPER"));
        this.rabbitTypes.put("KILLER_RABBIT", new GraniteRabbitType("KILLER"));
        RegistryHelper.mapFields(RabbitTypes.class, this.rabbitTypes);
    }

    private void registerRotations() {
        this.rotations.put("TOP", new GraniteRotation(0));
        this.rotations.put("TOP_RIGHT", new GraniteRotation(45));
        this.rotations.put("RIGHT", new GraniteRotation(90));
        this.rotations.put("BOTTOM_RIGHT", new GraniteRotation(135));
        this.rotations.put("BOTTOM", new GraniteRotation(180));
        this.rotations.put("BOTTOM_LEFT", new GraniteRotation(225));
        this.rotations.put("LEFT", new GraniteRotation(270));
        this.rotations.put("TOP_LEFT", new GraniteRotation(315));
        RegistryHelper.mapFields(Rotations.class, this.rotations);
    }

    private void registerSelectorTypes() {
        this.selectorTypes.put("ALL_PLAYERS", new GraniteSelectorType("a"));
        this.selectorTypes.put("ALL_ENTITIES", new GraniteSelectorType("e"));
        this.selectorTypes.put("NEAREST_PLAYER", new GraniteSelectorType("p"));
        this.selectorTypes.put("RANDOM_PLAYER", new GraniteSelectorType("r"));
        RegistryHelper.mapFields(SelectorTypes.class, this.selectorTypes);
    }

    /*private void registerServiceBuilders() {
        Game game = Granite.instance.getGame();
        SerializationService service = game.getServiceManager().provide(SerializationService.class).get();
        service.registerBuilder(Banner.class, new GraniteBannerBuilder(game));
        service.registerBuilder(Banner.PatternLayer.class, new GranitePatternLayerBuilder(game));
        service.registerBuilder(BrewingStand.class, new GraniteBrewingStandBuilder(game));
        service.registerBuilder(Chest.class, new GraniteChestBuilder(game));
        service.registerBuilder(CommandBlock.class, new GraniteCommandBlockBuilder(game));
        service.registerBuilder(Comparator.class, new GraniteComparatorBuilder(game));
        service.registerBuilder(DaylightDetector.class, new GraniteDaylightBuilder(game));
        service.registerBuilder(Dispenser.class, new GraniteDispenserBuilder(game));
        service.registerBuilder(Dropper.class, new GraniteDropperBuilder(game));
        service.registerBuilder(EnchantmentTable.class, new GraniteEnchantmentTableBuilder(game));
        service.registerBuilder(EnderChest.class, new GraniteEnderChestBuilder(game));
        service.registerBuilder(EndPortal.class, new GraniteEndPortalBuilder(game));
        service.registerBuilder(Furnace.class, new GraniteFurnaceBuilder(game));
        service.registerBuilder(Hopper.class, new GraniteHopperBuilder(game));
        service.registerBuilder(MobSpawner.class, new GraniteMobSpawnerBuilder(game));
        service.registerBuilder(ParticleEffect.Note.class, new GraniteNoteBuilder(game));
        service.registerBuilder(Sign.class, new GraniteSignBuilder(game));
        service.registerBuilder(Skull.class, new GraniteSkullBuilder(game));

        // Meta
        service.registerBuilder(DyeColor.class, new GraniteDyeBuilder());
        service.registerBuilder(HorseColor.class, new GraniteHorseColorBuilder());
        service.registerBuilder(HorseStyle.class, new GraniteHorseStyleBuilder());
        service.registerBuilder(HorseVariant.class, new GraniteHorseVariantBuilder());
        service.registerBuilder(OcelotType.class, new GraniteOcelotTypeBuilder());
        service.registerBuilder(PotionEffect.class, new GranitePotionEffectBuilder());
        service.registerBuilder(FireworkEffect.class, new GraniteFireworkDataBuilder());

        // User
    }*/

    private void registerSkeletonTypes() {
        this.skeletonTypes.put("NORMAL_SKELETON", new GraniteSkeletonType("NORMAL"));
        this.skeletonTypes.put("WITHER_SKELETON", new GraniteSkeletonType("WITHER"));
        RegistryHelper.mapFields(SkeletonTypes.class, this.skeletonTypes);
    }

    private void registerSoundTypes() {
        this.soundTypes.put("AMBIENCE_CAVE", new GraniteSoundType("ambient.cave.cave"));
        this.soundTypes.put("AMBIENCE_RAIN", new GraniteSoundType("ambient.weather.rain"));
        this.soundTypes.put("AMBIENCE_THUNDER", new GraniteSoundType("ambient.weather.thunder"));
        this.soundTypes.put("ANVIL_BREAK", new GraniteSoundType("random.anvil_break"));
        this.soundTypes.put("ANVIL_LAND", new GraniteSoundType("random.anvil_land"));
        this.soundTypes.put("ANVIL_USE", new GraniteSoundType("random.anvil_use"));
        this.soundTypes.put("ARROW_HIT", new GraniteSoundType("random.bowhit"));
        this.soundTypes.put("BURP", new GraniteSoundType("random.burp"));
        this.soundTypes.put("CHEST_CLOSE", new GraniteSoundType("random.chestclosed"));
        this.soundTypes.put("CHEST_OPEN", new GraniteSoundType("random.chestopen"));
        this.soundTypes.put("CLICK", new GraniteSoundType("random.click"));
        this.soundTypes.put("DOOR_CLOSE", new GraniteSoundType("random.door_close"));
        this.soundTypes.put("DOOR_OPEN", new GraniteSoundType("random.door_open"));
        this.soundTypes.put("DRINK", new GraniteSoundType("random.drink"));
        this.soundTypes.put("EAT", new GraniteSoundType("random.eat"));
        this.soundTypes.put("EXPLODE", new GraniteSoundType("random.explode"));
        this.soundTypes.put("FALL_BIG", new GraniteSoundType("game.player.hurt.fall.big"));
        this.soundTypes.put("FALL_SMALL", new GraniteSoundType("game.player.hurt.fall.small"));
        this.soundTypes.put("FIRE", new GraniteSoundType("fire.fire"));
        this.soundTypes.put("FIRE_IGNITE", new GraniteSoundType("fire.ignite"));
        this.soundTypes.put("FIZZ", new GraniteSoundType("random.fizz"));
        this.soundTypes.put("FUSE", new GraniteSoundType("game.tnt.primed"));
        this.soundTypes.put("GLASS", new GraniteSoundType("dig.glass"));
        this.soundTypes.put("HURT_FLESH", new GraniteSoundType("game.player.hurt"));
        this.soundTypes.put("ITEM_BREAK", new GraniteSoundType("random.break"));
        this.soundTypes.put("ITEM_PICKUP", new GraniteSoundType("random.pop"));
        this.soundTypes.put("LAVA", new GraniteSoundType("liquid.lava"));
        this.soundTypes.put("LAVA_POP", new GraniteSoundType("liquid.lavapop"));
        this.soundTypes.put("LEVEL_UP", new GraniteSoundType("random.levelup"));
        this.soundTypes.put("MINECART_BASE", new GraniteSoundType("minecart.base"));
        this.soundTypes.put("MINECART_INSIDE", new GraniteSoundType("minecart.inside"));
        this.soundTypes.put("NOTE_BASS", new GraniteSoundType("note.bass"));
        this.soundTypes.put("NOTE_PIANO", new GraniteSoundType("note.harp"));
        this.soundTypes.put("NOTE_BASS_DRUM", new GraniteSoundType("note.bd"));
        this.soundTypes.put("NOTE_STICKS", new GraniteSoundType("note.hat"));
        this.soundTypes.put("NOTE_BASS_GUITAR", new GraniteSoundType("note.bassattack"));
        this.soundTypes.put("NOTE_SNARE_DRUM", new GraniteSoundType("note.snare"));
        this.soundTypes.put("NOTE_PLING", new GraniteSoundType("note.pling"));
        this.soundTypes.put("ORB_PICKUP", new GraniteSoundType("random.orb"));
        this.soundTypes.put("PISTON_EXTEND", new GraniteSoundType("tile.piston.out"));
        this.soundTypes.put("PISTON_RETRACT", new GraniteSoundType("tile.piston.in"));
        this.soundTypes.put("PORTAL", new GraniteSoundType("portal.portal"));
        this.soundTypes.put("PORTAL_TRAVEL", new GraniteSoundType("portal.travel"));
        this.soundTypes.put("PORTAL_TRIGGER", new GraniteSoundType("portal.trigger"));
        this.soundTypes.put("SHOOT_ARROW", new GraniteSoundType("random.bow"));
        this.soundTypes.put("SPLASH", new GraniteSoundType("random.splash"));
        this.soundTypes.put("SPLASH2", new GraniteSoundType("game.player.swim.splash"));
        this.soundTypes.put("STEP_GRASS", new GraniteSoundType("step.grass"));
        this.soundTypes.put("STEP_GRAVEL", new GraniteSoundType("step.gravel"));
        this.soundTypes.put("STEP_LADDER", new GraniteSoundType("(step.ladder"));
        this.soundTypes.put("STEP_SAND", new GraniteSoundType("step.sand"));
        this.soundTypes.put("STEP_SNOW", new GraniteSoundType("step.snow"));
        this.soundTypes.put("STEP_STONE", new GraniteSoundType("step.stone"));
        this.soundTypes.put("STEP_WOOD", new GraniteSoundType("step.wood"));
        this.soundTypes.put("STEP_WOOL", new GraniteSoundType("step.cloth"));
        this.soundTypes.put("SWIM", new GraniteSoundType("game.player.swim"));
        this.soundTypes.put("WATER", new GraniteSoundType("liquid.water"));
        this.soundTypes.put("WOOD_CLICK", new GraniteSoundType("random.wood_click"));
        this.soundTypes.put("BAT_DEATH", new GraniteSoundType("mob.bat.death"));
        this.soundTypes.put("BAT_HURT", new GraniteSoundType("mob.bat.hurt"));
        this.soundTypes.put("BAT_IDLE", new GraniteSoundType("mob.bat.idle"));
        this.soundTypes.put("BAT_LOOP", new GraniteSoundType("mob.bat.loop"));
        this.soundTypes.put("BAT_TAKEOFF", new GraniteSoundType("mob.bat.takeoff"));
        this.soundTypes.put("BLAZE_BREATH", new GraniteSoundType("mob.blaze.breathe"));
        this.soundTypes.put("BLAZE_DEATH", new GraniteSoundType("mob.blaze.death"));
        this.soundTypes.put("BLAZE_HIT", new GraniteSoundType("mob.blaze.hit"));
        this.soundTypes.put("CAT_HISS", new GraniteSoundType("mob.cat.hiss"));
        this.soundTypes.put("CAT_HIT", new GraniteSoundType("mob.cat.hitt"));
        this.soundTypes.put("CAT_MEOW", new GraniteSoundType("mob.cat.meow"));
        this.soundTypes.put("CAT_PURR", new GraniteSoundType("mob.cat.purr"));
        this.soundTypes.put("CAT_PURREOW", new GraniteSoundType("mob.cat.purreow"));
        this.soundTypes.put("CHICKEN_IDLE", new GraniteSoundType("mob.chicken.say"));
        this.soundTypes.put("CHICKEN_HURT", new GraniteSoundType("mob.chicken.hurt"));
        this.soundTypes.put("CHICKEN_EGG_POP", new GraniteSoundType("mob.chicken.plop"));
        this.soundTypes.put("CHICKEN_WALK", new GraniteSoundType("mob.chicken.step"));
        this.soundTypes.put("COW_IDLE", new GraniteSoundType("mob.cow.say"));
        this.soundTypes.put("COW_HURT", new GraniteSoundType("mob.cow.hurt"));
        this.soundTypes.put("COW_WALK", new GraniteSoundType("mob.cow.step"));
        this.soundTypes.put("CREEPER_HISS", new GraniteSoundType("creeper.primed"));
        this.soundTypes.put("CREEPER_DEATH", new GraniteSoundType("mob.creeper.death"));
        this.soundTypes.put("ENDERDRAGON_DEATH", new GraniteSoundType("mob.enderdragon.end"));
        this.soundTypes.put("ENDERDRAGON_GROWL", new GraniteSoundType("mob.enderdragon.growl"));
        this.soundTypes.put("ENDERDRAGON_HIT", new GraniteSoundType("mob.enderdragon.hit"));
        this.soundTypes.put("ENDERDRAGON_WINGS", new GraniteSoundType("mob.enderdragon.wings"));
        this.soundTypes.put("ENDERMAN_DEATH", new GraniteSoundType("mob.endermen.death"));
        this.soundTypes.put("ENDERMAN_HIT", new GraniteSoundType("mob.endermen.hit"));
        this.soundTypes.put("ENDERMAN_IDLE", new GraniteSoundType("mob.endermen.idle"));
        this.soundTypes.put("ENDERMAN_TELEPORT", new GraniteSoundType("mob.endermen.portal"));
        this.soundTypes.put("ENDERMAN_SCREAM", new GraniteSoundType("mob.endermen.scream"));
        this.soundTypes.put("ENDERMAN_STARE", new GraniteSoundType("mob.endermen.stare"));
        this.soundTypes.put("GHAST_SCREAM", new GraniteSoundType("mob.ghast.scream"));
        this.soundTypes.put("GHAST_SCREAM2", new GraniteSoundType("mob.ghast.affectionate_scream"));
        this.soundTypes.put("GHAST_CHARGE", new GraniteSoundType("mob.ghast.charge"));
        this.soundTypes.put("GHAST_DEATH", new GraniteSoundType("mob.ghast.death"));
        this.soundTypes.put("GHAST_FIREBALL", new GraniteSoundType("mob.ghast.fireball"));
        this.soundTypes.put("GHAST_MOAN", new GraniteSoundType("mob.ghast.moan"));
        this.soundTypes.put("GUARDIAN_IDLE", new GraniteSoundType("mob.guardian.idle"));
        this.soundTypes.put("GUARDIAN_ELDER_IDLE", new GraniteSoundType("mob.guardian.elder.idle"));
        this.soundTypes.put("GUARDIAN_LAND_IDLE", new GraniteSoundType("mob.guardian.land.idle"));
        this.soundTypes.put("GUARDIAN_HIT", new GraniteSoundType("mob.guardian.hit"));
        this.soundTypes.put("GUARDIAN_ELDER_HIT", new GraniteSoundType("mob.guardian.elder.hit"));
        this.soundTypes.put("GUARDIAN_LAND_HIT", new GraniteSoundType("mob.guardian.land.hit"));
        this.soundTypes.put("GUARDIAN_DEATH", new GraniteSoundType("mob.guardian.death"));
        this.soundTypes.put("GUARDIAN_ELDER_DEATH", new GraniteSoundType("mob.guardian.elder.death"));
        this.soundTypes.put("GUARDIAN_LAND_DEATH", new GraniteSoundType("mob.guardian.land.death"));
        this.soundTypes.put("IRONGOLEM_DEATH", new GraniteSoundType("mob.irongolem.death"));
        this.soundTypes.put("IRONGOLEM_HIT", new GraniteSoundType("mob.irongolem.hit"));
        this.soundTypes.put("IRONGOLEM_THROW", new GraniteSoundType("mob.irongolem.throw"));
        this.soundTypes.put("IRONGOLEM_WALK", new GraniteSoundType("mob.irongolem.walk"));
        this.soundTypes.put("MAGMACUBE_WALK", new GraniteSoundType("mob.magmacube.big"));
        this.soundTypes.put("MAGMACUBE_WALK2", new GraniteSoundType("mob.magmacube.small"));
        this.soundTypes.put("MAGMACUBE_JUMP", new GraniteSoundType("mob.magmacube.jump"));
        this.soundTypes.put("PIG_IDLE", new GraniteSoundType("mob.pig.say"));
        this.soundTypes.put("PIG_DEATH", new GraniteSoundType("mob.pig.death"));
        this.soundTypes.put("PIG_WALK", new GraniteSoundType("mob.pig.step"));
        this.soundTypes.put("RABBIT_IDLE", new GraniteSoundType("mob.rabbit.idle"));
        this.soundTypes.put("RABBIT_HURT", new GraniteSoundType("mob.rabbit.hurt"));
        this.soundTypes.put("RABBIT_DEATH", new GraniteSoundType("mob.rabbit.death"));
        this.soundTypes.put("SHEEP_IDLE", new GraniteSoundType("mob.sheep.say"));
        this.soundTypes.put("SHEEP_SHEAR", new GraniteSoundType("mob.sheep.shear"));
        this.soundTypes.put("SHEEP_WALK", new GraniteSoundType("mob.sheep.step"));
        this.soundTypes.put("SILVERFISH_HIT", new GraniteSoundType("mob.silverfish.hit"));
        this.soundTypes.put("SILVERFISH_KILL", new GraniteSoundType("mob.silverfish.kill"));
        this.soundTypes.put("SILVERFISH_IDLE", new GraniteSoundType("mob.silverfish.say"));
        this.soundTypes.put("SILVERFISH_WALK", new GraniteSoundType("mob.silverfish.step"));
        this.soundTypes.put("SKELETON_IDLE", new GraniteSoundType("mob.skeleton.say"));
        this.soundTypes.put("SKELETON_DEATH", new GraniteSoundType("mob.skeleton.death"));
        this.soundTypes.put("SKELETON_HURT", new GraniteSoundType("mob.skeleton.hurt"));
        this.soundTypes.put("SKELETON_WALK", new GraniteSoundType("mob.skeleton.step"));
        this.soundTypes.put("SLIME_ATTACK", new GraniteSoundType("mob.slime.attack"));
        this.soundTypes.put("SLIME_WALK", new GraniteSoundType("mob.slime.small"));
        this.soundTypes.put("SLIME_WALK2", new GraniteSoundType("mob.slime.big"));
        this.soundTypes.put("SPIDER_IDLE", new GraniteSoundType("mob.spider.say"));
        this.soundTypes.put("SPIDER_DEATH", new GraniteSoundType("mob.spider.death"));
        this.soundTypes.put("SPIDER_WALK", new GraniteSoundType("mob.spider.step"));
        this.soundTypes.put("WITHER_DEATH", new GraniteSoundType("mob.wither.death"));
        this.soundTypes.put("WITHER_HURT", new GraniteSoundType("mob.wither.hurt"));
        this.soundTypes.put("WITHER_IDLE", new GraniteSoundType("mob.wither.idle"));
        this.soundTypes.put("WITHER_SHOOT", new GraniteSoundType("mob.wither.shoot"));
        this.soundTypes.put("WITHER_SPAWN", new GraniteSoundType("mob.wither.spawn"));
        this.soundTypes.put("WOLF_BARK", new GraniteSoundType("mob.wolf.bark"));
        this.soundTypes.put("WOLF_DEATH", new GraniteSoundType("mob.wolf.death"));
        this.soundTypes.put("WOLF_GROWL", new GraniteSoundType("mob.wolf.growl"));
        this.soundTypes.put("WOLF_HOWL", new GraniteSoundType("mob.wolf.howl"));
        this.soundTypes.put("WOLF_HURT", new GraniteSoundType("mob.wolf.hurt"));
        this.soundTypes.put("WOLF_PANT", new GraniteSoundType("mob.wolf.panting"));
        this.soundTypes.put("WOLF_SHAKE", new GraniteSoundType("mob.wolf.shake"));
        this.soundTypes.put("WOLF_WALK", new GraniteSoundType("mob.wolf.step"));
        this.soundTypes.put("WOLF_WHINE", new GraniteSoundType("mob.wolf.whine"));
        this.soundTypes.put("ZOMBIE_METAL", new GraniteSoundType("mob.zombie.metal"));
        this.soundTypes.put("ZOMBIE_WOOD", new GraniteSoundType("mob.zombie.wood"));
        this.soundTypes.put("ZOMBIE_WOODBREAK", new GraniteSoundType("mob.zombie.woodbreak"));
        this.soundTypes.put("ZOMBIE_IDLE", new GraniteSoundType("mob.zombie.say"));
        this.soundTypes.put("ZOMBIE_DEATH", new GraniteSoundType("mob.zombie.death"));
        this.soundTypes.put("ZOMBIE_HURT", new GraniteSoundType("mob.zombie.hurt"));
        this.soundTypes.put("ZOMBIE_INFECT", new GraniteSoundType("mob.zombie.infect"));
        this.soundTypes.put("ZOMBIE_UNFECT", new GraniteSoundType("mob.zombie.unfect"));
        this.soundTypes.put("ZOMBIE_REMEDY", new GraniteSoundType("mob.zombie.remedy"));
        this.soundTypes.put("ZOMBIE_WALK", new GraniteSoundType("mob.zombie.step"));
        this.soundTypes.put("ZOMBIE_PIG_IDLE", new GraniteSoundType("mob.zombiepig.zpig"));
        this.soundTypes.put("ZOMBIE_PIG_ANGRY", new GraniteSoundType("mob.zombiepig.zpigangry"));
        this.soundTypes.put("ZOMBIE_PIG_DEATH", new GraniteSoundType("mob.zombiepig.zpigdeath"));
        this.soundTypes.put("ZOMBIE_PIG_HURT", new GraniteSoundType("mob.zombiepig.zpighurt"));
        this.soundTypes.put("DIG_WOOL", new GraniteSoundType("dig.cloth"));
        this.soundTypes.put("DIG_GRASS", new GraniteSoundType("dig.grass"));
        this.soundTypes.put("DIG_GRAVEL", new GraniteSoundType("dig.gravel"));
        this.soundTypes.put("DIG_SAND", new GraniteSoundType("dig.sand"));
        this.soundTypes.put("DIG_SNOW", new GraniteSoundType("dig.snow"));
        this.soundTypes.put("DIG_STONE", new GraniteSoundType("dig.stone"));
        this.soundTypes.put("DIG_WOOD", new GraniteSoundType("dig.wood"));
        this.soundTypes.put("FIREWORK_BLAST", new GraniteSoundType("fireworks.blast"));
        this.soundTypes.put("FIREWORK_BLAST2", new GraniteSoundType("fireworks.blast_far"));
        this.soundTypes.put("FIREWORK_LARGE_BLAST", new GraniteSoundType("fireworks.largeBlast"));
        this.soundTypes.put("FIREWORK_LARGE_BLAST2", new GraniteSoundType("fireworks.largeBlast_far"));
        this.soundTypes.put("FIREWORK_TWINKLE", new GraniteSoundType("fireworks.twinkle"));
        this.soundTypes.put("FIREWORK_TWINKLE2", new GraniteSoundType("fireworks.twinkle_far"));
        this.soundTypes.put("FIREWORK_LAUNCH", new GraniteSoundType("fireworks.launch"));
        this.soundTypes.put("SUCCESSFUL_HIT", new GraniteSoundType("random.successful_hit"));
        this.soundTypes.put("HORSE_ANGRY", new GraniteSoundType("mob.horse.angry"));
        this.soundTypes.put("HORSE_ARMOR", new GraniteSoundType("mob.horse.armor"));
        this.soundTypes.put("HORSE_BREATHE", new GraniteSoundType("mob.horse.breathe"));
        this.soundTypes.put("HORSE_DEATH", new GraniteSoundType("mob.horse.death"));
        this.soundTypes.put("HORSE_GALLOP", new GraniteSoundType("mob.horse.gallop"));
        this.soundTypes.put("HORSE_HIT", new GraniteSoundType("mob.horse.hit"));
        this.soundTypes.put("HORSE_IDLE", new GraniteSoundType("mob.horse.idle"));
        this.soundTypes.put("HORSE_JUMP", new GraniteSoundType("mob.horse.jump"));
        this.soundTypes.put("HORSE_LAND", new GraniteSoundType("mob.horse.land"));
        this.soundTypes.put("HORSE_SADDLE", new GraniteSoundType("mob.horse.leather"));
        this.soundTypes.put("HORSE_SOFT", new GraniteSoundType("mob.horse.soft"));
        this.soundTypes.put("HORSE_WOOD", new GraniteSoundType("mob.horse.wood"));
        this.soundTypes.put("DONKEY_ANGRY", new GraniteSoundType("mob.horse.donkey.angry"));
        this.soundTypes.put("DONKEY_DEATH", new GraniteSoundType("mob.horse.donkey.death"));
        this.soundTypes.put("DONKEY_HIT", new GraniteSoundType("mob.horse.donkey.hit"));
        this.soundTypes.put("DONKEY_IDLE", new GraniteSoundType("mob.horse.donkey.idle"));
        this.soundTypes.put("HORSE_SKELETON_DEATH", new GraniteSoundType("mob.horse.skeleton.death"));
        this.soundTypes.put("HORSE_SKELETON_HIT", new GraniteSoundType("mob.horse.skeleton.hit"));
        this.soundTypes.put("HORSE_SKELETON_IDLE", new GraniteSoundType("mob.horse.skeleton.idle"));
        this.soundTypes.put("HORSE_ZOMBIE_DEATH", new GraniteSoundType("mob.horse.zombie.death"));
        this.soundTypes.put("HORSE_ZOMBIE_HIT", new GraniteSoundType("mob.horse.zombie.hit"));
        this.soundTypes.put("HORSE_ZOMBIE_IDLE", new GraniteSoundType("mob.horse.zombie.idle"));
        this.soundTypes.put("VILLAGER_DEATH", new GraniteSoundType("mob.villager.death"));
        this.soundTypes.put("VILLAGER_HAGGLE", new GraniteSoundType("mob.villager.haggle"));
        this.soundTypes.put("VILLAGER_HIT", new GraniteSoundType("mob.villager.hit"));
        this.soundTypes.put("VILLAGER_IDLE", new GraniteSoundType("mob.villager.idle"));
        this.soundTypes.put("VILLAGER_NO", new GraniteSoundType("mob.villager.no"));
        this.soundTypes.put("VILLAGER_YES", new GraniteSoundType("mob.villager.yes"));
        RegistryHelper.mapFields(SoundTypes.class, this.soundTypes);
    }

    private void registerTextColors() {
        this.textColors.put("BLACK", new GraniteTextColor(EnumChatFormatting.BLACK, Color.BLACK));
        this.textColors.put("DARK_BLUE", new GraniteTextColor(EnumChatFormatting.DARK_BLUE, new Color(0x0000AA)));
        this.textColors.put("DARK_GREEN", new GraniteTextColor(EnumChatFormatting.DARK_GREEN, new Color(0x00AA00)));
        this.textColors.put("DARK_AQUA", new GraniteTextColor(EnumChatFormatting.DARK_AQUA, new Color(0x00AAAA)));
        this.textColors.put("DARK_RED", new GraniteTextColor(EnumChatFormatting.DARK_RED, new Color(0xAA0000)));
        this.textColors.put("DARK_PURPLE", new GraniteTextColor(EnumChatFormatting.DARK_PURPLE, new Color(0xAA00AA)));
        this.textColors.put("GOLD", new GraniteTextColor(EnumChatFormatting.GOLD, new Color(0xFFAA00)));
        this.textColors.put("GRAY", new GraniteTextColor(EnumChatFormatting.GRAY, new Color(0xAAAAAA)));
        this.textColors.put("DARK_GRAY", new GraniteTextColor(EnumChatFormatting.DARK_GRAY, new Color(0x555555)));
        this.textColors.put("BLUE", new GraniteTextColor(EnumChatFormatting.BLUE, new Color(0x5555FF)));
        this.textColors.put("GREEN", new GraniteTextColor(EnumChatFormatting.GREEN, new Color(0x55FF55)));
        this.textColors.put("AQUA", new GraniteTextColor(EnumChatFormatting.AQUA, new Color(0x00FFFF)));
        this.textColors.put("RED", new GraniteTextColor(EnumChatFormatting.RED, new Color(0xFF5555)));
        this.textColors.put("LIGHT_PURPLE", new GraniteTextColor(EnumChatFormatting.LIGHT_PURPLE, new Color(0xFF55FF)));
        this.textColors.put("YELLOW", new GraniteTextColor(EnumChatFormatting.YELLOW, new Color(0xFFFF55)));
        this.textColors.put("WHITE", new GraniteTextColor(EnumChatFormatting.WHITE, Color.WHITE));
        this.textColors.put("RESET", new GraniteTextColor(EnumChatFormatting.RESET, Color.WHITE));
        RegistryHelper.mapFields(TextColors.class, this.textColors);
    }

    private void registerTextStyles() {
        this.textStyles.put("BOLD", GraniteTextStyle.of(EnumChatFormatting.BOLD));
        this.textStyles.put("ITALIC", GraniteTextStyle.of(EnumChatFormatting.ITALIC));
        this.textStyles.put("UNDERLINE", GraniteTextStyle.of(EnumChatFormatting.UNDERLINE));
        this.textStyles.put("STRIKETHROUGH", GraniteTextStyle.of(EnumChatFormatting.STRIKETHROUGH));
        this.textStyles.put("OBFUSCATED", GraniteTextStyle.of(EnumChatFormatting.OBFUSCATED));
        this.textStyles.put("RESET", GraniteTextStyle.of(EnumChatFormatting.RESET));
        RegistryHelper.mapFields(TextStyles.class, this.textStyles);
    }

    private void registerTranslations() {
    }
}

