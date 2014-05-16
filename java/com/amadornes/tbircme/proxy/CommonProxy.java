package com.amadornes.tbircme.proxy;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.ModInfo;

public class CommonProxy {

	public void loadConfig(File mainConfigFolder) {
		File configFolder = new File(mainConfigFolder, ModInfo.MODID + "/");

		if (!configFolder.exists())
			configFolder.mkdirs();

		loadConfig(configFolder);

	}

	protected void loadServerConfigurations(File configFolder) {
		Configuration cfg = new Configuration(new File(configFolder, "servers.cfg"));
		cfg.load();
		
		cfg.get("servers", "servers", new String[]{});
		cfg.get("servers", "autoconnectServers", new String[]{});
		
		cfg.save();
	}

}
