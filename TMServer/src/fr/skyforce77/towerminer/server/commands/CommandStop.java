package fr.skyforce77.towerminer.server.commands;

import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.players.PlayerManager;

public class CommandStop extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		for(Integer i : PlayerManager.getPlayers().keySet()) {
			PlayerManager.getPlayer(i).kick("Stopping server...");
		}
		System.exit(1);
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		}
		return false;
	}

}
