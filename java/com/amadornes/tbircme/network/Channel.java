package com.amadornes.tbircme.network;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.tbircme.permissions.User;

public class Channel {

	private Server server;
	private String channel;
	private List<User> users = new ArrayList<User>();

	private String chatFormatting = "<$u> $m";
	private String chatFormattingIRC = "[#$c] <$u> $m";

	private String connectionFormatting = "* $u has $a the server.";
	private String connectionFormattingIRC = "[#$c] * $u has $a the channel.";

	private String emoteFormatting = "* \002$u\002 $m";
	private String emoteFormattingIRC = "[#$c] * \u00A7l$u\u00A7r $m";

	private String deathFormatting = "* $m";

	public Channel(Server server, String channel) {
		this.server = server;
		this.channel = channel;
	}

	public Server getServer() {
		return server;
	}

	public List<User> getUsers() {
		return users;
	}

	public void addUserIfNotConnected(String username) {
		for (User u : users)
			if (u.getUsername().equalsIgnoreCase(username))
				return;
		users.add(new User(this, username, false, false, false));
	}

	public void addUser(User user) {
		users.add(user);
	}

	public User getUser(String username) {
		for (User u : users)
			if (u.getUsername().equalsIgnoreCase(username))
				return u;
		addUserIfNotConnected(username);
		return getUser(username);
	}

	public String getChannel() {
		return channel;
	}

	public String formatChatMessage(String user, String message) {
		return chatFormatting.replace("$u", user).replace("$m", message);
	}

	public String formatIRCMessage(String user, String message) {
		return chatFormattingIRC.replace("$u", user).replace("$m", message).replace("$c", channel);
	}

	public String formatChatEmote(String user, String message) {
		return emoteFormatting.replace("$u", user).replace("$m", message);
	}

	public String formatIRCEmote(String user, String message) {
		return emoteFormattingIRC.replace("$u", user).replace("$m", message).replace("$c", channel);
	}

	public String formatChatConnection(String user, boolean connected) {
		return connectionFormatting.replace("$u", user)
				.replace("$a", connected ? "joined" : "left");
	}

	public String formatIRCConnection(String user, boolean connected) {
		return connectionFormattingIRC.replace("$u", user)
				.replace("$a", connected ? "entered" : "left").replace("$c", channel);
	}

	public String formatDeath(String message) {
		return deathFormatting.replace("$m", message);
	}

	@Override
	public String toString() {
		return getChannel();
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public void setChatFormatting(String chatFormatting) {
		this.chatFormatting = chatFormatting;
	}

	public void setChatFormattingIRC(String chatFormattingIRC) {
		this.chatFormattingIRC = chatFormattingIRC;
	}

	public void setConnectionFormatting(String connectionFormatting) {
		this.connectionFormatting = connectionFormatting;
	}

	public void setConnectionFormattingIRC(String connectionFormattingIRC) {
		this.connectionFormattingIRC = connectionFormattingIRC;
	}

	public void setDeathFormatting(String deathFormatting) {
		this.deathFormatting = deathFormatting;
	}

	public void setEmoteFormatting(String emoteFormatting) {
		this.emoteFormatting = emoteFormatting;
	}

	public void setEmoteFormattingIRC(String emoteFormattingIRC) {
		this.emoteFormattingIRC = emoteFormattingIRC;
	}

	public void setServer(Server server) {
		this.server = server;
	}

}
