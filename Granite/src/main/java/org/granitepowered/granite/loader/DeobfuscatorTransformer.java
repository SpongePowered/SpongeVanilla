/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitepowered.granite.loader;

import javassist.bytecode.AccessFlag;
import javassist.bytecode.Descriptor;
import net.minecraft.launchwrapper.IClassNameTransformer;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class DeobfuscatorTransformer implements IClassTransformer, IClassNameTransformer {

    public static Mappings mappings;
    public static File minecraftJar;
    private static JarFile minecraftJarFile;

    private Map<String, List<String>> superclasses = new HashMap<>();

    @Override
    public String unmapClassName(String name) {
        if (mappings == null) {
            return name;
        }
        if (name.startsWith("mc.")) {
            name = name.split("mc\\.")[1];
        }
        if (mappings.getClasses().containsKey(name)) {
            return mappings.getClasses().get(name);
        } else {
            return name;
        }
    }

    @Override
    public String remapClassName(String name) {
        if (mappings == null) {
            return name;
        }
        if (mappings.getClasses().inverse().containsKey(name)) {
            return "mc." + mappings.getClasses().inverse().get(name);
        } else {
            if (!name.contains(".")) {
                name = "mc." + name;
            }
            return name;
        }
    }

    @Override
    public byte[] transform(String name, final String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return basicClass;
        }

        // If mappings are loaded and initted
        if (mappings != null) {
            ClassReader reader = new ClassReader(basicClass);
            ClassWriter writer = new ClassWriter(0);

            ClassVisitor output = writer;

            // Deobfuscate classes, methods and fields using a RemappingClassAdapter
            output = new RemappingClassAdapter(output, new Remapper() {
                @Override
                public String mapMethodName(String owner, String name, String desc) {
                    return findMappedMethodNameRecursively(owner, name, desc);
                }

                @Override
                public String mapFieldName(String owner, String name, String desc) {
                    return findMappedFieldNameRecursively(owner, name, desc);
                }

                @Override
                public String map(String typeName) {
                    return remapClassName(typeName.replaceAll("/", ".")).replaceAll("\\.", "/");
                }
            });

            // Enough is ENOUGH! I have had it with these motherfuckin' snowmen in these motherfuckin' classes! Everybody strap in! We're 'bout to edit some fuckin' bytecode.
            // PS. Damn you Grum
            output = new ClassVisitor(Opcodes.ASM5, output) {
                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)) {
                        @Override
                        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                            super.visitLocalVariable(name.replaceAll("\u2603", "g"), desc, signature, start, end, index);
                        }
                    };
                }
            };

            // Because fuck access modifiers that's why
            // Make ALL the things public!
            output = new ClassVisitor(Opcodes.ASM5, output) {
                @Override
                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    return super.visitField((access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC, name, desc, signature, value);
                }

                @Override
                public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                    return super.visitMethod((access & ~(Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED)) | Opcodes.ACC_PUBLIC, name, desc, signature, exceptions);
                }
            };

            reader.accept(output, ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }

        return basicClass;
    }

    public List<String> findSuperclasses(String obfClassName) {
        // Load a class, get its superclasses/interfaces and save it in a map

        if (superclasses.containsKey(obfClassName)) {
            return superclasses.get(obfClassName);
        }
        // Find the file in the jar
        JarEntry entry = minecraftJarFile.getJarEntry(obfClassName.replaceAll("\\.", "/") + ".class");

        if (entry != null) {
            try {
                ClassReader reader = new ClassReader(minecraftJarFile.getInputStream(entry));

                final List<String> superclassesList = new ArrayList<>();

                reader.accept(new ClassVisitor(Opcodes.ASM5) {
                    @Override
                    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                        superclassesList.add(superName.replaceAll("/", "."));

                        for (String interface_ : interfaces) {
                            superclassesList.add(interface_.replaceAll("/", "."));
                        }
                    }
                }, 0);

                superclasses.put(obfClassName, superclassesList);
                return superclassesList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    public String findMappedMethodNameRecursively(String owner, String name, String desc) {
        // ASM sometimes uses /s
        owner = owner.replaceAll("/", ".");

        String deobfOwner = remapClassName(owner);
        if (deobfOwner.contains("mc.")) {
            deobfOwner = deobfOwner.split("mc\\.")[1];
        }

        // If the owner is in the mappings
        if (mappings.getMethods().containsKey(deobfOwner)) {
            String key = name + Descriptor.toString(desc) + ":" + Descriptor.toString(desc.split("\\)")[1]);

            // Get the field from the mappings
            if (mappings.getMethods().get(deobfOwner).inverse().containsKey(key)) {
                // Return the mapped name
                return mappings.getMethods().get(deobfOwner).inverse().get(key);
            }
        }

        // If not found, try parent class
        // This is because if a ClassA has a method called foo, which deobfuscates to bar
        // And ClassB extends ClassA and overrides foo, that too needs to be renamed to bar
        // Even if ClassB.foo isn't directly in the mappings
        List<String> superclasses = findSuperclasses(owner);
        for (String superclass : superclasses) {
            String returnValue = findMappedMethodNameRecursively(superclass, name, desc);

            if (!returnValue.equals(name)) {
                return returnValue;
            }
        }

        // Not found - returning original name
        return name;
    }

    public String findMappedFieldNameRecursively(String owner, String name, String desc) {
        // ASM sometimes uses /s
        owner = owner.replaceAll("/", ".");

        String deobfOwner = remapClassName(owner);
        if (deobfOwner.contains("mc.")) {
            deobfOwner = deobfOwner.split("mc\\.")[1];
        }

        // If the owner is in the mappings
        if (mappings.getFields().containsKey(deobfOwner)) {
            String key = name + ":" + Descriptor.toString(desc);

            // Get the field from the mappings
            if (mappings.getFields().get(deobfOwner).inverse().containsKey(key)) {
                // Return the mapped name
                return mappings.getFields().get(deobfOwner).inverse().get(key);
            }
        }

        // If not found, try parent class
        // This is because if a ClassA has a field called foo, which deobfuscates to bar
        // And ClassB extends ClassA and used the method "foo" in ClassA, the compiler will register it as an access
        // To a field on ClassB, which the JVM will resolve to a field on ClassA
        // So this obviously needs to be fixed
        List<String> superclasses = findSuperclasses(owner);
        for (String superclass : superclasses) {
            String returnValue = findMappedFieldNameRecursively(superclass, name, desc);

            if (!returnValue.equals(name)) {
                return returnValue;
            }
        }

        // Not found - returning original name
        return name;
    }

    public static void init() {
        try {
            minecraftJarFile = new JarFile(minecraftJar);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}