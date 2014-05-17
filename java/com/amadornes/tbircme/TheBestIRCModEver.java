package com.amadornes.tbircme;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.gui.GuiHandler;
import com.amadornes.tbircme.network.IRCConnection;
import com.amadornes.tbircme.packet.PacketHandler;
import com.amadornes.tbircme.proxy.CommonProxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION)
public class TheBestIRCModEver {

	@Instance(ModInfo.MODID)
	public static TheBestIRCModEver inst;

	@SidedProxy(serverSide = ModInfo.PROXY_SERVER, clientSide = ModInfo.PROXY_CLIENT)
	public static CommonProxy proxy;

	public static Logger log = Logger.getLogger("TBIRCME");

	public static Configuration cfg = new Configuration();

	public static List<IRCConnection> connections = new ArrayList<IRCConnection>();

	public static EnumMap<Side, FMLEmbeddedChannel> channels;

	public static boolean canChangeServerConfig = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		proxy.loadConfig(ev.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {
		channels = NetworkRegistry.INSTANCE.newChannel(ModInfo.MODID, new PacketHandler());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		com.amadornes.tbircme.handler.EventHandler handler = new com.amadornes.tbircme.handler.EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
		
		proxy.registerRenders();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent ev) {
		// TODO Re-enable command commandev.registerServerCommand(new
		// CommandTBIRCME());
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
