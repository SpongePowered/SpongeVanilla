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
package org.spongepowered.server.launch.plugin.asm;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.spongepowered.plugin.meta.PluginMetadata;

import javax.annotation.Nullable;

public final class PluginClassVisitor extends ClassVisitor {

    private static final String PLUGIN_DESCRIPTOR = "Lorg/spongepowered/api/plugin/Plugin;";

    private String className;
    @Nullable private PluginAnnotationVisitor annotationVisitor;

    public PluginClassVisitor() {
        super(ASM5);
    }

    public String getClassName() {
        return this.className;
    }

    public PluginMetadata getMetadata() {
        return this.annotationVisitor != null ? this.annotationVisitor.getMetadata() : null;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
    }

    @Override @Nullable
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (visible && desc.equals(PLUGIN_DESCRIPTOR)) {
            return this.annotationVisitor = new PluginAnnotationVisitor(this.className);
        }

        return null;
    }

}
