
public class MythicEssentials {
	
	/*
	 * This class handles command events to provide Essentials-like functionality.
	 * It also demonstrates a plugin setup.
	 */
	
	public static String dbPrefix = "mythic_ess";
	
	public static void onInstall(){
		Mythic.getDatabase().install(dbPrefix, true, true, true, true, true, true, true, true);
		//register essentials event handler
		MythicEventHandlerRegistry.register(MythicEssentials.class);
	}
	
	public static void onUninstall(){
		
	}
	
	@MythicEventHandler
    public void handle(MythicEventCommand event) {
        if(routeCommand(event)){
        	event.cancelled = true;
        }
    }
	
	public boolean routeCommand(MythicEventCommand event){
		if(event.parameters.length == 0) return false;
		if(event.parameters[0].equalsIgnoreCase("spawn")){
			onCommandSpawn(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("home")){
			onCommandHome(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("sethome")){
			onCommandSetHome(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("warp")){
			onCommandWarp(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("setwarp")){
			onCommandSetWarp(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("delwarp")){
			onCommandDelWarp(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("tpa")){
			onCommandTpa(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("tpahere")){
			onCommandTpaHere(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("tpaccept")){
			onCommandTpAccept(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("tpdeny")){
			onCommandTpDeny(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("rules")){
			onCommandRules(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("plugins")){
			onCommandPlugins(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("heal")){
			onCommandHeal(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("feed")){
			onCommandFeed(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("ping")){
			onCommandPing(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("world")){
			onCommandWorld(event);
			return true;
		} else if(event.parameters[0].equalsIgnoreCase("back")){
			onCommandBack(event);
			return true;
		}
		return false;
	}

	private void onCommandBack(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandWorld(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandPing(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Pong!");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Pong!");	
		}
	}

	private void onCommandFeed(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandHeal(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandPlugins(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Running MythicAPI (Version: 1.0) - Plugins (1):");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Mythic-Essentials");	
		}
	}

	private void onCommandRules(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "No hacking, mods, or exploits.");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Be respectful of other players. Watch your language.");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "No griefing, raiding, or PvP.");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Do not ask for items or favors from staff.");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Act responsible and mature. Do not spam or advertise.");
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Do not scam, aggrevate, or harass other players.");	
		}
	}

	private void onCommandTpDeny(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			String requestFrom = getPlayerString(event.player, "teleport.request.from.username", null);
			if(requestFrom != null && !requestFrom.equalsIgnoreCase("X")){
				MythicPlayer requestor = MythicServer.getPlayerByName(requestFrom);
				if(requestor != null){
					requestor.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Your teleport request was denied.");
				}
			}
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Teleport request denied.");
			clearTeleportRequest(event.player);
		}
	}

	private void onCommandTpAccept(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			if(event.parameters.length != 1){
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Wrong number of parameters.");
			} else {
				String requestFrom = getPlayerString(event.player, "teleport.request.from.username", null);
				if(requestFrom == null || requestFrom.equalsIgnoreCase("X")){
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "You do not have a pending teleport request.");
				} else {
					MythicPlayer requestor = MythicServer.getPlayerByName(requestFrom);
					if(requestor == null){
						clearTeleportRequest(event.player);
						event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Could not locate player " + requestFrom + ".");
					} else {
						String direction = getPlayerString(event.player, "teleport.request.direction", null);
						if(direction.equalsIgnoreCase("you.to.them")){
							clearTeleportRequest(event.player);
							event.player.teleportToPlayer(requestor);
							event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Teleported to " + requestFrom + ".");
							requestor.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + event.player.getName() + " has teleported to you.");
						} else if(direction.equalsIgnoreCase("them.to.you")){
							clearTeleportRequest(event.player);
							requestor.teleportToPlayer(event.player);
							event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + requestor.getName() + " has teleported to you.");
							requestor.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Teleported to " + event.player.getName() + ".");
						} else {
							clearTeleportRequest(event.player);
							event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Could not complete teleport request safely.");
						}
					}
				}
			}
		}
	}
	
	private void clearTeleportRequest(MythicPlayer recipient){
		setPlayerString(recipient, "teleport.request.from.username", "X");
	}

	private void onCommandTpaHere(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			if(event.parameters.length != 2){
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Wrong number of parameters.");
			} else {
				String targetUsername = event.parameters[1];
				targetUsername = MythicServer.getMatchingPlayerName(targetUsername);
				if(targetUsername == null || targetUsername.equalsIgnoreCase("X")){
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Could not find a player matching " + event.parameters[1] + ".");
				} else {
					MythicPlayer target = MythicServer.getPlayerByName(targetUsername);
					target.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + event.player.getName() + " would like to teleport you to them.");
					target.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Type " + MythicChatFormatCodes.lightgreen + "/tpaccept " + MythicChatFormatCodes.gold + "to go to them, or " + MythicChatFormatCodes.lightred + "/tpdeny " + MythicChatFormatCodes.gold + "to refuse.");
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Sent teleport request to " + targetUsername + ".");
					setPlayerString(target, "teleport.request.from.username", event.player.getName());
					setPlayerString(target, "teleport.request.direction", "you.to.them");
				}
			}
		}
	}

	private void onCommandTpa(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			if(event.parameters.length != 2){
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Wrong number of parameters.");
			} else {
				String targetUsername = event.parameters[1];
				targetUsername = MythicServer.getMatchingPlayerName(targetUsername);
				if(targetUsername == null || targetUsername.equalsIgnoreCase("X")){
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Could not find a player matching " + event.parameters[1] + ".");
				} else {
					MythicPlayer target = MythicServer.getPlayerByName(targetUsername);
					target.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + event.player.getName() + " would like to teleport to you.");
					target.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Type " + MythicChatFormatCodes.lightgreen + "/tpaccept " + MythicChatFormatCodes.gold + "to bring them here, or " + MythicChatFormatCodes.lightred + "/tpdeny " + MythicChatFormatCodes.gold + "to refuse.");
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Sent teleport request to " + targetUsername + ".");
					setPlayerString(target, "teleport.request.from.username", event.player.getName());
					setPlayerString(target, "teleport.request.direction", "them.to.you");
				}
			}
		}
	}

	private void onCommandDelWarp(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandSetWarp(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandWarp(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "This command is not available yet.");	
		}
	}

	private void onCommandSetHome(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			if(event.parameters.length > 1){
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Too many parameters.");
			} else {
				setPlayerInt(event.player, "home.dimension", event.player.getDimension());
				setPlayerFloat(event.player, "home.x", (float) event.player.getX());
				setPlayerFloat(event.player, "home.y", (float) event.player.getY());
				setPlayerFloat(event.player, "home.z", (float) event.player.getZ());
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Home set.");
			}
		}
	}
	
	private void onCommandHome(MythicEventCommand event) {
		if(event.player == null){
			MythicLogger.info("Only a player can use this command.");
		} else {
			if(event.parameters.length > 1){
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Too many parameters.");
			} else {
				int dimId = getPlayerInt(event.player, "home.dimension", 999);
				if(dimId == 999){
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "You do not have a home set.");
				} else {
					if(event.player.getDimension() != dimId) event.player.travelToDimension(dimId);
					double x = getPlayerFloat(event.player, "home.x", (float) event.player.getX());
					double y = getPlayerFloat(event.player, "home.y", (float) event.player.getY());
					double z = getPlayerFloat(event.player, "home.z", (float) event.player.getZ());
					event.player.setPosition(x, y, z);
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Teleported home.");
				}
			}
		}
	}

	private void onCommandSpawn(MythicEventCommand event) {
		if(event.parameters.length > 1){
			if(event.parameters.length == 2){
				
			} else {
				if(event.player == null){
					MythicLogger.info("Too many parameters.");
					return;
				} else {
					event.player.sendChatMessage(MythicChatFormatCodes.preFormatError() + "Too many parameters.");
				}
			}
		} else {
			if(event.player == null){
				MythicLogger.info("Only a player is can execute the self-targeted variant of this command.");
				return;
			} else {
				if(event.player.getDimension() != 0) event.player.travelToDimension(0);
				event.player.setPosition(164.5D, 73.5D, 41.5D);
				event.player.sendChatMessage(MythicChatFormatCodes.preFormatInfo() + "Teleported to spawn.");
			}
		}
	}
	private void setPlayerString(MythicPlayer player, String dataName, String value){
		Mythic.getDatabase().setPlayerString(dbPrefix, dataName, player.getUUIDString(), value);
	}
	private void setPlayerFloat(MythicPlayer player, String dataName, float value){
		Mythic.getDatabase().setPlayerFloat(dbPrefix, dataName, player.getUUIDString(), value);
	}
	private void setPlayerInt(MythicPlayer player, String dataName, int value){
		Mythic.getDatabase().setPlayerInt(dbPrefix, dataName, player.getUUIDString(), value);
	}
	private String getPlayerString(MythicPlayer player, String dataName, String defaultVal){
		return Mythic.getDatabase().getPlayerString(dbPrefix, dataName, player.getUUIDString(), defaultVal);
	}
	private float getPlayerFloat(MythicPlayer player, String dataName, float defaultVal){
		return Mythic.getDatabase().getPlayerFloat(dbPrefix, dataName, player.getUUIDString(), defaultVal);
	}
	private int getPlayerInt(MythicPlayer player, String dataName, int defaultVal){
		return Mythic.getDatabase().getPlayerInt(dbPrefix, dataName, player.getUUIDString(), defaultVal);
	}
}
