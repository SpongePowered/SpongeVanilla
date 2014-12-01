package org.granitemc.granite.api.chat;

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

import org.granitemc.granite.api.chat.click.ClickEventChangePage;
import org.granitemc.granite.api.chat.click.ClickEventOpenURL;
import org.granitemc.granite.api.chat.click.ClickEventRunCommand;
import org.granitemc.granite.api.chat.click.ClickEventSuggestCommand;
import org.granitemc.granite.api.chat.hover.HoverEventShowAchievement;
import org.granitemc.granite.api.chat.hover.HoverEventShowEntity;
import org.granitemc.granite.api.chat.hover.HoverEventShowItem;
import org.granitemc.granite.api.chat.hover.HoverEventShowText;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.item.ItemStack;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Stack;

public class ChatComponentBuilder {
    ChatComponent root;
    ChatComponent component;

    boolean currentBold;
    boolean currentItalic;
    boolean currentUnderlined;
    boolean currentStrikethrough;
    boolean currentObfuscated;

    ChatColor currentColor;
    Stack<ChatColor> oldColors;
    HoverEvent currentHover;
    ClickEvent currentClick;

    public ChatComponentBuilder() {
        root = component = new TextComponent("");
        currentColor = ChatColor.WHITE;
        oldColors = new Stack<>();
    }

    public ChatComponentBuilder text(String text) {
        return component(new TextComponent(text));
    }

    public ChatComponentBuilder translation(String key, String... args) {
        return component(new TranslateComponent(key, ChatComponent.wrapStrings(args)));
    }

    public ChatComponentBuilder translation(String key, ChatComponent... args) {
        return component(new TranslateComponent(key, args));
    }

    public ChatComponentBuilder component(ChatComponent newComponent) {
        component = root.addChild(newComponent);
        component.setBold(currentBold);
        component.setItalic(currentItalic);
        component.setUnderlined(currentUnderlined);
        component.setStrikethrough(currentStrikethrough);
        component.setObfuscated(currentObfuscated);
        component.setColor(currentColor);
        component.setHoverEvent(currentHover);
        component.setClickEvent(currentClick);
        return this;
    }

    public ChatComponentBuilder componentRaw(ChatComponent newComponent) {
        component = root.addChild(newComponent);
        return this;
    }

    public ChatComponentBuilder color(ChatColor color) {
        oldColors.push(currentColor);
        currentColor = color;
        return this;
    }

    public ChatComponentBuilder lastColor() {
        currentColor = oldColors.pop();
        return this;
    }

    public ChatComponentBuilder bold(boolean bold) {
        currentBold = bold;
        return this;
    }

    public ChatComponentBuilder italic(boolean italic) {
        currentItalic = italic;
        return this;
    }

    public ChatComponentBuilder underlined(boolean underlined) {
        currentUnderlined = underlined;
        return this;
    }

    public ChatComponentBuilder strikethrough(boolean strikethrough) {
        currentStrikethrough = strikethrough;
        return this;
    }

    public ChatComponentBuilder obfuscated(boolean obfuscated) {
        currentObfuscated = obfuscated;
        return this;
    }

    public ChatComponentBuilder clickEvent(ClickEvent clickEvent) {
        currentClick = clickEvent;
        return this;
    }

    public ChatComponentBuilder hoverEvent(HoverEvent hoverEvent) {
        currentHover = hoverEvent;
        return this;
    }

    public ChatComponentBuilder url(String url) {
        try {
            currentClick = new ClickEventOpenURL(new URL(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ChatComponentBuilder url(URL url) {
        currentClick = new ClickEventOpenURL(url);
        return this;
    }

    public ChatComponentBuilder page(int page) {
        currentClick = new ClickEventChangePage(page);
        return this;
    }

    public ChatComponentBuilder command(String command) {
        currentClick = new ClickEventRunCommand(command);
        return this;
    }

    public ChatComponentBuilder suggest(String command) {
        currentClick = new ClickEventSuggestCommand(command);
        return this;
    }

    public ChatComponentBuilder showText(String text) {
        currentHover = new HoverEventShowText(new TextComponent(text));
        return this;
    }

    public ChatComponentBuilder showText(ChatComponent text) {
        currentHover = new HoverEventShowText(text);
        return this;
    }

    public ChatComponentBuilder showItem(ItemStack item) {
        currentHover = new HoverEventShowItem(item);
        return this;
    }

    public ChatComponentBuilder showEntity(Entity entity) {
        currentHover = new HoverEventShowEntity(entity);
        return this;
    }

    public ChatComponentBuilder showAchievement(String achievementName) {
        currentHover = new HoverEventShowAchievement(achievementName);
        return this;
    }

    public ChatComponentBuilder clear() {
        clearClick();
        clearHover();
        clearStyle();
        return this;
    }

    public ChatComponentBuilder clearHover() {
        currentHover = null;
        return this;
    }

    public ChatComponentBuilder clearClick() {
        currentClick = null;
        return this;
    }

    public ChatComponentBuilder clearStyle() {
        currentBold = false;
        currentItalic = false;
        currentUnderlined = false;
        currentStrikethrough = false;
        currentObfuscated = false;
        currentColor = ChatColor.WHITE;
        return this;
    }

    public ChatComponent build() {
        return root;
    }
}
