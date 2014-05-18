package com.amadornes.tbircme.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;

public class GuiServerList extends GuiScreen {
	private GuiScreen previousGui;
	private GuiSlotServerList serverList;
	private int selected = -1;
	private Server selectedServer;
	private int listWidth;
	private ArrayList<Server> servers;
	private GuiButton btnAddServer;
	private GuiButton btnDelServer;
	private GuiButton btnDone;

	private GuiTextFieldCustomizable fName, fHost, fUsername, fPassword;

	// private List<GuiTextFieldCustomizable> fields = new
	// ArrayList<GuiTextFieldCustomizable>();

	public GuiServerList(GuiScreen previousGui) {
		this.previousGui = previousGui;
		this.servers = (ArrayList<Server>) Config.servers;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		for (Server server : servers) {
			listWidth = Math
					.max(listWidth, getFontRenderer().getStringWidth(server.getName()) + 10);
			listWidth = Math.max(
					listWidth,
					getFontRenderer().getStringWidth(
							server.getChannels().size()
									+ " "
									+ I18n.format(ModInfo.MODID + ".config.servers.channels.title",
											new Object[0])) + 10);
		}
		listWidth = Math.min(listWidth, 150);
		this.buttonList.add(btnDone = new GuiButton(6, this.width / 2 - 75, this.height - 38, I18n
				.format("gui.done")));
		btnAddServer = new GuiButton(20, 10, this.height - 60, this.listWidth, 20, I18n.format(
				ModInfo.MODID + ".config.add", new Object[0]));
		btnDelServer = new GuiButton(21, 10, this.height - 38, this.listWidth, 20, I18n.format(
				ModInfo.MODID + ".config.remove", new Object[0]));
		this.buttonList.add(btnAddServer);
		this.buttonList.add(btnDelServer);
		this.serverList = new GuiSlotServerList(this, servers, listWidth);
		this.serverList.registerScrollButtons(this.buttonList, 7, 8);

		fName = (new GuiTextFieldCustomizable(fontRendererObj, 10 + listWidth + 10, 32, 200, 20));
		fName.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.name",
				new Object[0]));

		fHost = (new GuiTextFieldCustomizable(fontRendererObj, 10 + listWidth + 10, 32 + 20 + 5,
				200, 20));
		fHost.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.host",
				new Object[0]));

		fUsername = (new GuiTextFieldCustomizable(fontRendererObj, 10 + listWidth + 10, 32 + 20 + 5
				+ 20 + 5, 200, 20));
		fUsername.setPlaceholder(I18n.format(
				ModInfo.MODID + ".config.servers.placeholder.username", new Object[0]));

		fPassword = (new GuiTextFieldCustomizable(fontRendererObj, 10 + listWidth + 10, 32 + 20 + 5
				+ 20 + 5 + 20 + 5, 200, 20));
		fPassword.setPlaceholder(I18n.format(
				ModInfo.MODID + ".config.servers.placeholder.password", new Object[0]));
		fPassword.setTooltip(I18n.format(ModInfo.MODID
				+ ".config.servers.placeholder.password.tooltip", new Object[0]));
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == btnDone) {
				this.mc.displayGuiScreen(this.previousGui);
				return;
			}
			if (button == btnDelServer) {
				this.mc.displayGuiScreen(new GuiConfirmationDeleteServer(this));
			}
		}
		super.actionPerformed(button);
	}

	public int drawLine(String line, int offset, int shifty) {
		this.fontRendererObj.drawString(line, offset, shifty, 0xd7edea);
		return shifty + 10;
	}

	@Override
	public void drawScreen(int p_571_1_, int p_571_2_, float p_571_3_) {
		this.serverList.drawScreen(p_571_1_, p_571_2_, p_571_3_);
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".config.servers.title", new Object[0]),
				this.width / 2, 16, 0xFFFFFF);
		// int offset = this.listWidth + 20;
		btnAddServer.enabled = true;
		if (selectedServer != null) {
			btnDelServer.enabled = true;
			GL11.glEnable(GL11.GL_BLEND);

			GL11.glDisable(GL11.GL_BLEND);
		} else {
			btnDelServer.enabled = false;
		}
		int k;

		for (k = 0; k < this.buttonList.size(); ++k) {
			((GuiButton) this.buttonList.get(k)).drawButton(this.mc, p_571_1_, p_571_2_);
		}

		for (k = 0; k < this.labelList.size(); ++k) {
			((GuiLabel) this.labelList.get(k)).func_146159_a(this.mc, p_571_1_, p_571_2_);
		}

		fName.drawTextBox();
		fHost.drawTextBox();
		fUsername.drawTextBox();
		fPassword.drawTextBox();

		fName.renderTooltip(p_571_1_, p_571_2_);
		fHost.renderTooltip(p_571_1_, p_571_2_);
		fUsername.renderTooltip(p_571_1_, p_571_2_);
		fPassword.renderTooltip(p_571_1_, p_571_2_);
	}

	public void confirmClicked(boolean par1, int par2) {

		if (par2 == 0) {
			if (par1) {
				Config.servers.remove(selectedServer);
				selectedServer.getConnection().disconnect();
				if (selectedServer.getConfigFile().exists())
					selectedServer.getConfigFile().delete();
				Minecraft.getMinecraft().displayGuiScreen(this);
			}
		}
	}

	Minecraft getMinecraftInstance() {
		return mc;
	}

	FontRenderer getFontRenderer() {
		return fontRendererObj;
	}

	public void selectServerIndex(int var1) {
		this.selected = var1;
		if (var1 >= 0 && var1 <= servers.size()) {
			this.selectedServer = servers.get(selected);

		} else {
			this.selectedServer = null;
		}
	}

	public boolean serverIndexSelected(int var1) {
		return var1 == selected;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		fName.mouseClicked(par1, par2, par3);
		fHost.mouseClicked(par1, par2, par3);
		fUsername.mouseClicked(par1, par2, par3);
		fPassword.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		fName.textboxKeyTyped(par1, par2);
		fHost.textboxKeyTyped(par1, par2);
		fUsername.textboxKeyTyped(par1, par2);
		fPassword.textboxKeyTyped(par1, par2);
	}

}
