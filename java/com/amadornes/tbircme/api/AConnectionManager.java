package com.amadornes.tbircme.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class AConnectionManager {
    
    protected static AConnectionManager inst;
    
    public static AConnectionManager inst() {
    
        return inst;
    }
    
    public abstract List<IIRCConnection> getConnections();
    
    public abstract IIRCConnection getConnection(String id);
    
    public abstract IIRCConnection newConnection();
    
    public abstract void loadAll();
    
    public abstract void removeConnection(IIRCConnection connection);
    
    public void onGameChat(EntityPlayerMP player, String message) {
    
        for (IIRCConnection c : getConnections())
            c.onPlayerChat(player, message);
    }
    
    public void onGameEmote(EntityPlayerMP player, String emote) {
    
        for (IIRCConnection c : getConnections())
            c.onPlayerEmote(player, emote);
    }
    
    public void onPlayerJoin(EntityPlayer player) {
    
        for (IIRCConnection c : getConnections())
            c.onPlayerJoin(player);
    }
    
    public void onPlayerLeave(EntityPlayer player) {
    
        for (IIRCConnection c : getConnections())
            c.onPlayerLeave(player);
    }
    
    public void broadcast(String message) {
    
        for (IIRCConnection c : getConnections())
            c.broadcast(message);
    }
    
    public boolean chat(String channel, String message) {
    
        for (IIRCConnection c : getConnections())
            for (IIRCChannel ch : c.getChannels())
                if (ch.getName().equalsIgnoreCase(channel) || (channel.startsWith("#") && ch.getName().equalsIgnoreCase(channel.substring(1)))) {
                    ch.sendMessage(message);
                    return true;
                }
        return false;
    }
    
}
