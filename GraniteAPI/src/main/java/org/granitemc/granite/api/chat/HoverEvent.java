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

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.chat.hover.HoverEventShowAchievement;
import org.granitemc.granite.api.chat.hover.HoverEventShowEntity;
import org.granitemc.granite.api.chat.hover.HoverEventShowItem;
import org.granitemc.granite.api.chat.hover.HoverEventShowText;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.item.ItemType;
import org.granitemc.granite.api.item.ItemTypes;
import org.granitemc.granite.api.nbt.NBTCompound;
import org.json.simple.JSONObject;

import java.util.Objects;

public abstract class HoverEvent {
    protected abstract String getAction();

    public abstract Object getValue();

    JSONObject toConfigObject() {
        JSONObject obj = new JSONObject();
        obj.put("action", getAction());
        obj.put("value", getValue());

        return obj;
    }

    static HoverEvent fromConfigObject(JSONObject obj) {
        String action = (String) obj.get("action");

        if (Objects.equals(action, "show_achievement")) {
            return new HoverEventShowAchievement((String) obj.get("value"));
        } else if (Objects.equals(action, "show_entity")) {
            String value = (String) obj.get("value");

            NBTCompound nbt = Granite.getAPIHelper().getNBTFromString(value);

            String type = nbt.getString("type");
            String name = nbt.getString("name");
            int id = nbt.getInt("id");

            return new HoverEventShowEntity(type, name, id);
        } else if (Objects.equals(action, "show_item")) {
            String value = (String) obj.get("value");

            NBTCompound nbt = Granite.getAPIHelper().getNBTFromString(value);

            ItemType type = ItemTypes.getByName(nbt.getString("id").split(":")[1]);

            ItemStack stack = type.create(1);
            stack.setItemDamage(nbt.getInt("Damage"));
            stack.setNBTCompound(nbt.getNBTCompound("tag"));

            return new HoverEventShowItem(stack);
        } else if (Objects.equals(action, "show_text")) {
            return new HoverEventShowText(ChatComponent.fromConfigObject((JSONObject) obj.get("value")));
        }
        return null;
    }
}
