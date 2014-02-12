package fr.skyforce77.towerminer.server.commands;
import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.players.PlayerManager;

public class CommandName extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		String last = p.getDisplayName();
		p.setDisplayName(args[0]);
		
		for(Connection c : PlayerManager.getPlayers().keySet()) {
			PlayerManager.getPlayer(c).sendMessage(last+" is now know as "+p.getDisplayName());
		}
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length >= 1 && !args[0].equals("")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("name", false, false));
	}

}
