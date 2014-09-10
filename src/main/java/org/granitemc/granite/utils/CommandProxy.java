package org.granitemc.granite.utils;

import org.granitemc.granite.entities.player.EntityPlayer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CommandProxy implements java.lang.reflect.InvocationHandler {
    public Object obj;
    public CommandProxy(Object obj) {
        this.obj = obj;
    }

    //catches all methods invoked on ServerCommandManager
    public Object invoke(Object proxy, Method m, Object[] args){
        //TODO even though there is only one method, we should probably verify that this is the correct one anyway.
        boolean cancelVanillaCommand = false;
        System.out.println("Command Proxy successfully invoked!");

        Object commandSender = args[0];
        String commandString = (String) args[1];

        System.out.println("Command initiated by: " + getSenderName(commandSender));
        //pre-process command string
        if(commandString.startsWith("/")) {
            commandString = commandString.substring(1);
        }
        String[] commandParts = commandString.split(" ");
        System.out.println("Intercepted command: " + commandParts[0]);

        if(commandParts[0].equalsIgnoreCase("test")){
            if(isCommandSenderPlayer(commandSender)){
                System.out.println("A player has used the test command.");
                //create a mythicplayer
                EntityPlayer granitePlayer = new EntityPlayer(commandSender);
                System.out.println("Before Y: " + granitePlayer.getY());
                granitePlayer.setPosition(granitePlayer.getX(), granitePlayer.getY() + 50D, granitePlayer.getZ());
                System.out.println("After Y:" + granitePlayer.getY());
            } else {
                System.out.println("Silly server. Test commands are for players!");
            }
            cancelVanillaCommand = true;
        }

        if(!cancelVanillaCommand) {
            try {
                return m.invoke(obj, args); //forward to vanilla
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean isCommandSenderPlayer(Object commandSender){
        ArrayList<String> nonplayers = new ArrayList<>();
        nonplayers.add("Rcon");
        nonplayers.add("Server");
        nonplayers.add("@");
        return !nonplayers.contains(getSenderName(commandSender));
    }

    public static String getSenderName(Object commandSender){
        try {
            Class<?> iCommandSender = Class.forName("ae");
            return (String) iCommandSender.getDeclaredMethod("d_").invoke(commandSender);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
