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

public class Player {

	private Connection c;
	private String name = "Missigno";
	private int gold = 60;
	private int life = 20;
	private boolean ready = false;
	private String displayname;
	
	public Player(Connection c, String name) {
		this.c = c;
		if(!name.equals("test")) {
			this.name = name;
		}
		displayname = this.name;
	}
	
	public Connection getConnection() {
		return c;
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
		pf.round = 0;
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
		p.sendConnectionTCP(c);
	}
	
	public void sendTCP(Packet p) {
		p.sendConnectionTCP(c);
	}
	
	public void sendUDP(Packet p) {
		p.sendConnectionUDP(c);
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
