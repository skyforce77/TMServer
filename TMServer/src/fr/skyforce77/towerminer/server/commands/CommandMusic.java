package fr.skyforce77.towerminer.server.commands;
import fr.skyforce77.towerminer.protocol.packets.Packet16Sound;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandMusic extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		args[0] = args[0].replaceAll("https", "http");
		p.sendTCP(new Packet16Sound(0, true, args[0]));
		p.sendMessage("Sent music");
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length >= 0 && !args[0].equals("")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("url", ArgumentType.Url));
	}

}
