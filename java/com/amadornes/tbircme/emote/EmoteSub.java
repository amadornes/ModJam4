package com.amadornes.tbircme.emote;

public class EmoteSub extends Emote {
    
    private String channel;
    
    public EmoteSub(String name, String url, String channel) {
    
        super(name, url);
        this.channel = channel;
    }
    
    public String getChannel() {
    
        return channel;
    }
    
    @Override
    public String[] getTooltip() {
    
        return new String[] { getName(), getChannel() };
    }
    
}
