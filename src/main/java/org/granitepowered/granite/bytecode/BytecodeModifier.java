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

package org.granitepowered.granite.bytecode;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import com.google.common.reflect.ClassPath;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.block.GraniteBlockLoc;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.event.block.GraniteBlockBreakEvent;
import org.granitepowered.granite.impl.event.player.GranitePlayerJoinEvent;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.*;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.Messages;
import org.spongepowered.api.text.translation.Translations;
import org.spongepowered.api.world.Location;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.granitepowered.granite.utils.MinecraftUtils.wrap;

public class BytecodeModifier {
    Map<String, BytecodeClass> bcs;

    public void modify() {
        bcs = new LinkedHashMap<>();

        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClassesRecursive("org.granitepowered.granite.mc")) {
                CtClass ctClass = ClassPool.getDefault().get(classInfo.getName());

                if (ctClass.hasAnnotation(Implement.class)) {
                    CtClass mcClass = Mappings.getCtClass(((Implement) ctClass.getAnnotation(Implement.class)).name());

                    if (mcClass == null) {
                        throw new RuntimeException(((Implement) ctClass.getAnnotation(Implement.class)).name() + " not in mappings");
                    }

                    Granite.getInstance().getLogger().info("Injecting " + ctClass.getSimpleName() + " into " + mcClass.getName());

                    BytecodeClass bc = create(((Implement) ctClass.getAnnotation(Implement.class)).name());

                    //System.out.println(bc);
                    bc.implement(ctClass.toClass());
                }
            }

            BytecodeClass minecraftServer = create("MinecraftServer");
            minecraftServer.replaceMethod("getServerModName", "return \"granite\";");

            modifyScm();
            modifyItemInWorld();

            for (BytecodeClass bc : bcs.values()) {
                bc.writeClass();
            }
        } catch (IOException | NotFoundException | ClassNotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public void modifyScm() {
        BytecodeClass scm = create("ServerConfigurationManager");

        final Field[] loginMessageField = new Field[1];
        scm.injectField(Mappings.getCtClass("IChatComponent"), "loginMessage", new BytecodeClass.PostCallback() {
            @Override
            public void callback() {
                try {
                    loginMessageField[0] = Mappings.getClass("ServerConfigurationManager").getDeclaredField("loginMessage");
                    loginMessageField[0].setAccessible(true);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        });

        scm.instrumentMethod("initializeConnectionToPlayer", new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                CtMethod method = Mappings.getCtMethod("ServerConfigurationManager", "sendChatMsg");
                try {
                    if (m.getMethod().equals(method)) {
                        m.replace("$_ = $proceed(loginMessage);");
                    }
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        scm.proxy("initializeConnectionToPlayer", new BytecodeClass.ProxyHandler() {
            @Override
            protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                MCEntityPlayerMP player = (MCEntityPlayerMP) args[1];

                MCGameProfile newProfile = player.fieldGet$gameProfile();
                MCGameProfile oldProfile = Granite.getInstance().getServer().obj.fieldGet$playerCache().func_152652_a(newProfile.fieldGet$id());

                String oldName = oldProfile == null ? newProfile.fieldGet$name() : oldProfile.fieldGet$name();

                Message.Translatable joinMessage;
                if (!newProfile.fieldGet$name().equals(oldName)) {
                    joinMessage = Messages.builder(Translations.of("multiplayer.player.joined.renamed").get(), newProfile.fieldGet$name(), oldName).color(TextColors.YELLOW).build();
                } else {
                    joinMessage = Messages.builder(Translations.of("multiplayer.player.joined").get(), newProfile.fieldGet$name()).color(TextColors.YELLOW).build();
                }

                GranitePlayerJoinEvent event = new GranitePlayerJoinEvent((GranitePlayer) wrap(player), joinMessage);
                Granite.getInstance().getServer().getEventManager().post(event);

                loginMessageField[0].set(null, MinecraftUtils.graniteToMinecraftChatComponent(event.getJoinMessage()));

                return callback.invokeParent(args);
            }
        });

        scm.proxy("sendChatMsg", new BytecodeClass.ProxyHandler() {
            @Override
            protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                return callback.invokeParent(args);
            }
        });
    }

    public void modifyItemInWorld() {
        BytecodeClass iiwm = create("ItemInWorldManager");

        iiwm.proxy("func_180237_b", new BytecodeClass.ProxyHandler() {
            @Override
            protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                MCItemInWorldManager thisIiwm = (MCItemInWorldManager) caller;

                MCBlockPos mcBlockPos = (MCBlockPos) args[0];
                Vector3d pos = new Vector3d(mcBlockPos.fieldGet$x(), mcBlockPos.fieldGet$y(), mcBlockPos.fieldGet$z());

                GraniteBlockBreakEvent event = new GraniteBlockBreakEvent(new GraniteBlockLoc(new Location((GraniteWorld) wrap(thisIiwm.fieldGet$theWorld()), pos)));
                Granite.getInstance().getServer().getEventManager().post(event);

                return callback.invokeParent(args);
            }
        });
    }

    public BytecodeClass create(String className) {
        if (bcs.containsKey(className)) {
            return bcs.get(className);
        }

        BytecodeClass bc = new BytecodeClass(Mappings.getCtClass(className));
        bcs.put(className, bc);
        return bc;
    }

    public void post() {
        for (BytecodeClass bc : bcs.values()) {
            bc.post();
        }
    }
}
