package com.amadornes.tbircme.irc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;

import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.api.IIRCUser;
import com.amadornes.tbircme.api.IRCMode;

public class Bot extends PircBot {
    
    private IIRCConnection connection;
    
    public Bot(IIRCConnection connection, String nick) {
    
        this.connection = connection;
        
        setMessageDelay(50);
        setAutoNickChange(false);
        setName(nick);
        setLogin("TBIRCME");
        setVersion("TBIRCME by Amadornes");
        setFinger("TBIRCME by Amadornes");
    }
    
    @Override
    protected void onOp(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    
        alterMode(channel, recipient, IRCMode.OP);
    }
    
    @Override
    protected void onDeop(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    
        alterMode(channel, recipient, -IRCMode.OP);
    }
    
    @Override
    protected void onVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    
        alterMode(channel, recipient, IRCMode.VOICE);
    }
    
    @Override
    protected void onDeVoice(String channel, String sourceNick, String sourceLogin, String sourceHostname, String recipient) {
    
        alterMode(channel, recipient, -IRCMode.VOICE);
    }
    
    @Override
    protected void onJoin(String channel, String sender, String login, String hostname) {
    
        if (sender.equals(getNick())) return;
        
        connection.getChannel(channel).onUserJoin(new IRCUser(sender));
    }
    
    @Override
    protected void onPart(String channel, String sender, String login, String hostname) {
    
        if (sender.equals(getNick())) return;
        
        IIRCChannel ch = connection.getChannel(channel);
        ch.onUserPart(ch.getUser(sender));
    }
    
    @Override
    protected void onKick(String channel, String kickerNick, String kickerLogin, String kickerHostname, String recipientNick, String reason) {
    
        if (recipientNick.equals(getNick())) return;
        for (IIRCChannel c : connection.getChannels()) {
            IIRCUser u = c.getUser(recipientNick);
            if (u != null) c.onUserPart(u);
        }
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
    
        for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            ((EntityPlayer) o).addChatMessage(new ChatComponentText("[" + channel + "] " + sender + ": " + message));
        }
    }
    
    @Override
    protected void onQuit(String sourceNick, String sourceLogin, String sourceHostname, String reason) {
    
        if (sourceNick.equals(getNick())) return;
        for (IIRCChannel c : connection.getChannels()) {
            IIRCUser u = c.getUser(sourceNick);
            if (u != null) c.onUserPart(u);
        }
    }
    
    @Override
    protected void onUserList(String channel, User[] users) {
    
        List<IIRCUser> userList = new ArrayList<IIRCUser>();
        
        for (User user : users) {
            IRCUser u = new IRCUser(user.getNick());
            
            int mode = 0;
            if (user.hasVoice()) mode += IRCMode.VOICE;
            if (user.isOp()) mode += IRCMode.OP;
            u.setMode(mode);
            
            userList.add(u);
        }
        
        connection.getChannel(channel).onReceiveUserList(userList);
    }
    
    @Override
    protected void onNickChange(String oldNick, String login, String hostname, String newNick) {
    
        for (IIRCChannel c : connection.getChannels()) {
            IIRCUser u = c.getUser(oldNick);
            if (u != null) u.setNick(newNick);
        }
    }
    
    @Override
    protected void onAction(String sender, String login, String hostname, String target, String action) {
    
        if (!target.startsWith("#")) return;
        
        for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            ((EntityPlayer) o).addChatMessage(new ChatComponentText("[" + target + "] * " + sender + " " + action));
        }
    }
    
    private void alterMode(String channel, String user, int mode) {
    
        IIRCChannel ch = connection.getChannel(channel);
        ch.onUserMode(ch.getUser(user), mode);
    }
    
}
