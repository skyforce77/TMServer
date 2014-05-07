package fr.skyforce77.towerminer.server.players;

import java.awt.Color;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.protocol.packets.Packet4RoundFinished;
import fr.skyforce77.towerminer.protocol.packets.Packet5UpdateInfos;
import fr.skyforce77.towerminer.server.chat.ChatColor;
import fr.skyforce77.towerminer.server.match.Match;
import fr.skyforce77.towerminer.server.match.MatchManager;

public class Player {

	private Connection c;
	private Integer id = 0;
	private String name = "Missigno";
	private int gold = 60;
	private int life = 20;
	private boolean ready = false;
	private String displayname;
	private Integer match = null;
	private boolean blue = true;
	
	public Player(Integer id, Connection c, String name) {
		this.id = id;
		this.c = c;
		if(!name.equals("test")) {
			this.name = name;
		}
		displayname = this.name;
	}
	
	public Match getMatch() {
		return MatchManager.getMatch(match);
	}
	
	public void setMatch(Match m) {
		match = m.getId();
	}
	
	public Connection getConnection() {
		return c;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getClientName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayname;
	}
	
	public int getGolds() {
		return gold;
	}
	
	public boolean isRed() {
		return !blue;
	}
	
	public boolean isBlue() {
		return blue;
	}
	
	public void setRed() {
		blue = false;
	}
	
	public void setBlue() {
		blue = true;
	}
	
	public void setGolds(int gold) {
		this.gold = gold;
		updateInformations();
	}
	
	public int getLife() {
		return life;
	}
	
	public void setLife(int life) {
		this.life = life;
		updateInformations();
	}
	
	public void setDisplayName(String name) {
		displayname = name;
	}
	
	public boolean isReady() {
		return ready;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void sendMessage(String message) {
		Packet11ChatMessage p = new Packet11ChatMessage(ChatColor.getChatMessage(message));
		p.response = true;
		p.sendConnectionTCP(c);
	}
	
	public void sendMessage(ChatMessage message) {
		Packet11ChatMessage p = new Packet11ChatMessage(message);
		p.response = true;
		p.sendConnectionTCP(c);
	}
	
	public void sendPopupMessage(String message) {
		new Packet12Popup(message).sendConnectionTCP(c);
	}
	
	public void sendPopupMessage(String message, String option) {
		new Packet12Popup(message, option).sendConnectionTCP(c);
	}
	
	public void enableReadyButton(boolean timed) {
		Packet4RoundFinished pf = new Packet4RoundFinished();
		pf.gold = getGolds();
		pf.life = getLife();
		pf.round = getMatch().getRound();
		pf.timed = timed;
		sendTCP(pf);
	}
	
	public void kick(String message) {
		new Packet1Disconnecting(message).sendConnectionTCP(c);
	}
	
	public void sendServerMessage(String message) {
		ChatModel server = new ChatModel("Server");
		server.setForegroundColor(new Color(150,80,240));
		ChatMessage cms = new ChatMessage(server, new ChatModel(": "));
		cms.add(ChatColor.getChatMessage(message));
		Packet11ChatMessage p = new Packet11ChatMessage(cms);
		p.response = true;
		p.sendConnectionTCP(c);
	}
	
	public void sendTCP(Packet pa) {
		pa.sendConnectionTCP(c);
	}
	
	public void sendUDP(Packet pa) {
		pa.sendConnectionUDP(c);
	}
	
	public void sendObject(Object o, ReceivingThread thread) {
		BigSending.sendBigObject(o, getConnection(), thread);
	}
	
	public void updateInformations() {
		Packet5UpdateInfos p = new Packet5UpdateInfos();
		p.gold = gold;
		p.life = life;
		p.sendConnectionTCP(c);
	}
}
