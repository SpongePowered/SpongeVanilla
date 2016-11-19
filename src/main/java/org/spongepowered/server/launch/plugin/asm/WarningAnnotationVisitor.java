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

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.AnnotationVisitor;
import org.spongepowered.server.launch.VanillaLaunch;

import javax.annotation.Nullable;

abstract class WarningAnnotationVisitor extends AnnotationVisitor {

    private static final Logger logger = VanillaLaunch.getLogger();

    final String className;

    WarningAnnotationVisitor(int api, String className) {
        super(api);
        this.className = className;
    }

    abstract String getAnnotation();

    @Override
    public void visit(String name, Object value) {
        logger.warn("Found unknown {} annotation element in {}: {} = {}", getAnnotation(), this.className, name, value);
    }

    @Override
    public void visitEnum(String name, String desc, String value) {
        logger.warn("Found unknown {} annotation element in {}: {} ({}) = {}", getAnnotation(), this.className, name, desc, value);
    }

    @Override
    @Nullable
    public AnnotationVisitor visitAnnotation(String name, String desc) {
        logger.warn("Found unknown {} annotation element in {}: {} ({})", getAnnotation(), this.className, name, desc);
        return null;
    }

    @Override
    @Nullable
    public AnnotationVisitor visitArray(String name) {
        logger.warn("Found unknown {} annotation element in {}: {}", getAnnotation(), this.className, name);
        return null;
    }

}
