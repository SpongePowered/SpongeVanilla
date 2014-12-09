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

package org.granitepowered.granite.utils;

import com.google.common.collect.ImmutableMap;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.block.GraniteBlockProperty;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.block.GraniteBlockType;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.entity.living.GraniteLiving;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.item.GraniteItemBlock;
import org.granitepowered.granite.impl.item.GraniteItemStack;
import org.granitepowered.granite.impl.item.GraniteItemType;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.mappings.Mappings;
import org.spongepowered.api.text.message.Message;

import java.util.Objects;

public class MinecraftUtils {
    public static ImmutableMap<Class<?>, Class<? extends Composite>> composites = ImmutableMap.<Class<?>, Class<? extends Composite>>builder()
            .put(Mappings.getClass("Entity"), GraniteEntity.class)
            .put(Mappings.getClass("EntityLiving"), GraniteLiving.class)
            .put(Mappings.getClass("EntityPlayerMP"), GranitePlayer.class)
            .put(Mappings.getClass("BlockState"), GraniteBlockState.class)
            .put(Mappings.getClass("Block"), GraniteBlockType.class)
            .put(Mappings.getClass("ItemBlock"), GraniteItemBlock.class)
            .put(Mappings.getClass("ItemStack"), GraniteItemStack.class)
            .put(Mappings.getClass("Item"), GraniteItemType.class)
            .put(Mappings.getClass("PropertyHelper"), GraniteBlockProperty.class)
            .build();

    public static Composite wrapComposite(Object obj) {
        if (obj == null) return null;

        Class<?> clazz = obj.getClass();
        while (!composites.containsKey(clazz)) {
            if (Objects.equals(clazz.getName(), "Object")) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        if (composites.containsKey(clazz)) {
            return Composite.new_(obj, composites.get(clazz));
        } else {
            return null;
        }
    }

    public static Object graniteToMinecraftMessage(Message<?> message) {
        String json = Granite.getInstance().getGson().toJson(message, Message.class);
        return Mappings.invokeStatic("IChatComponent$Serializer", "jsonToComponent", json);
    }
}
