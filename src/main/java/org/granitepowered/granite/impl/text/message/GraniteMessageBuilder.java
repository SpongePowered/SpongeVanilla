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

import java.util.Arrays;
import java.util.List;

public abstract class GraniteMessageBuilder<T> implements MessageBuilder<T> {
    T content;
    List<Message<?>> children;
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

    @Override
    public MessageBuilder<T> color(TextColor color) {
        this.color = color;
        return this;
    }

    @Override
    public MessageBuilder<T> style(TextStyle... styles) {
        this.style = TextStyles.of(styles);
        return this;
    }

    @Override
    public MessageBuilder<T> onClick(ClickAction<?> action) {
        this.clickAction = Optional.<ClickAction<?>>fromNullable(action);
        return this;
    }

    @Override
    public MessageBuilder<T> onHover(HoverAction<?> action) {
        this.hoverAction = Optional.<HoverAction<?>>fromNullable(action);
        return this;
    }

    @Override
    public MessageBuilder<T> onShiftClick(ShiftClickAction<?> action) {
        this.shiftClickAction = Optional.<ShiftClickAction<?>>fromNullable(action);
        return this;
    }

    @Override
    public MessageBuilder<T> append(Message<?>... children) {
        return append(Arrays.asList(children));
    }

    @Override
    public MessageBuilder<T> append(Iterable<Message<?>> children) {
        this.children.addAll(Lists.newArrayList(children));
        return this;
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
        public MessageBuilder<String> content(String content) {
            this.content = content;
            return this;
        }

        @Override
        public Message<String> build() {
            GraniteMessage.GraniteText text = new GraniteMessage.GraniteText(ImmutableList.copyOf(children), color, style, clickAction, hoverAction, shiftClickAction, content);
            return text;
        }
    }

    // TODO: The other three types (translation, score and selector)
}
