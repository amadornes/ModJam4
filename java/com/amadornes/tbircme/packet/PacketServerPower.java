package com.amadornes.tbircme.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import com.amadornes.tbircme.TheBestIRCModEver;

import cpw.mods.fml.relauncher.Side;

public class PacketServerPower implements IPacket {

	private EntityPlayer p;

	public PacketServerPower(EntityPlayer player) {
		p = player;
	}

	public PacketServerPower() {
	}

	@Override
	public void write(NBTTagCompound tag) {
		tag.setBoolean("canChangeServerConfig", MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(p.getCommandSenderName()));
	}

	@Override
	public void read(NBTTagCompound tag) {
		TheBestIRCModEver.canChangeServerConfig = tag.getBoolean("canChangeServerConfig");
	}

	@Override
	public void handle(Side side) {
		// Already handled on the read method
	}

}
