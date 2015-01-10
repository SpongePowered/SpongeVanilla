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

package org.granitepowered.granite.util.json;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.google.common.base.Throwables;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCEntity;
import org.granitepowered.granite.mc.MCWorld;

import java.lang.reflect.Type;
import java.util.Map;

public class EntityJson implements JsonSerializer<GraniteEntity>, JsonDeserializer<GraniteEntity> {

    // Can't be bothered to wrap the proper methods so doing string manipulation
    @Override
    public GraniteEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String nbtString = json.getAsString();
        nbtString = nbtString.replaceAll("\\{", "");
        nbtString = nbtString.replaceAll("\\}", "");

        String[] parts = nbtString.split(",");

        String name = null;
        String type = null;

        for (String part : parts) {
            String[] partParts = part.split(":");
            String key = partParts[0];
            String value = partParts[1].replaceAll("\"", "");

            switch (key) {
                case "name":
                    name = value;
                    break;
                case "type":
                    type = value;
                    break;
            }
        }

        MCWorld world = unwrap(Granite.getInstance().getServer().getWorlds().iterator().next());

        MCEntity entity = (MCEntity) Mappings.invokeStatic("EntityList", "createEntityByName", type != null ? type : "XPOrb", world);
        if (!entity.getName().equals(name)) {
            entity.setCustomNameTag(name);
        }

        return wrap(entity);
    }

    @Override
    public JsonElement serialize(GraniteEntity src, Type typeOfSrc, JsonSerializationContext context) {
        String id = src.getUniqueId().toString();
        try {
            Map<Class<?>, String> map = (Map<Class<?>, String>) Mappings.getField("EntityList", "classToStringMapping").get(null);
            String type = map.get(src.obj.getClass());
            String name = ((MCEntity) unwrap(src)).getName();

            return new JsonPrimitive("{name:\"" + name + "\",id:\"" + id + "\n,type:\"" + type + "\n}");
        } catch (IllegalAccessException e) {
            Throwables.propagate(e);
        }
        return null;
    }
}
