package fr.skyforce77.towerminer.server.chat;

import java.awt.Color;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;

public class ChatColor {
	
	
	public static ChatColor WHITE = new ChatColor(0xF0F0F0);
	public static ChatColor ORANGE = new ChatColor(0xEB8844);
	public static ChatColor MAGENTA = new ChatColor(0xC354CD);
	public static ChatColor LIGHT_BLUE = new ChatColor(0x6689D3);
	public static ChatColor YELLOW = new ChatColor(0xDECF2A);
	public static ChatColor LIME = new ChatColor(0x41CD34);
	public static ChatColor PINK = new ChatColor(0xD88198);
	public static ChatColor GRAY = new ChatColor(0x434343);
	public static ChatColor SILVER = new ChatColor(0xABABAB);
	public static ChatColor CYAN = new ChatColor(0x287697);
	public static ChatColor PURPLE = new ChatColor(0x7B2FBE);
	public static ChatColor BLUE = new ChatColor(0x253192);
	public static ChatColor BROWN = new ChatColor(0x51301A);
	public static ChatColor GREEN = new ChatColor(0x3B511A);
	public static ChatColor RED = new ChatColor(0xB3312C);
	public static ChatColor BLACK = new ChatColor(0x1E1B1B);

	private String rgb;
	
	public ChatColor(int rgb) {
		this.rgb = "&Color:"+rgb+":&Color";
	}
	
	public ChatColor(int r, int g, int b) {
		rgb = "&Color:"+new Color(r, g, b).getRGB()+":&Color";
	}
	
	public int asRGB() {
		return Integer.parseInt(rgb.replace("&Color:", "").replace(":&Color", ""));
	}
	
	@Override
	public String toString() {
		return rgb;
	}

	public static ChatMessage getChatMessage(String text) {
		if(text.contains("&Color:")) {
			String[] cut = text.split("&Color:");
			ChatMessage msg = new ChatMessage();
			int i = 0;
			
			while(i <= cut.length-1) {
				if(cut[i].contains(":&Color")) {
					ChatModel model = new ChatModel(cut[i].split(":&Color")[1]);
					model.setForegroundColor(new Color(Integer.parseInt(cut[i].split(":&Color")[0])));
					msg.addModel(model);
				} else {
					msg.addModel(new ChatModel(cut[i]));
				}
				i++;
			}
			
			return msg;
		} else {
			return new ChatMessage(new ChatModel(text));
		}
	}
}
