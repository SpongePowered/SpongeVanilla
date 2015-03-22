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

package org.granitepowered.granite.impl.entity.living.animal;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import mc.MCEntitySheep;
import org.spongepowered.api.entity.living.animal.Sheep;
import org.spongepowered.api.item.DyeColor;

public class GraniteEntitySheep extends GraniteEntityAnimal<MCEntitySheep> implements Sheep {

    public GraniteEntitySheep(MCEntitySheep obj) {
        super(obj);
    }

    @Override
    public boolean isSheared() {
        return ((byte) obj.dataWatcher.getWatchedObject(16).watchedObject & 16) != 0;
    }

    @Override
    public void setSheared(boolean sheared) {
        byte isSheared = (byte) obj.dataWatcher.getWatchedObject(16).watchedObject;
        obj.dataWatcher.updateObject(16, (byte) (sheared ? isSheared | 16 : isSheared & -17));
    }

    @Override
    public DyeColor getColor() {
        //int color = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 15;
        //return (DyeColor) Granite.instance.getGameRegistry().dyeColors.values().toArray()[color];
        throw new NotImplementedException("");
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        byte currentColor = (byte) obj.dataWatcher.getWatchedObject(16).watchedObject;
        obj.dataWatcher.updateObject(16, ((byte) (currentColor & 240 | ((GraniteMeta) dyeColor).getType()) & 15));
    }
}
