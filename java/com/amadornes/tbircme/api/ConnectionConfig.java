package com.amadornes.tbircme.api;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.amadornes.tbircme.Constants;

public class ConnectionConfig {
    
    private String  id;
    
    private String  host             = "your.server.com";
    private int     port             = 6667;
    private String  pass             = "";
    private String  nickservPass     = "";
    private String  name             = "New Server";
    
    private String  nick             = "TBIRCME";
    
    private boolean showGameJoins    = true;
    private boolean showGameParts    = true;
    private boolean showIRCJoins     = true;
    private boolean showIRCParts     = true;
    private boolean showDeaths       = true;
    private boolean showAchievements = true;
    
    public ConnectionConfig(String id) {
    
        this.id = id;
    }
    
    public void save() {
    
        Configuration cfg = new Configuration(getConfigFile(id));
        
        cfg.load();
        
        cfg.get("misc", "name", "", "Name of the connection (the name that will be displayed in the config window (can contain spaces)").set(name);
        
        cfg.get("connection", "host", "", "IRC server host to connect to (DO NOT INCLUDE PORT)").set(host);
        cfg.get("connection", "port", "", "Port the IRC server runs on (usually 6667)").set(port);
        cfg.get("connection", "pass", "", "Password to join the irc server (this is not your nickserv password)").set(pass);
        cfg.get("connection", "nickservPass", "", "Nickserv Pass").set(nickservPass);
        cfg.get("connection", "nick", "", "Nick (username) of the bridge").set(nick);
        
        cfg.get("options", "showGameJoins", showGameJoins, "Show game joins on IRC").set(showGameJoins);
        cfg.get("options", "showGameParts", showGameParts, "Show game parts on IRC").set(showGameParts);
        cfg.get("options", "showIRCJoins", showIRCJoins, "Show IRC joins ingame").set(showIRCJoins);
        cfg.get("options", "showIRCParts", showIRCParts, "Show IRC parts ingame").set(showIRCParts);
        cfg.get("options", "showDeaths", showDeaths, "Show deaths on IRC").set(showDeaths);
        cfg.get("options", "showAchievements", showAchievements, "Show achievements on IRC").set(showAchievements);
        
        cfg.save();
    }
    
    public void load() {
    
        Configuration cfg = new Configuration(getConfigFile(id));
        
        cfg.load();
        
        name = cfg.get("misc", "name", name, "Name of the connection (the name that will be displayed in the config window (can contain spaces)")
                .getString();
        
        host = cfg.get("connection", "host", host, "IRC server host to connect to (DO NOT INCLUDE PORT)").getString().trim();
        port = cfg.get("connection", "port", port, "Port the IRC server runs on (usually 6667)").getInt();
        pass = cfg.get("connection", "pass", pass, "Password to join the irc server (this is not your nickserv password)").getString().trim();
        nickservPass = cfg.get("connection", "nickservPass", nickservPass, "Nickserv Pass").getString().trim();
        nick = cfg.get("connection", "nick", "", "Nick (username) of the bridge").getString().trim();
        
        showGameJoins = cfg.get("options", "showGameJoins", showGameJoins, "Show game joins on IRC").getBoolean(showGameJoins);
        showGameParts = cfg.get("options", "showGameParts", showGameParts, "Show game parts on IRC").getBoolean(showGameParts);
        showIRCJoins = cfg.get("options", "showIRCJoins", showIRCJoins, "Show IRC joins ingame").getBoolean(showIRCJoins);
        showIRCParts = cfg.get("options", "showIRCParts", showIRCParts, "Show IRC parts ingame").getBoolean(showIRCParts);
        showDeaths = cfg.get("options", "showDeaths", showDeaths, "Show deaths on IRC").getBoolean(showDeaths);
        showAchievements = cfg.get("options", "showAchievements", showAchievements, "Show achievements on IRC").getBoolean(showAchievements);
        
        cfg.save();
    }
    
    public static File getConfigFolder(String id) {
    
        return new File(Constants.CONFIG_SERVERS_FOLDER, id + "/");
    }
    
    public static File getConfigFile(String id) {
    
        return new File(getConfigFolder(id), "/config.cfg");
    }
    
    public String getHost() {
    
        return host;
    }
    
    public int getPort() {
    
        return port;
    }
    
    public String getNickservPass() {
    
        if (nickservPass.trim().length() == 0) return null;
        
        return nickservPass;
    }
    
    public String getPass() {
    
        if (pass.trim().length() == 0) return null;
        
        return pass;
    }
    
    public String getNick() {
    
        return nick;
    }
    
    public String getId() {
    
        return id;
    }
    
    public String getName() {
    
        return name;
    }
    
    public void setHost(String host) {
    
        this.host = host.trim();
    }
    
    public void setPort(int port) {
    
        this.port = port;
    }
    
    public void setNickservPass(String nickservPass) {
    
        this.nickservPass = nickservPass.trim();
    }
    
    public void setPass(String pass) {
    
        this.pass = pass.trim();
    }
    
    public void setNick(String nick) {
    
        this.nick = nick.trim();
    }
    
    public void setId(String id) {
    
        File f = getConfigFile(this.id);
        if (f.exists()) f.renameTo(getConfigFile(id));
        this.id = id;
    }
    
    public void setName(String name) {
    
        this.name = name;
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
    
        getConfigFile(id).delete();
        getConfigFolder(id).delete();
    }
}
