package fr.skyforce77.towerminer.server.save;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class TMImage implements Serializable{

	private static final long serialVersionUID = -2393731918366637418L;
	byte[] data;
	
	public TMImage(BufferedImage i) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
			ImageIO.write(i, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
        data = baos.toByteArray();
	}
	
	public BufferedImage getImage() {
		BufferedImage bu = null;
		try {
			bu = ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bu;
	}

}
