package com.amadornes.tbircme.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.tbircme.TheBestIRCModEver;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

public abstract class Packet<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
    
    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {
    
        if (ctx.side == Side.SERVER) {
            handleServerSide(message, ctx.getServerHandler().playerEntity);
        } else {
            handleClientSide(message, TheBestIRCModEver.proxy.getPlayer());
        }
        return null;
    }
    
    public abstract void handleClientSide(REQ message, EntityPlayer player);
    
    public abstract void handleServerSide(REQ message, EntityPlayer player);
    
    @Override
    public void fromBytes(ByteBuf buf) {
    
        read(ByteBufUtils.readTag(buf));
    }
    
    @Override
    public void toBytes(ByteBuf buf) {
    
        NBTTagCompound tag = new NBTTagCompound();
        write(tag);
        ByteBufUtils.writeTag(buf, tag);
    }
    
    public abstract void read(NBTTagCompound tag);
    
    public abstract void write(NBTTagCompound tag);
}
