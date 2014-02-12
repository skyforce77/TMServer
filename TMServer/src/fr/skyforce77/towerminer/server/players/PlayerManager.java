package fr.skyforce77.towerminer.server.players;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;

public class PlayerManager {

	static HashMap<Connection, Player> players = new HashMap<>();
	
	public static void onPlayerConnected(Connection c, String name) {
		players.put(c, new Player(c,name));
	}
	
	public static Player getPlayer(Connection c) {
		if(players.containsKey(c)) {
			return players.get(c);
		}
		return null;
	}
	
	public static HashMap<Connection, Player> getPlayers() {
		return players;
	}

}
