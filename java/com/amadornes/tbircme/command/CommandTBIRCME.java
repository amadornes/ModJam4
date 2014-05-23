package com.amadornes.tbircme.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandTBIRCME extends CommandBase {

	@Override
	public String getCommandName() {
		return "tbircme";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List addTabCompletionOptions(ICommandSender cmd, String[] args) {
		return null;
	}

}
