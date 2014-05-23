package com.amadornes.tbircme.compat.bukkit;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.amadornes.tbircme.network.IRCConnection;

public class ChatListener implements Listener {
	
	private IRCConnection con;
	
	public ChatListener(IRCConnection con) {
		this.con = con;
	}
	
	@EventHandler
	public void onIngameChat(AsyncPlayerChatEvent e) {
		con.onIngameChat(e.getPlayer().getName(), e.getMessage());
	}

}
