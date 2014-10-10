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
import org.granitemc.granite.api.chat.ChatComponentText;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteChatComponentText extends Composite implements ChatComponentText {
    public GraniteChatComponentText(String text) {
        super(Mappings.getClass("ChatComponentText"), new Class[]{String.class}, text);
    }

    public GraniteChatComponentText(Object parent, Object... args) {
        super(parent);
    }

    public ChatComponent add(String text) {
        return (ChatComponent) MinecraftUtils.wrap(invoke("IChatComponent", "appendText", text));
    }

    public ChatComponent add(ChatComponent component) {
        return (ChatComponent) MinecraftUtils.wrap(invoke("IChatComponent", "appendSibling", component));
    }

    public String getText() {
        return (String) invoke("IChatComponent", "getUnformattedTextForChat");
    }
}
