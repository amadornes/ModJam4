package com.amadornes.tbircme.network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

public class Server {

	private String name;

	private String host, serverpass, username;
	private List<String> channels, commands;
	private boolean showIngameJoins, showIngameParts, showDeaths, showIRCJoins, showIRCParts;
	private List<String> cmdVoice = new ArrayList<String>(), cmds = new ArrayList<String>();
	private File configFile;
	
	private boolean connecting = false;

	private IRCConnection irc;

	public Server(File configFile) {
		this.configFile = configFile;
	}

	public Server(String name, String host, String serverpass, String username,
			List<String> channels, List<String> commands, boolean showIngameJoins,
			boolean showIngameParts, boolean showDeaths, boolean showIRCJoins,
			boolean showIRCParts, File configFile) {
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

	public String getServerpass() {
		return serverpass;
	}

	public boolean isConnected() {
		if (irc == null)
			return false;
		return irc.isConnected();
	}
	
	public boolean isConnecting(){
		return connecting;
	}

	public synchronized void connect() {
		if(connecting)
			return;
		
		connecting = true;
		
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
				connecting = false;
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

	public void addVoiceCommand(String cmd) {
		cmdVoice.add(cmd);
	}

	public void removeVoiceCommand(String cmd) {
		cmdVoice.remove(cmd);
	}

	public void addCommand(String cmd) {
		cmds.add(cmd);
	}

	public void removeCommand(String cmd) {
		cmds.remove(cmd);
	}

	public File getConfigFile() {
		return configFile;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setServerpass(String serverpass) {
		this.serverpass = serverpass;
	}

	public void setShowDeaths(boolean showDeaths) {
		this.showDeaths = showDeaths;
	}

	public void setShowIngameJoins(boolean showIngameJoins) {
		this.showIngameJoins = showIngameJoins;
	}

	public void setShowIngameParts(boolean showIngameParts) {
		this.showIngameParts = showIngameParts;
	}

	public void setShowIRCJoins(boolean showIRCJoins) {
		this.showIRCJoins = showIRCJoins;
	}

	public void setShowIRCParts(boolean showIRCParts) {
		this.showIRCParts = showIRCParts;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void loadConfig() {
		String name = "";
		String host = "", username = "", serverpass = null;
		List<String> commands = new ArrayList<String>(), channels = new ArrayList<String>();
		boolean showIngameJoins = false, showIngameParts = false, showDeaths = false, showIRCJoins = false, showIRCParts = false;

		Configuration cfg = new Configuration(getConfigFile());
		cfg.load();
		// Misc section
		{
			name = cfg.get("misc", "serverName", "Server").getString();
		}
		// Login section
		{
			host = cfg
					.get("login", "host", "",
							"Host the bridge will connect to (for example: irc.esper.net). Can include a port.")
					.getString().trim();
			serverpass = cfg
					.get("login", "pass", "",
							"The server's password (if using twitch, this is your oauth code).")
					.getString().trim();
			username = cfg
					.get("login", "username", "TheBestIRCModEver",
							"Username the bridge will use when connected to this server.")
					.getString().trim();
			String[] cmds = cfg.get("login", "commands", new String[] {},
					"Commands to run after logging in (like Nickserv identify).").getStringList();
			for (String s : cmds)
				commands.add(s.trim());
		}
		// Channels section
		{
			String[] ch = cfg.get("channels", "channels", new String[] {},
					"Channels that the bridge will work with. One on each line.").getStringList();
			for (String s : ch)
				channels.add(s.trim());
		}
		// Messages
		{
			showIngameJoins = cfg.get("messages", "showIngameJoins", true).getBoolean(true);
			showIngameParts = cfg.get("messages", "showIngameParts", true).getBoolean(true);
			showDeaths = cfg.get("messages", "showDeaths", true).getBoolean(true);
			showIRCJoins = cfg.get("messages", "showIRCJoins", true).getBoolean(true);
			showIRCParts = cfg.get("messages", "showIRCParts", true).getBoolean(true);
		}

		cfg.save();

		this.name = name.trim();
		this.host = host.trim();
		this.serverpass = serverpass.trim().length() == 0 ? null : serverpass.trim();
		this.username = username.trim();
		this.channels = channels;
		this.commands = commands;
		this.showIngameJoins = showIngameJoins;
		this.showIngameParts = showIngameParts;
		this.showDeaths = showDeaths;
		this.showIRCJoins = showIRCJoins;
		this.showIRCParts = showIRCParts;

		// Permissions
		{
			String[] voice = cfg.get("permissions", "voiceCommands", new String[] {})
					.getStringList();
			String[] normal = cfg.get("permissions", "publicCommands", new String[] {})
					.getStringList();

			getVoiceCommands().clear();
			getCommands().clear();

			for (String s : voice)
				addVoiceCommand(s);
			for (String s : normal)
				addCommand(s);
		}
		cfg.save();
	}

	public void saveConfig() {
		Configuration cfg = new Configuration(getConfigFile());
		cfg.load();
		// Misc section
		{
			cfg.get("misc", "serverName", name).set(name == null ? "Server" : name);
		}
		// Login section
		{
			cfg.get("login", "host", host,
					"Host the bridge will connect to (for example: irc.esper.net). Can include a port.")
					.set(host);
			cfg.get("login", "pass", serverpass,
					"The server's password (if using twitch, this is your oauth code).").set(
					serverpass == null ? "" : serverpass);
			cfg.get("login", "username", username,
					"Username the bridge will use when connected to this server.").set(username);
			String[] cmds = commands.toArray(new String[] {});
			cfg.get("login", "commands", cmds,
					"Commands to run after logging in (like Nickserv identify).").set(cmds);
		}
		// Channels section
		{
			String[] ch = channels.toArray(new String[] {});
			cfg.get("channels", "channels", ch,
					"Channels that the bridge will work with. One on each line.").set(ch);
		}
		// Messages
		{
			cfg.get("messages", "showIngameJoins", showIngameJoins).set(showIngameJoins);
			cfg.get("messages", "showIngameParts", showIngameParts).set(showIngameParts);
			cfg.get("messages", "showDeaths", showDeaths).set(showDeaths);
			cfg.get("messages", "showIRCJoins", showIRCJoins).set(showIRCJoins);
			cfg.get("messages", "showIRCParts", showIRCParts).set(showIRCParts);
		}

		cfg.save();
	}

}
