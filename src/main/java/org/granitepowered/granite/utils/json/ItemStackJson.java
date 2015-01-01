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

import com.google.gson.*;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCItemStack;
import org.granitepowered.granite.mc.MCNBTTagCompound;

import java.lang.reflect.Type;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class ItemStackJson implements JsonSerializer<GraniteItemStack>, JsonDeserializer<GraniteItemStack> {
    @Override
    public GraniteItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String nbtString = json.getAsString();
        MCNBTTagCompound nbt = (MCNBTTagCompound) Mappings.invokeStatic("JsonToNBT", "func_180713_a", nbtString);

        return wrap((MCItemStack) Mappings.invokeStatic("ItemStack", "loadItemStackFromNBT", nbt));
    }

    @Override
    public JsonElement serialize(GraniteItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        try {
            Object nbt = Mappings.invoke(src.obj, "writeToNBT", Mappings.getClass("NBTTagCompound").newInstance());
            return new JsonPrimitive(nbt.toString());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
