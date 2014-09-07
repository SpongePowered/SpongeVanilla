package com.mythicmc.mythic.events;

public class MythicEventCancellable extends MythicEvent {
    public boolean cancelled = false;

    public void cancel() {
        cancelled = true;
    }

    public boolean isCanceled() {
        return this.cancelled;
    }
}
