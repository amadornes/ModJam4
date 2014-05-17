package com.amadornes.tbircme.network;

import java.util.List;

public class Server {

	private String host, serverpass, username;
	private List<String> channels, commands;
	
	private IRCConnection irc;

	public Server(String host, String serverpass, String username, List<String> channels, List<String> commands) {
		this.host = host;
		this.serverpass = serverpass;
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
		final Server me = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				final IRCConnection irc = new IRCConnection(host, username, serverpass);
				me.irc = irc;
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
	
	public void disconnect(){
		irc.ragequit();
	}
	
	public IRCConnection getConnection() {
		return irc;
	}

}
