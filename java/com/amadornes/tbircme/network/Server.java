package com.amadornes.tbircme.network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private String name;
	
	private String host, serverpass, username;
	private List<String> channels, commands;
	private boolean showIngameJoins, showIngameParts, showDeaths, showIRCJoins, showIRCParts;
	private List<String> cmdVoice = new ArrayList<String>(), cmds = new ArrayList<String>();
	private File configFile;

	private IRCConnection irc;

	public Server(String name, String host, String serverpass, String username, List<String> channels,
			List<String> commands, boolean showIngameJoins, boolean showIngameParts,
			boolean showDeaths, boolean showIRCJoins, boolean showIRCParts, File configFile) {
		this.name = name;
		
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
		
		this.configFile = configFile;
	}
	
	public String getName() {
		return name;
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

	public List<String> getStartupCommands() {
		return commands;
	}

	public void connect() {
		final Server me = this;
		new Thread(new Runnable() {

			@Override
			public void run() {
				final IRCConnection irc = new IRCConnection(me, host, username, serverpass);
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

	public List<String> getVoiceCommands() {
		return cmdVoice;
	}

	public List<String> getCommands() {
		return cmds;
	}
	
	public void addVoiceCommand(String cmd){
		cmdVoice.add(cmd);
	}
	
	public void removeVoiceCommand(String cmd){
		cmdVoice.remove(cmd);
	}
	
	public void addCommand(String cmd){
		cmds.add(cmd);
	}
	
	public void removeCommand(String cmd){
		cmds.remove(cmd);
	}
	
	public File getConfigFile() {
		return configFile;
	}

}
