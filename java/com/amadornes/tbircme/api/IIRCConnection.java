package com.amadornes.tbircme.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public interface IIRCConnection {
    
    public String getId();
    
    public void setId(String id);
    
    public String getName();
    
    public List<IIRCChannel> getChannels();
    
    public void loadConfig();
    
    public void saveConfig();
    
    public ConnectionConfig getConfig();
    
    public void connect();
    
    public void disconnect();
    
    public boolean isConnected();
    
    public boolean isConnecting();
    
    public IIRCChannel join(String channel);
    
    public void part(String channel);
    
    public IIRCChannel getChannel(String channel);
    
    public void broadcast(String message);
    
    public void sendMessage(String channel, String message);
    
    public void onPlayerChat(EntityPlayer player, String message);
    
    public void onPlayerEmote(EntityPlayer player, String emote);
    
    public void onPlayerJoin(EntityPlayer player);
    
    public void onPlayerLeave(EntityPlayer player);
    
}
