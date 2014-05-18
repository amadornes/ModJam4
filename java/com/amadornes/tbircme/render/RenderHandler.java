package com.amadornes.tbircme.render;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.emote.Emote;
import com.amadornes.tbircme.emote.EmoteWrapper;
import com.amadornes.tbircme.util.ChatComponentEmote;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.ReflectionUtils;
import com.amadornes.tbircme.util.Util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

	private List<String> tip = null;

	@SuppressWarnings("rawtypes")
	@SubscribeEvent
	public void onRenderOverlayTick(RenderGameOverlayEvent ev) {
		if (ev.type != ElementType.CHAT)
			return;

		if (Minecraft.getMinecraft().gameSettings.chatVisibility == EnumChatVisibility.HIDDEN)
			return;

		tip = null;

		if (!Config.emotesEnabled)
			return;

		GL11.glPushMatrix();

		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glScaled(Minecraft.getMinecraft().gameSettings.chatScale,
				Minecraft.getMinecraft().gameSettings.chatScale, 1);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		List chatLines = (List) ReflectionUtils.get(chat, Util.isDeobfuscated() ? "chatLines"
				: "field_146252_h");
		List chatLines2 = (List) ReflectionUtils.get(chat, "field_146253_i");
		boolean isOpen = chat.getChatOpen();

		if (chatLines == null)
			return;

		int i = 0;
		for (Object o : chatLines) {
			ChatLine l = (ChatLine) o;
			ChatLine l2 = (ChatLine) chatLines2.get(i);
			try {
				renderEmotesForLine(l, l2, chat, chatLines, chatLines2, isOpen, ev);
			} catch (Exception ex) {
			}
			i++;
		}

		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_LIGHTING);
			if (isOpen && tip != null)
				RenderHelper.drawHoveringText(tip, ev.mouseX, ev.mouseY,
						Minecraft.getMinecraft().fontRenderer);
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
		GL11.glDisable(GL11.GL_POINT_SMOOTH);
		GL11.glScaled(1 / Minecraft.getMinecraft().gameSettings.chatScale,
				1 / Minecraft.getMinecraft().gameSettings.chatScale, 1);
		if (lighting)
			GL11.glEnable(GL11.GL_LIGHTING);

		GL11.glPopMatrix();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void renderEmotesForLine(ChatLine l, ChatLine l2, GuiNewChat chat, List chatLines,
			List chatLines2, boolean isOpen, RenderGameOverlayEvent ev) {

		// Make it an emote line :D
		if (!(l.func_151461_a() instanceof ChatComponentEmote)
				|| !(l2.func_151461_a() instanceof ChatComponentEmote)) {
			ReflectionUtils.set(l, Util.isDeobfuscated() ? "lineString" : "field_74541_b",
					new ChatComponentEmote(l.func_151461_a()));
			ReflectionUtils.set(l2, Util.isDeobfuscated() ? "lineString" : "field_74541_b",
					new ChatComponentEmote(l2.func_151461_a()));
		}

		ChatComponentEmote cc = (ChatComponentEmote) l.func_151461_a();

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int id = 0;
		for (ChatLine l3 : ((List<ChatLine>) chatLines)) {
			if (l3 == l)
				break;
			id += fr.listFormattedStringToWidth(
					((ChatComponentEmote) l3.func_151461_a()).getOriginalText(),
					(int) (Minecraft.getMinecraft().gameSettings.chatWidth * 320)).size();
		}
		int id2 = (id - ((Integer) ReflectionUtils.get(chat, "field_146250_j")));
		if ((id2 + 1) * 9 > (isOpen ? Minecraft.getMinecraft().gameSettings.chatHeightFocused
				: Minecraft.getMinecraft().gameSettings.chatHeightUnfocused) * 180 || id2 < 0)
			return;
		id = id2;

		double opacity = Minecraft.getMinecraft().gameSettings.chatOpacity * 0.9F + 0.1F;

		int k1 = Minecraft.getMinecraft().ingameGUI.getUpdateCounter() - l.getUpdatedCounter();

		if (k1 < 200 || isOpen) {
			double d0 = (double) k1 / 200.0D;
			d0 = 1.0D - d0;
			d0 *= 10.0D;

			if (d0 < 0.0D) {
				d0 = 0.0D;
			}

			if (d0 > 1.0D) {
				d0 = 1.0D;
			}

			d0 *= d0;
			double i2 = (255.0D * d0);

			if (isOpen) {
				i2 = 255;
			}

			i2 = ((float) i2 * opacity);

			if (i2 > 3) {
				List<EmoteWrapper> emoteL = cc.getEmotes();
				if (emoteL == null) {
					emoteL = new ArrayList<EmoteWrapper>();
					cc.setEmotes(emoteL);
					List lines = fr.listFormattedStringToWidth(cc.getOriginalText().trim(),
							(int) (Minecraft.getMinecraft().gameSettings.chatWidth * 320));
					for (int i4 = 0; i4 < lines.size(); i4++) {
						String unformatted = (String) lines.get(i4);
						String s = unformatted;
						int index = 0;
						int lastIndex = 0;
						int translation = 0;

						boolean found = false;
						do {
							found = false;
							Emote emote = null;
							int i = s.length();

							for (Emote e : TheBestIRCModEver.emotes) {
								if (s.contains(" " + e.getEmote() + " ")
										|| s.startsWith(e.getEmote() + " ")
										|| (s.startsWith(e.getEmote()) && s.length() == e
												.getEmote().length())
										|| s.endsWith(" " + e.getEmote())
										|| (s.endsWith(e.getEmote()) && s.length() == e.getEmote()
												.length())) {
									if (!e.hasFile()) {
										e.download();
										continue;
									}

									found = true;
									int i3 = s.indexOf(e.getEmote());
									if (i3 < i) {
										i = i3;
										emote = e;
									}
								}
							}

							if (found) {
								lastIndex = index;
								index += s.indexOf(emote.getEmote());
								s = s.substring(s.indexOf(emote.getEmote())
										+ emote.getEmote().length());
							}

							if (found) {
								String before = unformatted.substring(lastIndex, index);
								translation += fr.getStringWidth(before) + 7;
								emoteL.add(new EmoteWrapper(emote, lines.size() - i4 - 1,
										translation - 11));
							}
						} while (found);
					}
				} else {
					for (EmoteWrapper w : emoteL) {
						try {
							renderEmote(w.getX(), id + w.getLine(), w.getEmote(), opacity, ev);
						} catch (Exception ex) {
						}
					}
				}
			}
		}
	}

	public void renderEmote(double x, int line, Emote emote, double opacity,
			RenderGameOverlayEvent ev) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		double emoteSize = 28;
		double renderSize = 14;
		GL11.glPushMatrix();
		{
			try {
				BufferedImage bi = null;
				int tex;
				if ((tex = emote.getTexture()) == -1 || (bi = emote.getImg()) == null) {
					try {
						bi = ImageIO.read(emote.getFile());
						tex = TextureUtil.uploadTextureImage(GL11.glGenTextures(), bi);
						emote.setTexture(tex);
						emote.setImg(bi);
					} catch (Exception ex) {

					}
				}

				double mul = (renderSize) / ((double) bi.getWidth());
				if (mul * bi.getHeight() > renderSize) {
					mul = (renderSize) / ((double) bi.getHeight());
				}

				GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);

				GL11.glColor4d(1, 1, 1, opacity);

				double x2 = x + 3 + ((renderSize / 2D) - ((mul * bi.getWidth()) / 2D));
				double y = ev.resolution.getScaledHeight_double() - (9 * line) - 32
						- (emoteSize / 4D);

				if (ev.mouseX >= x2 && ev.mouseX < x2 + renderSize && ev.mouseY >= y
						&& ev.mouseY < y + renderSize)
					tip = emote.getToolTip();

				GL11.glTranslated(x2, y, 0);
				GL11.glScaled(mul, mul, 1);
				RenderHelper.drawTexturedRect(0, 0, 0, 0, bi.getWidth(), bi.getHeight());
				GL11.glScaled(1D / mul, 1D / mul, 1);
			} catch (Exception e) {
			}
		}
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
	}

}
