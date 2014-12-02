/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.mappings;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.SignatureAttribute;
import org.apache.commons.lang3.StringUtils;
import org.granitepowered.granite.utils.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SignatureParser {
    public static MethodSignature parseHuman(String signature) {
        String returnType = null;
        String preSig = signature.split("\\(")[0];
        if (preSig.contains(" ")) {
            returnType = preSig.split(" ")[0].trim();
            preSig = preSig.split(" ")[1];
        }

        String name = preSig;
        String[] params = signature.split("\\(")[1].split("\\)")[0].trim().replaceAll(";", ",").split(",");

        Class<?>[] paramTypes = new Class<?>[params.length];

        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = ReflectionUtils.getClassByName(params[i].trim());
        }

        return new MethodSignature(name, ReflectionUtils.getClassByName(returnType), paramTypes);
    }

    public static MethodSignature parseJvm(String signature) {
        SignatureAttribute.MethodSignature sig = null;
        try {
            sig = SignatureAttribute.toMethodSignature("(" + signature.split("\\(")[1]);
        } catch (BadBytecode badBytecode) {
            badBytecode.printStackTrace();
        }

        Class<?>[] paramClassTypes = new Class<?>[sig.getParameterTypes().length];
        for (int i = 0; i < paramClassTypes.length; i++) {
            paramClassTypes[i] = ReflectionUtils.getClassByName(sig.getParameterTypes()[i].toString());

            if (paramClassTypes[i] == null) {
                String aa = "a";
            }
        }

        return new MethodSignature(signature.split("\\(")[0], ReflectionUtils.getClassByName(sig.getReturnType().toString()), paramClassTypes);
    }

    public static MethodSignature getFromMethod(Method method) {
        //return new MethodSignature(method.getName());
        throw new RuntimeException("not implemented");
    }

    public static String normalizeHuman(String signature) {

        return parseHuman(signature).toHumanSignature();
    }

    public static class MethodSignature {
        private String name;
        private Class<?> returnType;
        private Class<?>[] paramTypes;

        public MethodSignature(String name, Class<?> returnType, Class<?>[] paramTypes) {
            this.name = name;
            this.returnType = returnType;
            this.paramTypes = paramTypes;
        }

        public String getName() {
            return name;
        }

        public Class<?>[] getParamTypes() {
            return paramTypes;
        }

        public Class<?> getReturnType() {
            return returnType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodSignature that = (MethodSignature) o;

            if (!name.equals(that.name)) return false;
            if (!Arrays.equals(paramTypes, that.paramTypes)) return false;
            if (returnType != null ? !returnType.equals(that.returnType) : that.returnType != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(paramTypes);
            return result;
        }

        @Override
        public String toString() {
            return toHumanSignature();
        }

        public String toHumanSignature() {
            String[] paramNames = new String[paramTypes.length];
            for (int i = 0; i < paramNames.length; i++) {
                paramNames[i] = paramTypes[i].getName();
            }
            return returnType.getName() + " " + name + "(" + StringUtils.join(paramNames, ", ") + ")";
        }

        public String toJvmSignature() {
            SignatureAttribute.ClassType[] paramClassTypes = new SignatureAttribute.ClassType[paramTypes.length];
            for (int i = 0; i < paramClassTypes.length; i++) {
                paramClassTypes[i] = new SignatureAttribute.ClassType(paramTypes[i].getName());
            }
            return new SignatureAttribute.MethodSignature(null, paramClassTypes, new SignatureAttribute.ClassType(returnType.getName()), null).encode();
        }
    }
}
