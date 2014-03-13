package fr.skyforce77.towerminer.server.match;

import java.util.HashMap;

import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;

public class MatchManager {

	static HashMap<Integer, Match> matchs = new HashMap<>();
	static Integer count = 1;
	
	public static Integer registerCreatedMatch(Match m) {
		matchs.put(count, m);
		count++;
		return count-1;
	}
	
	public static Match getMatch(Integer id) {
		return matchs.get(id);
	}
	
	public static void deleteMatch(Match m) {
		matchs.remove(m.getId());
		if(m.getRed() != null) {
			m.getRed().getConnection().sendTCP(new Packet1Disconnecting("Your partner disconnected!"));
		}
		if(m.getBlue() != null) {
			m.getBlue().getConnection().sendTCP(new Packet1Disconnecting("Your partner disconnected!"));
		}
	}
	
	public static HashMap<Integer, Match> getMatchs() {
		return matchs;
	}

}
