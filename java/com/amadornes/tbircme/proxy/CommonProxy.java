package com.amadornes.tbircme.proxy;

import java.io.File;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;

public class CommonProxy {

	public File configFolder = null;

	public void loadConfig(File configFile) {
		File serverConfigFolder = new File(configFile.getParentFile(), ModInfo.MODID + "/servers/");
		if (!serverConfigFolder.exists())
			serverConfigFolder.mkdirs();

		configFolder = serverConfigFolder.getParentFile();

		File exampleServerConfig = new File(serverConfigFolder, "example.cfg");
		if (!exampleServerConfig.exists())
			new Server(exampleServerConfig).loadConfig();

		for (File f : serverConfigFolder.listFiles()) {
			if (f.isFile() && f.getName().toLowerCase().endsWith(".cfg")) {
				if (!f.getName().equalsIgnoreCase("example.cfg")) {
					Server s = new Server(f);
					s.loadConfig();
					Config.servers.add(s);
				}
			}
		}
	}

	public void connectToServers() {
		for (Server s : Config.servers)
			s.connect();
	}

	public void disconnectFromServers() {
		for (Server s : Config.servers)
			s.disconnect();
	}

	public void registerRenders() {
	}

}
