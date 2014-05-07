package fr.skyforce77.towerminer.server.commands;

import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;

public class Argument {

	private String name;
	private boolean optional;
	private ArgumentType type = ArgumentType.String;
	
	public Argument(String name, ArgumentType type) {
		this.name = name;
		this.optional = false;
		this.type = type;
	}
	
	public Argument(String name, ArgumentType type, boolean optional) {
		this.name = name;
		this.optional = optional;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOptional() {
		return optional;
	}
	
	public ChatModel getRender() {
		String s = type.getDisplay();
		ChatModel model;
		if(optional) {
			model = new ChatModel("["+name+"]");
		} else {
			model = new ChatModel(name);
		}
		model.setMouseModel(new MessageModel(s));
		return model;
	}
	
	public ArgumentType getType() {
		return type;
	}
	
	static class ArgumentType {
		
		public static ArgumentType String = new ArgumentType("text");
		public static ArgumentType Url = new ArgumentType("url");
		public static ArgumentType Integer = new ArgumentType("number");
		public static ArgumentType Boolean = new ArgumentType("true/false");
		
		private String display;
		
		public ArgumentType(String display) {
			this.display = display;
		}
		
		public String getDisplay() {
			return display;
		}
	}
}
