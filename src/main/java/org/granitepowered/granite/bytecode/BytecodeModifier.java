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

import com.google.common.base.Function;
import com.google.common.reflect.ClassPath;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.text.message.GraniteMessageBuilder;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.Implement;
import org.granitepowered.granite.mc.MCEntityPlayerMP;
import org.granitepowered.granite.utils.BooleanReturner;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.MessageBuilder;
import org.spongepowered.api.text.message.Messages;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.granitepowered.granite.utils.MinecraftUtils.*;

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

            BytecodeClass scm = create("ServerConfigurationManager");
            /*scm.injectField("iCTP$handler", new BooleanReturner() {
                @Override
                public boolean apply() {
                    return false;
                }
            });
            scm.instrumentMethod("initializeConnectionToPlayer", new ExprEditor() {

            });*/
            /*scm.proxy("initializeConnectionToPlayer", new BytecodeClass.ProxyHandler() {
                @Override
                protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                    MCEntityPlayerMP p = (MCEntityPlayerMP) args[1];
                    ((GranitePlayer) wrap(p)).sendMessage(Messages.builder("Hello").color(TextColors.BLUE).style(TextStyles.of(TextStyles.BOLD, TextStyles.ITALIC)).build());

                    return callback.invokeParent(args);
                }
            });z*/
            scm.proxy("playerLoggedIn", new BytecodeClass.ProxyHandler() {
                @Override
                protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                    callback.invokeParent(args);

                    Message.Text hello = Messages.builder(" HELLO").color(TextColors.AQUA).style(TextStyles.BOLD, TextStyles.ITALIC, TextStyles.UNDERLINE, TextStyles.STRIKETHROUGH.negate()).build();

                    Message.Text andnot = Messages.builder("ANDNOT").style(TextStyles.BOLD.andNot(TextStyles.STRIKETHROUGH)).color(TextColors.GOLD).build();

                    Message.Text and = Messages.builder(" AND").style(TextStyles.BOLD, TextStyles.ITALIC, TextStyles.UNDERLINE).color(TextColors.GOLD).build();

                    Message.Text mainmessage = Messages.builder("PIG SAYS").color(TextColors.GREEN).style(TextStyles.STRIKETHROUGH).append(hello).append(andnot).append(and).build();

                    MCEntityPlayerMP p = (MCEntityPlayerMP) args[0];
                    ((GranitePlayer) wrap(p)).sendMessage(mainmessage);
                    // TODO: add player join event here

                    return null;
                }
            });

            for (BytecodeClass bc : bcs.values()) {
                bc.post();
            }
        } catch (IOException | NotFoundException | ClassNotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
    }

    public BytecodeClass create(String className) {
        if (bcs.containsKey(className)) {
            return bcs.get(className);
        }

        BytecodeClass bc = new BytecodeClass(Mappings.getCtClass(className));
        bcs.put(className, bc);
        return bc;
    }
}
