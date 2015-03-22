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

package org.granitepowered.granite.impl.text.message;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.MessageBuilder;
import org.spongepowered.api.text.translation.Translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public abstract class GraniteMessageBuilder<T extends Message> implements MessageBuilder {

    List<Message> children;
    TextColor color;
    TextStyle style;
    Optional<ClickAction<?>> clickAction;
    Optional<HoverAction<?>> hoverAction;
    Optional<ShiftClickAction<?>> shiftClickAction;

    public GraniteMessageBuilder(ImmutableList<Message> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction,
            Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction) {
        this.children = children;
        this.color = color;
        this.style = style;
        this.clickAction = clickAction;
        this.hoverAction = hoverAction;
        this.shiftClickAction = shiftClickAction;
    }

    public GraniteMessageBuilder() {
        children = new ArrayList<>();
        color = TextColors.RESET;
        style = TextStyles.RESET;
        clickAction = Optional.absent();
        hoverAction = Optional.absent();
        shiftClickAction = Optional.absent();
    }

    public static class GraniteTextMessageBuilder extends GraniteMessageBuilder<Message.Text> implements Text {

        String content;

        public GraniteTextMessageBuilder(String content, ImmutableList<Message> children, TextColor color, TextStyle style,
                Optional<ClickAction<?>> clickAction, Optional<HoverAction<?>> hoverAction,
                Optional<ShiftClickAction<?>> shiftClickAction) {
            super(children, color, style, clickAction, hoverAction, shiftClickAction);
            this.content = content;
        }

        public GraniteTextMessageBuilder(String content) {
            this.content = content;
        }

        @Override
        public Message.Text build() {
            return new GraniteMessage.GraniteText(ImmutableList.copyOf(children), color, style, clickAction, hoverAction, shiftClickAction, content);
        }

        @Override
        public MessageBuilder.Text color(@Nullable TextColor color) {
            this.color = color;
            return this;
        }

        @Override
        public MessageBuilder.Text style(TextStyle... styles) {
            this.style = TextStyles.of(styles);
            return this;
        }

        @Override
        public MessageBuilder.Text onClick(@Nullable ClickAction<?> action) {
            this.clickAction = Optional.<ClickAction<?>>fromNullable(action);
            return this;
        }

        @Override
        public MessageBuilder.Text onHover(@Nullable HoverAction<?> action) {
            this.hoverAction = Optional.<HoverAction<?>>fromNullable(action);
            return this;
        }

        @Override
        public MessageBuilder.Text onShiftClick(@Nullable ShiftClickAction<?> action) {
            this.shiftClickAction = Optional.<ShiftClickAction<?>>fromNullable(action);
            return this;
        }

        @Override
        public MessageBuilder.Text content(String s) {
            this.content = s;
            return this;
        }

        @Override
        public MessageBuilder.Text append(Message... children) {
            return append(Arrays.asList(children));
        }

        @Override
        public MessageBuilder.Text append(Iterable<Message> children) {
            this.children.addAll(Lists.newArrayList(children));
            return this;
        }
    }

    public static class GraniteTranslatableMessageBuilder extends GraniteMessageBuilder<Message.Translatable> implements Translatable {

        Translation translation;
        Object[] objects;

        public GraniteTranslatableMessageBuilder(Translation translation, Object... objects) {
            this.translation = translation;
            this.objects = objects;
        }

        public GraniteTranslatableMessageBuilder(ImmutableList<Message> children, TextColor color, TextStyle style,
                Optional<ClickAction<?>> clickAction, Optional<HoverAction<?>> hoverAction,
                Optional<ShiftClickAction<?>> shiftClickAction, Translation translation, Object[] objects) {
            super(children, color, style, clickAction, hoverAction, shiftClickAction);
            this.translation = translation;
            this.objects = objects;
        }

        @Override
        public Translatable content(Translation translation, Object... objects) {
            this.translation = translation;
            this.objects = objects;
            return this;
        }

        @Override
        public Translatable content(org.spongepowered.api.text.translation.Translatable translatable, Object... objects) {
            return content(translatable.getTranslation(), objects);
        }

        @Override
        public Translatable append(Message... messages) {
            return append(Arrays.asList(messages));
        }

        @Override
        public Translatable append(Iterable<Message> iterable) {
            this.children.addAll(Lists.newArrayList(children));
            return this;
        }

        @Override
        public Translatable color(@Nullable TextColor textColor) {
            this.color = textColor;
            return this;
        }

        @Override
        public Translatable style(TextStyle... textStyles) {
            this.style = TextStyles.of(textStyles);
            return this;
        }

        @Override
        public Translatable onClick(@Nullable ClickAction<?> clickAction) {
            this.clickAction = Optional.<ClickAction<?>>fromNullable(clickAction);
            return this;
        }

        @Override
        public Translatable onHover(@Nullable HoverAction<?> hoverAction) {
            this.hoverAction = Optional.<HoverAction<?>>fromNullable(hoverAction);
            return this;
        }

        @Override
        public Translatable onShiftClick(@Nullable ShiftClickAction<?> shiftClickAction) {
            this.shiftClickAction = Optional.<ShiftClickAction<?>>fromNullable(shiftClickAction);
            return this;
        }

        @Override
        public Message.Translatable build() {
            return new GraniteMessage.GraniteTranslatable(ImmutableList.copyOf(children), color, style, clickAction, hoverAction, shiftClickAction,
                    translation, ImmutableList.copyOf(objects));
        }
    }

    // TODO: The other two types (score and selector)
}
