package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonToggle extends GuiButton {

	private boolean state = false;

	public GuiButtonToggle(int par1, int par2, int par3, String par4Str) {
		super(par1, par2, par3, par4Str);
	}

	public GuiButtonToggle(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}

	protected int getHoverState(boolean p_146114_1_) {
		byte st = 1;

		if(state)
			st = 2;
		
		if (!this.enabled) {
			st = 0;
		} else if (p_146114_1_) {
			st = 2;
		}

		return st;
	}

}
