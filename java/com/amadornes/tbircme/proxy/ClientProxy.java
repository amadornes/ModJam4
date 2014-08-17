package com.amadornes.tbircme.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import com.amadornes.tbircme.client.render.RenderHandler;
import com.amadornes.tbircme.command.CommandTBIRCME;
import com.amadornes.tbircme.emote.EmoteApi;
import com.amadornes.tbircme.exception.IRCException;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerRenders() {
    
        MinecraftForge.EVENT_BUS.register(new RenderHandler());
    }
    
    @Override
    public void loadEmotes() {
    
        EmoteApi.init();
    }
    
    @Override
    public void exception(IRCException exception) {
    
        // TODO TBIRCME: Show exception popup if the user is on a config gui
        
        super.exception(exception);
    }
    
    @Override
    public void registerCommands() {
    
        ClientCommandHandler.instance.registerCommand(new CommandTBIRCME());
    }
    
    @Override
    public EntityPlayer getPlayer() {
    
        return Minecraft.getMinecraft().thePlayer;
    }
    
}
