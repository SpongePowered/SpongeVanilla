package org.granitemc.granite.events;

import java.util.Arrays;
import java.util.UUID;

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
