package com.amadornes.tbircme.util;

import java.io.File;
import java.net.URL;

import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.FileUtils;

import com.amadornes.tbircme.ModInfo;

public class Emote {

	private String emote;
	private String url;
	private File file;

	public Emote(String name, String url) {
		this.emote = name;
		this.url = url;
		this.file = new File("./mods/" + ModInfo.MODID + "/emotes/" + emote + ".png");
	}

	public void download() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileUtils.copyURLToFile(new URL(url), file);
				} catch (Exception e) {
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
		return file.exists();
	}

	public ResourceLocation getResource() {
		return new ResourceLocation(getFilePath());
	}

	public String getEmote() {
		return emote;
	}

}
