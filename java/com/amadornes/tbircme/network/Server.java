package com.amadornes.tbircme.network;

import java.util.List;

public class Server {

	private String host, serverpass, username;
	private List<String> channels, commands;
	private boolean showIngameJoins, showIngameParts, showDeaths, showIRCJoins, showIRCParts;

	private IRCConnection irc;

	public Server(String host, String serverpass, String username, List<String> channels,
			List<String> commands, boolean showIngameJoins, boolean showIngameParts,
			boolean showDeaths, boolean showIRCJoins, boolean showIRCParts) {
		this.host = host;
		this.serverpass = serverpass;
		this.username = username;
		this.commands = commands;
		this.channels = channels;

		this.showIngameJoins = showIngameJoins;
		this.showIngameParts = showIngameParts;
		this.showDeaths = showDeaths;
		this.showIRCJoins = showIRCJoins;
		this.showIRCParts = showIRCParts;
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

	public void disconnect() {
		irc.ragequit();
	}

	public IRCConnection getConnection() {
		return irc;
	}

	public boolean shouldShowDeaths() {
		return showDeaths;
	}

	public boolean shouldShowIngameJoins() {
		return showIngameJoins;
	}

	public boolean shouldShowIngameParts() {
		return showIngameParts;
	}

	public boolean shouldShowIRCJoins() {
		return showIRCJoins;
	}

	public boolean shouldShowIRCParts() {
		return showIRCParts;
	}

}
