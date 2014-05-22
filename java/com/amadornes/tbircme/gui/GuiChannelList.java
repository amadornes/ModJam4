package com.amadornes.tbircme.gui;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.gui.comp.DropDownOption;
import com.amadornes.tbircme.gui.comp.GuiButtonCustom;
import com.amadornes.tbircme.gui.comp.GuiDropDown;
import com.amadornes.tbircme.gui.comp.GuiTextFieldCustomizable;
import com.amadornes.tbircme.network.Channel;
import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.IChangeListener;

public class GuiChannelList extends TBIRCMEGuiScreen implements IChangeListener {
	private GuiScreen previousGui;
	private GuiSlotChannelList channelList;
	private int selected = -1;
	private Channel selectedChannel;
	private int listWidth;
	private Server server = null;
	private ArrayList<Channel> channels;
	private GuiButton btnAddChannel;
	private GuiButton btnDelChannel;
	private GuiButtonCustom btnDone;
	private GuiButtonCustom btnCustomize;

	private GuiTextFieldCustomizable fChannel;
	private GuiDropDown ddServer;

	@SuppressWarnings("unused")
	private boolean mainMenu = false;

	private boolean hasChanged = false;

	public GuiChannelList(GuiScreen previousGui, boolean mainMenu) {
		this.previousGui = previousGui;
		this.channels = new ArrayList<Channel>();
		for (Server s : Config.servers)
			channels.addAll(s.getChannels());
		this.mainMenu = mainMenu;
	}

