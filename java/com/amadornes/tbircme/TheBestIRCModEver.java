package com.amadornes.tbircme;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.command.CommandTBIRCME;
import com.amadornes.tbircme.emote.Emote;
import com.amadornes.tbircme.emote.EmoteAPI;
import com.amadornes.tbircme.network.IRCConnection;
import com.amadornes.tbircme.proxy.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, acceptableRemoteVersions = "*", guiFactory = ModInfo.GUI_FACTORY)
public class TheBestIRCModEver {

	@Instance(ModInfo.MODID)
	public static TheBestIRCModEver inst;

	@SidedProxy(serverSide = ModInfo.PROXY_SERVER, clientSide = ModInfo.PROXY_CLIENT)
	public static CommonProxy proxy;

	public static Logger log = Logger.getLogger("TBIRCME");

	public static Configuration cfg = new Configuration();

	public static List<IRCConnection> connections = new ArrayList<IRCConnection>();

	public static boolean canChangeServerConfig = false;

	public static List<Emote> emotes = new ArrayList<Emote>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		System.out.println("Test!");
		proxy.loadConfig(ev.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {
		com.amadornes.tbircme.handler.EventHandler handler = new com.amadornes.tbircme.handler.EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);

		proxy.registerRenders();
	}

	@EventHandler
	@SideOnly(Side.CLIENT)
	public void postInit(FMLPostInitializationEvent ev) {
		log.log(Level.INFO, "Loading emotes...");
		EmoteAPI.init();
		log.log(Level.INFO, "Emotes loaded!");
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent ev) {
		ev.registerServerCommand(new CommandTBIRCME());
	}

	@EventHandler
	public void onServerPreStart(FMLServerAboutToStartEvent ev) {
		proxy.connectToServers();
	}

	@EventHandler
	public void onServerPreStop(FMLServerStoppingEvent ev) {
		proxy.disconnectFromServers();
	}

}
