/*****************************************************************************************
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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
 ****************************************************************************************/

package org.granitemc.granite.entities;

import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;

public class Entity {

    private Object entityInstance = null;

    public int getEntityId() {
        //Obf: F
        return (Integer) invoke("getEntityID");
    }

    public void setEntityId(int Id) {
        //Obf: d
        invoke("setEntityId", Id);
    }

    public Object getDataWatcher() {
        //Obf: H
        return invoke("getDataWatcher");
    }

    public void setDead() {
        //Obf: J
        invoke("setDead");
    }

    public void setSize(float width, float height) {
        //Obf: a
        invoke("setSize", width, height);
    }

    public void setRotation(float yaw, float pitch) {
        //Obf: b
        invoke("setRotation", yaw, pitch);
    }

    public void setPosition(double x, double y, double z) {
        //Obf: b
        invoke("setPosition", x, y, z);
    }

    public void onUpdate() {
        //Obf: s_
        invoke("onUpdate");
    }

    public void onEntityUpdate() {
        //Obf: K
        invoke("onEntityUpdate");
    }

    //Up to L()

    private Object invoke(String targetMethod, Object... parameters) {
        try {
            String targetClass = "net.minecraft.entity.EntityPlayer";
            return Mappings.getMethod(targetClass, targetMethod).invoke(entityInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
