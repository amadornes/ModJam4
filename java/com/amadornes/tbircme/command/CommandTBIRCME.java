package com.amadornes.tbircme.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import com.amadornes.tbircme.gui.GuiConfig;

public class CommandTBIRCME extends CommandBase {

	@Override
	public String getCommandName() {
		return "tbircme";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "";// return "Do /tbirmce help for a detailed list of commands.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			// EntityPlayer p = (EntityPlayer) sender;
			// if (args.length == 0) {
			// sender.addChatMessage(new
			// ChatComponentText("Available commands: cfg"));
			// }

			// if (args.length >= 1) {
			// String cmd = args[0].toLowerCase();
			// if (cmd.equals("cfg")) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiConfig(false));
			// //}
			// }
		} else {
			sender.addChatMessage(new ChatComponentText(
					"You must be a player in order to use this command."));
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender cmd, String[] args) {
		return null;
	}

}
