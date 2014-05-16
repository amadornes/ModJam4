package com.amadornes.tbircme;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.network.IRCConnection;
import com.amadornes.tbircme.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class TheBestIRCModEver {

	@Instance(ModInfo.MODID)
	public static TheBestIRCModEver inst;

	@SidedProxy(serverSide = ModInfo.PROXY_SERVER, clientSide = ModInfo.PROXY_CLIENT)
	public static CommonProxy proxy;

	public static Logger log = Logger.getLogger("TBIRCME");

	public static Configuration cfg = new Configuration();

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		// proxy.loadConfig(ev.getSuggestedConfigurationFile().getParentFile());
		log.log(Level.INFO, "STARTING CONNECTION \\o/");
		final IRCConnection irc = new IRCConnection("irc.esper.net", "TheBestIRCMod_Test");
		irc.connect();
		irc.waitUntilConnected();
		irc.join("Framez");
		irc.chat("Framez", "HOLY SHIT THIS WORKS \\o/");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				irc.disconnect();
			}
		}));
		log.log(Level.INFO, irc.isConnected() ? "CONNECTED \\o/" : "Not connected D:");
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {

	}

}
