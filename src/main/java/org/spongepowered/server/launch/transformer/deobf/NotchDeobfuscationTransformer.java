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

import static org.objectweb.asm.Opcodes.ASM5;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.Launch;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.spongepowered.server.launch.transformer.deobf.reader.SrgReader;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

public final class NotchDeobfuscationTransformer extends DeobfuscationTransformer implements IClassNameTransformer {

    private final ImmutableBiMap<String, String> classes;
    private final ImmutableTable<String, String, String> rawFields;
    private final ImmutableTable<String, String, String> rawMethods;

    private final Map<String, Map<String, String>> fields;
    private final Map<String, Map<String, String>> methods;

    private final Set<String> loadedClasses = new HashSet<>();

    public NotchDeobfuscationTransformer() throws IOException {
        URL mappings = (URL) Launch.blackboard.get("vanilla.srg_mappings");

        SrgReader reader = new SrgReader();
        reader.read(mappings);

        this.classes = reader.getClasses();
        this.rawFields = reader.getFields();
        this.rawMethods = reader.getMethods();

        this.fields = Maps.newHashMapWithExpectedSize(this.rawFields.size());
        this.methods = Maps.newHashMapWithExpectedSize(this.rawMethods.size());
    }

    @Override
    public String map(String className) {
        String name = this.classes.get(className);

        if (name != null) {
            return name;
        }

        // We may have no name for the inner class directly, but it should be still part of the outer class
        int innerClassPos = className.lastIndexOf('$');
        if (innerClassPos >= 0) {
            return map(className.substring(0, innerClassPos)).concat(className.substring(innerClassPos));
        }

        return className; // Unknown class
    }

    @Override
    public String unmap(String className) {
        String name = this.classes.inverse().get(className);
        if (name != null) {
            return name;
        }

        // We may have no name for the inner class directly, but it should be still part of the outer class
        int innerClassPos = className.lastIndexOf('$');
        if (innerClassPos >= 0) {
            return unmap(className.substring(0, innerClassPos)).concat(className.substring(innerClassPos));
        }

        return className; // Unknown class
    }

    @Override
    public String mapFieldName(String owner, String fieldName, String desc) {
        Map<String, String> fields = getFieldMap(owner);
        if (fields != null) {
            String name = fields.get(fieldName + ':' + desc);
            if (name != null) {
                return name;
            }
        }

        return fieldName;
    }

    @Nullable
    private Map<String, String> getFieldMap(String owner) {
        Map<String, String> result = this.fields.get(owner);
        if (result != null) {
            return result;
        }

        loadSuperMaps(owner);
        return this.fields.get(owner);
    }


    @Override
    public String mapMethodName(String owner, String methodName, String desc) {
        Map<String, String> methods = getMethodMap(owner);
        if (methods != null) {
            String name = methods.get(methodName.concat(desc));
            if (name != null) {
                return name;
            }
        }

        return methodName;
    }

    @Nullable
    private Map<String, String> getMethodMap(String owner) {
        Map<String, String> result = this.methods.get(owner);
        if (result != null) {
            return result;
        }

        loadSuperMaps(owner);
        return this.methods.get(owner);
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


    private void loadSuperMaps(String name) {
        if (this.loadedClasses.contains(name)) {
            return;
        }

        this.loadedClasses.add(name);

        byte[] bytes;
        try {
            bytes = Launch.classLoader.getClassBytes(name);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (bytes != null) {
            ClassReader reader = new ClassReader(bytes);
            createSuperMaps(reader, name, reader.getSuperName(), reader.getInterfaces());
        }
    }

    private void addInheritedMembers(String parent, Map<String, String> fields, Map<String, String> methods) {
        loadSuperMaps(parent);
        Map<String, String> m;
        m = this.fields.get(parent);
        if (m != null) {
            fields.putAll(m);
        }
        m = this.methods.get(parent);
        if (m != null) {
            methods.putAll(m);
        }
    }

    private void createSuperMaps(ClassReader reader, String name, @Nullable String superName, @Nullable String[] interfaces) {
        this.loadedClasses.add(name);

        Map<String, String> fields = new HashMap<>();
        Map<String, String> methods = new HashMap<>();

        if (superName != null) {
            addInheritedMembers(superName, fields, methods);
        }

        if (interfaces != null) {
            for (String parent : interfaces) {
                addInheritedMembers(parent, fields, methods);
            }
        }

        Map<String, String> raw = this.rawFields.row(name);
        if (!raw.isEmpty()) {
            // Resolve field descriptors
            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            for (FieldNode fieldNode : classNode.fields) {
                String newName = raw.get(fieldNode.name);
                if (newName != null) {
                    fields.put(fieldNode.name + ':' + fieldNode.desc, newName);
                }
            }
        }

        methods.putAll(this.rawMethods.row(name));

        this.fields.put(name, ImmutableMap.copyOf(fields));
        this.methods.put(name, ImmutableMap.copyOf(methods));
    }


    @Override
    ClassVisitor createClassRemapper(ClassReader reader, ClassVisitor cv) {
        return new NotchClassRemapper(reader, cv);
    }

    private class NotchClassRemapper extends ClassRemapper {

        private final ClassReader reader;

        private NotchClassRemapper(ClassReader reader, ClassVisitor cv) {
            super(ASM5, cv, NotchDeobfuscationTransformer.this);
            this.reader = reader;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            createSuperMaps(this.reader, name, superName, interfaces);
            super.visit(version, access, name, signature, superName, interfaces);
        }
    }

}
