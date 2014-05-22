package com.amadornes.tbircme.util;

public class Util {

	public static final boolean isMCPCInstalled() {
		try {
			Class.forName("org.bukkit.Bukkit");
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

	public static boolean isDeobfuscated() {
		return !isDeobfuscated_();
	}

	private static boolean isDeobfuscated_() {

		try {
			Class.forName("net.minecraft.block.Block").getDeclaredField("field_149768_d");
			return true;
		} catch (Exception ex) {
		}
		return false;
	}

}
