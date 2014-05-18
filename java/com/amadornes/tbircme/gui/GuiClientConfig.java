package com.amadornes.tbircme.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.ReflectionUtils;

public class GuiClientConfig extends GuiScreen {

	private GuiConfig parent;

	private boolean initialized = false;

	private GuiButton btn1, btn2, btn3, btn4, btn5, btn6;
	
	private boolean mainMenu;

	public GuiClientConfig(GuiConfig parent, boolean mainMenu) {
		this.parent = parent;
		this.mainMenu = mainMenu;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		int sepH = 20;
		int sepV = 10;
		int btnHeight = 20;
		int btnWidth = 150;
		int vert = 3;
		int hor = 1;
		int max = 3;

		for (int x = 0; x < hor; x++) {
			for (int y = 0; y < vert; y++) {
				int id = y * hor + x + 1;
				if (id > max)
					continue;
				int btnx = (width / 2) - (btnWidth / 2)
						+ ((int) ((btnWidth + sepH) * (((x / ((double) (hor - 1))) - (1D / hor)))));
				int btny = (height / 2)
						- (btnHeight / 2)
						+ ((int) ((double) (((btnHeight + sepV) * 2 * (((y / ((double) (vert - 1))) - (1D / vert)))))));
				GuiButton b = new GuiButton(id, btnx, btny, btnWidth, btnHeight, "");
				buttonList.add(b);
				ReflectionUtils.set(this, "btn" + id, b);
			}
		}

		btn1.displayString = I18n.format(ModInfo.MODID + ".config.servers.title");
		btn3.displayString = I18n.format("gui.done");

		initialized = true;
	}

	@Override
	public void drawScreen(int mx, int my, float frame) {
		this.drawGradientRect(0, 0, this.width, this.height, 0xC010100F, 0xD010100F);

		GL11.glPushMatrix();
		GL11.glTranslated(this.width / 2, 20, 0);
		GL11.glScaled(3, 3, 1);
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".name", new Object[0]), 0, 0, 0x96F2F2);
		GL11.glPopMatrix();
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".config.client.title", new Object[0]), this.width / 2,
				67, 16777215);

		if (!initialized)
			return;

		// Draw other buttons
		{
			if (btn1 != null)
				btn1.drawButton(mc, mx, my);
			if (btn2 != null)
				btn2.drawButton(mc, mx, my);
			if (btn3 != null)
				btn3.drawButton(mc, mx, my);
			if (btn4 != null)
				btn4.drawButton(mc, mx, my);
			if (btn5 != null)
				btn5.drawButton(mc, mx, my);
			if (btn6 != null)
				btn6.drawButton(mc, mx, my);
		}

	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void mouseClickMove(int p_146273_1_, int p_146273_2_, int p_146273_3_,
			long p_146273_4_) {
		super.mouseClickMove(p_146273_1_, p_146273_2_, p_146273_3_, p_146273_4_);
	}

	@Override
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_) {
		super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
	}

	@Override
	protected void actionPerformed(GuiButton btn) {
		if (btn == btn1) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiServerList(this, mainMenu));
		} else if (btn == btn2) {

		} else if (btn == btn3) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}

}
