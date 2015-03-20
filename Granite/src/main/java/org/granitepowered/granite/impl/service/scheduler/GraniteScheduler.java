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
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import org.granitepowered.granite.Granite;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.scheduler.Scheduler;
import org.spongepowered.api.service.scheduler.Task;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GraniteScheduler implements Scheduler {

    private Set<Task> tasks;
    private long tickCount;

    public GraniteScheduler() {
        tasks = new HashSet<>();
    }

    public void tick() {
        for (Task t : tasks) {
            GraniteTask gt = (GraniteTask) t;
            if (gt.isCancelled()) {
                tasks.remove(t);
            }

            long delta = tickCount - gt.getTimeCreated();

            if (gt.getInterval().isPresent()) {
                if (delta % gt.getInterval().or(1L) == 0) {
                    gt.getWrapperRunnable().run();
                }
            } else {
                if (gt.hasRan()) {
                    tasks.remove(t);
                } else if (delta >= gt.getDelay().or(0L)) {
                    gt.getWrapperRunnable().run();
                }
            }
        }

        tickCount++;
    }

    @Override
    public Optional<Task> runTask(Object plugin, Runnable task) {
        return runTaskAfter(plugin, task, 0);
    }

    @Override
    public Optional<Task> runTaskAfter(Object plugin, Runnable task, long delay) {
        Optional<PluginContainer> container = Granite.getInstance().getPluginManager().fromInstance(plugin);

        if (container.isPresent()) {
            return Optional.<Task>of(new GraniteTask(container.get(), Optional.of(delay), Optional.<Long>absent(), tickCount, task));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<Task> runRepeatingTask(Object plugin, Runnable task, long interval) {
        return runRepeatingTaskAfter(plugin, task, interval, 0);
    }

    @Override
    public Optional<Task> runRepeatingTaskAfter(Object plugin, Runnable task, long interval, long delay) {
        Optional<PluginContainer> container = Granite.getInstance().getPluginManager().fromInstance(plugin);

        if (container.isPresent()) {
            return Optional.<Task>of(
                    new GraniteTask(container.get(), Optional.of(delay), Optional.fromNullable(tickCount == 0 ? null : tickCount), interval, task));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<Task> getTaskById(UUID id) {
        for (Task t : tasks) {
            if (t.getUniqueId().equals(id)) {
                return Optional.of(t);
            }
        }
        return Optional.absent();
    }

    @Override
    public Collection<Task> getScheduledTasks() {
        return tasks;
    }

    @Override
    public Collection<Task> getScheduledTasks(final Object plugin) {
        return Sets.filter(tasks, new Predicate<Task>() {
            @Override
            public boolean apply(Task task) {
                return plugin == task.getOwner().getInstance();
            }
        });
    }
}
