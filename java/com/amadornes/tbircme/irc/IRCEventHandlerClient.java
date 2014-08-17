package com.amadornes.tbircme.irc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import com.amadornes.tbircme.emote.EmoteApi;
import com.amadornes.tbircme.util.ReflectUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class IRCEventHandlerClient {
    
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
}
