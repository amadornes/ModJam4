package com.amadornes.tbircme;

import com.amadornes.tbircme.command.CommandTBIRCME;
import com.amadornes.tbircme.network.NetworkHandler;
import com.amadornes.tbircme.proxy.CommonProxy;
import com.amadornes.tbircme.ref.ModInfo;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

@Mod(modid = ModInfo.MODID, name = ModInfo.NAME, version = ModInfo.VERSION, acceptableRemoteVersions = "*", guiFactory = ModInfo.GUI_FACTORY)
public class TheBestIRCModEver {
    
    @Instance(ModInfo.MODID)
    public static TheBestIRCModEver inst;
    
    @SidedProxy(serverSide = ModInfo.PROXY_SERVER, clientSide = ModInfo.PROXY_CLIENT)
    public static CommonProxy       proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    
        Constants.CONFIG_FOLDER.mkdirs();
        Constants.CONFIG_FOLDER.mkdir();
        
        Constants.CONFIG_SERVERS_FOLDER.mkdirs();
        Constants.CONFIG_SERVERS_FOLDER.mkdir();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
    
        proxy.loadConnections();
        proxy.loadEmotes();
        proxy.setupIRCEvents();
        proxy.registerRenders();
        // proxy.registerCommands();
        
        NetworkHandler.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    
    }
    
    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
    
        event.registerServerCommand(new CommandTBIRCME());
        proxy.connectToIRC(event.getServer());
    }
    
    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
    
        proxy.disconnectFromIRC();
    }
    
}
