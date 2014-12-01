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
import org.granitemc.granite.api.Granite;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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

    private static String getColorizedText(ChatComponent component, FormattingOutputType type) {
        String txt = component.getValue();
        if (type == FormattingOutputType.ANSI) {
            Ansi ansi = Ansi.ansi();

            if (component.getColor().isBright()) {
                ansi.fgBright(component.getColor().getAnsiColor());
            } else {
                ansi.fg(component.getColor().getAnsiColor());
            }

            if (component.getBold()) ansi = ansi.a(Ansi.Attribute.INTENSITY_BOLD);
            if (component.getItalic()) ansi = ansi.a(Ansi.Attribute.ITALIC);
            if (component.getUnderlined()) ansi = ansi.a(Ansi.Attribute.UNDERLINE);
            if (component.getStrikethrough()) ansi = ansi.a(Ansi.Attribute.STRIKETHROUGH_ON);
            if (component.getObfuscated()) ansi = ansi.a(Ansi.Attribute.BLINK_FAST);

            ansi = ansi.a(txt);
            ansi = ansi.reset();
            return ansi.toString();
        } else if (type == FormattingOutputType.MINECRAFT) {
            String formatting = "";
            formatting += "\u00A7" + component.getColor().getId();
            if (component.getBold()) formatting += "\u00A7l";
            if (component.getItalic()) formatting += "\u00A7o";
            if (component.getUnderlined()) formatting += "\u00A7n";
            if (component.getStrikethrough()) formatting += "\u00A7m";
            if (component.getObfuscated()) formatting += "\u00A7k";

            return formatting + txt + "\u00A7r";
        }
        return txt;
    }

    public String toPlainText(FormattingOutputType type) {
        String txt = getColorizedText(this, type);
        for (ChatComponent extra : children) {
            txt += getColorizedText(extra, type);
        }
        return txt;
    }

    public static ChatComponent fromJson(String json, boolean lenient) {
        // Minecraft-JSON -> NBT -> Map -> JSON string -> JSON - whew
        JSONObject val;
        if (lenient) {
            val = (JSONObject) JSONValue.parse(JSONValue.toJSONString(Granite.getAPIHelper().getNBTFromString(json).getNBTMap()));
        } else {
            val = (JSONObject) JSONValue.parse(json);
        }

        if (val == null) {
            return new TextComponent(json);
        } else {
            return fromConfigObject(val);
        }
    }

    public static ChatComponent fromConfigObject(JSONObject obj) {
        ChatComponent val = null;

        if (obj.containsKey("text")) {
            val = TextComponent.fromConfigObjectLocal(obj);
        } else if (obj.containsKey("translate")) {
            val = TranslateComponent.fromConfigObjectLocal(obj);
        }

        if (val != null) {
            for (ChatColor color : ChatColor.values()) {
                if (color.getName().equals(obj.get("color"))) {
                    val.setColor(color);
                    break;
                }
            }

            boolean bold, underlined, italic, strikethrough, obfuscated;

            /*bold = obj.containsKey("bold") && (obj.get("bold") instanceof Long ? ((Long) obj.get("bold")) > 0 : (Boolean) obj.get("bold"));
            underlined = obj.containsKey("underlined") && (obj.get("underlined") instanceof Long ? ((Long) obj.get("underlined")) > 0 : (Boolean) obj.get("underlined"));
            italic = obj.containsKey("italic") && (obj.get("italic") instanceof Long ? ((Long) obj.get("italic")) > 0 : (Boolean) obj.get("italic"));
            strikethrough = obj.containsKey("strikethrough") && (obj.get("strikethrough") instanceof Long ? ((Long) obj.get("strikethrough")) > 0 : (Boolean) obj.get("strikethrough"));
            obfuscated = obj.containsKey("obfuscated") && (obj.get("obfuscated") instanceof Long ? ((Long) obj.get("obfuscated")) > 0 : (Boolean) obj.get("obfuscated"));*/

            bold = Boolean.valueOf(String.valueOf(obj.get("bold")));
            underlined = Boolean.valueOf(String.valueOf(obj.get("underlined")));
            italic = Boolean.valueOf(String.valueOf(obj.get("italic")));
            strikethrough = Boolean.valueOf(String.valueOf(obj.get("strikethrough")));
            obfuscated = Boolean.valueOf(String.valueOf(obj.get("obfuscated")));

            val.setBold(bold);
            val.setUnderlined(underlined);
            val.setItalic(italic);
            val.setStrikethrough(strikethrough);
            val.setObfuscated(obfuscated);

            val.setInsertion((String) obj.get("insertion"));

            if (obj.containsKey("clickEvent")) val.setClickEvent(ClickEvent.fromConfigObject((JSONObject) obj.get("clickEvent")));
            if (obj.containsKey("hoverEvent")) val.setHoverEvent(HoverEvent.fromConfigObject((JSONObject) obj.get("hoverEvent")));

            if (obj.containsKey("extra")) {
                for (Object extraObj : (JSONArray) obj.get("extra")) {
                    val.getChildren().add(ChatComponent.fromConfigObject((JSONObject) extraObj));
                }
            }

            return val;
        }
        return null;
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
        for (ChatComponent extra : children) {
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
