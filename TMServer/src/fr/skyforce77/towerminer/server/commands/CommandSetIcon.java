package fr.skyforce77.towerminer.server.commands;
import java.awt.image.BufferedImage;

import fr.skyforce77.towerminer.protocol.save.TMImage;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.server.Server;
import fr.skyforce77.towerminer.server.chat.ChatColor;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandSetIcon extends Command {

	@Override
	public void onTyped(final Player p, String[] args) {
		String s = "";
		for(String arg : args) {
			if(!s.equals("")) {
				s = s+" "+arg;
			} else {
				s = s+arg;
			}
		}
		final String st = s;

		if(!(s.endsWith("png") || s.endsWith("gif"))) {
			p.sendMessage(ChatColor.RED+"Your image isn't correct. You have to use a gif or png image");
			return;
		}

		new Thread() {
			public void run() {
				p.sendMessage(ChatColor.CYAN+"Reading URL...");
				final BufferedImage i = RessourcesManager.getDistantBufferedTexture(st);
				p.sendMessage(ChatColor.CYAN+"Finished!");
				if(i != null && (i.getWidth() > 500 || i.getHeight() > 500)) {
					p.sendMessage(ChatColor.RED+"Your image isn't correct. You have to use a smaller image than 500*500");
					return;
				} else if(i != null) {
					Server.storage.addObject("icon", new TMImage(i));
					p.sendServerPopup();
				}
			};
		}.start();
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
		setArguments(new Argument("url", ArgumentType.Url));
	}

}
