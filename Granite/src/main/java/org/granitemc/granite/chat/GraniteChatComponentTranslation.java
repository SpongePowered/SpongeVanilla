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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.chat;

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.chat.ChatComponentTranslation;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteChatComponentTranslation extends Composite implements ChatComponentTranslation {
    public GraniteChatComponentTranslation(String key, Object... values) {
        super(Mappings.getClass("n.m.util.ChatComponentTranslation"), new Class[]{String.class, Object[].class}, key, values);
    }

    public ChatComponent add(String text) {
        return (ChatComponent) MinecraftUtils.wrap(invoke("n.m.util.IChatComponent", "appendText(String)", text));
    }

    public ChatComponent add(ChatComponent component) {
        return (ChatComponent) MinecraftUtils.wrap(invoke("n.m.util.IChatComponent", "appendSibling(String)", component));
    }

    @Override
    public String getText() {
        return (String) invoke("n.m.util.IChatComponent", "getUnformattedTextForChat");
    }

    @Override
    public Object[] getArguments() {
        return (Object[]) fieldGet("formatArgs");
    }

    @Override
    public String getKey() {
        return (String) fieldGet("key");
    }
}
