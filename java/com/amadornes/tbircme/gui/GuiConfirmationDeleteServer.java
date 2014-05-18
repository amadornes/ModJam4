package com.amadornes.tbircme.gui;

import net.minecraft.client.gui.GuiYesNo;

import com.amadornes.tbircme.util.Config;

public class GuiConfirmationDeleteServer extends GuiYesNo {

	public GuiConfirmationDeleteServer(GuiServerList parent) {
		super(parent, "Are you sure you want to delete this server?", "", 0);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}
}