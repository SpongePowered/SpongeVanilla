package com.mythicmc.mythic.entitys;

import com.mythicmc.mythic.Mappings;

import java.lang.reflect.InvocationTargetException;

public class Entity {

    Object vanillaEntityInstance = null;
    String targetClass = "net.minecraft.entity.Entity";

    public Entity(Object instance) {
        instance = vanillaEntityInstance;
    }

    public void setPosition(double x, double y, double z) {
        invoke("setPosition", new Object[]{x, y, z});
    }

    public Object invoke(String targetMethod, Object[] parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaEntityInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
