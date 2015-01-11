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

package org.granitepowered.granite.impl.service.scheduler;

import com.google.common.base.Optional;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.scheduler.Task;

import java.util.UUID;

public class GraniteTask implements Task {

    private final PluginContainer owner;
    private final Optional<Long> delay;
    private final Optional<Long> interval;
    private final long timeCreated;
    boolean cancelled;
    private boolean hasRan;
    private final UUID uuid;
    private final Runnable runnable;
    private final Runnable wrapperRunnable;

    public GraniteTask(PluginContainer owner, Optional<Long> delay, Optional<Long> interval, long timeCreated, final Runnable runnable) {
        this.owner = owner;
        this.delay = delay;
        this.interval = interval;
        this.timeCreated = timeCreated;
        this.runnable = runnable;
        cancelled = false;
        hasRan = false;
        uuid = UUID.randomUUID();
        wrapperRunnable = new Runnable() {
            @Override
            public void run() {
                setHasRan();
                runnable.run();
            }
        };
    }

    @Override
    public Optional<String> getName() {
        return null;
    }

    @Override
    public PluginContainer getOwner() {
        return owner;
    }

    @Override
    public Optional<Long> getDelay() {
        return delay;
    }

    @Override
    public Optional<Long> getInterval() {
        return interval;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setHasRan() {
        hasRan = true;
    }

    @Override
    public boolean cancel() {
        if (!hasRan) {
            cancelled = true;
        }

        return !hasRan;
    }

    @Override
    public Runnable getRunnable() {
        return runnable;
    }

    public Runnable getWrapperRunnable() {
        return wrapperRunnable;
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean hasRan() {
        return hasRan;
    }
}
