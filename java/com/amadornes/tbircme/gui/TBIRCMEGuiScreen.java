package com.amadornes.tbircme.gui;

import org.lwjgl.input.Keyboard;

import com.amadornes.tbircme.util.Config;

import net.minecraft.client.gui.GuiScreen;

public class TBIRCMEGuiScreen extends GuiScreen {

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public final boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}
	
}
