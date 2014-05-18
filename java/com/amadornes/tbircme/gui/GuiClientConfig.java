package com.amadornes.tbircme.gui;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.render.RenderHelper;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.ReflectionUtils;

public class GuiClientConfig extends GuiScreen {

	private boolean initialized = false;

	private GuiButtonToggle btnEmotes, btnPause;
	private GuiButton btn1, btn2, btn3, btn4, btn5, btn6;

	public GuiClientConfig() {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		// Emote button
		{
			int btnEmotesSize = 20;
			int btnEmotesDist = 10;
			buttonList.add(btnEmotes = new GuiButtonToggle(0,
					width - btnEmotesSize - btnEmotesDist, btnEmotesDist, btnEmotesSize,
					btnEmotesSize, ""));
			btnEmotes.setState(Config.emotesEnabled);
		}

		// Pause button
		{
			int btnPauseSize = 20;
			int btnPauseDist = 10;
			buttonList.add(btnPause = new GuiButtonToggle(7, btnPauseDist, btnPauseDist,
					btnPauseSize, btnPauseSize, ""));
			btnPause.setState(Config.shouldConfigGuiPauseGame);
		}

		int sepH = 20;
		int sepV = 10;
		int btnHeight = 20;
		int btnWidth = 150;
		int vert = 3;
		int hor = 1;
		int max = 6;

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

		// Draw emote toggle button
		{
			btnEmotes.drawButton(mc, mx, my);
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID
					+ ":emotes/kappa.png"));
			RenderHelper.drawTexturedRect(btnEmotes.xPosition + 3, btnEmotes.yPosition + 3, 0, 0,
					14, 14);
			if (!btnEmotes.getState()) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
						ModInfo.MODID + ":others/nope.png"));
				RenderHelper.drawTexturedRect(btnEmotes.xPosition + 2, btnEmotes.yPosition + 2, 0,
						0, btnEmotes.getButtonWidth() - 4, btnEmotes.getButtonWidth() - 4);
			}

			GL11.glEnable(GL11.GL_LIGHTING);
			if (mx >= btnEmotes.xPosition && mx < btnEmotes.xPosition + btnEmotes.getButtonWidth()
					&& my >= btnEmotes.yPosition
					&& my < btnEmotes.yPosition + btnEmotes.getButtonWidth()) {
				drawHoveringText(Arrays.asList(new String[] { btnEmotes.getState() ? I18n.format(
						ModInfo.MODID + ".config.client.emotes.disable", new Object[0]) : I18n
						.format(ModInfo.MODID + ".config.client.emotes.enable", new Object[0]) }),
						mx, my, mc.fontRenderer);
			}
			GL11.glDisable(GL11.GL_LIGHTING);
		}

		// Draw pause toggle button
		{
			btnPause.drawButton(mc, mx, my);
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID
					+ ":others/pause.png"));
			RenderHelper.drawTexturedRect(btnPause.xPosition + 3, btnPause.yPosition + 3, 0, 0, 14,
					14);
			if (!btnPause.getState()) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
						ModInfo.MODID + ":others/nope.png"));
				RenderHelper.drawTexturedRect(btnPause.xPosition + 2, btnPause.yPosition + 2, 0, 0,
						btnPause.getButtonWidth() - 4, btnPause.getButtonWidth() - 4);
			}

			GL11.glEnable(GL11.GL_LIGHTING);
			if (mx >= btnPause.xPosition && mx < btnPause.xPosition + btnPause.getButtonWidth()
					&& my >= btnPause.yPosition
					&& my < btnPause.yPosition + btnPause.getButtonWidth()) {
				drawHoveringText(Arrays.asList(new String[] { btnPause.getState() ? I18n.format(
						ModInfo.MODID + ".config.pause.disable", new Object[0]) : I18n.format(
						ModInfo.MODID + ".config.pause.enable", new Object[0]) }), mx, my,
						mc.fontRenderer);
			}
			GL11.glDisable(GL11.GL_LIGHTING);
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
		if (btn == btnEmotes) {
			btnEmotes.setState(Config.emotesEnabled = !btnEmotes.getState());
		} else if (btn == btnPause) {
			btnPause.setState(Config.shouldConfigGuiPauseGame = !btnPause.getState());
			Minecraft.getMinecraft().displayGuiScreen(new GuiClientConfig());
		} else if (btn == btn1) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiServerList(this));
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}

}
