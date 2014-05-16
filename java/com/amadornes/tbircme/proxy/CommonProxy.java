package com.amadornes.tbircme.proxy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.Config;
import com.amadornes.tbircme.network.IRCConnection;

public class CommonProxy {

	public void loadConfig(File configFile) {
		Configuration cfg = new Configuration(configFile);
		cfg.load();
		
		cfg.get("servers", "irc.esper.net", new String[]{"Framez", "amadornes"});

		// Servers and channels
		{
			ConfigCategory servers = cfg.getCategory("servers");
			// Loop through all of them
			for (String server : servers.keySet()) {
				// Get the data for that server
				String[] channels = servers.get(server).getStringList();
				List<String> channelsL = new ArrayList<String>();

				for (String ch : channels)
					channelsL.add(ch);

				Config.servers.put(server, channelsL);
			}
		}

		// Usernames for each server
		{
			ConfigCategory users = cfg.getCategory("usernames");
			// Loop through all of them
			for (String server : users.keySet()) {
				String username = users.get(server).getString();

				Config.usernames.put(server, username);
			}
		}

		// Get&set default username for all servers that don't have a custom one
		{
			Config.defaultUsername = cfg.get("login", "username", "TheBestIRCModEver_User").getString();
			
			for(String server : Config.servers.keySet()){
				boolean exists = false;
				for(String sv : Config.usernames.keySet())
					if(server.equalsIgnoreCase(sv)){
						exists = true;
						break;
					}
				if(!exists)
					Config.usernames.put(server, Config.defaultUsername);
			}
		}

		cfg.save();
	}

	protected void loadServerConfigurations(Configuration cfg) {
	}

	public void connectToServers() {
		for (final String s : Config.servers.keySet()) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					final IRCConnection irc = new IRCConnection(s, "TheBestIRCMod_Test");
					irc.connect();
					irc.waitUntilConnected();
					for (String c : Config.servers.get(s)) {
						irc.join(c);
					}
					Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
						@Override
						public void run() {
							irc.disconnect();
						}
					}));
				}
			}, "[IRC] " + s).start();
		}
	}

}
