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

import mc.MCEntityCreeper;
import org.spongepowered.api.entity.living.monster.Creeper;

public class GraniteEntityCreeper extends GraniteEntityMonster<MCEntityCreeper> implements Creeper {

    public GraniteEntityCreeper(MCEntityCreeper obj) {
        super(obj);
    }

    @Override
    public boolean isPowered() {
        return (byte) obj.dataWatcher.getWatchedObject(17).watchedObject == 1;
    }

    @Override
    public void setPowered(boolean powered) {
        obj.dataWatcher.updateObject(17, powered ? (byte) 1 : (byte) 0);
    }

    @Override
    public int getExplosionRadius() {
        return obj.explosionRadius;
    }

    @Override
    public void setExplosionRadius(int explosionRadius) {
        obj.explosionRadius = explosionRadius;
    }

    @Override
    public void ignite() {
        obj.dataWatcher.updateObject(18, (byte) 1);
    }

    @Override
    public void ignite(int fuseTime) {
        obj.lastActiveTime = 0;
        obj.fuseTime = fuseTime;
        ignite();
    }

    @Override
    public int getFuseDuration() {
        return obj.fuseTime - obj.lastActiveTime;
    }

    @Override
    public void setFuseDuration(int fuseDuration) {
        obj.timeSinceIgnited = (obj.fuseTime - fuseDuration);
    }

    @Override
    public void detonate() {
        obj.detonate();
    }
}
