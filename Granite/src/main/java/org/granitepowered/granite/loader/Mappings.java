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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class Mappings {

    // All Deobf -> Obf
    private BiMap<String, String> classes;
    private Map<String, BiMap<String, String>> methods;
    private Map<String, BiMap<String, String>> fields;

    public Mappings() {
        classes = HashBiMap.create();
        methods = new HashMap<>();
        fields = new HashMap<>();
    }

    // NOTE
    // Make sure to add ALL classes before starting to add the methods and fields

    public void addClassMapping(String obf, String deobf) {
        // Add a class mapping
        classes.put(deobf, obf);
        methods.put(deobf, HashBiMap.<String, String>create());
        fields.put(deobf, HashBiMap.<String, String>create());
    }

    public void addMethodMapping(String deobfClass, String obfMethod, String deobfMethod) {
        // Break the obfuscated method into bits
        // (obfuscated method: a(int,boolean,java.lang.Object):void)
        String obfName = obfMethod.split("\\(")[0];
        String obfSig = obfMethod.split("\\(")[1].split("\\)")[0];

        // There's really no point in the return type
        // Since it's not possible to have two methods with the same return type in Java
        // However, Mojang's obfuscator may catch onto that, so we're keeping it here in case that happens
        String obfReturn = obfMethod.split(":")[1];

        // Split the signature into classes, and re-obfuscate deobfuscated names if possible
        String[] paramNames = obfSig.split(",");
        for (int i = 0; i < paramNames.length; i++) {
            String paramName = paramNames[i];

            if (classes.containsKey(paramName)) {
                paramNames[i] = classes.get(paramName);
            }
        }

        // Re-obfuscate the return type
        if (classes.containsKey(obfReturn)) {
            obfReturn = classes.get(obfReturn);
        }

        obfSig = StringUtils.join(paramNames, ",");

        String fullObfSig = obfName + "(" + obfSig + "):" + obfReturn;

        methods.get(deobfClass).put(deobfMethod, fullObfSig);
    }

    public void addFieldMapping(String deobfClass, String obfField, String deobfField) {
        String obfName = obfField.split(":")[0];
        String obfType = obfField.split(":")[1];

        // Re-obfuscate the type
        if (classes.containsKey(obfType)) {
            obfType = classes.get(obfType);
        }

        String fullObf = obfName + ":" + obfType;

        fields.get(deobfClass).put(deobfField, fullObf);
    }

    public BiMap<String, String> getClasses() {
        return classes;
    }

    public Map<String, BiMap<String, String>> getMethods() {
        return methods;
    }

    public Map<String, BiMap<String, String>> getFields() {
        return fields;
    }
}
