package com.amadornes.tbircme.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.api.IIRCUser;
import com.amadornes.tbircme.gui.GuiTBIRCME;
import com.amadornes.tbircme.network.NetworkHandler;
import com.amadornes.tbircme.network.packet.PacketGUI;
import com.amadornes.tbircme.util.StringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CommandTBIRCME extends CommandBase {
    
    @Override
    public String getCommandName() {
    
        return "tbircme";
    }
    
    @Override
    public String getCommandUsage(ICommandSender var1) {
    
        return "/tbircme";
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
    
        if (args.length == 0) {
            if (sender instanceof EntityPlayerMP) {
                NetworkHandler.sendTo(new PacketGUI(), (EntityPlayerMP) sender);
            }
        } else {
            if ((args[0].equalsIgnoreCase("b") || args[0].equalsIgnoreCase("broadcast")) && args.length > 1) {
                String message = StringUtils.join(1, args.length, args);
                AConnectionManager.inst().broadcast(message);
            } else if ((args[0].equalsIgnoreCase("m") || args[0].equalsIgnoreCase("msg") || args[0].equalsIgnoreCase("message")) && args.length > 2) {
                String channel = args[1];
                String message = StringUtils.join(2, args.length, args);
                AConnectionManager.inst().chat(channel, message);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    private boolean processClient(ICommandSender sender, String[] args) {
    
        if (args.length == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTBIRCME());
            return true;
        }
        return false;
    }
    
    @Override
    public int getRequiredPermissionLevel() {
    
        return 0;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender par1iCommandSender, String[] par2ArrayOfStr) {
    
        List<String> l = new ArrayList<String>();
        
        for (IIRCConnection c : AConnectionManager.inst().getConnections()) {
            for (IIRCChannel ch : c.getChannels()) {
                for (IIRCUser u : ch.getUsers()) {
                    l.add(u.getNick());
                }
            }
        }
        
        return l;
    }
    
}
