package com.amadornes.tbircme.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.gui.comp.GuiButtonCustom;
import com.amadornes.tbircme.gui.comp.GuiButtonToggle;
import com.amadornes.tbircme.gui.comp.GuiCheckbox;
import com.amadornes.tbircme.gui.comp.GuiTextFieldCustomizable;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.render.RenderHelper;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.IChangeListener;

public class GuiServerList extends TBIRCMEGuiScreen implements IChangeListener {
	private GuiScreen previousGui;
	private GuiSlotServerList serverList;
	private int selected = -1;
	private Server selectedServer;
	private int listWidth;
	private ArrayList<Server> servers;
	private GuiButton btnAddServer;
	private GuiButton btnDelServer;
	private GuiButton btnDone;
	private GuiButtonToggle btnConnect;
	private GuiButton btnChannels;

	private GuiCheckbox cbDeath, cbGameJoin, cbGameLeave, cbIRCJoin, cbIRCLeave;

	private GuiTextFieldCustomizable fName, fHost, fUsername, fPassword;

	private boolean mainMenu = false;

	private boolean hasChanged = false;

	public GuiServerList(GuiScreen previousGui, boolean mainMenu) {
		this.previousGui = previousGui;
		this.servers = (ArrayList<Server>) Config.servers;
		this.mainMenu = mainMenu;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();
		if (fName != null) {
			this.buttonList.add(btnAddServer);
			this.buttonList.add(btnDelServer);
			this.buttonList.add(btnDone);
			this.buttonList.add(btnConnect);
			this.buttonList.add(btnChannels);
			return;
		}

		listWidth = 150;
		this.buttonList.add(btnDone = new GuiButtonCustom(6, this.width / 2 - 75, this.height - 38,
				I18n.format("gui.done")));
		btnAddServer = new GuiButton(20, 10, this.height - 60, this.listWidth, 20,
				I18n.format(ModInfo.MODID + ".config.add"));
		btnDelServer = new GuiButton(21, 10, this.height - 38, this.listWidth, 20,
				I18n.format(ModInfo.MODID + ".config.remove"));
		this.buttonList.add(btnAddServer);
		this.buttonList.add(btnDelServer);
		this.serverList = new GuiSlotServerList(this, servers, listWidth);
		this.serverList.registerScrollButtons(this.buttonList, 7, 8);

		fName = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32, 200,
				20, 200));
		fName.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.name",
				new Object[0]));
		fName.setEnabled(false);

		fHost = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10,
				32 + 20 + 5, 200, 20, 200));
		fHost.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.host",
				new Object[0]));
		fName.setEnabled(false);

		fUsername = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32
				+ 20 + 5 + 20 + 5, 200, 20, 200));
		fUsername.setPlaceholder(I18n
				.format(ModInfo.MODID + ".config.servers.placeholder.username"));
		fUsername.setEnabled(false);

		fPassword = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32
				+ 20 + 5 + 20 + 5 + 20 + 5, 200, 20, 200));
		fPassword.setPlaceholder(I18n
				.format(ModInfo.MODID + ".config.servers.placeholder.password"));
		fPassword.setTooltip(I18n.format(ModInfo.MODID
				+ ".config.servers.placeholder.password.tooltip"));
		fPassword.setPassword(true);
		fPassword.setEnabled(false);

		int cbSize = 12;

		cbGameJoin = new GuiCheckbox(this, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5 + 20 + 5 + 20
				+ 5, cbSize,
				I18n.format(ModInfo.MODID + ".config.servers.checkbox.showIngameJoins"));
		cbGameLeave = new GuiCheckbox(this, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5 + 20 + 5 + 20
				+ 5 + cbSize + 5, cbSize, I18n.format(ModInfo.MODID
				+ ".config.servers.checkbox.showIngameParts"));
		cbDeath = new GuiCheckbox(this, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5 + 20 + 5 + 20 + 5
				+ cbSize + 5 + cbSize + 5, cbSize, I18n.format(ModInfo.MODID
				+ ".config.servers.checkbox.showDeaths"));
		cbIRCJoin = new GuiCheckbox(this, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5 + 20 + 5 + 20
				+ 5 + cbSize + 5 + cbSize + 5 + cbSize + 5, cbSize, I18n.format(ModInfo.MODID
				+ ".config.servers.checkbox.showIRCJoins"));
		cbIRCLeave = new GuiCheckbox(this, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5 + 20 + 5 + 20
				+ 5 + cbSize + 5 + cbSize + 5 + cbSize + 5 + cbSize + 5, cbSize,
				I18n.format(ModInfo.MODID + ".config.servers.checkbox.showIRCParts"));

		int btnSize = 20;
		int btnDist = 10;
		this.buttonList.add(btnConnect = new GuiButtonToggle(10, width - btnSize - btnDist,
				btnDist, btnSize, btnSize, ""));
		this.buttonList.add(btnChannels = new GuiButton(10, width - btnSize - btnDist - btnSize
				- 10, btnDist, btnSize, btnSize, ""));
	}

	private static Random rnd = new Random();

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == btnDone) {
				if (hasChanged) {
					if (selectedServer != null) {
						selectedServer.saveConfig();
						selectedServer.setName(fName.getText());
						selectedServer.setHost(fHost.getText());
						selectedServer.setUsername(fUsername.getText());
						selectedServer.setServerpass(fPassword.getText() == "" ? null : fPassword
								.getText());

						selectedServer.setShowIngameJoins(cbGameJoin.getState());
						selectedServer.setShowIngameParts(cbGameLeave.getState());
						selectedServer.setShowDeaths(cbDeath.getState());
						selectedServer.setShowIRCJoins(cbIRCJoin.getState());
						selectedServer.setShowIRCParts(cbIRCLeave.getState());
					}
					onChange(null);
					return;
				} else {
					this.mc.displayGuiScreen(this.previousGui);
					return;
				}
			}
			if (button == btnDelServer) {
				this.mc.displayGuiScreen(new GuiConfirmationDeleteServer(this));
				return;
			}
			if (button == btnAddServer) {
				File f = null;
				do {
					f = new File(TheBestIRCModEver.proxy.configFolder, rnd.nextInt(134132) + ".cfg");
				} while (f.exists());
				servers.add(new Server(I18n.format(ModInfo.MODID
						+ ".config.servers.placeholder.name"), "", "", "", "", new ArrayList<String>(),
						new ArrayList<String>(), true, true, true, true, true, f));
				selectServerIndex(servers.size() - 1);
				return;
			}
			if (button == btnConnect) {
				if (selectedServer.isConnected()) {
					selectedServer.disconnect();
				} else {
					selectedServer.loadConfig();
					selectedServer.connect();
				}
			}
			if (button == btnChannels) {
				Minecraft.getMinecraft().displayGuiScreen(
						new GuiChannelList(this, selectedServer, mainMenu));
			}
		}
		super.actionPerformed(button);
	}

	public int drawLine(String line, int offset, int shifty) {
		this.fontRendererObj.drawString(line, offset, shifty, 0xd7edea);
		return shifty + 10;
	}

	@Override
	public void drawScreen(int mx, int my, float p_571_3_) {
		this.serverList.drawScreen(mx, my, p_571_3_);
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".config.servers.title"), this.width / 2, 16, 0xFFFFFF);

		btnAddServer.enabled = true;
		btnDelServer.enabled = selectedServer != null;

		btnDone.xPosition = listWidth + 10 + 5;

		int fWidth = this.width - (10 + listWidth + 10 + 10);
		fName.setWidth(fWidth);
		fHost.setWidth(fWidth);
		fUsername.setWidth(fWidth);
		fPassword.setWidth(fWidth);
		((GuiButtonCustom) btnDone).setWidth(fWidth + 5);

		if (selectedServer != null) {
			fName.setEnabled(true);
			fHost.setEnabled(true);
			fUsername.setEnabled(true);
			fPassword.setEnabled(true);

			cbGameJoin.setEnabled(true);
			cbGameLeave.setEnabled(true);
			cbDeath.setEnabled(true);
			cbIRCJoin.setEnabled(true);
			cbIRCLeave.setEnabled(true);

			btnChannels.enabled = true;
		} else {
			fName.setEnabled(false);
			fHost.setEnabled(false);
			fUsername.setEnabled(false);
			fPassword.setEnabled(false);

			cbGameJoin.setEnabled(false);
			cbGameLeave.setEnabled(false);
			cbDeath.setEnabled(false);
			cbIRCJoin.setEnabled(false);
			cbIRCLeave.setEnabled(false);

			btnChannels.enabled = false;
		}

		for (int k = 0; k < this.buttonList.size(); ++k) {
			GuiButton b = ((GuiButton) this.buttonList.get(k));
			if (b == btnConnect || b == btnChannels)
				continue;
			b.drawButton(this.mc, mx, my);
		}

		fName.drawTextBox();
		fHost.drawTextBox();
		fUsername.drawTextBox();
		fPassword.drawTextBox();

		cbGameJoin.drawCheckbox();
		cbGameLeave.drawCheckbox();
		cbDeath.drawCheckbox();
		cbIRCJoin.drawCheckbox();
		cbIRCLeave.drawCheckbox();

		// Draw connect toggle button
		{
			if (!mainMenu && selectedServer != null) {
				btnConnect.enabled = true;
				btnConnect.setState(selectedServer.isConnected());
			} else {
				btnConnect.enabled = false;
			}

			btnConnect.drawButton(mc, mx, my);

			if (selectedServer != null) {
				if (btnConnect.getState()) {
					Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
							ModInfo.MODID + ":others/yup.png"));
					RenderHelper.drawTexturedRect(btnConnect.xPosition + 2,
							btnConnect.yPosition + 2, 0, 0, btnConnect.getButtonWidth() - 4,
							btnConnect.getButtonWidth() - 4);
				} else {
					Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
							ModInfo.MODID + ":others/nope.png"));
					RenderHelper.drawTexturedRect(btnConnect.xPosition + 2,
							btnConnect.yPosition + 2, 0, 0, btnConnect.getButtonWidth() - 4,
							btnConnect.getButtonWidth() - 4);
				}
			}
		}

		// Draw connect toggle button
		{
			if (!mainMenu && selectedServer != null) {
				btnChannels.enabled = true;
			} else {
				btnChannels.enabled = false;
			}

			btnChannels.drawButton(mc, mx, my);

			if (selectedServer != null) {
				Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
						ModInfo.MODID + ":others/channels.png"));
				RenderHelper.drawTexturedRect(btnChannels.xPosition + 2, btnChannels.yPosition + 2,
						0, 0, btnChannels.getButtonWidth() - 4, btnChannels.getButtonWidth() - 4);
			}
		}

		// Tooltips
		{

			GL11.glEnable(GL11.GL_LIGHTING);
			if (mx >= btnConnect.xPosition
					&& mx < btnConnect.xPosition + btnConnect.getButtonWidth()
					&& my >= btnConnect.yPosition
					&& my < btnConnect.yPosition + btnConnect.getButtonWidth()) {
				drawHoveringText(Arrays.asList(new String[] { selectedServer != null ? (btnConnect
						.getState() ? I18n.format(ModInfo.MODID + ".config.servers.disconnect")
						: I18n.format(ModInfo.MODID + ".config.servers.connect")) : I18n
						.format(ModInfo.MODID + ".config.servers.select") }), mx, my,
						mc.fontRenderer);
			}
			GL11.glDisable(GL11.GL_LIGHTING);

			GL11.glEnable(GL11.GL_LIGHTING);
			if (mx >= btnChannels.xPosition
					&& mx < btnChannels.xPosition + btnChannels.getButtonWidth()
					&& my >= btnChannels.yPosition
					&& my < btnChannels.yPosition + btnChannels.getButtonWidth()) {
				drawHoveringText(
						Arrays.asList(new String[] { I18n.format(ModInfo.MODID
								+ ".config.servers.editChannels") }), mx, my, mc.fontRenderer);
			}
			GL11.glDisable(GL11.GL_LIGHTING);
		}

		fName.renderTooltip(mx, my);
		fHost.renderTooltip(mx, my);
		fUsername.renderTooltip(mx, my);
		fPassword.renderTooltip(mx, my);

		cbGameJoin.renderTooltip(mx, my);
		cbGameLeave.renderTooltip(mx, my);
		cbDeath.renderTooltip(mx, my);
		cbIRCJoin.renderTooltip(mx, my);
		cbIRCLeave.renderTooltip(mx, my);
	}

	public void confirmClicked(boolean par1, int par2) {

		if (par2 == 0) {
			if (par1) {
				Config.servers.remove(selectedServer);
				if (selectedServer.isConnected())
					selectedServer.getConnection().disconnect();
				if (selectedServer.getConfigFile().exists())
					selectedServer.getConfigFile().delete();
			}
			Minecraft.getMinecraft().displayGuiScreen(this);
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

			fName.setText(selectedServer.getName());
			fHost.setText(selectedServer.getHost());
			fUsername.setText(selectedServer.getUsername());
			fPassword.setText(selectedServer.getServerpass());

			cbGameJoin.setState(selectedServer.shouldShowIngameJoins());
			cbGameLeave.setState(selectedServer.shouldShowIngameParts());
			cbDeath.setState(selectedServer.shouldShowDeaths());
			cbIRCJoin.setState(selectedServer.shouldShowIRCJoins());
			cbIRCLeave.setState(selectedServer.shouldShowIRCParts());
		} else {
			this.selectedServer = null;
		}
		onChange(null);
	}

	public boolean serverIndexSelected(int var1) {
		return var1 == selected;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		if (fName.isEnabled())
			fName.mouseClicked(par1, par2, par3);
		if (fHost.isEnabled())
			fHost.mouseClicked(par1, par2, par3);
		if (fUsername.isEnabled())
			fUsername.mouseClicked(par1, par2, par3);
		if (fPassword.isEnabled())
			fPassword.mouseClicked(par1, par2, par3);

		if (cbGameJoin.isEnabled())
			cbGameJoin.mouseClick(par1, par2, par3);
		if (cbGameLeave.isEnabled())
			cbGameLeave.mouseClick(par1, par2, par3);
		if (cbDeath.isEnabled())
			cbDeath.mouseClick(par1, par2, par3);
		if (cbIRCJoin.isEnabled())
			cbIRCJoin.mouseClick(par1, par2, par3);
		if (cbIRCLeave.isEnabled())
			cbIRCLeave.mouseClick(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		if (fName.isEnabled())
			fName.textboxKeyTyped(par1, par2);
		if (fHost.isEnabled())
			fHost.textboxKeyTyped(par1, par2);
		if (fUsername.isEnabled())
			fUsername.textboxKeyTyped(par1, par2);
		if (fPassword.isEnabled())
			fPassword.textboxKeyTyped(par1, par2);
	}

	@Override
	public void onChange(Gui component) {
		if (component != null) {
			hasChanged = true;
			btnDone.displayString = I18n.format(ModInfo.MODID + ".config.save");
		} else {
			hasChanged = false;
			btnDone.displayString = I18n.format("gui.done");
		}
	}

}
