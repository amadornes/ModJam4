package com.amadornes.tbircme.util;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	
	private static File mainConfigFile = null;
	
	public static void setConfigFile(File file){
		mainConfigFile = file;
	}
	
	public static void loadMainConfig(){
		Configuration cfg = new Configuration(mainConfigFile);
		cfg.load();

		Config.emotesEnabled = cfg.get("config", "emotes_enabled", true).getBoolean(true);
		Config.shouldConfigGuiPauseGame = cfg.get("config", "guiPauseGame", true).getBoolean(true);
		
		cfg.save();
	}
	
	public static void saveMainConfig(){
		Configuration cfg = new Configuration(mainConfigFile);
		cfg.load();
		
		cfg.get("config", "emotes_enabled", Config.emotesEnabled).set(Config.emotesEnabled);
		cfg.get("config", "guiPauseGame", Config.shouldConfigGuiPauseGame).set(Config.shouldConfigGuiPauseGame);
		
		cfg.save();
	}

}
