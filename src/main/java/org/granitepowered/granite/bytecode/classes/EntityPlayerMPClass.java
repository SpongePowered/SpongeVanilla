/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

import javassist.CannotCompileException;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.event.player.GranitePlayerDeathEvent;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCChatComponent;
import org.granitepowered.granite.mc.MCDamageSource;
import org.granitepowered.granite.mc.MCEntityPlayerMP;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.text.message.Message;

import java.lang.reflect.Field;

public class EntityPlayerMPClass extends BytecodeClass {

    public EntityPlayerMPClass() {
        super("EntityPlayerMP");

        final Field[] deathMessageField = new Field[1];
        injectField(Mappings.getCtClass("IChatComponent"), "deathMessage", new BytecodeClass.PostCallback() {
            @Override
            public void callback() {
                try {
                    deathMessageField[0] = Mappings.getClass("EntityPlayerMP").getDeclaredField("deathMessage");
                    deathMessageField[0].setAccessible(true);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });

        instrumentMethod("onDeath", new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                try {
                    if (m.getMethod().equals(Mappings.getCtMethod("CombatTracker", "func_151521_b"))) {
                        m.replace("$_ = this.deathMessage;");
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        proxy("onDeath", new BytecodeClass.ProxyHandler() {
            @Override
            protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                GranitePlayer player = new GranitePlayer((MCEntityPlayerMP) caller);
                MCDamageSource source = (MCDamageSource) args[0];

                MCChatComponent deathComponent = ((MCEntityPlayerMP) caller).fieldGet$_combatTracker().func_151521_b();
                Message deathMessage = MinecraftUtils.minecraftToGraniteMessage(deathComponent);

                GranitePlayerDeathEvent deathEvent = new GranitePlayerDeathEvent(player, source, deathMessage);
                Granite.getInstance().getEventManager().post(deathEvent);

                deathMessageField[0].set(null, MinecraftUtils.graniteToMinecraftChatComponent(deathEvent.getDeathMessage()));

                return callback.invokeParent(args);
            }
        });
    }
}
