package com.amadornes.tbircme.api;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ChannelConfig {
    
    private IIRCConnection connection;
    private String         channel          = "TBIRCME";
    
    private boolean        showGameJoins    = true;
    private boolean        showGameParts    = true;
    private boolean        showIRCJoins     = true;
    private boolean        showIRCParts     = true;
    private boolean        showDeaths       = true;
    private boolean        showAchievements = true;
    
    private boolean        overrideServer   = false;
    
    private boolean        sendMessages     = true;
    private boolean        receiveMessages  = true;
    
    public ChannelConfig(IIRCConnection connection, String channel) {
    
        this.connection = connection;
        this.channel = channel;
    }
    
    public void save() {
    
        Configuration cfg = new Configuration(getConfigFile(connection, channel));
        
        cfg.load();
        
        cfg.get("options", "channel", "", "Channel name").set(channel);
        
        cfg.get("options", "showGameJoins", showGameJoins, "Show game joins on IRC").set(showGameJoins);
        cfg.get("options", "showGameParts", showGameParts, "Show game parts on IRC").set(showGameParts);
        cfg.get("options", "showIRCJoins", showIRCJoins, "Show IRC joins ingame").set(showIRCJoins);
        cfg.get("options", "showIRCParts", showIRCParts, "Show IRC parts ingame").set(showIRCParts);
        cfg.get("options", "showDeaths", showDeaths, "Show deaths on IRC").set(showDeaths);
        cfg.get("options", "showAchievements", showAchievements, "Show achievements on IRC").set(showAchievements);
        
        cfg.get("options", "overrideServer", overrideServer, "Override server config").set(overrideServer);
        
        cfg.get("options", "sendMessages", sendMessages, "Send ingame messages to IRC").set(sendMessages);
        cfg.get("options", "receiveMessages", receiveMessages, "Show IRC messages ingame").set(receiveMessages);
        
        cfg.save();
    }
    
    public void load() {
    
        Configuration cfg = new Configuration(getConfigFile(connection, channel));
        
        cfg.load();
        
        channel = cfg.get("options", "channel", channel, "Channel name").getString();
        
        showGameJoins = cfg.get("options", "showGameJoins", showGameJoins, "Show game joins on IRC").getBoolean(showGameJoins);
        showGameParts = cfg.get("options", "showGameParts", showGameParts, "Show game parts on IRC").getBoolean(showGameParts);
        showIRCJoins = cfg.get("options", "showIRCJoins", showIRCJoins, "Show IRC joins ingame").getBoolean(showIRCJoins);
        showIRCParts = cfg.get("options", "showIRCParts", showIRCParts, "Show IRC parts ingame").getBoolean(showIRCParts);
        showDeaths = cfg.get("options", "showDeaths", showDeaths, "Show deaths on IRC").getBoolean(showDeaths);
        showAchievements = cfg.get("options", "showAchievements", showAchievements, "Show achievements on IRC").getBoolean(showAchievements);
        
        overrideServer = cfg.get("options", "overrideServer", overrideServer, "Override server config").getBoolean(overrideServer);
        
        sendMessages = cfg.get("options", "sendMessages", sendMessages, "Send ingame messages to IRC").getBoolean(sendMessages);
        receiveMessages = cfg.get("options", "receiveMessages", receiveMessages, "Show IRC messages ingame").getBoolean(receiveMessages);
        
        cfg.save();
    }
    
    public static File getConfigFile(IIRCConnection connection, String channel) {
    
        return new File(ConnectionConfig.getConfigFolder(connection.getId()), "/channels/" + channel + ".cfg");
    }
    
    public String getChannel() {
    
        return channel;
    }
    
    public IIRCConnection getConnection() {
    
        return connection;
    }
    
    public final boolean isShowGameJoins() {
    
        return showGameJoins;
    }
    
    public final boolean isShowGameParts() {
    
        return showGameParts;
    }
    
    public final boolean isShowIRCJoins() {
    
        return showIRCJoins;
    }
    
    public final boolean isShowIRCParts() {
    
        return showIRCParts;
    }
    
    public final boolean isShowDeaths() {
    
        return showDeaths;
    }
    
    public final boolean isShowAchievements() {
    
        return showAchievements;
    }
    
    public final void setShowGameJoins(boolean showGameJoins) {
    
        this.showGameJoins = showGameJoins;
    }
    
    public final void setShowGameParts(boolean showGameParts) {
    
        this.showGameParts = showGameParts;
    }
    
    public final void setShowIRCJoins(boolean showIRCJoins) {
    
        this.showIRCJoins = showIRCJoins;
    }
    
    public final void setShowIRCParts(boolean showIRCParts) {
    
        this.showIRCParts = showIRCParts;
    }
    
    public final void setShowDeaths(boolean showDeaths) {
    
        this.showDeaths = showDeaths;
    }
    
    public final void setShowAchievements(boolean showAchievements) {
    
        this.showAchievements = showAchievements;
    }
    
    public void remove() {
    
        getConfigFile(connection, channel).delete();
    }
    
    public void setChannel(String channel) {
    
        File f = getConfigFile(connection, this.channel);
        if (f.exists()) f.renameTo(getConfigFile(connection, channel));
        this.channel = channel;
    }
    
    public void setParent(IIRCConnection connection) {
    
        File f = getConfigFile(this.connection, channel);
        if (f.exists()) f.renameTo(getConfigFile(connection, channel));
        this.connection = connection;
    }
}
