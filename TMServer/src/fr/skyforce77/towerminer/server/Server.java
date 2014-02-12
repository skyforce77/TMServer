package fr.skyforce77.towerminer.server;

import java.awt.Point;
import java.io.File;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.Connect;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.protocol.Protocol;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.listeners.ConnectionListener;
import fr.skyforce77.towerminer.protocol.listeners.ListenersManager;
import fr.skyforce77.towerminer.protocol.listeners.PacketListener;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet0Connecting;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.protocol.packets.Packet14ServerPing;
import fr.skyforce77.towerminer.protocol.packets.Packet15ServerInfos;
import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.protocol.packets.Packet2BigSending;
import fr.skyforce77.towerminer.protocol.packets.Packet3Action;
import fr.skyforce77.towerminer.protocol.packets.Packet6Entity;
import fr.skyforce77.towerminer.protocol.packets.Packet9MouseClick;
import fr.skyforce77.towerminer.server.chat.ChatColor;
import fr.skyforce77.towerminer.server.commands.CommandManager;
import fr.skyforce77.towerminer.server.match.Match;
import fr.skyforce77.towerminer.server.match.MatchManager;
import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.players.PlayerManager;

public class Server implements PacketListener, ConnectionListener{

	public static Server instance;
	public static String version = "Alpha 0.1";
	static String name = "Test server";
	static String message = "motd";
	static File folder;
	static Match wait = null;

