package fr.skyforce77.towerminer.server.commands;

import fr.skyforce77.towerminer.server.players.Player;

public class Command {

	private Argument[] arguments;
	public String label;
	
	public void onTyped(Player p, String[] args) {}
	
	public void onInitialized(String label) {}
	
	public void setArguments(Argument... args) {
		arguments = args;
	}
	
	public String getUse() {
		if(arguments != null) {
			String arg = label;
			for(Argument argu : arguments) {
				arg = arg+" "+argu.getRender();
			}
			return arg;
		} else {
			return null;
		}
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
