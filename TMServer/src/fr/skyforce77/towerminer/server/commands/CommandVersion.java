package fr.skyforce77.towerminer.server.commands;

import fr.skyforce77.towerminer.protocol.Protocol;
import fr.skyforce77.towerminer.server.Server;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandVersion extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		p.sendMessage("TMServer - "+Server.version+" - Protocol: "+Protocol.version);
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length == 1 && args[0].equals("")) {
			return true;
		}
		return false;
	}

}
