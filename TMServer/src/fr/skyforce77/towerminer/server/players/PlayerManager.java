package fr.skyforce77.towerminer.server.players;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;

public class PlayerManager {

	static HashMap<Integer, Player> players = new HashMap<>();
	
	public static void onPlayerConnected(Integer id, Connection c, String name) {
		players.put(id, new Player(id, c, name));
	}
	
	public static void onPlayerDisconnected(Integer id) {
		if(players.containsKey(id))
			players.remove(id);
	}
	
	public static Player getPlayer(Integer id) {
		if(players.containsKey(id)) {
			return players.get(id);
		}
		return null;
	}
	
	public static HashMap<Integer, Player> getPlayers() {
		return players;
	}

}
