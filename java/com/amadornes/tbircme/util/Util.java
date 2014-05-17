package com.amadornes.tbircme.util;

public class Util {
	
	public static final boolean isMCPCInstalled(){
		try{
			Class.forName("org.bukkit.Bukkit");
		}catch(Exception ex){}
		return false;
	}

}
