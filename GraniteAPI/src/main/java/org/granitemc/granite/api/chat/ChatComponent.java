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

import org.fusesource.jansi.Ansi;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class ChatComponent {
    private ChatComponent parent;

    private Boolean bold;
    private Boolean italic;
    private Boolean underlined;
    private Boolean strikethrough;
    private Boolean obfuscated;

    private String insertion;

    private ChatColor color;

    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;

    private List<ChatComponent> children;

    public ChatComponent() {
        insertion = "";
        children = new ArrayList<>();

        bold = italic = underlined = strikethrough = obfuscated = false;
        color = ChatColor.WHITE;
    }

    public ChatComponent getParent() {
        return parent;
    }

    public void setParent(ChatComponent parent) {
        this.parent = parent;
    }

    public boolean getBold() {
        return bold == null ? parent.bold : bold;
    }

    public boolean getItalic() {
        return italic == null ? parent.italic : italic;
    }

    public boolean getUnderlined() {
        return underlined == null ? parent.underlined : underlined;
    }

    public boolean getStrikethrough() {
        return strikethrough == null ? parent.strikethrough : strikethrough;
    }

    public boolean getObfuscated() {
        return obfuscated == null ? parent.obfuscated : obfuscated;
    }

    public ChatColor getColor() {
        return color == null ? parent.color : color;
    }

    public ClickEvent getClickEvent() {
        return clickEvent == null ? parent.clickEvent : clickEvent;
    }

    public HoverEvent getHoverEvent() {
        return hoverEvent == null ? parent.hoverEvent : hoverEvent;
    }

    public boolean isBoldRaw() {
        return bold;
    }

    public ChatComponent setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalicRaw() {
        return italic;
    }

    public ChatComponent setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnderlinedRaw() {
        return underlined;
    }

    public ChatComponent setUnderlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }

    public boolean isStrikethroughRaw() {
        return strikethrough;
    }

    public ChatComponent setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }

    public boolean isObfuscatedRaw() {
        return obfuscated;
    }

    public ChatComponent setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }

    public ChatColor getColorRaw() {
        return color;
    }

    public ChatComponent setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public ClickEvent getClickEventRaw() {
        return clickEvent;
    }

    public ChatComponent setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }

    public HoverEvent getHoverEventRaw() {
        return hoverEvent;
    }

    public ChatComponent setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }

    public String getInsertion() {
        return insertion;
    }

    public ChatComponent setInsertion(String insertion) {
        this.insertion = insertion;
        return this;
    }

    public List<ChatComponent> getChildren() {
        return children;
    }

    public ChatComponent addChild(ChatComponent component) {
        component.setParent(this);
        children.add(component);
        return component;
    }

    public ChatComponent addChild(String text) {
        return addChild(new TextComponent(text));
    }

    private static String getColorizedText(ChatComponent component) {
        String txt = component.getValue();
        Ansi ansi = Ansi.ansi();
        if (component.bold) ansi = ansi.a(Ansi.Attribute.INTENSITY_BOLD);
        if (component.italic) ansi = ansi.a(Ansi.Attribute.ITALIC);
        if (component.underlined) ansi = ansi.a(Ansi.Attribute.UNDERLINE);
        if (component.strikethrough) ansi = ansi.a(Ansi.Attribute.STRIKETHROUGH_ON);
        if (component.obfuscated) ansi = ansi.a(Ansi.Attribute.BLINK_FAST);

        ansi = ansi.reset();
        return ansi.toString();
    }

    public String toPlainText(boolean useAnsiEscapeCodes) {
        String txt = getColorizedText(this);
        for (ChatComponent extra : children) {
            txt += getColorizedText(extra);
        }
        return txt;
    }

    abstract String getValue();

    protected JSONObject toConfigObject() {
        JSONObject obj = new JSONObject();
        obj.put("color", color.getName());
        obj.put("bold", bold);
        obj.put("underlined", underlined);
        obj.put("italic", italic);
        obj.put("strikethrough", strikethrough);
        obj.put("obfuscated", obfuscated);
        if (insertion != null && insertion.length() > 0) obj.put("insertion", insertion);

        List<Object> extras = new ArrayList<>();
        for (ChatComponent extra : children)  {
            extras.add(extra.toConfigObject());
        }

        if (extras.size() > 0) obj.put("extra", extras);
        if (clickEvent != null) obj.put("clickEvent", clickEvent.toConfigObject());
        if (hoverEvent != null) obj.put("hoverEvent", hoverEvent.toConfigObject());
        return obj;
    }

    public String toJson() {
        return toConfigObject().toJSONString();
    }

    static ChatComponent[] wrapStrings(String... strings) {
        ChatComponent[] comps = new ChatComponent[strings.length];
        for (int i = 0; i < comps.length; i++) {
            comps[i] = new TextComponent(strings[i]);
        }
        return comps;
    }

    static String[] unwrapStrings(ChatComponent... components) {
        String[] strings = new String[components.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = components[i].getValue();
        }
        return strings;
    }
}
