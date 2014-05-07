package fr.skyforce77.towerminer.server.commands;
import java.awt.image.BufferedImage;

import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.server.Server;
import fr.skyforce77.towerminer.server.chat.ChatColor;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.save.TMImage;

public class CommandSetIcon extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		String s = "";
		for(String arg : args) {
			if(!s.equals("")) {
				s = s+" "+arg;
			} else {
				s = s+arg;
			}
		}
		
		if(!(s.endsWith("png") || s.endsWith("gif"))) {
			p.sendMessage(ChatColor.RED+"Your image isn't correct. You have to use a gif or png image");
			return;
		}
		
		BufferedImage i = RessourcesManager.getDistantBufferedTexture(s);
		if(i != null && (i.getWidth() > 100 || i.getHeight() > 100)) {
			p.sendMessage(ChatColor.RED+"Your image isn't correct. You have to use a smaller image than 100*100");
			return;
		} else if(i != null) {
			Server.storage.addObject("icon", new TMImage(i));
		}
		
		p.sendServerPopup();
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
