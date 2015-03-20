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

package org.granitepowered.granite.bytecode.classes;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.Proxy;
import org.granitepowered.granite.bytecode.ProxyCallbackInfo;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.event.entity.GraniteEntityMoveEvent;
import org.granitepowered.granite.mc.MCEntity;
import org.granitepowered.granite.mc.MCEntityPlayerMP;
import org.spongepowered.api.world.Location;

public class EntityClass extends BytecodeClass {

    public EntityClass() {
        super("Entity");
    }

    @Proxy(methodName = "moveEntity")
    public Object moveEntity(ProxyCallbackInfo<MCEntity> info) throws Throwable {
        if (!(info.getCaller() instanceof MCEntityPlayerMP)) {
            double oldX, oldY, oldZ;
            oldX = info.getCaller().fieldGet$posX();
            oldY = info.getCaller().fieldGet$posY();
            oldZ = info.getCaller().fieldGet$posZ();

            info.callback();

            double newX, newY, newZ;
            newX = info.getCaller().fieldGet$posX();
            newY = info.getCaller().fieldGet$posY();
            newZ = info.getCaller().fieldGet$posZ();

            if (oldX != newX || oldY != newY || oldZ != newZ) {
                GraniteEntity entity = wrap(info.getCaller());

                GraniteEntityMoveEvent event = new GraniteEntityMoveEvent(
                        entity,
                        new Location(entity.getWorld(), new Vector3d(oldX, oldY, oldZ)),
                        new Location(entity.getWorld(), new Vector3d(newX, newY, newZ))
                );

                Granite.getInstance().getEventManager().post(event);

                if (event.isCancelled()) {
                    entity.setLocation(entity.getLocation().setPosition(new Vector3d(oldX, oldY, oldZ)));
                }
            }
        } else {
            info.callback();
        }
        return null;
    }
}
