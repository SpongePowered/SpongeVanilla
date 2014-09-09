package org.granitemc.granite.entities;

import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;

/**
 * License (MIT)
 *
 * Copyright (c) 2014. avarisc
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

public class Entity {

    Object vanillaEntityInstance = null;
    String targetClass = "net.minecraft.entity.Entity";

    public Entity(Object instance) {
        instance = vanillaEntityInstance;
    }

    public void setPosition(double x, double y, double z) {
        invoke("setPosition", x, y, z);
    }

    public Object invoke(String targetMethod, Object... parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaEntityInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
