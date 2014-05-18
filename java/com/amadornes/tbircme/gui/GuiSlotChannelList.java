package com.amadornes.tbircme.gui;

import java.util.ArrayList;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.network.Channel;

import cpw.mods.fml.client.GuiScrollingList;

public class GuiSlotChannelList extends GuiScrollingList {
	private GuiChannelList parent;
	private ArrayList<Channel> channels;

	public GuiSlotChannelList(GuiChannelList parent, ArrayList<Channel> channels, int listWidth) {
		super(parent.getMinecraftInstance(), listWidth, parent.height, 32, parent.height - 66 + 4,
				10, 35);
		this.parent = parent;
		this.channels = channels;
	}

	@Override
	protected int getSize() {
		return channels.size();
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
		Channel channel = channels.get(listIndex);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth(channel.getChannel(),
						listWidth - 10), this.left + 3, var3 + 2,
				channel.getServer().isConnected() ? 0xFFFFFF : 0xFF0000);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth(channel.getServer().getName(),
						listWidth - 10 - 5), this.left + 3 + 5, var3 + 12,
				channel.getServer().isConnected() ? 0xCCCCCC : 0xFF0000);
		this.parent.getFontRenderer().drawString(
				this.parent.getFontRenderer().trimStringToWidth(
						(!channel.getServer().isConnected() ? "\u00A7n" : "")
								+ I18n.format(ModInfo.MODID
										+ ".config.servers."
										+ (channel.getServer().isConnected() ? "connected"
												: "notconnected")), listWidth - 10 - 5),
				this.left + 3 + 5, var3 + 12 + 10,
				channel.getServer().isConnected() ? 0xCCCCCC : 0xFF0000);
	}

}