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

package org.granitepowered.granite.impl.entity.living;

import org.granitepowered.granite.mc.MCEntityBat;
import org.spongepowered.api.entity.living.Bat;

public class GraniteEntityBat extends GraniteEntityAmbientCreature<MCEntityBat> implements Bat {

    public GraniteEntityBat(MCEntityBat obj) {
        super(obj);
    }

    @Override
    public boolean isAwake() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 1) != 0;
    }

    @Override
    public void setAwake(boolean awake) {
        byte isAwake = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        if (awake) {
            obj.fieldGet$dataWatcher().updateObject(16, Byte.valueOf((byte) (isAwake | 1)));
        } else {
            obj.fieldGet$dataWatcher().updateObject(16, Byte.valueOf((byte) (isAwake & -2)));
        }
    }
}
