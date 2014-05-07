package fr.skyforce77.towerminer.server.commands;

import java.awt.Color;
import java.util.ArrayList;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;
import fr.skyforce77.towerminer.server.players.Player;

public class Command {

	private Argument[] arguments;
	private ArrayList<String> alias = new ArrayList<>();
	public String label;
	public boolean isalias;
	public String real;
	
	public void onTyped(Player p, String[] args) {}
	
	public void onInitialized(String label) {}
	
	public void setArguments(Argument... args) {
		arguments = args;
	}
	
	public void setAlias(String... args) {
		for(String s : args) {
			alias.add(s);
		}
	}
	
	public ChatMessage getUse() {
		String renderonmouse = null;
		if(alias != null && alias.size() > 0) {
			renderonmouse = "Alias: ";
			for(String alia : alias) {
				renderonmouse = renderonmouse+","+alia;
			}
			renderonmouse = renderonmouse.replaceFirst(",", "");
		}
		
		ChatModel model = new ChatModel(label);
		if(renderonmouse != null) {
			model.setMouseModel(new MessageModel(renderonmouse));
			model.setForegroundColor(Color.cyan);
		}
		ChatMessage msg = new ChatMessage(model);
		
		if(arguments != null) {
			for(Argument argu : arguments) {
				msg.addModel(new ChatModel(" "));
				msg.addModel(argu.getRender());
			}
		}
		return msg;
	}
	
	public boolean hasAlias(String alias) {
		return this.alias.contains(alias);
	}
	
	public ArrayList<String> getAlias() {
		return alias;
	}
	
	public boolean isCorrect(String[] args) {
		return true;
	};
	
	protected boolean isNumber(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}

}
