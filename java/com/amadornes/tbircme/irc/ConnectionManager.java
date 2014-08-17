package com.amadornes.tbircme.irc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.amadornes.tbircme.Constants;
import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.IIRCConnection;

public class ConnectionManager extends AConnectionManager {
    
    public static void init() {
    
        AConnectionManager.inst = new ConnectionManager();
    }
    
    private ConnectionManager() {
    
    }
    
    private List<IIRCConnection> connections = new ArrayList<IIRCConnection>();
    
    @Override
    public List<IIRCConnection> getConnections() {
    
        return Collections.unmodifiableList(connections);
    }
    
    @Override
    public IIRCConnection getConnection(String id) {
    
        for (IIRCConnection con : connections)
            if (con.getId().equalsIgnoreCase(id)) return con;
        
        return null;
    }
    
    @Override
    public IIRCConnection newConnection() {
    
        IIRCConnection con = new IRCConnection();
        connections.add(con);
        return con;
    }
    
    @Override
    public void removeConnection(IIRCConnection connection) {
    
        connections.remove(connection);
        connection.getConfig().remove();
        
    }
    
    @Override
    public void loadAll() {
    
        for (File f : Constants.CONFIG_SERVERS_FOLDER.listFiles()) {
            if (!f.isDirectory()) continue;
            
            String id = f.getName();
            IIRCConnection con = newConnection();
            con.setId(id);
            con.loadConfig();
        }
    }
    
}
