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

package org.granitepowered.granite.scheduler;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.service.scheduler.Scheduler;
import org.spongepowered.api.service.scheduler.Task;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Collection;
import java.util.UUID;

@NonnullByDefault
public class GraniteScheduler implements Scheduler {

    @Override
    public Optional<Task> runTask(Object o, Runnable runnable) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Task> runTaskAfter(Object o, Runnable runnable, long l) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Task> runRepeatingTask(Object o, Runnable runnable, long l) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Task> runRepeatingTaskAfter(Object o, Runnable runnable, long l, long l1) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Task> getTaskById(UUID uuid) {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Task> getScheduledTasks() {
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Task> getScheduledTasks(Object o) {
        throw new NotImplementedException("");
    }
}