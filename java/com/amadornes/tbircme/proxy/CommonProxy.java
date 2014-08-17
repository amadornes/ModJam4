package com.amadornes.tbircme.proxy;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.exception.IRCException;
import com.amadornes.tbircme.irc.ConnectionManager;
import com.amadornes.tbircme.irc.IRCEventHandler;
import com.amadornes.tbircme.ref.ModInfo;

public class CommonProxy {
    
    public void registerRenders() {
    
        // Clientside-only
    }
    
    public void loadEmotes() {
    
        // Clientside-only
    }
    
    public void exception(IRCException exception) {
    
        exception.printStackTrace();
    }
    
    public void loadConnections() {
    
        ConnectionManager.init();
        AConnectionManager.inst().loadAll();
    }
    
    public void setupIRCEvents() {
    
        MinecraftForge.EVENT_BUS.register(new IRCEventHandler());
    }
    
    public void connectToIRC(final MinecraftServer server) {
    
        for (final IIRCConnection con : AConnectionManager.inst().getConnections()) {
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                
                    for (Object o : server.getConfigurationManager().playerEntityList)
                        ((EntityPlayer) o).addChatMessage(new ChatComponentText(I18n.format(ModInfo.MODID + ".messages.connecting.server",
                                con.getName())));
                    
                    con.connect();
                    
                    for (Object o : server.getConfigurationManager().playerEntityList)
                        ((EntityPlayer) o).addChatMessage(new ChatComponentText(I18n.format(ModInfo.MODID + ".messages.connected.server",
                                con.getName())));
                }
            }, "TBIRCME Connection: " + con.getId()).start();
        }
    }
    
    public void disconnectFromIRC() {
    
        for (IIRCConnection con : AConnectionManager.inst().getConnections()) {
            con.disconnect();
        }
    }
    
    public void registerCommands() {
    
    }
    
    public EntityPlayer getPlayer() {
    
        return null;
    }
    
}
