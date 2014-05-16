package com.amadornes.tbircme.network;

import java.util.List;

public class Server {

	private String host, username;
	private List<String> channels, commands;

	public Server(String host, String username, List<String> channels, List<String> commands) {
		this.host = host;
		this.username = username;
		this.commands = commands;
		this.channels = channels;
	}

	public String getHost() {
		return host;
	}

	public String getUsername() {
		return username;
	}

	public List<String> getChannels() {
		return channels;
	}

	public List<String> getCommands() {
		return commands;
	}

	public void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				final IRCConnection irc = new IRCConnection(host, username);
				irc.connect();
				irc.waitUntilConnected();
				for (String c : commands) {
					irc.cmd(c);
				}
				for (String c : channels) {
					irc.join(c);
				}
				Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
					@Override
					public void run() {
						irc.disconnect();
					}
				}));
			}
		}, "[IRC] " + host).start();
	}

}
