package com.amadornes.tbircme.network;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.tbircme.permissions.User;

public class Channel {

	private Server server;
	private String channel;
	private List<User> users = new ArrayList<User>();

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
	
	public void addUser(User user){
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

}
