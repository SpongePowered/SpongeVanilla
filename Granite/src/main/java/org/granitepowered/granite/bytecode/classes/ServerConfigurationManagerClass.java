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

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.*;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.event.entity.living.player.GranitePlayerJoinEvent;
import org.granitepowered.granite.mc.MCEntityPlayerMP;
import org.granitepowered.granite.mc.MCGameProfile;
import org.granitepowered.granite.mc.MCIChatComponent;
import org.granitepowered.granite.mc.MCServerConfigurationManager;
import org.granitepowered.granite.util.MinecraftUtils;
import org.granitepowered.granite.util.ThreadedContainer;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.text.translation.Translations;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class ServerConfigurationManagerClass extends BytecodeClass {

    // Using this here so race conditions don't happen
    // (If one thread runs iCTP, then quickly another, joinMessage is set to the second thread's value
    // And then the first thread runs the sendChatMsg call and gets the second's message
    private ThreadedContainer<MCIChatComponent> joinMessage = new ThreadedContainer<>();

    public ServerConfigurationManagerClass() {
        super("ServerConfigurationManager");
    }

    @MethodCallArgument(methodName = "initializeConnectionToPlayer", methodCallClass = "ServerConfigurationManager", methodCallName = "sendChatMsg", argumentIndex = 0)
    public MCIChatComponent initializeConnectionToPlayerJoinMessage(CallbackInfo<MCServerConfigurationManager> info) {
        return joinMessage.get();
    }

    @Proxy(methodName = "initializeConnectionToPlayer")
    public Object initializeConnectionToPlayer(ProxyCallbackInfo<MCServerConfigurationManager> info) throws Throwable {
        MCEntityPlayerMP player = (MCEntityPlayerMP) info.getArguments()[1];

        MCGameProfile newProfile = player.fieldGet$gameProfile();
        MCGameProfile oldProfile = Granite.getInstance().getServer().obj.fieldGet$playerCache().getProfileByUUID(newProfile.fieldGet$id());

        String oldName = oldProfile == null ? newProfile.fieldGet$name() : oldProfile.fieldGet$name();

        Message.Translatable joinMessage;
        if (!newProfile.fieldGet$name().equals(oldName)) {
            joinMessage =
                    Messages.builder(Translations.of("multiplayer.player.joined.renamed").get(), newProfile.fieldGet$name(), oldName)
                            .color(TextColors.YELLOW).build();
        } else {
            joinMessage =
                    Messages.builder(Translations.of("multiplayer.player.joined").get(), newProfile.fieldGet$name()).color(TextColors.YELLOW)
                            .build();
        }

        GranitePlayerJoinEvent event = new GranitePlayerJoinEvent((GranitePlayer) wrap(player), joinMessage);
        Granite.getInstance().getServer().getEventManager().post(event);

        this.joinMessage.set(MinecraftUtils.graniteToMinecraftChatComponent(event.getJoinMessage()));
        return info.callback(info.getArguments()[0], info.getArguments()[1]);
    }
}
