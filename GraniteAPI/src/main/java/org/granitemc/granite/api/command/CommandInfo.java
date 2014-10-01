/**
 *
 */
package org.granitemc.granite.api.command;

import org.granitemc.granite.api.Player;

import java.util.List;

/**
 * **************************************************************************************
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
 * **************************************************************************************
 */

public class CommandInfo {
    public CommandSender commandSender;
    public String[] args;
    public String usedCommandName;

    public CommandContainer command;
    public List<Player> targets;

    /**
     * Returns the sender of this command (either a {@link org.granitemc.granite.api.Player} or a {@link org.granitemc.granite.api.Server})
     */
    public CommandSender getCommandSender() {
        return commandSender;
    }

    /**
     * Returns the command args that were passed to this command (i.e. what's after the command name)
     */
    public String[] getArgs() {
        return args;
    }

    /**
     * Returns the actual command name used when calling the command
     */
    public String getUsedCommandName() {
        return usedCommandName;
    }

    /**
     * Returns the command that was called
     */
    public CommandContainer getCommand() {
        return command;
    }

    /**
     * Returns the targets of this command. This is a list of players selected using the @-notation used by command blocks, or the sender itself if the sender is a player and no other targets were selected.
     */
    public List<Player> getTargets() {
        return targets;
    }
}
