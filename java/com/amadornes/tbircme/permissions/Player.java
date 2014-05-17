package com.amadornes.tbircme.permissions;

public class Player implements IUser {

	private String username;

	public Player(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
