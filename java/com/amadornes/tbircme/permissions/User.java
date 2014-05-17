package com.amadornes.tbircme.permissions;

public class User implements IUser {

	private String username;

	public User(String username) {
		this.username = username;
	}

	public boolean isOp() {
		return false;
	}

	public boolean isVoice() {
		return false;
	}

	public String getUsername() {
		return username;
	}
}
