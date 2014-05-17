package com.amadornes.tbircme.permissions;

public class User {

	private String username = "";
	private boolean online = true, op = false, voice = false;

	public User(String username, boolean online, boolean op, boolean voice) {
		this.username = username;
		this.online = online;
		this.op = op;
		this.voice = voice;
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

}
