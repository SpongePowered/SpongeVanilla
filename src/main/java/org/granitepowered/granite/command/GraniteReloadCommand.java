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

package org.granitepowered.granite.command;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.plugin.GranitePluginManager;
import org.granitepowered.granite.impl.service.event.GraniteEventManager;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.plugin.PluginContainer;

public class GraniteReloadCommand
{

    public static void onCommand(){
        GraniteEventManager handler = (GraniteEventManager) Granite.getInstance().getEventManager();
        GranitePluginManager manager = (GranitePluginManager) Granite.getInstance().getPluginManager();
        for (PluginContainer pluginContainer : manager.getPlugins()){
            handler.unregister(pluginContainer.getInstance());
        }

        manager.unloadPlugins();

        manager.loadPlugins();

        for (Player p : Granite.getInstance().getServer().getOnlinePlayers()){

                //todo: send "Server reloaded" message to operators. p.isOP() Not yet implemented in SpongeAPI (pr #340)

        }

        Granite.getInstance().getLogger().info("\u001b[1;32mServer reloaded\u001b[m"); //Woo fancy green text :P

    }

}
