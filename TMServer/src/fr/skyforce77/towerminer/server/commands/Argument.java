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
		String s = "text";
		
		if(type.equals(ArgumentType.Integer)) {
			s = "number";
		} else if(type.equals(ArgumentType.Boolean)) {
			s = "true/false";
		}
		
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
	
	enum ArgumentType {
		
		String(0),
		Integer(1),
		Boolean(2);
		
		private int id;
		
		private ArgumentType(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
	}
}
