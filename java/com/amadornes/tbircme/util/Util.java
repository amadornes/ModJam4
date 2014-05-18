package com.amadornes.tbircme.util;

public class Util {
	
	public static final boolean isMCPCInstalled(){
		try{
			Class.forName("org.bukkit.Bukkit");
			return true;
		}catch(Exception ex){}
		return false;
	}
	
	public static boolean isDeobfuscated(){
		try{
			Class.forName("net.minecraft.block.Block");
			return true;
		}catch(Exception ex){}
		return false;
	}

}
