package com.amadornes.tbircme.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import org.lwjgl.input.Keyboard;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.network.Channel;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;

public class GuiChannelList extends GuiScreen implements IChangeListener {
	private GuiScreen previousGui;
	private GuiSlotChannelList channelList;
	private int selected = -1;
	private Channel selectedChannel;
	private int listWidth;
	private ArrayList<Channel> channels;
	private GuiButton btnAddServer;
	private GuiButton btnDelServer;
	private GuiButton btnDone;

	private GuiCheckbox cbDeath, cbGameJoin, cbGameLeave, cbIRCJoin, cbIRCLeave;

	private GuiTextFieldCustomizable fName, fHost, fUsername, fPassword;

	@SuppressWarnings("unused")
	private boolean mainMenu = false;

	private boolean hasChanged = false;

	public GuiChannelList(GuiScreen previousGui, boolean mainMenu) {
		this.previousGui = previousGui;
		this.channels = new ArrayList<Channel>();
		for (Server s : Config.servers)
			channels.addAll(s.getConnection().getChannels());
		this.mainMenu = mainMenu;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return Config.shouldConfigGuiPauseGame;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);

		for (Channel channel : channels) {
			listWidth = Math.max(listWidth,
					getFontRenderer().getStringWidth(channel.getChannel()) + 10);
			listWidth = Math.max(listWidth,
					getFontRenderer().getStringWidth(channel.getServer().getName()) + 10);
		}
		listWidth = Math.min(listWidth, 150);
		this.buttonList.add(btnDone = new GuiButton(6, this.width / 2 - 75, this.height - 38, I18n
				.format("gui.done")));
		btnAddServer = new GuiButton(20, 10, this.height - 60, this.listWidth, 20,
				I18n.format(ModInfo.MODID + ".config.add"));
		btnDelServer = new GuiButton(21, 10, this.height - 38, this.listWidth, 20,
				I18n.format(ModInfo.MODID + ".config.remove"));
		this.buttonList.add(btnAddServer);
		this.buttonList.add(btnDelServer);
		this.channelList = new GuiSlotChannelList(this, channels, listWidth);
		this.channelList.registerScrollButtons(this.buttonList, 7, 8);

		fName = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32, 200,
				20));
		fName.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.name",
				new Object[0]));
		fName.setEnabled(false);

		fHost = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10,
				32 + 20 + 5, 200, 20));
		fHost.setPlaceholder(I18n.format(ModInfo.MODID + ".config.servers.placeholder.host",
				new Object[0]));
		fName.setEnabled(false);

		fUsername = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32
				+ 20 + 5 + 20 + 5, 200, 20));
		fUsername.setPlaceholder(I18n
				.format(ModInfo.MODID + ".config.servers.placeholder.username"));
		fUsername.setEnabled(false);

		fPassword = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32
				+ 20 + 5 + 20 + 5 + 20 + 5, 200, 20));
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
	}

	private static Random rnd = new Random();

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == btnDone) {
				if (hasChanged) {
					if (selectedChannel != null) {
						// TODO selectedChannel.saveConfig();
						// selectedChannel.setName(fName.getText());
						// selectedChannel.setHost(fHost.getText());
						// selectedChannel.setUsername(fUsername.getText());
						// selectedChannel.setServerpass(fPassword.getText() ==
						// "" ? null : fPassword
						// .getText());
						//
						// selectedChannel.setShowIngameJoins(cbGameJoin.getState());
						// selectedChannel.setShowIngameParts(cbGameLeave.getState());
						// selectedChannel.setShowDeaths(cbDeath.getState());
						// selectedChannel.setShowIRCJoins(cbIRCJoin.getState());
						// selectedChannel.setShowIRCParts(cbIRCLeave.getState());
					}
					onChange(null);
					return;
				} else {
					this.mc.displayGuiScreen(this.previousGui);
					return;
				}
			}
			if (button == btnDelServer) {
				this.mc.displayGuiScreen(new GuiConfirmationDeleteChannel(this));
				return;
			}
			if (button == btnAddServer) {
				File f = null;
				do {
					f = new File(TheBestIRCModEver.proxy.configFolder, rnd.nextInt(134132) + ".cfg");
				} while (f.exists());
				// TODO channels.add(new Server(I18n.format(ModInfo.MODID
				// + ".config.servers.placeholder.name"), "", "", "", new
				// ArrayList<String>(),
				// new ArrayList<String>(), true, true, true, true, true, f));
				selectServerIndex(channels.size() - 1);
				return;
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
		this.channelList.drawScreen(mx, my, p_571_3_);
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".config.servers.title"), this.width / 2, 16, 0xFFFFFF);

		btnAddServer.enabled = true;
		btnDelServer.enabled = selectedChannel != null;
		int k;

		for (k = 0; k < this.buttonList.size(); ++k) {
			((GuiButton) this.buttonList.get(k)).drawButton(this.mc, mx, my);
		}

		for (k = 0; k < this.labelList.size(); ++k) {
			((GuiLabel) this.labelList.get(k)).func_146159_a(this.mc, mx, my);
		}

		if (selectedChannel != null) {
			fName.setEnabled(true);
			fHost.setEnabled(true);
			fUsername.setEnabled(true);
			fPassword.setEnabled(true);

			cbGameJoin.setEnabled(true);
			cbGameLeave.setEnabled(true);
			cbDeath.setEnabled(true);
			cbIRCJoin.setEnabled(true);
			cbIRCLeave.setEnabled(true);
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
				Config.servers.remove(selectedChannel);
				/*
				 * if (selectedChannel.isConnected())
				 * selectedChannel.getConnection().disconnect(); if
				 * (selectedChannel.getConfigFile().exists())
				 * selectedChannel.getConfigFile().delete();
				 */
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
		if (var1 >= 0 && var1 <= channels.size()) {
			this.selectedChannel = channels.get(selected);

			/*
			 * fName.setText(selectedChannel.getName());
			 * fHost.setText(selectedChannel.getHost());
			 * fUsername.setText(selectedChannel.getUsername());
			 * fPassword.setText(selectedChannel.getServerpass());
			 * 
			 * cbGameJoin.setState(selectedChannel.shouldShowIngameJoins());
			 * cbGameLeave.setState(selectedChannel.shouldShowIngameParts());
			 * cbDeath.setState(selectedChannel.shouldShowDeaths());
			 * cbIRCJoin.setState(selectedChannel.shouldShowIRCJoins());
			 * cbIRCLeave.setState(selectedChannel.shouldShowIRCParts());
			 */
		} else {
			this.selectedChannel = null;
		}
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
