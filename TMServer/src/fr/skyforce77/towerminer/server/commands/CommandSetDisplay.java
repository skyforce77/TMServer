package fr.skyforce77.towerminer.server.commands;
import fr.skyforce77.towerminer.server.Server;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandSetDisplay extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		String s = "";
		int i = 0;
		for(String arg : args) {
			if(i != 0) {
				s = s+" "+arg;
			}
			i++;
		}
		
		if(args[0].equals("motd")) {
			Server.storage.addString("motd", s);
		} else {
			Server.storage.addString("name", s);
		}
		p.sendServerPopup();
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length >= 2 && (args[0].equals("motd") || args[0].equals("name"))) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("type", new ArgumentType("motd/name")), new Argument("value", ArgumentType.String));
	}

}
