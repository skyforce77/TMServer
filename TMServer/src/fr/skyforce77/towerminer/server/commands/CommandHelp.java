package fr.skyforce77.towerminer.server.commands;

import java.util.ArrayList;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandHelp extends Command {

	int pagesnumber = 0;
	ArrayList<ArrayList<String>> pages = new ArrayList<>();
	
	@Override
	public void onTyped(Player p, String[] args) {
		update();
		if(args.length == 1 && args[0].equals("")) {
			sendPage(p, 0);
		} else if(args.length == 1 && !args[0].equals("") && isNumber(args[0])) {
			sendPage(p, Integer.parseInt(args[0])-1);
		}
	}
	
	public void sendPage(Player p, int number) {
		if(number > pagesnumber) {
			p.sendMessage("Page #"+(number+1)+" do not exists");
			return;
		}
		p.sendMessage("<Commands> Page: "+(number+1)+"/"+(pagesnumber+1));
		for(String command : pages.get(number)) {
			Command c = CommandManager.getCommand(command);
			if(c.getUse() == null) {
				p.sendMessage("- "+command);
			} else {
				ChatMessage message = new ChatMessage("- ");
				message.add(c.getUse());
				p.sendMessage(message);
			}
		}
	}
	
	public void update() {
		int i = 1;
		pagesnumber = 0;
		pages = new ArrayList<>();
		ArrayList<String> commands = new ArrayList<>();
		for(String command : CommandManager.getCommands()) {
			if(i < 8) {
				commands.add(command);
				i++;
			} else {
				pages.add(commands);
				commands = new ArrayList<>();
				commands.add(command);
				i = 2;
				pagesnumber++;
			}
		}
		if(i > 1) {
			pages.add(commands);
		}
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("page", ArgumentType.Integer, true));
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		} else if(args.length == 1 && !args[0].equals("") && isNumber(args[0])) {
			return true;
		}
		return false;
	}

}
