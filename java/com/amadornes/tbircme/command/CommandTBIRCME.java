package com.amadornes.tbircme.command;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CommandTBIRCME extends CommandBase {

	@Override
	public String getCommandName() {
		return "tbircme";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "Do /tbirmce help for a detailed list of commands.";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length == 0) {
			sender.addChatMessage(new ChatComponentText("Available commands: cfg, svcfg"));
		}

		if (args.length >= 1) {
			String cmd = args[0].toLowerCase();
			if(cmd.equals("cfg")){
				//TODO Open config GUI
			}else if(cmd.equals("svcfg")){
				//TODO Open config GUI for the server
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender cmd, String[] args) {
		List<String> options = new ArrayList<String>();

		if (args.length == 0) {
			options.add("cfg");
			options.add("svcfg");
		}

		return options.size() > 0 ? options : null;
	}

}
