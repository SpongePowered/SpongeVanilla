package org.granitemc.granite.reflect;

/*****************************************************************************************
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
 ****************************************************************************************/

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.event.player.PlayerJoinEvent;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SCMComposite extends ProxyComposite {
    public SCMComposite(GraniteServerComposite server) {
        super(Mappings.getClass("n.m.server.dedicated.DedicatedPlayerList"), new Class[]{Mappings.getClass("n.m.server.dedicated.DedicatedServer")}, server.parent);

        addHook("initializeConnectionToPlayer(n.m.network.NetworkManager;n.m.entity.player.EntityPlayerMP)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                Player p = (Player) MinecraftUtils.wrap(args[1]);

                PlayerJoinEvent event = new PlayerJoinEvent(p);
                Granite.getEventQueue().fireEvent(event);

                PlayServerComposite psc = new PlayServerComposite(GraniteServerComposite.instance, args[0], (GranitePlayer) p);

                try {
                    Field f = self.getClass().getSuperclass().getSuperclass().getDeclaredField("_playServerArgument");
                    f.setAccessible(true);
                    f.set(self, psc.parent);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        addHook("func_148545_a(com.mojang.authlib.GameProfile)", new HookListener() {
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
