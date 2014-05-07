package fr.skyforce77.towerminer.server.commands;

import java.util.HashMap;
import java.util.Set;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandManager {

	private static HashMap<String, Command> commands = new HashMap<String, Command>();
	private static HashMap<String, String> alias = new HashMap<String, String>();
	
	public static void createCommands() {
		register("help", new CommandHelp());
		register("stop", new CommandStop());
		register("save", new CommandSave());
		register("version", new CommandVersion());
		register("broadcast", new CommandBroadcast());
		register("name", new CommandName());
		register("music", new CommandMusic());
		register("notif", new CommandNotif());
		register("sendblock", new CommandSendBlock());
		register("seticon", new CommandSetIcon());
		register("setdisplay", new CommandSetDisplay());
	}
	
	public static void register(String label, Command command) {
		commands.put(label, command);
		command.label = label;
		command.onInitialized(label);
		
		for(String s : command.getAlias()) {
			registerAlias(label, s);
		}
	}
	
	public static void registerAlias(String label, String alia) {
		alias.put(alia, label);
	}
	
	public static boolean isCommand(String label) {
		if(commands.containsKey(label)) {
			return true;
		}
		if(alias.containsKey(label)) {
			return true;
		}
		return false;
	}
	
	public static Command getCommand(String label) {
		if(isCommand(label)) {
			if(commands.containsKey(label)) {
				return commands.get(label);
			}
			if(alias.containsKey(label)) {
				return commands.get(alias.get(label));
			}
		} else {
			return null;
		}
		return null;
	}
	
	public static Set<String> getCommands() {
		return commands.keySet();
	}
	
	public static void onCommandTyped(Player p, String label, String[] args) {
		String s = label;
		for(String a : args) {
			s = s+" "+a;
		}
		System.out.println("Player #"+p.getId()+":"+p.getDisplayName()+" typed command: "+s);
		if(isCommand(label)) {
			if(getCommand(label).isCorrect(args)) {
				getCommand(label).onTyped(p, args);
			} else {
				if(getCommand(label).getUse() != null) {
					ChatMessage message = new ChatMessage("Incorrect arguments. Usage: /");
					message.add(getCommand(label).getUse());
					p.sendMessage(message);
				} else {
					p.sendMessage("Incorrect arguments. Usage: /"+label);
				}
			}
		} else {
			p.sendMessage("Unknown command, type /help for help");
		}
	}

}
