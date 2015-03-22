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

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import mc.MCEntityTameable;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.Sittable;
import org.spongepowered.api.entity.living.Tameable;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.living.animal.Wolf;

public class GraniteEntityTameable<T extends MCEntityTameable> extends GraniteEntityAnimal<T> implements Sittable, Tameable {

    public GraniteEntityTameable(T obj) {
        super(obj);
    }

    @Override
    public boolean isSitting() {
        return ((byte) obj.dataWatcher.getWatchedObject(16).watchedObject & 1) != 0;
    }

    @Override
    public void setSitting(boolean sitting) {
        byte isSitting = (byte) obj.dataWatcher.getWatchedObject(16).watchedObject;
        obj.dataWatcher.updateObject(16, sitting ? Byte.valueOf((byte) (isSitting | 1)) : Byte.valueOf((byte) (isSitting | -2)));
    }

    @Override
    public boolean isTamed() {
        if (this instanceof Horse) {
            return ((int) obj.dataWatcher.getWatchedObject(16).watchedObject & 2) != 0;
        } else {
            return ((byte) obj.dataWatcher.getWatchedObject(16).watchedObject & 4) != 0;
        }
    }

    @Override
    public void setTamed(boolean tamed) {
        if (this instanceof Horse) {
            obj.dataWatcher.updateObject(2, tamed);
        } else {
            byte isTamed = (byte) obj.dataWatcher.getWatchedObject(16).watchedObject;
            obj.dataWatcher.updateObject(16, tamed ? Byte.valueOf((byte) (isTamed | 4)) : Byte.valueOf((byte) (isTamed & -5)));
            if (this instanceof Wolf) {
                // TODO: Add SharedMonsterAttributes (Base health of 8 when false, Base health of 20 when true)
                // TODO: Set attack damage to 4
            }
        }
    }

    @Override
    public Optional<Tamer> getOwner() {
        return Optional.fromNullable((Tamer) Granite.getInstance().getServer().get()
                .getPlayer((String) obj.dataWatcher.getWatchedObject(21).watchedObject));
    }

    @Override
    public void setOwner(Tamer tamer) {
        obj.dataWatcher.updateObject(21, tamer.getUniqueId().toString());
        setTamed(true);
    }
}
