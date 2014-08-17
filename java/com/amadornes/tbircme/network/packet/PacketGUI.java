package com.amadornes.tbircme.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.tbircme.gui.GuiTBIRCME;
import com.amadornes.tbircme.network.Packet;

public class PacketGUI extends Packet<PacketGUI> {
    
    @Override
    public void read(NBTTagCompound tag) {
    
    }
    
    @Override
    public void write(NBTTagCompound tag) {
    
    }
    
    @Override
    public void handleClientSide(PacketGUI message, EntityPlayer player) {
    
        Minecraft.getMinecraft().displayGuiScreen(new GuiTBIRCME());
    }
    
    @Override
    public void handleServerSide(PacketGUI message, EntityPlayer player) {
    
    }
    
}
