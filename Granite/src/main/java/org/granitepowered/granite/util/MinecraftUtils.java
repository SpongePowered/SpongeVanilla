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
import mc.*;
import org.spongepowered.api.text.message.Message;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinecraftUtils {

    public static ImmutableMap<Class<?>, Class<? extends Composite>> composites = ImmutableMap.<Class<?>, Class<? extends Composite>>builder()
            .put(MCBiomeGenBase.class, GraniteBiomeType.class)
            .put(MCBlock.class, GraniteBlockType.class)
            .put(MCBlockState.class, GraniteBlockState.class)
            .put(MCChunk.class, GraniteChunk.class)
            .put(MCDedicatedServer.class, GraniteServer.class)
            .put(MCEnchantment.class, GraniteServer.class)
            .put(MCEntity.class, GraniteEntity.class)
            .put(MCEntityArmorStand.class, GraniteEntityArmorStand.class)
            .put(MCEntityArrow.class, GraniteEntityArrow.class)
            .put(MCEntityBat.class, GraniteEntityBat.class)
            .put(MCEntityEnderCrystal.class, GraniteEntityEnderCrystal.class)
            .put(MCEntityDragon.class, GraniteEntityDragon.class)
            .put(MCEntityDragonPart.class, GraniteEntityDragonPart.class)
            .put(MCEntityEgg.class, GraniteEntityEgg.class)
            .put(MCEntityFallingBlock.class, GraniteEntityFallingBlock.class)
            .put(MCEntityItem.class, GraniteEntityItem.class)
            .put(MCEntityLargeFireball.class, GraniteLargeFireball.class)
            .put(MCEntityLightningBolt.class, GraniteEntityLightningBolt.class)
            .put(MCEntityLivingBase.class, GraniteEntityLivingBase.class)
            .put(MCEntityLiving.class, GraniteEntityLiving.class)
            .put(MCEntityPlayerMP.class, GranitePlayer.class)
            .put(MCEntitySmallFireball.class, GraniteSmallFireball.class)
            .put(MCEntityTNTPrimed.class, GraniteEntityPrimedTNT.class)
            .put(MCEntityXPOrb.class, GraniteEntityExperienceOrb.class)
            .put(MCEnumArt.class, GraniteArt.class)
            .put(MCEnumBannerPattern.class, GraniteBannerPatternShape.class)
            .put(MCEnumDyeColor.class, GraniteDyeColor.class)
            .put(MCGameProfile.class, GraniteGameProfile.class)
            .put(MCItem.class, GraniteItemType.class)
            .put(MCItemBlock.class, GraniteItemBlock.class)
            .put(MCItemStack.class, GraniteItemStack.class)
            .put(MCMerchantRecipe.class, GraniteTradeOffer.class)
            .put(MCNetworkManager.class, GraniteStatusClient.class)
            .put(MCPotion.class, GranitePotionEffectType.class)
            .put(MCPotionEffect.class, GranitePotionEffect.class)
            .put(MCPropertyHelper.class, GraniteBlockProperty.class)
            .put(MCWorldBorder.class, GraniteWorldBorder.class)
            .put(MCWorldServer.class, GraniteWorld.class)
            .put(MCWorldProvider.class, GraniteDimension.class)
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
        return MCIChatComponent.Serializer.jsonToComponent(json);
    }

    public static MCBlockPos graniteToMinecraftBlockPos(Vector3i vector) {
        return Instantiator.get().newBlockPos(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Enum enumValue(Class<?> clazz, int number) {
        return (Enum) clazz.getEnumConstants()[number];
    }

    public static Message minecraftToGraniteMessage(MCIChatComponent component) {
        String json = MCIChatComponent.Serializer.componentToJson(component);
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
