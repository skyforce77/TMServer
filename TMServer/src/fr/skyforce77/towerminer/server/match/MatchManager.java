package fr.skyforce77.towerminer.server.match;

import java.util.ArrayList;

import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.server.players.Player;

public class MatchManager {

	static ArrayList<Match> matchs = new ArrayList<>();
	
	public static void registerMatch(Match m) {
		matchs.add(m);
	}
	
	public static Match getMatch(Player p) {
		for(Match ma : matchs) {
			if(ma.getRed().equals(p) || ma.getBlue().equals(p)) {
				return ma;
			}
		}
		return null;
	}
	
	public static void deleteMatch(Match m) {
		matchs.remove(m);
		if(m.getRed() != null) {
			m.getRed().getConnection().sendTCP(new Packet1Disconnecting("Your partner disconnected!"));
		}
		if(m.getBlue() != null) {
			m.getBlue().getConnection().sendTCP(new Packet1Disconnecting("Your partner disconnected!"));
		}
	}

}
