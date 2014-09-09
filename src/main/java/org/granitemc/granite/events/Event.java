package org.granitemc.granite.events;

import java.util.Arrays;
import java.util.UUID;

/**
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
 */

public class Event {

    /**
     * The cause of this event
     */
    public Object cause;

    /**
     * The data of this event
     */
    public Object[] data;

    /**
     * The type of this event.
     */
    public String type;

    /**
     * The unique identifier of this Event.
     */
    private Object uuid;

    /**
     * Creates an event
     *
     * @param oCause the new cause
     * @param oData  the new data
     */
    public Event(Object oCause, String type, Object... oData) {
        cause = oCause;
        data = oData;
        this.type = type;
        this.uuid = UUID.nameUUIDFromBytes(oData.toString().getBytes());
    }

    /**
     * Returns a string representation of the cause and data of this event.
     *
     * @param prettyPrint if should append end-of-line chars.
     */
    public String toString(boolean prettyPrint) {
        return String.format("cause %s, type %s, uuid %s, data %s", cause, type, uuid, Arrays.deepToString(data));
    }
}
