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

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BytecodeModifier {
    // This contains our own mini-mappings
    // Since all this MUST happen before mappings load
    public static final String DedicatedServer = "po";
    public static final String DedicatedServer_StartServer = "i";

    public static final String DedicatedPlayerList = "pn";

    public static final String ServerConfigurationManager = "sn";
    public static final String ServerConfigurationManager_InitializeConnectionToPlayer = "a";
    public static final String ServerConfigurationManager_CreatePlayer = "f";

    public static final String NetworkManager = "gr";

    public static final String EntityPlayerMP = "qw";

    public static final String NetHandlerPlayServer = "rj";

    public static final String GameProfile = "com.mojang.authlib.GameProfile";

    public static final String ItemInWorldManager = "qx";

    public static Set<CtClass> toSave;

    public static void modify() {
        try {
            toSave = new HashSet<>();

            ClassPool pool = new ClassPool(true);

            injectScm(pool);
            injectPlayServer(pool);
            injectItemInWorld(pool);

            for (CtClass cc : toSave) {
                cc.toClass();
            }
        } catch (CannotCompileException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save(CtClass cc) {
        toSave.add(cc);
    }

    public static void injectScm(ClassPool pool) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get(DedicatedServer);
        CtMethod m = cc.getDeclaredMethod(DedicatedServer_StartServer);

        m.instrument(new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException {
                if (Objects.equals(e.getClassName(), DedicatedPlayerList)) {
                    // replace pn constructor with null - we'll set it to something in a proxy later
                    e.replace("$_ = null;");
                }
                super.edit(e);
            }
        });

        // Save everything
        cc.toClass();
    }

    public static void injectPlayServer(ClassPool pool) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get(ServerConfigurationManager);
        // Find the method
        final CtMethod method = cc.getDeclaredMethod(ServerConfigurationManager_InitializeConnectionToPlayer, new CtClass[]{pool.get(NetworkManager), pool.get(EntityPlayerMP)});

        // Add a field to the class (we'll write to that just before this method is called)
        cc.addField(new CtField(pool.get(NetHandlerPlayServer), "_playServerArgument", cc));

        // Modify method to use the field
        method.instrument(new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException {
                if (Objects.equals(e.getClassName(), NetHandlerPlayServer)) {
                    // Replace new rj() with the field
                    e.replace("$_ = _playServerArgument;");
                }
            }
        });

        // Save it
        save(cc);
    }

    public static void injectItemInWorld(ClassPool pool) throws NotFoundException, CannotCompileException {
        CtClass cc = pool.get(ServerConfigurationManager);
        // Find the method
        final CtMethod method = cc.getDeclaredMethod(ServerConfigurationManager_CreatePlayer, new CtClass[]{pool.get(GameProfile)});

        // Add a field to the class (we'll write to that just before this method is called)
        cc.addField(new CtField(pool.get(ItemInWorldManager), "_itemInWorldArgument", cc));

        // Modify method to use the field
        method.instrument(new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException {
                if (Objects.equals(e.getClassName(), ItemInWorldManager)) {
                    // Replace new qx() with the field
                    e.replace("$_ = _itemInWorldArgument;");
                }
            }
        });

        // Save it
        save(cc);
    }
}
