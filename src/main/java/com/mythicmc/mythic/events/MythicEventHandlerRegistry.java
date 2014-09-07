package com.mythicmc.mythic.events;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class MythicEventHandlerRegistry {
    private static List<Class> handlers = new ArrayList<Class>();

    public static void register(Class clazz) {
        handlers.add(clazz);
    }

    public static List<Class> getHandlers() {
        return handlers;
    }
}