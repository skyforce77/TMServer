package fr.skyforce77.towerminer.server.commands;

import java.util.HashMap;
import java.util.Set;

import fr.skyforce77.towerminer.server.players.Player;

public class CommandManager {

	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	
	public static void createCommands() {
		register("help", new CommandHelp());
		register("version", new CommandVersion());
		register("broadcast", new CommandBroadcast());
		register("name", new CommandName());
	}
	
	public static void register(String label, Command command) {
		commands.put(label, command);
		command.label = label;
		command.onInitialized(label);
	}
	
	public static boolean isCommand(String label) {
		return commands.containsKey(label);
	}
	
	public static Command getCommand(String label) {
		if(isCommand(label)) {
			return commands.get(label);
		} else {
			return null;
		}
	}
	
	public static Set<String> getCommands() {
		return commands.keySet();
	}
	
	public static void onCommandTyped(Player p, String label, String[] args) {
		if(isCommand(label)) {
			if(getCommand(label).isCorrect(args)) {
				getCommand(label).onTyped(p, args);
			} else {
				if(getCommand(label).getUse() != null) {
					p.sendMessage("Incorrect arguments. Usage: /"+getCommand(label).getUse());
				} else {
					p.sendMessage("Incorrect arguments. Usage: /"+label);
				}
			}
		} else {
			p.sendMessage("Unknown command, type /help for help");
		}
	}

}
