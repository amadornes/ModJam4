package com.amadornes.tbircme.irc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import com.amadornes.tbircme.Constants;
import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.ConnectionConfig;
import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.exception.IRCException;
import com.amadornes.tbircme.exception.IRCExceptionLevel;
import com.amadornes.tbircme.util.Utils;

public class IRCConnection implements IIRCConnection {
    
    private Bot               bot;
    private String            id         = "";
    private List<IIRCChannel> channels   = new ArrayList<IIRCChannel>();
    private ConnectionConfig  config;
    private boolean           connecting = false;
    private boolean           connected  = false;
    
    public IRCConnection() {
    
        config = new ConnectionConfig("tmp");
    }
    
    public IRCConnection(String id) {
    
        config = new ConnectionConfig(id);
    }
    
    @Override
    public String getId() {
    
        return id;
    }
    
    @Override
    public void setId(String id) {
    
        if (id == this.id) return;
        if (AConnectionManager.inst().getConnection(id) != null) {
            TheBestIRCModEver.proxy.exception(new IRCException(I18n.format(Constants.LOC_EXCEPTION_SAME_ID), IRCExceptionLevel.ERROR));
            return;
        }
        
        config.setId(id);
        
        this.id = id;
    }
    
    @Override
    public List<IIRCChannel> getChannels() {
    
        return channels;
    }
    
    private void createConfigIfNeeded() {
    
        File file = ConnectionConfig.getConfigFile(getId());
        
        file.getParentFile().mkdirs();
        file.getParentFile().mkdir();
    }
    
    @Override
    public void loadConfig() {
    
        createConfigIfNeeded();
        
        config.load();
    }
    
    @Override
    public void saveConfig() {
    
        createConfigIfNeeded();
        
        config.save();
    }
    
    @Override
    public void connect() {
    
        if (isConnected() || isConnecting()) bot.disconnect();
        
        connecting = true;
        
        bot = new Bot(this, config.getNick());
        
        try {
            bot.connect(config.getHost(), config.getPort(), config.getPass());
            connecting = false;
            connected = bot.isConnected();
        } catch (Exception e) {
            TheBestIRCModEver.proxy.exception(new IRCException(e.getLocalizedMessage(), IRCExceptionLevel.SEVERE));
            connecting = false;
            connected = false;
            return;
        }
        
        if (connected) {
            bot.sendMessage("nickserv", "identify " + getConfig().getNickservPass());
            
            File fi = new File(ConnectionConfig.getConfigFolder(getId()), "channels");
            if (fi.exists()) {
                for (File f : fi.listFiles()) {
                    if (f.isDirectory()) continue;
                    if (!f.getName().toLowerCase().endsWith(".cfg")) ;
                    IIRCChannel c = join(f.getName().substring(0, f.getName().lastIndexOf(".cfg")));
                    c.loadConfig();
                }
            }
            
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                
                    while (bot.isConnected()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                        }
                    }
                    connected = false;
                }
            }, "Server monitoring - " + id).start();
        }
    }
    
    @Override
    public void disconnect() {
    
        if (bot != null) {
            bot.quitServer("Server closed");
            for (IIRCChannel c : channels)
                c.dispose();
            channels.clear();
        }
    }
    
    @Override
    public boolean isConnected() {
    
        return connected;
    }
    
    @Override
    public boolean isConnecting() {
    
        return connecting;
    }
    
    @Override
    public IIRCChannel join(String channel) {
    
        IIRCChannel c = getChannel(channel);
        
        if (c == null) {
            c = new IRCChannel(this, channel);
            c.loadConfig();
            channels.add(c);
        }
        
        if (c.isConnected()) return c;
        
        bot.joinChannel("#" + channel);
        c.join();
        
        return c;
    }
    
    @Override
    public void part(String channel) {
    
        IIRCChannel c = getChannel(channel);
        
        if (c == null) return;
        
        bot.partChannel("#" + channel);
        c.part();
        channels.remove(c);
    }
    
    @Override
    public void broadcast(String message) {
    
        for (IIRCChannel c : getChannels())
            c.sendMessage(message);
    }
    
    @Override
    public void sendMessage(String channel, String message) {
    
        bot.sendMessage("#" + channel, Utils.formatForIRC(message));
    }
    
    @Override
    public void onPlayerChat(EntityPlayer player, String message) {
    
        for (IIRCChannel c : getChannels())
            c.onPlayerChat(player, message);
    }
    
    @Override
    public void onPlayerEmote(EntityPlayer player, String emote) {
    
        for (IIRCChannel c : getChannels())
            c.onPlayerEmote(player, emote);
    }
    
    @Override
    public void onPlayerJoin(EntityPlayer player) {
    
        for (IIRCChannel c : getChannels())
            c.onPlayerJoin(player);
    }
    
    @Override
    public void onPlayerLeave(EntityPlayer player) {
    
        for (IIRCChannel c : getChannels())
            c.onPlayerLeave(player);
    }
    
    @Override
    public IIRCChannel getChannel(String channel) {
    
        for (IIRCChannel c : channels)
            if (c.getName().equalsIgnoreCase(channel) || (channel.startsWith("#") && c.getName().equalsIgnoreCase(channel.substring(1)))) return c;
        
        return null;
    }
    
    @Override
    public String getName() {
    
        return config.getName();
    }
    
    @Override
    public ConnectionConfig getConfig() {
    
        return config;
    }
    
}
