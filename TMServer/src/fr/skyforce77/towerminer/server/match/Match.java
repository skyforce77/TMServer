package fr.skyforce77.towerminer.server.match;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.protocol.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet17Player;
import fr.skyforce77.towerminer.server.players.Player;

public class Match {

	private Player red;
	private Player blue;
	private Integer id = 1;
	
	public boolean started = false;
	public Maps map;
	
	public CopyOnWriteArrayList<Turret> turrets = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Mob> mobs = new CopyOnWriteArrayList<>();

	public Match() {
		ArrayList<Maps> m = new ArrayList<>();
		for(Maps ma : Maps.maps) {
			if(ma != null)
				m.add(ma);
		}
		map = m.get(new Random().nextInt(m.size()-1));	
		id = MatchManager.registerCreatedMatch(this);
	}
	
	public Player getAnother(Player p) {
		if(p.isRed()) {
			return blue;
		} else {
			return red;
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public Player getBlue() {
		return blue;
	}
	
	public Player getRed() {
		return red;
	}
	
	public Maps getMap() {
		return map;
	}
	
	public void setMap(Maps m) {
		map = m;
	}
	
	public void setBlue(Player p) {
		p.sendTCP(new Packet17Player("menu.mp.blue"));
		blue = p;
		p.setMatch(this);
		p.setBlue();
	}
	
	public void setRed(Player p) {
		p.sendTCP(new Packet17Player("menu.mp.red"));
		red = p;
		p.setMatch(this);
		p.setRed();
	}
	
	public void sendTCP(Packet p) {
		if(red != null)
			red.sendTCP(p);
		if(blue != null)
			blue.sendTCP(p);
	}
	
	public void sendUDP(Packet p) {
		if(red != null)
			red.sendUDP(p);
		if(blue != null)
			blue.sendUDP(p);
	}
	
	public void sendObject(Object object, ReceivingThread thread) {
		if(red != null)
			red.sendObject(object, thread);
		if(blue != null)
			blue.sendObject(object, thread);
	}
	
	public void onTick() {}

}
