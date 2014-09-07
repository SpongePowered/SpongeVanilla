
public class MythicEventHooks {
	/*
	 * This class provides a reciever for hooks inserted into vanilla code.
	 * The hooks will have to be reinstalled every update -
	 * Comments above each hook provide information that is useful in finding the hook.
	 */
	
	
	//HOOK: onExecuteCommand
	//hooked in: CommandHandler.executeCommand(CommandSender var1, String var2)
	//last known obfuscated name: ab.a(ae var1, String var2)
	//XXX: obfuscation reference
	public boolean onExecuteCommand(ae commandSender, String[] commandParts){
		MythicEventCommand event = new MythicEventCommand();
		//XXX: obfuscation reference
		if(!commandSender.d_().equalsIgnoreCase("Server") && !commandSender.d_().equalsIgnoreCase("Rcon")){
			//XXX: obfuscation reference
			if(commandSender instanceof ahd){
				//XXX: obfuscation reference
				event.player = new MythicPlayer((ahd) commandSender);
			} else {
				MythicLogger.error("Invalid player type executed command!");
			}
		}
		event.parameters = commandParts;
		event = (MythicEventCommand) MythicEventPublisher.raise(event);
		//a hardcoded hook to uninstall if "stop" is run from console
		if(commandParts[0].equalsIgnoreCase("stop") && event.player == null){
			Mythic.uninstall();
		}
		return !event.cancelled;
	}
}
