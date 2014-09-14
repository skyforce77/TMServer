package fr.skyforce77.towerminer.server.chat;

import java.awt.Color;
import java.util.ArrayList;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;

public class ChatColor {
	
	public static ArrayList<ChatColor> colors = new ArrayList<>();
	
	public static ChatColor WHITE = new ChatColor('0', 0xF0F0F0);
	public static ChatColor ORANGE = new ChatColor('1', 0xEB8844);
	public static ChatColor MAGENTA = new ChatColor('2', 0xC354CD);
	public static ChatColor LIGHT_BLUE = new ChatColor('3', 0x6689D3);
	public static ChatColor YELLOW = new ChatColor('4', 0xDECF2A);
	public static ChatColor LIME = new ChatColor('5', 0x41CD34);
	public static ChatColor PINK = new ChatColor('6', 0xD88198);
	public static ChatColor GRAY = new ChatColor('7', 0x434343);
	public static ChatColor SILVER = new ChatColor('8', 0xABABAB);
	public static ChatColor CYAN = new ChatColor('9', 0x287697);
	public static ChatColor PURPLE = new ChatColor('a', 0x7B2FBE);
	public static ChatColor BLUE = new ChatColor('b', 0x253192);
	public static ChatColor BROWN = new ChatColor('c', 0x51301A);
	public static ChatColor GREEN = new ChatColor('d', 0x3B511A);
	public static ChatColor RED = new ChatColor('e', 0xB3312C);
	public static ChatColor BLACK = new ChatColor('f', 0x1E1B1B);

	private int rgb;
	private char identifier;
	
	public ChatColor(char identifier, int rgb) {
		this.rgb = rgb;
		this.identifier = identifier;
	}
	
	public ChatColor(char identifier, int r, int g, int b) {
		rgb = new Color(r, g, b).getRGB();
		this.identifier = identifier;
	}
	
	public int asRGB() {
		return rgb;
	}
	
	public char asChar() {
		return identifier;
	}
	
	@Override
	public String toString() {
		return "&Color:"+asRGB()+":&Color";
	}

	public static ChatMessage getChatMessage(String text) {
		for(ChatColor c : colors) {
			text.replaceAll("&"+c.asChar(), c.toString());
		}
		
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
