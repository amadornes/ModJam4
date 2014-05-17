package com.amadornes.tbircme.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.amadornes.tbircme.ModInfo;

public class Emote {

	private String emote;
	private String url;
	private File file;
	private boolean hasFile = false;
	private int tex = -1;
	private BufferedImage img;

	public Emote(String name, String url) {
		this.emote = name;
		this.url = url;
		this.file = new File("../../mods/" + ModInfo.MODID + "/emotes/" + emote + ".png");
		hasFile = this.file.exists();
	}

	public void download() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileUtils.copyURLToFile(new URL(url), file);
					hasFile = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	public File getFile() {
		return file;
	}

	public String getFilePath() {
		return file.getAbsolutePath();
	}

	public boolean hasFile() {
		return hasFile;
	}

	public int getTexture() {
		return tex;
	}

	public void setTexture(int texture) {
		this.tex = texture;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}

	public String getEmote() {
		return emote;
	}

}
