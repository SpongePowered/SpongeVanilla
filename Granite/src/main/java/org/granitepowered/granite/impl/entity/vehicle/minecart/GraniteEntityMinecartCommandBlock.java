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

public class GraniteEntityMinecartCommandBlock<T extends MCEntityMinecartCommandBlock> extends GraniteEntityMinecart<T> implements MinecartCommandBlock {

    public GraniteEntityMinecartCommandBlock(T obj) {
        super(obj);
    }

    @Override
    public String getCommand() {
        return obj.fieldGet$commandLogic().fieldGet$commandStored();
    }

    @Override
    public void setCommand(String command) {
        obj.fieldGet$commandLogic().fieldSet$commandStored(command);
        obj.fieldGet$commandLogic().fieldSet$successCount(0);
    }

    @Override
    public String getCommandName() {
        return obj.fieldGet$commandLogic().fieldGet$customName();
    }

    @Override
    public void setCommandName(String commandName) {
        obj.fieldGet$commandLogic().fieldSet$customName(commandName);
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
