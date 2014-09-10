package org.granitemc.granite.utils;

import org.granitemc.granite.api.GraniteAPI;
import org.granitemc.granite.api.commands.CommandContainer;
import org.granitemc.granite.api.commands.CommandInfo;
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
        Logger.info("Command Proxy successfully invoked!");

        Object commandSender = args[0];
        String commandString = (String) args[1];

        Logger.info("Command initiated by: " + getSenderName(commandSender));
        //pre-process command string
        if(commandString.startsWith("/")) {
            commandString = commandString.substring(1);
        }
        String[] commandParts = commandString.split(" ");
        Logger.info("Intercepted command: " + commandParts[0]);

        if(commandParts[0].equalsIgnoreCase("test")){
            if(isCommandSenderPlayer(commandSender)){
                Logger.info("A player has used the test command.");
                //create a mythicplayer
                EntityPlayer granitePlayer = new EntityPlayer(commandSender);
                Logger.info("Before Y:%s ", granitePlayer.getY());
                granitePlayer.setPosition(granitePlayer.getX(), granitePlayer.getY() + 50D, granitePlayer.getZ());
                Logger.info("After Y:%s", granitePlayer.getY());
            } else {
                Logger.info("Silly server. Test commands are for players!");
            }
            cancelVanillaCommand = true;
        }else {
        	CommandContainer container = GraniteAPI.instance().getCommandByAlias(commandParts[0]);
        	String[] copiedArgs = new String[commandParts.length-1];
        	for(int i = 1; i < commandParts.length; i++)
        		copiedArgs[i-1] = commandParts[i];
			try {
				container.invoke(new CommandInfo(commandSender, copiedArgs ));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | InstantiationException e) {
				e.printStackTrace();
			}
        	
        }
        
        if(cancelVanillaCommand) {
            try {
                return m.invoke(obj, args); //forward to vanilla
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    private static final ArrayList<String> nonplayers = new ArrayList<>();
    static {
    	nonplayers.add("Rcon");
    	nonplayers.add("Server");
    	nonplayers.add("@");
    }
    public static boolean isCommandSenderPlayer(Object commandSender){
       
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
