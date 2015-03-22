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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class MappingsLoader {

    public static Mappings load(File mappingsFile) {
        try {
            JsonElement root = new Gson().fromJson(new FileReader(mappingsFile), JsonElement.class);
            JsonObject classes = root.getAsJsonObject().getAsJsonObject("classes");

            Mappings mappings = new Mappings();

            // Load all the classes into Mappings first
            for (Map.Entry<String, JsonElement> classEntry : classes.entrySet()) {
                String deobfClassName = classEntry.getKey();
                String obfClassName = classEntry.getValue().getAsJsonObject().getAsJsonPrimitive("name").getAsString();

                mappings.addClassMapping(obfClassName, deobfClassName);
            }

            // Load methods and fields in a second pass
            for (Map.Entry<String, JsonElement> classEntry : classes.entrySet()) {
                String deobfClassName = classEntry.getKey();

                JsonObject methods = classEntry.getValue().getAsJsonObject().getAsJsonObject("methods");
                if (methods != null) {
                    for (Map.Entry<String, JsonElement> methodEntry : methods.entrySet()) {
                        String deobfMethodName = methodEntry.getValue().getAsString();
                        String obfMethodSig = methodEntry.getKey();

                        mappings.addMethodMapping(deobfClassName, obfMethodSig, deobfMethodName);
                    }
                }

                JsonObject fields = classEntry.getValue().getAsJsonObject().getAsJsonObject("fields");
                if (fields != null) {
                    for (Map.Entry<String, JsonElement> fieldEntry : fields.entrySet()) {
                        String deobfFieldName = fieldEntry.getValue().getAsString();
                        String obfFieldSig = fieldEntry.getKey();

                        mappings.addFieldMapping(deobfClassName, obfFieldSig, deobfFieldName);
                    }
                }
            }

            return mappings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
