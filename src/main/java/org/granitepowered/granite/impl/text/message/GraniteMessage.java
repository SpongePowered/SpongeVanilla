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
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import org.apache.commons.lang3.NotImplementedException;
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

import java.util.List;

public abstract class GraniteMessage<T> implements Message {

    T content;
    ImmutableList<Message> children;
    TextColor color;
    TextStyle style;
    Optional<ClickAction<?>> clickAction;
    Optional<HoverAction<?>> hoverAction;
    Optional<ShiftClickAction<?>> shiftClickAction;

    GraniteMessage() {
        children = ImmutableList.<Message>builder().build();
        color = TextColors.RESET;
        style = TextStyles.RESET;
        clickAction = Optional.absent();
        hoverAction = Optional.absent();
        shiftClickAction = Optional.absent();
    }

    GraniteMessage(ImmutableList<Message> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction,
                   Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction) {
        this.children = children;
        this.color = color;
        this.style = style;
        this.clickAction = clickAction;
        this.hoverAction = hoverAction;
        this.shiftClickAction = shiftClickAction;
    }

    @Override
    public T getContent() {
        return content;
    }

    @Override
    public TextColor getColor() {
        return color;
    }

    @Override
    public TextStyle getStyle() {
        return style;
    }

    @Override
    public List<Message> getChildren() {
        return children;
    }

    @Override
    public Optional<ClickAction<?>> getClickAction() {
        return clickAction;
    }

    @Override
    public Optional<HoverAction<?>> getHoverAction() {
        return hoverAction;
    }

    @Override
    public Optional<ShiftClickAction<?>> getShiftClickAction() {
        return shiftClickAction;
    }

    public static class GraniteText extends GraniteMessage<String> implements Message.Text {

        public GraniteText(ImmutableList<Message> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction,
                           Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction, String text) {
            super(children, color, style, clickAction, hoverAction, shiftClickAction);
            this.content = text;
        }

        @Override
        public Iterable<Message> withChildren() {
            // I still have no idea what I'm doing
            return (Iterable<Message>) Iterators.concat(Iterators.singletonIterator(this), Iterables.concat(children).iterator());
        }

        @Override
        public MessageBuilder.Text builder() {
            return new GraniteMessageBuilder.GraniteTextMessageBuilder(getContent(), children, color, style, clickAction, hoverAction,
                                                                       shiftClickAction);
        }

        @Override
        public String toLegacy() {
            throw new NotImplementedException("");
        }

        @Override
        public String toLegacy(char c) {
            throw new NotImplementedException("");
        }
    }

    public static class GraniteTranslatable extends GraniteMessage<Translation> implements Message.Translatable {

        List<Object> arguments;

        public GraniteTranslatable(ImmutableList<Message> children, TextColor color, TextStyle style, Optional<ClickAction<?>> clickAction,
                                   Optional<HoverAction<?>> hoverAction, Optional<ShiftClickAction<?>> shiftClickAction, Translation translation,
                                   List<Object> arguments) {
            super(children, color, style, clickAction, hoverAction, shiftClickAction);
            this.content = translation;
            this.arguments = arguments;
        }

        @Override
        public List<Object> getArguments() {
            return arguments;
        }

        @Override
        public Iterable<Message> withChildren() {
            // I still have no idea what I'm doing
            return (Iterable<Message>) Iterators.concat(Iterators.singletonIterator(this), Iterables.concat(children).iterator());
        }

        @Override
        public MessageBuilder.Translatable builder() {
            return new GraniteMessageBuilder.GraniteTranslatableMessageBuilder(children, color, style, clickAction, hoverAction, shiftClickAction,
                                                                               content, arguments.toArray(new Object[arguments.size()]));
        }

        @Override
        public String toLegacy() {
            throw new NotImplementedException("");
        }

        @Override
        public String toLegacy(char c) {
            throw new NotImplementedException("");
        }
    }

    // TODO: The other two types (score and selector)
}
