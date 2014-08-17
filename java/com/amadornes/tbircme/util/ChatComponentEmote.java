package com.amadornes.tbircme.util;

import java.util.Iterator;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.amadornes.tbircme.api.IEmote;

public class ChatComponentEmote extends ChatComponentText {
    
    private IEmote emote;
    
    public ChatComponentEmote(IEmote emote) {
    
        super("");
        this.emote = emote;
    }
    
    @Override
    public String getUnformattedTextForChat() {
    
        emote.downloadIfNeeded();
        
        return "\"" + emote.getName() + "\"";
    }
    
    @Override
    public String getChatComponentText_TextValue() {
    
        return getUnformattedTextForChat();
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public ChatComponentEmote createCopy() {
    
        ChatComponentEmote cce = new ChatComponentEmote(emote);
        cce.setChatStyle(getChatStyle().createShallowCopy());
        Iterator iterator = getSiblings().iterator();
        
        while (iterator.hasNext()) {
            IChatComponent ichatcomponent = (IChatComponent) iterator.next();
            cce.appendSibling(ichatcomponent.createCopy());
        }
        
        return cce;
    }
    
    @Override
    public boolean equals(Object obj) {
    
        if (this == obj) {
            return true;
        } else if (!(obj instanceof ChatComponentEmote)) {
            return false;
        } else {
            ChatComponentEmote cce = (ChatComponentEmote) obj;
            return emote.equals(cce.emote);
        }
    }
    
}
