package com.amadornes.tbircme.util;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.tbircme.network.Server;

public class Config {
	
	public static List<Server> servers = new ArrayList<Server>();
	
	public static String command = "tbircme cmd";
	
	public static boolean emotesEnabled = true;
	public static boolean shouldConfigGuiPauseGame = true;
	
	public static void saveConfig(){
		
	}
	
}
