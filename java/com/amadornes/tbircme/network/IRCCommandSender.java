package com.amadornes.tbircme.network;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class IRCCommandSender implements ICommandSender {

	private IRCConnection con;
	private String sender = "";
	
	public IRCCommandSender(IRCConnection con, String host) {
		this.con = con;
		this.sender = host;
	}

	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return null;
	}

	@Override
	public World getEntityWorld() {
		return null;
	}

	@Override
	public String getCommandSenderName() {
		return sender;
	}

	@Override
	public IChatComponent func_145748_c_() {
		return new ChatComponentText("");
	}

	@Override
	public boolean canCommandSenderUseCommand(int var1, String var2) {
		return true;
	}

	@Override
	public void addChatMessage(IChatComponent var1) {
		con.chat(sender.substring(1), " > " + var1.getUnformattedTextForChat() + " < ");
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
}
