package com.amadornes.tbircme.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import com.amadornes.tbircme.TheBestIRCModEver;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler extends FMLIndexedMessageToMessageCodec<IPacket> {

	public PacketHandler() {
		addDiscriminator(0, PacketServerConfig.class);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, IPacket msg, ByteBuf target) throws Exception {
		NBTTagCompound tag = new NBTTagCompound();
		msg.write(tag);
		target.writeBytes(CompressedStreamTools.compress(tag));
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IPacket msg) {
		NBTTagCompound tag;
		try {
			tag = CompressedStreamTools.decompress(source.array());
			msg.read(tag);
			msg.handle(FMLCommonHandler.instance().getEffectiveSide());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendToServer(IPacket p) {
		TheBestIRCModEver.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		TheBestIRCModEver.channels.get(Side.CLIENT).writeOutbound(p);
	}

	public static void sendToClients(IPacket p, EntityPlayer player) {
		TheBestIRCModEver.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		TheBestIRCModEver.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		TheBestIRCModEver.channels.get(Side.SERVER).writeOutbound(p);
	}
	
	public static void sendToAllClients(IPacket p) {
		TheBestIRCModEver.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		TheBestIRCModEver.channels.get(Side.SERVER).writeOutbound(p);
	}
}
