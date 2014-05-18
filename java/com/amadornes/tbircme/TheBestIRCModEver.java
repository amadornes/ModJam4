package com.amadornes.tbircme;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.command.CommandTBIRCME;
import com.amadornes.tbircme.emote.Emote;
import com.amadornes.tbircme.emote.EmoteAPI;
import com.amadornes.tbircme.gui.GuiConfig;
import com.amadornes.tbircme.network.IRCConnection;
import com.amadornes.tbircme.packet.PacketHandler;
import com.amadornes.tbircme.proxy.CommonProxy;
import com.amadornes.tbircme.util.ReflectionUtils;
import com.google.common.collect.BiMap;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

	public static List<Emote> emotes = new ArrayList<Emote>();

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		proxy.loadConfig(ev.getSuggestedConfigurationFile());
	}

	@EventHandler
	public void init(FMLInitializationEvent ev) {
		channels = NetworkRegistry.INSTANCE.newChannel(ModInfo.MODID, new PacketHandler());

		com.amadornes.tbircme.handler.EventHandler handler = new com.amadornes.tbircme.handler.EventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);

		proxy.registerRenders();
	}

	//@SuppressWarnings("unchecked")
	@EventHandler
	@SideOnly(Side.CLIENT)
	public void postInit(FMLPostInitializationEvent ev) {
		log.log(Level.INFO, "Loading emotes...");
		EmoteAPI.init();
		log.log(Level.INFO, "Emotes loaded!");
		/*new Thread(new Runnable() {

			@Override
			public void run() {
				BiMap<ModContainer, IModGuiFactory> factories = null;
				do {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					factories = (BiMap<ModContainer, IModGuiFactory>) ReflectionUtils.get(
							FMLClientHandler.instance(), "guiFactories");
				} while (!Loader.instance().hasReachedState(LoaderState.AVAILABLE)
						|| factories == null);

				ModContainer c = null;
				for (ModContainer mc : Loader.instance().getActiveModList())
					if (mc.getModId() == ModInfo.MODID) {
						c = mc;
						break;
					}
				final IModGuiFactory f = factories.get(c);
				factories.remove(c);
				factories.put(c, new IModGuiFactory() {

					@Override
					public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
						return f.runtimeGuiCategories();
					}

					@Override
					public Class<? extends GuiScreen> mainConfigGuiClass() {
						return GuiConfig.class;
					}

					@Override
					public void initialize(Minecraft minecraftInstance) {
						f.initialize(minecraftInstance);
					}

					@Override
					public RuntimeOptionGuiHandler getHandlerFor(
							RuntimeOptionCategoryElement element) {
						return f.getHandlerFor(element);
					}
				});
			}
		}).start();*/
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
