package org.granitemc.granite.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandProxy implements java.lang.reflect.InvocationHandler {
    public Object obj;
    public CommandProxy(Object obj) {
        this.obj = obj;
    }
    public Object invoke(Object proxy, Method m, Object[] args){
        try {
            System.out.println("Command Proxy successfully invoked!");
        } catch (Exception e) {
            throw e;
        }
        // return something
        return null;
    }
    public Object getObject(){
        return this.obj;
    }
}
