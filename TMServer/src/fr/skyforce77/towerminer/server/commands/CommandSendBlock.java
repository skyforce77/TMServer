package fr.skyforce77.towerminer.server.commands;
import fr.skyforce77.towerminer.protocol.packets.Packet23BlockChange;
import fr.skyforce77.towerminer.server.commands.Argument.ArgumentType;
import fr.skyforce77.towerminer.server.players.Player;

public class CommandSendBlock extends Command {

	@Override
	public void onTyped(Player p, String[] args) {
		int x = Integer.parseInt(args[0]);
		int y = Integer.parseInt(args[1]);
		int id = Integer.parseInt(args[2]);
		int data = Integer.parseInt(args[3]);
		boolean overlay = Boolean.parseBoolean(args[4]);
		p.sendTCP(new Packet23BlockChange(x, y, id, data, overlay));
	}
	
	@Override
	public boolean isCorrect(String[] args) {
		if(args.length >= 5 && !args[0].equals("")) {
			return true;
		}
		return false;
	}
	
	@Override
	public void onInitialized(String label) {
		setArguments(new Argument("x", ArgumentType.Integer), new Argument("y", ArgumentType.Integer), new Argument("id", ArgumentType.Integer),
				new Argument("data", ArgumentType.Integer), new Argument("overlay", ArgumentType.Boolean));
	}

}
