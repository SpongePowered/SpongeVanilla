package org.granitemc.granite.utils;

import org.granitemc.granite.chat.GraniteChatComponentText;

public class MinecraftUtils {
    // Todo: This method was missing from the repo, and I really have no idea what it's supposed to do. I have recreated it to the best of my abilities. ~Jckf
    public static Object wrap(Object object) {
        if (Mappings.getClass("net.minecraft.command.ICommandSender").isInstance(object)) {
            // return new GraniteCommandSender(object);
        } else if (Mappings.getClass("net.minecraft.util.IChatComponent").isInstance(object)) {
            return new GraniteChatComponentText(object);
        }

        return null;
    }
}
