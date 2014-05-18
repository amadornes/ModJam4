package com.amadornes.tbircme.gui;

import net.minecraft.client.gui.GuiYesNo;

import com.amadornes.tbircme.util.Config;

public class GuiConfirmationDeleteChannel extends GuiYesNo {

	public GuiConfirmationDeleteChannel(GuiChannelList parent) {
		super(parent, "Are you sure you want to delete this channel?", "", 0);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}
}