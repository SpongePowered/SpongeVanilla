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
package org.spongepowered.server.launch.transformer.deobf;

import org.objectweb.asm.Type;
import org.spongepowered.asm.mixin.extensibility.IRemapper;

public interface SrgRemapper extends IRemapper {

    @Override
    default String unmap(String typeName) {
        return typeName;
    }

    // Copied from Remapper#mapDesc with references to 'map' replaced with 'unmap'
    @Override
    default String unmapDesc(String desc) {
        Type t = Type.getType(desc);
        switch (t.getSort()) {
            case Type.ARRAY:
                String s = unmapDesc(t.getElementType().getDescriptor());
                StringBuilder sb = new StringBuilder(s.length());
                for (int i = 0; i < t.getDimensions(); ++i) {
                    sb.append('[');
                }
                sb.append(s);
                return sb.toString();
            case Type.OBJECT:
                String newType = unmap(t.getInternalName());
                if (newType != null) {
                    return 'L' + newType + ';';
                }
        }
        return desc;
    }

    default String mapSrgField(String name) {
        return name;
    }

    default String mapSrgMethod(String name) {
        return name;
    }

    // Method name + descriptor
    default String mapSrgMethodIdentifier(String identifier) {
        int pos = identifier.indexOf('(');
        return mapSrgMethod(identifier.substring(0, pos)).concat(identifier.substring(pos));
    }

}
