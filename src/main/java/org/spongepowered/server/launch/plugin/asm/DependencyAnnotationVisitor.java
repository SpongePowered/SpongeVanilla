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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.objectweb.asm.Opcodes.ASM5;

import org.spongepowered.plugin.meta.PluginDependency;
import org.spongepowered.plugin.meta.PluginMetadata;
import org.spongepowered.server.launch.plugin.InvalidPluginException;

import javax.annotation.Nullable;

final class DependencyAnnotationVisitor extends WarningAnnotationVisitor {

    private final PluginMetadata metadata;

    @Nullable private String id;
    @Nullable private String version;
    private boolean optional;

    DependencyAnnotationVisitor(String className, PluginMetadata metadata) {
        super(ASM5, className);
        this.metadata = metadata;
    }

    @Override
    String getAnnotation() {
        return "@Dependency";
    }

    @Override
    public void visit(String name, Object value) {
        checkNotNull(name, "name");
        switch (name) {
            case "id":
                if (!(value instanceof String)) {
                    throw new InvalidPluginException("Plugin annotation has invalid element 'id'");
                }
                this.id = (String) value;
                return;
            case "version":
                if (!(value instanceof String)) {
                    throw new InvalidPluginException("Plugin annotation has invalid element 'version'");
                }
                this.version = (String) value;
                return;
            case "optional":
                if (!(value instanceof Boolean)) {
                    throw new InvalidPluginException("Plugin annotation has invalid element 'optional'");
                }
                this.optional = (boolean) value;
                return;
            default:
                super.visit(name, value);
        }
    }

    @Override
    public void visitEnd() {
        if (this.id == null) {
            throw new IllegalArgumentException("Dependency plugin ID is required");
        }

        this.metadata.addDependency(new PluginDependency(PluginDependency.LoadOrder.BEFORE, this.id, this.version, this.optional));
    }

}
