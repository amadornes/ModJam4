package com.amadornes.tbircme.api;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public interface IIRCChannel {
    
    public String getName();
    
    public IIRCChannel setName(String name);
    
    public List<IIRCUser> getUsers();
    
    public void onUserJoin(IIRCUser user);
    
    public void onUserPart(IIRCUser user);
    
    public void onUserMode(IIRCUser user, int mode);
    
    public int getMode(IIRCUser user);
    
    public void onReceiveUserList(List<IIRCUser> users);
    
    public IIRCUser getUser(String nick);
    
    public void join();
    
    public void part();
    
    public boolean isConnected();
    
    public void setParent(IIRCConnection connection);
    
    public IIRCConnection getConnection();
    
    public void dispose();
    
    public void loadConfig();
    
    public void saveConfig();
    
    public ChannelConfig getConfig();
    
    public void sendMessage(String message);
    
    public void onPlayerChat(EntityPlayer player, String message);
    
    public void onPlayerEmote(EntityPlayer player, String emote);
    
    public void onPlayerJoin(EntityPlayer player);
    
    public void onPlayerLeave(EntityPlayer player);
    
}
