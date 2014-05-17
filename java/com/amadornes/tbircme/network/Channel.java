package com.amadornes.tbircme.network;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.tbircme.permissions.User;

public class Channel {

	private String channel;
	private List<User> users = new ArrayList<User>();

	public Channel(String channel) {
		this.channel = channel;
	}
	
	public List<User> getUsers() {
		return users;
	}

	public void addUserIfNotConnected(String username) {
		for (User u : users)
			if (u.getUsername().equalsIgnoreCase(username))
				return;
		users.add(new User(username, false, false, false));
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

}