	public static void main(String[] args) {
		System.out.println("Server version: "+version);
		System.out.println("Protocol version: "+Protocol.version);
		System.out.println("Client version: "+Game.version);
		System.out.println("Launching...");
		folder = new File(Server.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
		Blocks.createNativeBlocks();
		EntityTypes.createTurrets();

		File maps = new File(folder, "/maps");
		if(!maps.exists()) {
			maps.mkdirs();
		}

		for(File f : maps.listFiles()) {
			Maps m = Maps.deserialize(f);
			if(m != null) {
				int i = 0;
				while(Maps.maps[i] != null) {
					i++;
				}
				Maps.maps[i] = m;
				System.out.println("Map "+m.getName()+" loaded as #"+i);
			}
		}

		CommandManager.createCommands();
		instance = new Server();
		try {
			Connect.initServer();
		} catch(Exception e) {
			System.out.println("Can't be launched, error occured");
		}
		ListenersManager.register(instance);
		System.out.println("Successfully launched");
	}

	@Override
	public void onPacketReceived(final Connection c, Packet p) {}

	@Override
	public void onClientReceived(Connection c, Packet p) {}

	@Override
	public void onServerReceived(final Connection c, Packet p) {
		if(p.getId() == 0) {
			final Packet0Connecting pack = (Packet0Connecting)p;
			if(pack.version != Protocol.version) {
				System.out.println("Player "+pack.player+" tryed connecting with protocol "+pack.version);
				c.sendTCP(new Packet1Disconnecting("menu.mp.client.kick.version"));
			} else {
				System.out.println("Player "+pack.player+"("+c.getRemoteAddressTCP().getHostString()+") successfully connected");
				PlayerManager.onPlayerConnected(c, pack.player);
				Player pl = PlayerManager.getPlayer(c);
				if(wait == null || wait.getRed() == null) {
					wait = new Match();
					MatchManager.registerMatch(wait);
					wait.setRed(pl);
				} else {
					wait.setBlue(pl);
					wait.getRed().sendMessage(ChatColor.LIGHT_BLUE+"Player "+pl.getDisplayName()+" connected");
					wait = null;
				}

				new Packet3Action("sendingmap").sendConnectionTCP(c);
				BigSending.sendBigObject(MatchManager.getMatch(pl).map, c, new ReceivingThread() {
					@Override
					public void run(int objectid) {
						new Packet3Action("finishedsendingmap", (byte)objectid).sendConnectionTCP(c);
						PlayerManager.getPlayer(c).enableReadyButton(false);
					}
				});
			}
		}
		else if(p.getId() == 2) {
			Packet2BigSending pack2 = (Packet2BigSending)p;
			if(pack2.type == 2) {
				if(pack2.pack == 1) {
					BigSending.sending.get(pack2.pid).thread.run(pack2.pid);
				} else {
					for(byte b : pack2.data) {
						BigSending.sending.get(pack2.pid).packets[(int)b].sendConnectionTCP(MPInfos.connection);
					}
					BigSending.sending.get(pack2.pid).testpacket.sendConnectionTCP(MPInfos.connection);
				}
			}
		}
		else if(p.getId() == 3) {
			Packet3Action pack3 = (Packet3Action)p;
			if(pack3.action.equals("canstartgame")) {
				//TODO assigner une partie
			} else if(pack3.action.equals("ready")) {
				Player pl = PlayerManager.getPlayer(c);
				Match m = MatchManager.getMatch(pl);
				if(m.getRed() != null && m.getBlue() != null) {
					new Packet12Popup("menu.mp.ready", pl.getDisplayName()).sendConnectionTCP(m.getAnother(pl).getConnection());
					pl.setReady(true);
				} else {
					pl.enableReadyButton(false);
					pl.sendMessage(ChatColor.RED+"You can't play without partner/rival");
					pl.setReady(true);
				}
			}
		}
		else if(p.getId() == 9) {
			Packet9MouseClick pack9 = (Packet9MouseClick)p;
			final Player pl = PlayerManager.getPlayer(c);
			final Match m = MatchManager.getMatch(pl);
			if(m.getRed() != null && m.getBlue() != null) {
				//TODO créer les entitées ect...
				Turret aimed = null;
				for(Turret en : m.turrets) {
					if(en.getUUID() == pack9.aimed) {
						aimed = en;
					}
				}
				if(pack9.modifier == 16) {
					pl.setGolds(pl.getGolds() - EntityTypes.turrets[pack9.selected].getPrice());
					EntityTypes type = EntityTypes.turrets[pack9.selected];
					try {
						String s = m.getRed().equals(pl) ? "menu.mp.red" : "menu.mp.blue";
						Turret tu = (Turret)type.getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class).newInstance(EntityTypes.turrets[pack9.selected], new Point(pack9.x,pack9.y-1), s);
						m.turrets.add(tu);
						m.sendObject(tu, new ObjectReceiver.ReceivingThread() {
							@Override
							public void run(int objectid) {
								Packet6Entity pe = new Packet6Entity();
								pe.eid = objectid;
								m.sendTCP(pe);
							}
						});
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else if(pack9.modifier == 4) {
					if(aimed != null && aimed.getPrice() <= pl.getGolds()) {
						pl.setGolds(pl.getGolds() - aimed.getPrice());
						aimed.addData();
					}
				} else if(pack9.modifier == 8) {
					if(aimed != null) {
						pl.setGolds(pl.getGolds() + (int)(aimed.getCost()*0.75));
						m.turrets.remove(aimed);
					}
				}
			} else {
				pl.sendMessage(ChatColor.RED+"You can't play without partner/rival");
			}
		}
		else if(p.getId() == 11) {
			Packet11ChatMessage pack11 = (Packet11ChatMessage)p;
			if(pack11.response)
				return;
			pack11.response = true;
			Player pl = PlayerManager.getPlayer(c);
			Match m = MatchManager.getMatch(pl);
			String msg1 = ((ChatModel)pack11.getMessage().getModels().toArray()[1]).getText().replaceFirst(": ", "");
			if(m.getRed() != null && m.getBlue() != null && !msg1.startsWith("/")) {
				ChatMessage msg = new ChatMessage();
				for(ChatModel mo : pack11.getMessage().getModels()) {
					if(mo.getText().equals(pl.getClientName())) {
						ChatModel mod = new ChatModel(pl.getDisplayName());
						mod.setForegroundColor(mo.getForegroundColor());
						msg.addModel(mod);
					} else {
						msg.addModel(mo);
					}
				}
				Packet11ChatMessage chm = new Packet11ChatMessage(msg);
				c.sendTCP(chm);
				m.getAnother(pl).getConnection().sendTCP(chm);
			} else if(msg1.startsWith("/")) {
				String label = msg1.replaceFirst("/", "").split(" ")[0];
				CommandManager.onCommandTyped(pl, label, msg1.replaceFirst("/"+label, "").replaceFirst(" ", "").split(" "));
			} else {
				pl.sendMessage(ChatColor.RED+"You can't talk without partner/rival");
			}
		}
		else if(p.getId() == 14) {
			new Packet15ServerInfos(((Packet14ServerPing)p).name, message).sendConnectionTCP(c);
		}
	}

	@Override
	public void onConnected(Connection c) {}

	@Override
	public void onDisconnected(Connection c) {
		Player p = PlayerManager.getPlayer(c);
		if(p != null) {
			Match m = MatchManager.getMatch(p);
			System.out.println("Player "+p.getClientName()+"("+c.getRemoteAddressTCP().getHostString()+") disconnected");
			m.getAnother(p).kick("Your partner disconnected");
		}
	}

}
