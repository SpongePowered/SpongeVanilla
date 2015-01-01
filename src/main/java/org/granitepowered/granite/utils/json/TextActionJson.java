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

package org.granitepowered.granite.utils.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.impl.text.action.GraniteClickAction;
import org.granitepowered.granite.impl.text.action.GraniteHoverAction;
import org.granitepowered.granite.impl.text.action.GraniteShiftClickAction;
import org.granitepowered.granite.impl.text.action.GraniteTextAction;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.message.Message;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

public class TextActionJson implements JsonSerializer<GraniteTextAction<?>>, JsonDeserializer<GraniteTextAction<?>> {

    @Override
    public GraniteTextAction<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement val = json.getAsJsonObject().get("value");

        switch (json.getAsJsonObject().get("action").getAsString()) {
            case "open_url":
                try {
                    return new GraniteClickAction.GraniteOpenUrl(new URL(val.getAsString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case "run_command":
                return new GraniteClickAction.GraniteRunCommand(val.getAsString());
            case "change_page":
                return new GraniteClickAction.GraniteChangePage(val.getAsInt());
            case "suggest_command":
                return new GraniteClickAction.GraniteSuggestCommand(val.getAsString());

            case "show_text":
                return new GraniteHoverAction.GraniteShowText((Message) context.deserialize(val, GraniteMessage.class));
            case "show_item":
                return new GraniteHoverAction.GraniteShowItem((ItemStack) context.deserialize(val, GraniteItemStack.class));
            case "show_achievement":
                // TODO: add Achievement class
                throw new NotImplementedException("");
            case "show_entity":
                return new GraniteHoverAction.GraniteShowEntity((Entity) context.deserialize(val, GraniteEntity.class));

            case "insertion":
                return new GraniteShiftClickAction.GraniteInsertText(val.getAsString());
        }
        throw new RuntimeException("Could not parse TextAction");
    }

    @Override
    public JsonElement serialize(GraniteTextAction<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject out = new JsonObject();
        out.add("action", context.serialize(src.getId()));

        Type t = null;
        Object result = src.getResult();
        if (result instanceof GraniteEntity) {
            // t must be GraniteEntity, can't be GraniteLiving or another subclass
            t = GraniteEntity.class;
        } else if (result instanceof GraniteMessage) {
            t = GraniteMessage.class;
        } else {
            t = result.getClass();
        }

        out.add("value", context.serialize(src.getResult(), t));
        return out;
    }
}
