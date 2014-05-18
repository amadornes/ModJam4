package com.amadornes.tbircme.gui;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.network.Server;

import cpw.mods.fml.client.GuiScrollingList;

public class GuiSlotServerList extends GuiScrollingList {
	private GuiServerList parent;
	private ArrayList<Server> servers;

	public GuiSlotServerList(GuiServerList parent, ArrayList<Server> servers, int listWidth) {
		super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 66 + 4,
				10, 35);
		this.parent = parent;
		this.servers = servers;
	}

	@Override
	protected int getSize() {
		return servers.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2) {
		this.parent.selectServerIndex(var1);
	}

	@Override
	protected boolean isSelected(int var1) {
		return this.parent.serverIndexSelected(var1);
	}

	@Override
	protected void drawBackground() {
		this.parent.drawDefaultBackground();
	}

	@Override
	protected int getContentHeight() {
		return (this.getSize()) * 35 + 1;
	}

	@Override
	protected void drawSlot(int listIndex, int var2, int var3, int var4, Tessellator var5) {
		Server server = servers.get(listIndex);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth(server.getName(), listWidth - 10),
				this.left + 3, var3 + 2, server.isConnected() ? 0xFFFFFF : 0xFF0000);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth(
						server.getChannels().size()
								+ " "
								+ I18n.format(ModInfo.MODID + ".config.servers.channels"), listWidth - 10 - 5), this.left + 3 + 5, var3 + 12,
				server.isConnected() ? 0xCCCCCC : 0xFF0000);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth((!server.isConnected() ? "\u00A7n" : "") + I18n.format(ModInfo.MODID + ".config.servers." + (server.isConnected() ? "connected" : "notconnected")), listWidth - 10 - 5), this.left + 3 + 5, var3 + 12 + 10,
				server.isConnected() ? 0xCCCCCC : 0xFF0000);
	}

}