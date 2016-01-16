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
import net.minecraft.launchwrapper.Launch;
import org.spongepowered.server.launch.transformer.deobf.reader.McpCsvReader;

import java.io.IOException;
import java.nio.file.Path;

public final class SeargeDeobfuscationTransformer extends DeobfuscationTransformer implements SrgRemapper {

    private final ImmutableMap<String, String> fields;
    private final ImmutableMap<String, String> methods;

    public SeargeDeobfuscationTransformer() throws IOException {
        Path mcpDir = (Path) Launch.blackboard.get("vanilla.mcp_mappings");

        McpCsvReader reader = new McpCsvReader();
        reader.read(mcpDir);

        this.fields = reader.getFields();
        this.methods = reader.getMethods();
    }

    @Override
    public String mapSrgField(String name) {
        String result = this.fields.get(name);
        return result != null ? result : name;
    }

    @Override
    public String mapFieldName(String owner, String name, String desc) {
        return mapSrgField(name);
    }

    @Override
    public String mapSrgMethod(String name) {
        String result = this.methods.get(name);
        return result != null ? result : name;
    }

    @Override
    public String mapMethodName(String owner, String name, String desc) {
        return mapSrgMethod(name);
    }

    @Override
    public String mapInvokeDynamicMethodName(String name, String desc) {
        String result = this.methods.get(name);
        return result != null ? result : name;
    }

    // No need to remap these, we don't change class names
    @Override
    public String mapDesc(String desc) {
        return desc;
    }

    @Override
    public String unmapDesc(String desc) {
        return desc;
    }

    @Override
    public String mapType(String type) {
        return type;
    }

    @Override
    public String[] mapTypes(String[] types) {
        return types;
    }

    @Override
    public String mapMethodDesc(String desc) {
        return desc;
    }

    @Override
    public String mapSignature(String signature, boolean typeSignature) {
        return signature;
    }

}
