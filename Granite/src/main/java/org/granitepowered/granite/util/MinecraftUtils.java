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

package org.granitepowered.granite.util;

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.GraniteGameProfile;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.block.GraniteBlockProperty;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.entity.GraniteEntityEnderCrystal;
import org.granitepowered.granite.impl.entity.GraniteEntityExperienceOrb;
import org.granitepowered.granite.impl.entity.GraniteEntityFallingBlock;
import org.granitepowered.granite.impl.entity.GraniteEntityItem;
import org.granitepowered.granite.impl.entity.explosive.GraniteEntityPrimedTNT;
import org.granitepowered.granite.impl.entity.hanging.art.GraniteArt;
import org.granitepowered.granite.impl.entity.living.GraniteEntityArmorStand;
import org.granitepowered.granite.impl.entity.living.GraniteEntityBat;
import org.granitepowered.granite.impl.entity.living.GraniteEntityLiving;
import org.granitepowered.granite.impl.entity.living.GraniteEntityLivingBase;
import org.granitepowered.granite.impl.entity.living.complex.GraniteEntityDragon;
import org.granitepowered.granite.impl.entity.living.complex.GraniteEntityDragonPart;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.entity.projectile.GraniteEntityArrow;
import org.granitepowered.granite.impl.entity.projectile.GraniteEntityEgg;
import org.granitepowered.granite.impl.entity.projectile.fireball.GraniteLargeFireball;
import org.granitepowered.granite.impl.entity.projectile.fireball.GraniteSmallFireball;
import org.granitepowered.granite.impl.entity.weather.GraniteEntityLightningBolt;
import org.granitepowered.granite.impl.item.GraniteItemBlock;
import org.granitepowered.granite.impl.item.GraniteItemType;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.impl.item.merchant.GraniteTradeOffer;
import org.granitepowered.granite.impl.meta.GraniteBannerPatternShape;
import org.granitepowered.granite.impl.meta.GraniteDyeColor;
import org.granitepowered.granite.impl.potion.GranitePotionEffect;
import org.granitepowered.granite.impl.potion.GranitePotionEffectType;
import org.granitepowered.granite.impl.status.GraniteStatusClient;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.impl.world.GraniteChunk;
import org.granitepowered.granite.impl.world.GraniteDimension;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.impl.world.GraniteWorldBorder;
import org.granitepowered.granite.impl.world.biome.GraniteBiomeType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MC;
import org.granitepowered.granite.mc.MCBlockPos;
import org.granitepowered.granite.mc.MCIChatComponent;
import org.granitepowered.granite.mc.MCRotations;
import org.spongepowered.api.text.message.Message;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinecraftUtils {

    public static ImmutableMap<Class<?>, Class<? extends Composite>> composites = ImmutableMap.<Class<?>, Class<? extends Composite>>builder()
            .put(Mappings.getClass("BiomeGenBase"), GraniteBiomeType.class)
            .put(Mappings.getClass("Block"), GraniteBlockType.class)
            .put(Mappings.getClass("BlockState"), GraniteBlockState.class)
            .put(Mappings.getClass("Chunk"), GraniteChunk.class)
            .put(Mappings.getClass("DedicatedServer"), GraniteServer.class)
            .put(Mappings.getClass("Enchantment"), GraniteServer.class)
            .put(Mappings.getClass("Entity"), GraniteEntity.class)
            .put(Mappings.getClass("EntityArmorStand"), GraniteEntityArmorStand.class)
            .put(Mappings.getClass("EntityArrow"), GraniteEntityArrow.class)
            .put(Mappings.getClass("EntityBat"), GraniteEntityBat.class)
            .put(Mappings.getClass("EntityEnderCrystal"), GraniteEntityEnderCrystal.class)
            .put(Mappings.getClass("EntityDragon"), GraniteEntityDragon.class)
            .put(Mappings.getClass("EntityDragonPart"), GraniteEntityDragonPart.class)
            .put(Mappings.getClass("EntityEgg"), GraniteEntityEgg.class)
            .put(Mappings.getClass("EntityFallingBlock"), GraniteEntityFallingBlock.class)
            .put(Mappings.getClass("EntityItem"), GraniteEntityItem.class)
            .put(Mappings.getClass("EntityLargeFireball"), GraniteLargeFireball.class)
            .put(Mappings.getClass("EntityLightningBolt"), GraniteEntityLightningBolt.class)
            .put(Mappings.getClass("EntityLivingBase"), GraniteEntityLivingBase.class)
            .put(Mappings.getClass("EntityLiving"), GraniteEntityLiving.class)
            .put(Mappings.getClass("EntityPlayerMP"), GranitePlayer.class)
            .put(Mappings.getClass("EntitySmallFireball"), GraniteSmallFireball.class)
            .put(Mappings.getClass("EntityTNTPrimed"), GraniteEntityPrimedTNT.class)
            .put(Mappings.getClass("EntityXPOrb"), GraniteEntityExperienceOrb.class)
            .put(Mappings.getClass("EnumArt"), GraniteArt.class)
            .put(Mappings.getClass("EnumBannerPattern"), GraniteBannerPatternShape.class)
            .put(Mappings.getClass("EnumDyeColor"), GraniteDyeColor.class)
            .put(Mappings.getClass("GameProfile"), GraniteGameProfile.class)
            .put(Mappings.getClass("Item"), GraniteItemType.class)
            .put(Mappings.getClass("ItemBlock"), GraniteItemBlock.class)
            .put(Mappings.getClass("ItemStack"), GraniteItemStack.class)
            .put(Mappings.getClass("MerchantRecipe"), GraniteTradeOffer.class)
            .put(Mappings.getClass("NetworkManager"), GraniteStatusClient.class)
            .put(Mappings.getClass("Potion"), GranitePotionEffectType.class)
            .put(Mappings.getClass("PotionEffect"), GranitePotionEffect.class)
            .put(Mappings.getClass("PropertyHelper"), GraniteBlockProperty.class)
            .put(Mappings.getClass("WorldBorder"), GraniteWorldBorder.class)
            .put(Mappings.getClass("WorldServer"), GraniteWorld.class)
            .put(Mappings.getClass("WorldProvider"), GraniteDimension.class)
            .build();

    @Nonnull
    public static <T extends Composite> T wrap(MC obj) {
        if (obj == null) {
            return null;
        }

        Class<?> clazz = obj.getClass();
        while (!composites.containsKey(clazz)) {
            if (Objects.equals(clazz.getName(), "Object")) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        if (composites.containsKey(clazz)) {
            return (T) Composite.new_(obj, composites.get(clazz));
        } else {
            return null;
        }
    }

    public static <T extends MC> T unwrap(Object composite) {
        return (T) unwrap((Composite<?>) composite);
    }

    public static <T extends MC> T unwrap(Composite<T> composite) {
        return composite.obj;
    }

    public static MCIChatComponent graniteToMinecraftChatComponent(Message message) {
        String json = Granite.getInstance().getGson().toJson(message, GraniteMessage.class);
        return (MCIChatComponent) Mappings.invokeStatic("IChatComponent$Serializer", "jsonToComponent", json);
    }

    public static MCBlockPos graniteToMinecraftBlockPos(Vector3i vector) {
        return Instantiator.get().newBlockPos(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Enum enumValue(Class<?> clazz, int number) {
        return (Enum) clazz.getEnumConstants()[number];
    }

    public static Message minecraftToGraniteMessage(MCIChatComponent deathComponent) {
        String json = (String) Mappings.invokeStatic("IChatComponent$Serializer", "componentToJson", deathComponent);
        return Granite.getInstance().getGson().fromJson(json, GraniteMessage.class);
    }

    public static Vector3f minecraftToGraniteRotations(MCRotations mcRotations) {
        return new Vector3f(mcRotations.x, mcRotations.y, mcRotations.z);
    }

    public static MCRotations graniteToMinecraftRotations(Vector3f vector3f) {
        return Instantiator.get().newRotations(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public static class WrapFunction<T extends Composite> implements Function<MC, T> {

        @Nullable
        @Override
        public T apply(@Nullable MC input) {
            return wrap(input);
        }
    }
}
