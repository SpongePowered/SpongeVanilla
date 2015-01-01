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

package org.granitepowered.granite.impl.text.title;

import com.google.common.base.Optional;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.title.Title;
import org.spongepowered.api.text.title.TitleBuilder;

public class GraniteTitleBuilder implements TitleBuilder {

    Optional<Message> title;
    Optional<Message> subtitle;
    Optional<Integer> fadeIn;
    Optional<Integer> stay;
    Optional<Integer> fadeOut;
    boolean clear;
    boolean reset;

    public GraniteTitleBuilder() {
        title = Optional.absent();
        subtitle = Optional.absent();
        fadeIn = Optional.absent();
        stay = Optional.absent();
        fadeOut = Optional.absent();
        clear = reset = true;
    }

    public GraniteTitleBuilder(Optional<Message> title, Optional<Message> subtitle, Optional<Integer> fadeIn, Optional<Integer> stay,
                               Optional<Integer> fadeOut, boolean clear, boolean reset) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
        this.clear = clear;
        this.reset = reset;
    }

    @Override
    public TitleBuilder title(Message message) {
        title = Optional.fromNullable(message);
        return this;
    }

    @Override
    public TitleBuilder subtitle(Message message) {
        subtitle = Optional.fromNullable(message);
        return this;
    }

    @Override
    public TitleBuilder fadeIn(int ticks) {
        fadeIn = Optional.of(ticks);
        return this;
    }

    @Override
    public TitleBuilder stay(int ticks) {
        stay = Optional.of(ticks);
        return this;
    }

    @Override
    public TitleBuilder fadeOut(int ticks) {
        fadeOut = Optional.of(ticks);
        return this;
    }

    @Override
    public TitleBuilder clear() {
        clear = true;
        return this;
    }

    @Override
    public TitleBuilder reset() {
        reset = true;
        return this;
    }

    @Override
    public Title build() {
        return new GraniteTitle(title, subtitle, fadeIn, stay, fadeOut, clear, reset);
    }
}
