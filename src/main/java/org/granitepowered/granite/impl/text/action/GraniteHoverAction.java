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

package org.granitepowered.granite.impl.text.action;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.message.Message;

public abstract class GraniteHoverAction<R> extends GraniteTextAction<R> implements HoverAction<R> {
    public static class GraniteShowText extends GraniteHoverAction<Message> implements ShowText {
        Message message;

        public GraniteShowText(Message message) {
            this.message = message;
        }

        @Override
        public String getId() {
            return "show_text";
        }

        @Override
        public Message getResult() {
            return message;
        }
    }

    public static class GraniteShowAchievement extends GraniteHoverAction<Object> implements ShowAchievement {
        Object object;

        public GraniteShowAchievement(Object object) {
            this.object = object;
        }

        @Override
        public String getId() {
            return "show_achievement";
        }

        @Override
        public Object getResult() {
            return object;
        }
    }

    public static class GraniteShowItem extends GraniteHoverAction<ItemStack> implements ShowItem {
        ItemStack itemStack;

        public GraniteShowItem(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Override
        public String getId() {
            return "show_item";
        }

        @Override
        public ItemStack getResult() {
            return itemStack;
        }
    }

    public static class GraniteShowEntity extends GraniteHoverAction<Entity> implements ShowEntity {
        Entity entity;

        public GraniteShowEntity(Entity entity) {
            this.entity = entity;
        }

        @Override
        public String getId() {
            return "show_entity";
        }

        @Override
        public Entity getResult() {
            return entity;
        }
    }
}
