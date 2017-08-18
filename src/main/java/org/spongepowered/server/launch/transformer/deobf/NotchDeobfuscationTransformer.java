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

import com.google.common.collect.ImmutableMap;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.server.launch.transformer.deobf.mappings.ClassMappings;
import org.spongepowered.server.launch.transformer.deobf.mappings.CompactMappingsReader;
import org.spongepowered.server.launch.transformer.deobf.mappings.MemberDescriptor;

import java.io.IOException;
import java.net.URL;

public final class NotchDeobfuscationTransformer extends DeobfuscationTransformer implements IClassNameTransformer {

    private static final String MINECRAFT_PACKAGE_PREFIX = "net.minecraft.";

    private final ImmutableMap<String, ClassMappings> mappings;
    private final ImmutableMap<String, String> reverseClasses;

    public NotchDeobfuscationTransformer() throws IOException {
        URL mappings = (URL) Launch.blackboard.get("vanilla.cmap");

        CompactMappingsReader reader = new CompactMappingsReader();
        reader.read(mappings);

        this.mappings = reader.getMappings();
        this.reverseClasses = reader.getReverseClasses();
    }

    @Override
    protected boolean shouldTransformClass(String transformedName) {
        return transformedName.startsWith(MINECRAFT_PACKAGE_PREFIX);
    }

    @Override
    public String map(String className) {
        ClassMappings mappings = this.mappings.get(className);
        if (mappings == null) {
            return className;
        }

        return mappings.getMappedName();
    }

    @Override
    public String unmap(String className) {
        String name = this.reverseClasses.get(className);
        return name != null ? name : className;
    }

    @Override
    public String mapFieldName(String owner, String fieldName, String desc) {
        ClassMappings mappings = this.mappings.get(owner);
        if (mappings == null) {
            return fieldName;
        }

        String mappedName = mappings.getFields().get(new MemberDescriptor(fieldName, desc));
        return mappedName != null ? mappedName : fieldName;
    }

    @Override
    public String mapMethodName(String owner, String methodName, String desc) {
        ClassMappings mappings = this.mappings.get(owner);
        if (mappings == null) {
            return methodName;
        }

        String mappedName = mappings.getMethods().get(new MemberDescriptor(methodName, desc));
        return mappedName != null ? mappedName : methodName;
    }

    @Override
    public String mapSrgMethodIdentifier(String identifier) {
        return identifier;
    }

    @Override
    public String remapClassName(String className) {
        return map(className.replace('.', '/')).replace('/', '.');
    }

    @Override
    public String unmapClassName(String className) {
        return unmap(className.replace('.', '/')).replace('/', '.');
    }

}
