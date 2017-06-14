/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.launch.transformer.at;

import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import com.google.common.collect.ImmutableMap;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import javax.annotation.Nullable;

public final class AccessTransformer implements IClassTransformer {

    private final ImmutableMap<String, ClassAccessModifiers> modifiers;

    public AccessTransformer()  {
        this.modifiers = AccessTransformers.build();
    }

    @Override @Nullable
    public byte[] transform(String name, String transformedName, @Nullable byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        ClassAccessModifiers modifiers = this.modifiers.get(transformedName);
        if (modifiers == null) {
            return bytes;
        }

        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(reader, 0);
        reader.accept(new AccessTransformingClassAdapter(writer, modifiers), 0);
        return writer.toByteArray();
    }

    private static class AccessTransformingClassAdapter extends ClassVisitor {

        private final ClassAccessModifiers modifiers;
        @Nullable private String name;

        public AccessTransformingClassAdapter(ClassVisitor cv, ClassAccessModifiers modifiers) {
            super(ASM5, cv);
            this.modifiers = modifiers;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.name = name;

            if (this.modifiers.modifier != null) {
                access = this.modifiers.modifier.apply(access);
            }

            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            AccessModifier modifier = this.modifiers.getField(name);
            if (modifier != null) {
                access = modifier.apply(access);
            }

            return super.visitField(access, name, desc, signature, value);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            AccessModifier modifier = this.modifiers.getMethod(name, desc);
            if (modifier != null) {
                access = modifier.apply(access);
            }

            return new AccessTransformingMethodAdapter(super.visitMethod(access, name, desc, signature, exceptions), this);
        }

    }

    private static class AccessTransformingMethodAdapter extends MethodVisitor {

        private final AccessTransformingClassAdapter owner;

        public AccessTransformingMethodAdapter(MethodVisitor mv, AccessTransformingClassAdapter owner) {
            super(ASM5, mv);
            this.owner = owner;
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (!itf && opcode == INVOKESPECIAL && owner.equals(this.owner.name) && !name.equals("<init>")) {
                // Replace INVOKESPECIAL with INVOKEVIRTUAL to allow overriding (if we change the access)
                AccessModifier modifier = this.owner.modifiers.getMethod(name, desc);
                if (modifier != null && modifier.hasAccessChange()) {
                    opcode = INVOKEVIRTUAL;
                }
            }

            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

}
