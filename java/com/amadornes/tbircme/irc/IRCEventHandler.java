package com.amadornes.tbircme.irc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;

import com.amadornes.lib.reflect.ReflectUtils;
import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.emote.EmoteApi;
import com.amadornes.tbircme.util.StringUtils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IRCEventHandler {
    
    @SideOnly(Side.CLIENT)
    public static List<ChatLine> messages = new ArrayList<ChatLine>();
    
    /**
     * Used to replace emotes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPlayerChatClient(ClientChatReceivedEvent event) {
    
        IChatComponent msg = event.message;
        
        if (msg instanceof ChatComponentTranslation) {
            List<Object> contentTranslation = new ArrayList<Object>();
            ChatComponentText cct = new ChatComponentText("");
            for (Object o : ((ChatComponentTranslation) msg).getFormatArgs()) {
                if (o instanceof String) {
                    String str = (String) o;
                    for (Object o2 : EmoteApi.lookForEmotes(str)) {
                        if (o2 instanceof String) {
                            cct.appendText((String) o2);
                        } else if (o2 instanceof IChatComponent) {
                            cct.appendSibling((IChatComponent) o2);
                        }
                    }
                } else {
                    contentTranslation.add(o);
                }
            }
            contentTranslation.add(cct);
            
            event.message = new ChatComponentTranslation(((ChatComponentTranslation) msg).getKey(), contentTranslation.toArray());
            contentTranslation.clear();
        } else if (msg instanceof ChatComponentText) {
            ChatComponentText cct = new ChatComponentText("");
            for (Object o2 : EmoteApi.lookForEmotes(msg.getUnformattedTextForChat())) {
                if (o2 instanceof String) {
                    cct.appendText((String) o2);
                } else if (o2 instanceof IChatComponent) {
                    cct.appendSibling((IChatComponent) o2);
                }
            }
            event.message = cct;
        }
        
        // Here comes some reflection... :P
        GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        messages.clear();
        messages.addAll((List) ReflectUtils.get(chat, "chatLines"));
    }
    
    /**
     * Used to send ingame chat to IRC
     */
    @SubscribeEvent
    public void onPlayerChat(ServerChatEvent event) {
    
        AConnectionManager.inst().onGameChat(event.player, event.message);
    }
    
    /**
     * Used to detect the use of the "/me" command ingame
     */
    @SubscribeEvent
    public void onCommand(CommandEvent event) {
    
        if (event.sender instanceof EntityPlayerMP && event.command.getCommandName().equalsIgnoreCase("me")) AConnectionManager.inst().onGameEmote(
                (EntityPlayerMP) event.sender, StringUtils.join(event.parameters));
    }
    
    private static List<EntityPlayer> players = new ArrayList<EntityPlayer>();
    
    @SubscribeEvent
    public void onTick(ServerTickEvent event) {
    
        if (event.side == Side.SERVER || isLocal()) {
            List<EntityPlayer> serverPlayers = new ArrayList<EntityPlayer>();
            for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
                serverPlayers.add((EntityPlayer) o);
            
            List<EntityPlayer> joined = new ArrayList<EntityPlayer>();
            List<EntityPlayer> left = new ArrayList<EntityPlayer>();
            
            for (EntityPlayer p : players) {
                boolean found = false;
                for (EntityPlayer pl : serverPlayers)
                    if (p.getDisplayName() == pl.getDisplayName()) {
                        found = true;
                        break;
                    }
                if (!found) left.add(p);
            }
            
            for (EntityPlayer p : serverPlayers) {
                boolean found = false;
                for (EntityPlayer pl : players)
                    if (p.getDisplayName() == pl.getDisplayName()) {
                        found = true;
                        break;
                    }
                if (!found) joined.add(p);
            }
            
            for (EntityPlayer p : joined) {
                AConnectionManager.inst().onPlayerJoin(p);
                players.add(p);
            }
            
            for (EntityPlayer p : left) {
                AConnectionManager.inst().onPlayerLeave(p);
                players.remove(p);
            }
            
            serverPlayers.clear();
            joined.clear();
            left.clear();
        }
    }
    
    private boolean isLocal() {
    
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) { return isLocal(true); }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    private boolean isLocal(boolean unused) {
    
        return MinecraftServer.getServer() instanceof IntegratedServer;
    }
}
