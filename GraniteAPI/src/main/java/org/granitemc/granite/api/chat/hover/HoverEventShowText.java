package org.granitemc.granite.api.chat.hover;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.HoverEvent;
import org.granitemc.granite.api.chat.TextComponent;
import org.json.simple.JSONValue;

public class HoverEventShowText extends HoverEvent {
    ChatComponent text;

    public HoverEventShowText(ChatComponent text) {
        this.text = text;
    }

    @Override
    protected String getAction() {
        return "show_text";
    }

    @Override
    public Object getValue() {
        return JSONValue.parse(text.toJson());
    }

    public ChatComponent getText() {
        return text;
    }

    public void setText(ChatComponent text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = new TextComponent(text);
    }
}
