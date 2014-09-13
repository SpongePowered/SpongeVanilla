package org.granitemc.granite.reflect;

import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.Method;

public class CommandComposite extends Composite {
    public CommandComposite() {
        super(Mappings.getClass("n.m.command.ServerCommandManager"));

        addHook("executeCommand(n.m.command.ICommandSender;String)", new HookListener() {
            @Override
            public Object activate(Method method, Method proxyCallback, Object[] args) {
                return null;
            }
        });
    }
}
