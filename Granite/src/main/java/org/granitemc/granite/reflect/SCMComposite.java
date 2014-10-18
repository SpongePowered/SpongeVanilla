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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.reflect;

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.event.player.EventPlayerJoin;
import org.granitemc.granite.api.event.player.EventPlayerQuit;
import org.granitemc.granite.entity.player.GraniteEntityPlayer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SCMComposite extends ProxyComposite {
    public SCMComposite(GraniteServerComposite server) {
        super(Mappings.getClass("DedicatedPlayerList"), new Class[]{Mappings.getClass("DedicatedServer")}, server.parent);

        addHook("initializeConnectionToPlayer", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                Player p = (Player) MinecraftUtils.wrap(args[1]);

                PlayServerComposite psc = new PlayServerComposite(GraniteServerComposite.instance, args[0], (GraniteEntityPlayer) p);

                try {
                    Field f = self.getClass().getSuperclass().getSuperclass().getDeclaredField("_playServerArgument");
                    f.setAccessible(true);
                    f.set(self, psc.parent);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                try {
                    proxyCallback.invoke(self, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                hook.setWasHandled(true);

                EventPlayerJoin event = new EventPlayerJoin(p);
                Granite.getEventQueue().fireEvent(event);

                return null;
            }
        });

        addHook("playerLoggedOut", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                Player player = (Player) MinecraftUtils.wrap(args[0]);

                EventPlayerQuit event = new EventPlayerQuit(player);
                Granite.getEventQueue().fireEvent(event);
                return null;
            }
        });

        addHook("func_148545_a", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                Object world = GraniteServerComposite.instance.worldServerForDimension(0);
                ItemInWorldComposite iiw = new ItemInWorldComposite(world);

                try {
                    Field f = self.getClass().getSuperclass().getSuperclass().getDeclaredField("_itemInWorldArgument");
                    f.setAccessible(true);
                    f.set(self, iiw.parent);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
