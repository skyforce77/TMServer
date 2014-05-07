package fr.skyforce77.towerminer.server.commands;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;
import fr.skyforce77.towerminer.server.players.PlayerManager;

public class CommandNotif extends Command {

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
		
		for(Integer c : PlayerManager.getPlayers().keySet()) {
			PlayerManager.getPlayer(c).sendPopupMessage(s);
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
		setArguments(new Argument("text", ArgumentType.String));
	}

}
