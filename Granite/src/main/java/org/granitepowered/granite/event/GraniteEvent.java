/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.event;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.util.event.Event;
import org.spongepowered.api.util.event.callback.CallbackList;

public class GraniteEvent implements Event {

    public boolean isModifiable;
    public boolean isCancellable;

    public boolean cancelled;

    public void checkModify() {
        checkCancel();

        if (!isModifiable) {
            throw new UnsupportedOperationException("Can not modify event at this event Order");
        }
    }

    public void checkCancel() {
        if (!isCancellable) {
            throw new UnsupportedOperationException("Can not modify event at this event Order");
        }
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        checkCancel();
        this.cancelled = cancel;
    }

    @Override
    public CallbackList getCallbacks() {
        throw new NotImplementedException("");
    }
}
