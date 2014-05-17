package com.amadornes.tbircme.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;

public class CommonProxy {

	public void loadConfig(File configFile) {
		File serverConfigFolder = new File(configFile.getParentFile(), ModInfo.MODID + "/servers/");
		if (!serverConfigFolder.exists())
			serverConfigFolder.mkdirs();

		File exampleServerConfig = new File(serverConfigFolder, "example.cfg");
		if (!exampleServerConfig.exists())
			createExampleServerConfig(exampleServerConfig);

		for (File f : serverConfigFolder.listFiles()) {
			if (f.isFile() && f.getName().toLowerCase().endsWith(".cfg")) {
				if (!f.getName().equalsIgnoreCase("example.cfg")) {
					loadServerConfig(f);
				}
			}
		}
	}

	private void loadServerConfig(File file) {
		String host = "", username = "", serverpass = null;
		List<String> commands = new ArrayList<String>(), channels = new ArrayList<String>();
		boolean showIngameJoins = false, showIngameParts = false, showDeaths = false, showIRCJoins = false, showIRCParts = false;

		Configuration cfg = new Configuration(file);
		cfg.load();
		{
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
						"Commands to run after logging in (like Nickserv identify).")
						.getStringList();
				for (String s : cmds)
					commands.add(s.trim());
			}
			// Channels section
			{
				String[] ch = cfg.get("channels", "channels", new String[] {},
						"Channels that the bridge will work with. One on each line.")
						.getStringList();
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
		}
		cfg.save();

		if (host == null || host == "" || username == null || username == ""
				|| channels.size() == 0)
			return;

		Config.servers.add(new Server(host.trim(), serverpass.trim().length() == 0 ? null
				: serverpass.trim(), username.trim(), channels, commands, showIngameJoins,
				showIngameParts, showDeaths, showIRCJoins, showIRCParts));
	}

	private void createExampleServerConfig(File file) {
		Configuration cfg = new Configuration(file);
		cfg.load();
		{
			// Login section
			{
				cfg.get("login", "host", "",
						"Host the bridge will connect to (for example: irc.esper.net). Can include a port.");
				cfg.get("login", "pass", "",
						"The server's password (if using twitch, this is your oauth code).");
				cfg.get("login", "username", "TheBestIRCModEver",
						"Username the bridge will use when connected to this server.");
				cfg.get("login", "commands", new String[] {},
						"Commands to run after logging in (like Nickserv identify).");
			}
			// Channels section
			{
				cfg.get("channels", "channels", new String[] {},
						"Channels that the bridge will work with. One on each line.");
			}
			// Messages
			{
				cfg.get("messages", "showIngameJoins", true);
				cfg.get("messages", "showIngameParts", true);
				cfg.get("messages", "showDeaths", true);
				cfg.get("messages", "showIRCJoins", true);
				cfg.get("messages", "showIRCParts", true);
			}
		}
		cfg.save();
	}

	public void connectToServers() {
		for (Server s : Config.servers)
			s.connect();
	}

	public void disconnectFromServers() {
		for (Server s : Config.servers)
			s.disconnect();
	}

}