	public GuiChannelList(GuiScreen previousGui, Server server, boolean mainMenu) {
		this.previousGui = previousGui;
		this.server = server;
		this.channels = (ArrayList<Channel>) server.getChannels();
		this.mainMenu = mainMenu;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		super.initGui();

		listWidth = 150;
		this.buttonList.add(btnDone = new GuiButtonCustom(6, this.width / 2 - 75, this.height - 38,
				I18n.format("gui.done")));
		this.channelList = new GuiSlotChannelList(this, channels, listWidth);
		this.channelList.registerScrollButtons(this.buttonList, 7, 8);
		this.buttonList.add(btnAddChannel = new GuiButton(20, 10, this.height - 60, this.listWidth,
				20, I18n.format(ModInfo.MODID + ".config.add")));
		this.buttonList.add(btnDelChannel = new GuiButton(21, 10, this.height - 38, this.listWidth,
				20, I18n.format(ModInfo.MODID + ".config.remove")));

		fChannel = (new GuiTextFieldCustomizable(this, fontRendererObj, 10 + listWidth + 10, 32,
				200, 20));
		fChannel.setPlaceholder(I18n.format(ModInfo.MODID + ".config.channels.placeholder.name"));
		fChannel.setEnabled(false);

		if (server == null) {
			ddServer = new GuiDropDown(this, fontRendererObj, 10 + listWidth + 10, 32 + 20 + 5,
					200, 20);
			ddServer.setPlaceholder(I18n.format(ModInfo.MODID
					+ ".config.channels.placeholder.server"));
			for (Server s : Config.servers) {
				ddServer.addOption(new DropDownOption(s.getName() + " (" + s.getHost() + ")", true));
			}
			this.buttonList.add(btnCustomize = new GuiButtonCustom(15, 10 + listWidth + 10, 32 + 20 + 5 + 20 + 5, I18n.format(ModInfo.MODID + ".config.channels.customize")));
		}else{
			this.buttonList.add(btnCustomize = new GuiButtonCustom(15, 10 + listWidth + 10, 32 + 20 + 5, I18n.format(ModInfo.MODID + ".config.channels.customize")));
		}
		btnCustomize.enabled = false;
		btnDelChannel.enabled = false;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			if (button == btnDone) {
				if (hasChanged) {
					if (isValid()) {
						if (selectedChannel != null) {
							Server nServer = selectedChannel.getServer();
							Server oServer = selectedChannel.getServer();
							try {
								if (server == null)
									nServer = Config.servers.get(ddServer.getSelectedOptionID());
							} catch (Exception ex) {
							}

							boolean shouldRelog = !fChannel.getText().equalsIgnoreCase(
									selectedChannel.getChannel())
									|| nServer != oServer;
							if (shouldRelog)
								oServer.getChannels().remove(selectedChannel);
							oServer.saveConfig();
							if (oServer != null && oServer.isConnected())
								oServer.getConnection().part(selectedChannel.getChannel());

							// Save everything
							selectedChannel.setChannel(fChannel.getText());
							selectedChannel.setServer(nServer);

							// Finish saving

							if (shouldRelog) {
								nServer.getChannels().add(selectedChannel);
								nServer.saveConfig();
								if (nServer != null && nServer.isConnected())
									nServer.getConnection().join(selectedChannel);
							}
						}

					} else {
						channelIndexSelected(selected);
					}
					onChange(null);
					return;
				} else {
					this.mc.displayGuiScreen(this.previousGui);
					return;
				}
			}
			if (button == btnAddChannel) {
				channels.add(new Channel(server, ""));
				selectChannelIndex(channels.size() - 1);
				onChange(ddServer);
			}
			if (button == btnDelChannel) {

			}
		}
		super.actionPerformed(button);
	}

	public int drawLine(String line, int offset, int shifty) {
		this.fontRendererObj.drawString(line, offset, shifty, 0xd7edea);
		return shifty + 10;
	}

	@Override
	public void drawScreen(int mx, int my, float f) {
		this.channelList.drawScreen(mx, my, f);

		btnDone.xPosition = listWidth + 10 + 5;

		int fWidth = this.width - (10 + listWidth + 10 + 10);
		/*fChannel.setWidth(fWidth);
		if (server == null)
			ddServer.setWidth(fWidth);*/
		((GuiButtonCustom) btnDone).setWidth(fWidth + 5);

		String s = (server != null ? (" (" + server.getName() + ")") : "");
		this.drawCenteredString(this.fontRendererObj,
				I18n.format(ModInfo.MODID + ".config.channels.title") + s, this.width / 2, 16,
				0xFFFFFF);

		for (int k = 0; k < this.buttonList.size(); ++k) {
			((GuiButton) this.buttonList.get(k)).drawButton(this.mc, mx, my);
		}

		fChannel.setEnabled(selectedChannel != null);
		btnCustomize.enabled = selectedChannel != null;
		btnDelChannel.enabled = selectedChannel != null;
		if (server == null)
			ddServer.setEnabled(selectedChannel != null);

		fChannel.drawTextBox();
		if (server == null)
			ddServer.drawDropDown(mx, my, f);

		if (server == null)
			ddServer.drawDropDownMenu(mx, my, f);
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

	public void selectChannelIndex(int var1) {
		this.selected = var1;
		if (var1 >= 0 && var1 <= channels.size()) {
			this.selectedChannel = channels.get(selected);

			fChannel.setText(selectedChannel.getChannel());
			if (server == null)
				ddServer.setSelected(Config.servers.indexOf(selectedChannel.getServer()));
		} else {
			this.selectedChannel = null;
		}
		onChange(null);
	}

	public boolean channelIndexSelected(int var1) {
		return var1 == selected;
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) {
		super.mouseClicked(x, y, btn);

		if (fChannel.isEnabled())
			fChannel.mouseClicked(x, y, btn);

		if (server == null)
			if (ddServer.isEnabled())
				ddServer.mouseClicked(x, y, btn);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		if (fChannel.isEnabled())
			fChannel.textboxKeyTyped(par1, par2);
	}

	@Override
	public void onChange(Gui component) {
		if (component != null) {
			hasChanged = true;
			if (isValid()) {
				btnDone.displayString = I18n.format(ModInfo.MODID + ".config.save");
			} else {
				btnDone.displayString = I18n.format(ModInfo.MODID + ".config.cancel");
			}
		} else {
			hasChanged = false;
			btnDone.displayString = I18n.format("gui.done");
		}
	}

	public boolean isValid() {
		if (fChannel.getText().trim().length() == 0)
			return false;
		if (server == null)
			if (ddServer.getSelectedOption() == null)
				return false;

		return true;
	}

}
