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
import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.event.block.BlockBreakEvent;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.Method;

public class PlayServerComposite extends ProxyComposite {
    public PlayServerComposite(GraniteServerComposite server, Object networkManager, GranitePlayer entityPlayer) {
        super(Mappings.getClass("n.m.network.NetHandlerPlayServer"), new Class[]{
                Mappings.getClass("n.m.server.MinecraftServer"),
                Mappings.getClass("n.m.network.NetworkManager"),
                Mappings.getClass("n.m.entity.player.EntityPlayerMP")
        }, server.parent, networkManager, entityPlayer.parent);

        addHook("func_147345_a(n.m.network.play.client.C07PacketPlayerDigging)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                if (!GraniteServerComposite.instance.isOnServerThread()) {
                    Player p = (Player) MinecraftUtils.wrap(fieldGet("playerEntity"));

                    World w = (World) MinecraftUtils.wrap(GraniteServerComposite.instance.worldServerForDimension(p.getDimension()));

                    Block b = ((GraniteWorld) w).getBlock(Mappings.invoke(args[0], args[0].getClass(), "getPosition()"));

                    BlockBreakEvent event = new BlockBreakEvent(b, p);
                    Granite.getEventQueue().fireEvent(event);

                    if (event.isCancelled()) {
                        hook.setWasHandled(true);
                        ((GranitePlayer) p).sendBlockUpdate(b);
                        return false;
                    }
                }
                return null;
            }
        });
    }
}
