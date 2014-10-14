package org.granitemc.granite;

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

import org.granitemc.granite.api.APIHelper;
import org.granitemc.granite.api.block.ItemType;
import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteAPIHelper implements APIHelper {
    @Override
    public ItemStack createItemStack(ItemType type, int amount) {
        return new GraniteItemStack(type, amount);
    }

    @Override
    public String getItemNBTCompoundString(ItemStack stack) {
        Object tagCompound = ((GraniteItemStack) stack).invoke("getTagCompound");
        return "{id:minecraft:" + stack.getType().getTechnicalName() + ",Damage:" + stack.getItemDamage() +
                (tagCompound != null ? ",tag:" + tagCompound.toString() : "") + "}";
    }

    @Override
    public String getEntityNBTCompoundString(Entity entity) {
        return getEntityNBTCompoundString(entity.getType(), entity.getCustomNameTag(), entity.getUniqueID().toString());
    }

    @Override
    public String getEntityNBTCompoundString(String type, String name, String id) {
        return "{type:" + type + ",name:" + name + ",id:" + id + "}";
    }

    @Override
    public String getTranslation(String key, Object... args) {
        return MinecraftUtils.getTranslation(key, args);
    }
}
