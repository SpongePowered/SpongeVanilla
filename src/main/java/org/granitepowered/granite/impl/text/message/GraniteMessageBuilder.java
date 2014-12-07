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

package org.granitepowered.granite.impl.text.message;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.MessageBuilder;

import java.util.Arrays;

public abstract class GraniteMessageBuilder<T> implements MessageBuilder<T> {
    T content;
    ImmutableList<Message<?>> children;
    TextColor color;
    TextStyle style;
    Optional<ClickAction<?>> clickAction;
    Optional<HoverAction<?>> hoverAction;
    Optional<ShiftClickAction<?>> shiftClickAction;

    public GraniteMessageBuilder(T content, ImmutableList<Message<?>> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction, Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction) {
        this.content = content;
        this.children = children;
        this.color = color;
        this.style = style;
        this.clickAction = clickAction;
        this.hoverAction = hoverAction;
        this.shiftClickAction = shiftClickAction;
    }

    public GraniteMessageBuilder() {
        children = ImmutableList.<Message<?>>builder().build();
        color = TextColors.RESET;
        style = TextStyles.RESET;
        clickAction = Optional.absent();
        hoverAction = Optional.absent();
        shiftClickAction = Optional.absent();
    }

    public static class GraniteTextMessageBuilder extends GraniteMessageBuilder<String> {
        public GraniteTextMessageBuilder() {
            super();
            content = "";
        }

        public GraniteTextMessageBuilder(String content, ImmutableList<Message<?>> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction, Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction) {
            super(content, children, color, style, clickAction, hoverAction, shiftClickAction);
        }

        @Override
        public MessageBuilder<String> append(Message<?>... children) {
            return append(Arrays.asList(children));
        }

        @Override
        public MessageBuilder<String> append(Iterable<Message<?>> children) {
            return new GraniteTextMessageBuilder(content,
                    ImmutableList.<Message<?>>builder().addAll(this.children).addAll(children).build(),
                    color, style, clickAction, hoverAction, shiftClickAction
            );
        }

        @Override
        public MessageBuilder<String> content(String content) {
            return new GraniteTextMessageBuilder(content, children, color, style, clickAction, hoverAction, shiftClickAction);
        }

        @Override
        public MessageBuilder<String> color(TextColor color) {
            return new GraniteTextMessageBuilder(content, children, color, style, clickAction, hoverAction, shiftClickAction);
        }

        @Override
        public MessageBuilder<String> style(TextStyle... styles) {
            return new GraniteTextMessageBuilder(content, children, color, style, clickAction, hoverAction, shiftClickAction);
        }

        @Override
        public MessageBuilder<String> onClick(ClickAction<?> action) {
            return new GraniteTextMessageBuilder(content, children, color, style, Optional.<ClickAction<?>>fromNullable(action), hoverAction, shiftClickAction);
        }

        @Override
        public MessageBuilder<String> onHover(HoverAction<?> action) {
            return new GraniteTextMessageBuilder(content, children, color, style, clickAction, Optional.<HoverAction<?>>fromNullable(action), shiftClickAction);
        }

        @Override
        public MessageBuilder<String> onShiftClick(ShiftClickAction<?> action) {
            return new GraniteTextMessageBuilder(content, children, color, style, clickAction, hoverAction, Optional.<ShiftClickAction<?>>fromNullable(action));
        }

        @Override
        public Message<String> build() {
            return new GraniteMessage.GraniteText();
        }
    }

    // TODO: The other three types (translation, score and selector)
}
