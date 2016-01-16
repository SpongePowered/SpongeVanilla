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

import com.google.common.collect.ImmutableMap;
import org.spongepowered.server.launch.transformer.deobf.SrgRemapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

final class ClassAccessModifiers {

    final AccessModifier modifier;
    private final AccessModifier fieldModifier;
    private final AccessModifier methodModifier;

    private final ImmutableMap<String, AccessModifier> fields;
    private final ImmutableMap<String, AccessModifier> methods;

    private ClassAccessModifiers(AccessModifier modifier, AccessModifier fieldModifier, AccessModifier methodModifier,
            ImmutableMap<String, AccessModifier> fields, ImmutableMap<String, AccessModifier> methods) {
        this.modifier = modifier;
        this.fieldModifier = fieldModifier;
        this.methodModifier = methodModifier;
        this.fields = fields;
        this.methods = methods;
    }

    AccessModifier getField(String name) {
        AccessModifier modifier = fields.get(name);
        return modifier != null ? modifier : fieldModifier;
    }

    AccessModifier getMethod(String name, String desc) {
        return getMethod(name.concat(desc));
    }

    AccessModifier getMethod(String identifier) {
        AccessModifier modifier = methods.get(identifier);
        return modifier != null ? modifier : methodModifier;
    }

    static final class Builder {
        private AccessModifier modifier;
        private AccessModifier fieldModifier;
        private AccessModifier methodModifier;

        private final Map<String, AccessModifier> fields = new HashMap<>();
        private final Map<String, AccessModifier> methods = new HashMap<>();

        void applyToClass(AccessModifier modifier) {
            this.modifier = modifier.merge(this.modifier);
        }

        void applyToFields(AccessModifier modifier) {
            this.fieldModifier = modifier.merge(this.fieldModifier);
        }

        void applyToMethods(AccessModifier modifier) {
            this.methodModifier = modifier.merge(this.methodModifier);
        }

        void applyToField(String name, AccessModifier modifier) {
            fields.put(name, modifier.merge(fields.get(name)));
        }

        void applyToMethod(String identifier, AccessModifier modifier) {
            methods.put(identifier, modifier.merge(methods.get(identifier)));
        }

        private static ImmutableMap<String, AccessModifier> build(Map<String, AccessModifier> map, AccessModifier base,
                Function<String, String> remapper) {
            ImmutableMap.Builder<String, AccessModifier> builder = ImmutableMap.builder();
            for (Map.Entry<String, AccessModifier> entry : map.entrySet()) {
                String key = remapper.apply(entry.getKey());
                builder.put(key, entry.getValue().merge(base));
            }
            return builder.build();
        }

        ClassAccessModifiers build(SrgRemapper remapper) {
            return new ClassAccessModifiers(this.modifier, this.fieldModifier, this.methodModifier,
                    build(this.fields, this.fieldModifier, remapper::mapSrgField),
                    build(this.methods, this.methodModifier, remapper::mapSrgMethodIdentifier));
        }
    }

}
