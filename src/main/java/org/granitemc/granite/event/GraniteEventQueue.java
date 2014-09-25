package org.granitemc.granite.event;

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

import org.granitemc.granite.api.event.Event;
import org.granitemc.granite.api.event.EventHandlerContainer;
import org.granitemc.granite.api.event.EventQueue;

import java.util.*;

public class GraniteEventQueue implements EventQueue {
    private Map<Class<? extends Event>, List<EventHandlerContainer>> handlers;

    public GraniteEventQueue() {
        handlers = new HashMap<>();
    }

    /**
     * Fire an event, broadcasting it out to any eventual listeners of the event queue
     *
     * @param event The event to fire
     */
    public void fireEvent(Event event) {
        if (!handlers.containsKey(event.getClass())) {
            List<EventHandlerContainer> list = new ArrayList<>();
            handlers.put(event.getClass(), list);
        }

        for (EventHandlerContainer handler : handlers.get(event.getClass())) {
            handler.invoke(event);
        }
    }

    public void addHandler(Class<? extends Event> type, EventHandlerContainer handler) {
        if (!handlers.containsKey(type)) {
            List<EventHandlerContainer> list = new ArrayList<>();
            handlers.put(type, list);
        }

        handlers.get(type).add(handler);
    }
}
