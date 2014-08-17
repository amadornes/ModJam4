package com.amadornes.tbircme.api;

public interface IIRCEventHandler {
    
    public void onOp(IIRCChannel channel, IIRCUser user);
    
    public void onDeOp(IIRCChannel channel, IIRCUser user);
    
    public void onVoice(IIRCChannel channel, IIRCUser user);
    
    public void onDeVoice(IIRCChannel channel, IIRCUser user);
    
    public void onJoin(IIRCChannel channel, IIRCUser user);
    
    public void onPart(IIRCChannel channel, IIRCUser user);
    
    public void onKick(IIRCChannel channel, IIRCUser user, String reason);
    
    public void onMessage(IIRCChannel channel, IIRCUser user, String message);
    
    public void onPrivateMessage(IIRCUser user, String message);
    
    public void onConnect();
    
    public void onQuit();
    
    public void onQuit(IIRCChannel channel, IIRCUser user, String reason);
    
}
