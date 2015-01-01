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

package org.granitepowered.granite.utils;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.GraniteServer;
import org.granitepowered.granite.impl.block.GraniteBlockProperty;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.entity.living.GraniteLiving;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.item.GraniteItemBlock;
import org.granitepowered.granite.impl.item.GraniteItemType;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.impl.world.GraniteWorldBorder;
import org.granitepowered.granite.impl.world.biome.GraniteBiomeType;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.Implement;
import org.granitepowered.granite.mc.MCBlockPos;
import org.granitepowered.granite.mc.MCChatComponent;
import org.granitepowered.granite.mc.MCInterface;
import org.spongepowered.api.text.message.Message;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinecraftUtils {

    public static ImmutableMap<Class<?>, Class<? extends Composite>> composites = ImmutableMap.<Class<?>, Class<? extends Composite>>builder()
            .put(Mappings.getClass("BiomeGenBase"), GraniteBiomeType.class)
            .put(Mappings.getClass("Block"), GraniteBlockType.class)
            .put(Mappings.getClass("BlockState"), GraniteBlockState.class)
            .put(Mappings.getClass("DedicatedServer"), GraniteServer.class)
            .put(Mappings.getClass("Enchantment"), GraniteServer.class)
            .put(Mappings.getClass("Entity"), GraniteEntity.class)
            .put(Mappings.getClass("EntityLiving"), GraniteLiving.class)
            .put(Mappings.getClass("EntityPlayerMP"), GranitePlayer.class)
            .put(Mappings.getClass("Item"), GraniteItemType.class)
            .put(Mappings.getClass("ItemBlock"), GraniteItemBlock.class)
            .put(Mappings.getClass("ItemStack"), GraniteItemStack.class)
            .put(Mappings.getClass("PropertyHelper"), GraniteBlockProperty.class)
            .put(Mappings.getClass("WorldBorder"), GraniteWorldBorder.class)
            .put(Mappings.getClass("WorldServer"), GraniteWorld.class)
            .build();

    @Nonnull
    public static <T extends Composite> T wrap(MCInterface obj) {
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

    public static <T extends MCInterface> T unwrap(Object composite) {
        return (T) unwrap((Composite<?>) composite);
    }

    public static <T extends MCInterface> T unwrap(Composite<T> composite) {
        return composite.obj;
    }

    public static MCChatComponent graniteToMinecraftChatComponent(Message message) {
        String json = Granite.getInstance().getGson().toJson(message, GraniteMessage.class);
        return (MCChatComponent) Mappings.invokeStatic("IChatComponent$Serializer", "jsonToComponent", json);
    }

    public static MCBlockPos graniteToMinecraftBlockPos(Vector3i vector) {
        try {
            return (MCBlockPos) Mappings.getClass("BlockPos").getConstructor(int.class, int.class, int.class)
                    .newInstance(vector.getX(), vector.getY(), vector.getZ());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T instantiate(Class<?> clazz, Class<?>[] types, Object... args) {
        Class<?> mcClass;
        if (clazz.isAnnotationPresent(Implement.class)) {
            mcClass = Mappings.getClass(clazz.getAnnotation(Implement.class).name());
        } else {
            mcClass = clazz;
        }

        try {
            MethodHandle constructor = MethodHandles.lookup().findConstructor(mcClass, MethodType.methodType(void.class, types));
            return (T) constructor.invokeWithArguments(Arrays.asList(args));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static Enum enumValue(Class<?> clazz, int number) {
        return (Enum) clazz.getEnumConstants()[number];
    }

    public static Message minecraftToGraniteMessage(MCChatComponent deathComponent) {
        String json = (String) Mappings.invokeStatic("IChatComponent$Serializer", "componentToJson", deathComponent);
        return Granite.getInstance().getGson().fromJson(json, GraniteMessage.class);
    }

    public static class WrapFunction<T extends Composite> implements Function<MCInterface, T> {

        @Nullable
        @Override
        public T apply(@Nullable MCInterface input) {
            return wrap(input);
        }
    }
}
