package com.amadornes.tbircme.permissions;

import com.amadornes.tbircme.network.Channel;

public class User {

	private String username = "";
	private boolean online = true, op = false, voice = false;
	private Channel channel;

	public User(Channel channel, String username, boolean online, boolean op, boolean voice) {
		this.username = username;
		this.channel = channel;
		this.online = online;
		this.op = op;
		this.voice = voice;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getUsername() {
		return username;
	}

	public boolean isOnline() {
		return online;
	}

	public boolean isOp() {
		return op;
	}

	public boolean isVoice() {
		return voice;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public void setOp(boolean op) {
		this.op = op;
	}

	public void setVoice(boolean voice) {
		this.voice = voice;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean canRunCommand(String command) {

		System.out.println(username + " " + isOp() + " " + isVoice());
		
		if (isOp())
			return true;

		if (isVoice())
			for (String c : channel.getServer().getVoiceCommands())
				if (command.toLowerCase().startsWith(c.toLowerCase()))
					return true;

		for (String c : channel.getServer().getCommands())
			if (command.toLowerCase().startsWith(c.toLowerCase()))
				return true;

		return false;
	}

}
