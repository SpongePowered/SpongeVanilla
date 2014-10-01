package org.granitemc.granite.api.chat;

/*****************************************************************************************
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
 ****************************************************************************************/

public interface ChatComponentBuilder {
    /**
     * Adds a text string to the end of this {@link org.granitemc.granite.api.chat.ChatComponentBuilder}
     * @param text The text string
     */
    ChatComponentBuilder text(String text);

    /**
     * Adds a translation to the end of this {@link org.granitemc.granite.api.chat.ChatComponentBuilder}
     * @param key The key of the translation
     * @param args The object arguments to pass to the translation
     */
    ChatComponentBuilder translation(String key, Object... args);

    /**
     * Adds a component to the end of this {@link org.granitemc.granite.api.chat.ChatComponentBuilder}
     * @param component The component
     */
    ChatComponentBuilder component(ChatComponent component);

    /**
     * Returns the underlying {@link org.granitemc.granite.api.chat.ChatComponent}
     */
    ChatComponent build();
}
