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

package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityWitch;
import org.spongepowered.api.entity.living.monster.Witch;
import org.spongepowered.api.entity.projectile.Projectile;

public class GraniteEntityWitch extends GraniteEntityMonster<MCEntityWitch> implements Witch {

    public GraniteEntityWitch(MCEntityWitch obj) {
        super(obj);
    }

    @Override
    public boolean isAggressive() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(21).fieldGet$watchedObject() == 1;
    }

    @Override
    public void setAggressive(boolean aggressive) {
        obj.fieldGet$dataWatcher().updateObject(21, aggressive ? (byte) 1 : (byte) 0);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass, Vector3f vector3f) {
        throw new NotImplementedException("");
    }
}
