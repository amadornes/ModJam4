package com.amadornes.tbircme.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientConfig extends GuiScreen {

	List<Object> objects = new ArrayList<Object>();
	
	public GuiClientConfig() {
		System.out.println("Test");
	}
	
	@Override
	public void drawScreen(int mx, int my, float frame) {
		super.drawScreen(mx, my, frame);
		
		drawRect(0, 0, 100, 100, 0xFF0000);
	}

	@Override
	public void onGuiClosed() {
		
	}

	@Override
	protected void actionPerformed(GuiButton btn) {

	}

}
