package com.amadornes.tbircme.gui;

import net.minecraft.client.gui.GuiYesNo;

public class GuiConfirmationDeleteServer extends GuiYesNo {

	public GuiConfirmationDeleteServer(GuiServerList parent) {
		super(parent, "Are you sure you want to delete this server?", "", 0);
	}
}