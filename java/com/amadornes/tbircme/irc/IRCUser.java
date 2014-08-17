package com.amadornes.tbircme.irc;

import com.amadornes.tbircme.api.IIRCUser;

public class IRCUser implements IIRCUser {
    
    private String nick;
    private int    mode = 0;
    
    public IRCUser(String nick) {
    
        this.nick = nick;
    }
    
    @Override
    public void setNick(String nick) {
    
        this.nick = nick;
    }
    
    @Override
    public String getNick() {
    
        return nick;
    }
    
    @Override
    public void setMode(int mode) {
    
        this.mode = mode;
    }
    
    @Override
    public int getMode() {
    
        return mode;
    }
    
}
