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
import org.granitemc.granite.api.event.EventQueue;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GraniteEventQueue implements EventQueue {
    private Map<Class<? extends Event>, Queue<Event>> queues;

    /**
     * Fire an event, broadcasting it out to any eventual listeners of the event queue
     *
     * @param event The event to fire
     */
    public void fireEvent(Event event) {
        if (!queues.containsKey(event.getClass())) {
            Queue<Event> queue = new ConcurrentLinkedQueue<>();
            queues.put(event.getClass(), queue);
        }

        queues.get(event.getClass()).add(event);
    }

    /**
     * Fire an event, and block until it has been processed.
     *
     * @param event The event to fire
     */
    public void fireEventBlocking(Event event) {
        fireEventBlocking(event, 0);
    }

    public void fireEventBlocking(Event event, long timeout) {
        fireEvent(event);

        synchronized (event) {
            while (!event.hasBeenProcessed()) {
                try {
                    event.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
