package fr.skyforce77.towerminer.server.commands;
import fr.skyforce77.towerminer.server.chat.ChatColor;
import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.players.PlayerManager;

public class CommandName extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		String last = p.getDisplayName();
		p.setDisplayName(args[0]);
		
		ChatColor cc = ChatColor.WHITE;
		if(p.getMatch() != null) {
			if(p.getMatch().getBlue().equals(p)) {
				cc = ChatColor.CYAN;
			} else {
				cc = ChatColor.RED;
			}
		}
		for(Integer id : PlayerManager.getPlayers().keySet()) {
			PlayerManager.getPlayer(id).sendMessage(cc+last+ChatColor.WHITE+" is now know as "+cc+p.getDisplayName());
		}
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
		setArguments(new Argument("name", false, false));
	}

}
