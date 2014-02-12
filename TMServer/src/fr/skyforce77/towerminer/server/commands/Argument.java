package fr.skyforce77.towerminer.server.commands;

public class Argument {

	private String name;
	private boolean optional;
	private boolean number = false;
	
	public Argument(String name, boolean optional) {
		this.name = name;
		this.optional = optional;
	}
	
	public Argument(String name, boolean optional, boolean number) {
		this.name = name;
		this.optional = optional;
		this.number = number;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isOptional() {
		return optional;
	}
	
	public String getRender() {
		String s = "";
		if(number) {
			s = "N:";
		}
		if(optional) {
			return "["+s+name+"]";
		} else {
			return s+name;
		}
	}
	
	public boolean isNumber() {
		return number;
	}

}
