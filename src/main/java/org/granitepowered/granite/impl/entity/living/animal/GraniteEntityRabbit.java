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

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityRabbit;
import org.spongepowered.api.entity.living.animal.Rabbit;
import org.spongepowered.api.entity.living.animal.RabbitType;
import org.spongepowered.api.text.translation.Translations;

public class GraniteEntityRabbit extends GraniteEntityAnimal<MCEntityRabbit> implements Rabbit {

    public GraniteEntityRabbit(MCEntityRabbit obj) {
        super(obj);
    }

    @Override
    public RabbitType getRabbitType() {
        return Granite.instance.getGameRegistry().rabbitTypes.get(obj.fieldGet$dataWatcher().getWatchedObject(18).fieldGet$watchedObject());
    }

    @Override
    public void setRabbitType(RabbitType rabbitType) {
        int type = ((GraniteMeta) rabbitType).getType();

        if (type == 99) {
            // TODO: Add and Remove AI Tasks
            setCustomName(Translations.of("entity.KillerBunny.name").toString());
        }

        obj.fieldGet$dataWatcher().updateObject(18, (byte) (type << 8));
    }
}
