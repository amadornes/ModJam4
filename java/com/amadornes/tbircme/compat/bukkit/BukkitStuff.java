package com.amadornes.tbircme.compat.bukkit;

import org.bukkit.Bukkit;

import com.amadornes.tbircme.network.IRCConnection;

public class BukkitStuff {

	public static void registerChatEventsForConnection(IRCConnection con) {
		Bukkit.getPluginManager().registerEvents(new ChatListener(con),
				Bukkit.getPluginManager().getPlugin("tbircme"));
	}

	public static void waitUntilEnabled() {
		while (!Bukkit.getPluginManager().getPlugin("tbircme").isEnabled()) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
			}
		}
	}

}
