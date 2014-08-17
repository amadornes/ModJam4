package com.amadornes.tbircme.irc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.amadornes.tbircme.api.ChannelConfig;
import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.api.IIRCUser;
import com.amadornes.tbircme.api.IRCMode;

public class IRCChannel implements IIRCChannel {
    
    private String         name      = "";
    private List<IIRCUser> users     = new ArrayList<IIRCUser>();
    private boolean        connected = false;
    
    private ChannelConfig  config;
    
    public IRCChannel(IIRCConnection parent, String name) {
    
        this.name = name;
        config = new ChannelConfig(parent, name);
    }
    
    @Override
    public String getName() {
    
        return name;
    }
    
    @Override
    public IIRCChannel setName(String name) {
    
        if (this.name.equalsIgnoreCase(name)) return this;
        getConnection().part(this.name);
        IIRCChannel c = getConnection().join(name);
        
        getConfig().setChannel(name);
        
        return c;
    }
    
    @Override
    public List<IIRCUser> getUsers() {
    
        return users;
    }
    
    @Override
    public void onUserJoin(IIRCUser user) {
    
        getUsers().add(user);
        
        // TODO TBIRCME: Send message
    }
    
    @Override
    public void onUserPart(IIRCUser user) {
    
        getUsers().remove(user);
        
        // TODO TBIRCME: Send message
    }
    
    @Override
    public void onUserMode(IIRCUser user, int mode) {
    
        int original = user.getMode();
        if ((IRCMode.hasMode(original, mode) && mode < 0) || (!IRCMode.hasMode(original, mode) && mode > 0)) user.setMode(Math
                .max(original + mode, 0));
    }
    
    @Override
    public int getMode(IIRCUser user) {
    
        return user.getMode();
    }
    
    @Override
    public void onReceiveUserList(List<IIRCUser> users) {
    
        for (IIRCUser u : users) {
            boolean exists = false;
            
            for (IIRCUser us : getUsers())
                if (us.getNick().equals(u)) {
                    exists = true;
                    break;
                }
            
            if (!exists) getUsers().add(u);
        }
    }
    
    @Override
    public IIRCUser getUser(String nick) {
    
        for (IIRCUser u : getUsers())
            if (u.getNick().equals(nick)) return u;
        
        return null;
    }
    
    @Override
    public void join() {
    
        connected = true;
    }
    
    @Override
    public void part() {
    
        connected = false;
        getUsers().clear();
        ChannelConfig.getConfigFile(getConnection(), getName()).delete();
    }
    
    @Override
    public boolean isConnected() {
    
        return connected;
    }
    
    @Override
    public void setParent(IIRCConnection connection) {
    
        getConfig().setParent(connection);
    }
    
    @Override
    public void dispose() {
    
        getUsers().clear();
    }
    
    @Override
    public IIRCConnection getConnection() {
    
        return getConfig().getConnection();
    }
    
    private void createConfigIfNeeded() {
    
        File file = ChannelConfig.getConfigFile(getConnection(), getName());
        
        file.getParentFile().mkdirs();
        file.getParentFile().mkdir();
    }
    
    @Override
    public void loadConfig() {
    
        createConfigIfNeeded();
        
        getConfig().load();
    }
    
    @Override
    public void saveConfig() {
    
        createConfigIfNeeded();
        
        getConfig().save();
    }
    
    @Override
    public ChannelConfig getConfig() {
    
        return config;
    }
    
    @Override
    public void sendMessage(String message) {
    
        getConnection().sendMessage(getName(), message);
    }
    
    @Override
    public void onPlayerChat(EntityPlayer player, String message) {
    
        sendMessage("<" + player.getDisplayName() + "> " + message);
    }
    
    @Override
    public void onPlayerEmote(EntityPlayer player, String emote) {
    
        sendMessage(" * " + player.getDisplayName() + " " + emote);
    }
    
    @Override
    public void onPlayerJoin(EntityPlayer player) {
    
        sendMessage(player.getDisplayName() + " joined the game.");
    }
    
    @Override
    public void onPlayerLeave(EntityPlayer player) {
    
        sendMessage(player.getDisplayName() + " left the game.");
    }
    
}
