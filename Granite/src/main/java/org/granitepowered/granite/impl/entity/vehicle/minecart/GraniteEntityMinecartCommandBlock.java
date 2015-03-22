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

package org.granitepowered.granite.impl.entity.vehicle.minecart;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityMinecartCommandBlock;
import org.spongepowered.api.entity.vehicle.minecart.MinecartCommandBlock;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.service.permission.context.Context;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.command.CommandSource;

import java.util.List;
import java.util.Set;

public class GraniteEntityMinecartCommandBlock<T extends MCEntityMinecartCommandBlock> extends GraniteEntityMinecart<T>
        implements MinecartCommandBlock {

    public GraniteEntityMinecartCommandBlock(T obj) {
        super(obj);
    }

    @Override
    public String getCommand() {
        return obj.commandLogic.commandStored;
    }

    @Override
    public void setCommand(String command) {
        obj.commandLogic.commandStored = command;
        obj.commandLogic.successCount = 0;
    }

    @Override
    public String getCommandName() {
        return obj.commandLogic.customName;
    }

    @Override
    public void setCommandName(String commandName) {
        obj.commandLogic.customName = commandName;
    }

    @Override
    public String getName() {
        return "MinecartCommandBlock";
    }

    @Override
    public void sendMessage(String... strings) {
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(Message... messages) {
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(Iterable<Message> iterable) {
        throw new NotImplementedException("");
    }

    @Override
    public String getIdentifier() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        throw new NotImplementedException("");
    }

    @Override
    public SubjectCollection getContainingCollection() {
        throw new NotImplementedException("");
    }

    @Override
    public SubjectData getData() {
        throw new NotImplementedException("");
    }

    @Override
    public SubjectData getTransientData() {
        throw new NotImplementedException("");
    }

    @Override
    public boolean hasPermission(Set<Context> set, String s) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean hasPermission(String s) {
        throw new NotImplementedException("");
    }

    @Override
    public Tristate getPermissionValue(Set<Context> set, String s) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isChildOf(Subject subject) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isChildOf(Set<Context> set, Subject subject) {
        throw new NotImplementedException("");
    }

    @Override
    public List<Subject> getParents() {
        throw new NotImplementedException("");
    }

    @Override
    public List<Subject> getParents(Set<Context> set) {
        throw new NotImplementedException("");
    }

    @Override
    public Set<Context> getActiveContexts() {
        throw new NotImplementedException("");
    }
}
