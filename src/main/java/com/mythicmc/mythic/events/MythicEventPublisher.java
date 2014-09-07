package com.mythicmc.mythic.events;

import java.lang.reflect.Method;


public class MythicEventPublisher {
    //not used yet because we have to figure out how to get a return
    public static void raiseEvent(final MythicEvent event) {
        new Thread() {
            @Override
            public void run() {
                raise(event);
            }
        }.start();
    }

    @SuppressWarnings("rawtypes")
    public static MythicEvent raise(final MythicEvent event) {
        for (Class handler : MythicEventHandlerRegistry.getHandlers()) {
            Method[] methods = handler.getMethods();

            for (int i = 0; i < methods.length; ++i) {
                MythicEventHandler eventHandler = methods[i].getAnnotation(MythicEventHandler.class);
                if (eventHandler != null) {
                    Class[] methodParams = methods[i].getParameterTypes();

                    if (methodParams.length < 1)
                        continue;

                    if (!event.getClass().getSimpleName()
                            .equals(methodParams[0].getSimpleName()))
                        continue;

                    // defence from runtime exceptions:
                    try {
                        methods[i].invoke(handler.newInstance(), event);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
        return event;
    }
}