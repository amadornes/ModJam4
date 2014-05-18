package com.amadornes.tbircme.render;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.util.ChatComponentEmote;
import com.amadornes.tbircme.util.Emote;
import com.amadornes.tbircme.util.ReflectionUtils;

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
		List chatLines = (List) ReflectionUtils.get(chat, "chatLines");
		List chatLines2 = (List) ReflectionUtils.get(chat, "field_146253_i");
		boolean isOpen = chat.getChatOpen();

		if (chatLines == null)
			return;

		int i = 0;
		for (Object o : chatLines) {
			ChatLine l = (ChatLine) o;
			ChatLine l2 = (ChatLine) chatLines2.get(i);
			renderEmotesForLine(l, l2, chat, chatLines, chatLines2, isOpen, ev);
			i++;
		}

		GL11.glPushMatrix();
		{
			GL11.glEnable(GL11.GL_LIGHTING);
			if (isOpen && tip != null)
				drawHoveringText(tip, ev.mouseX, ev.mouseY, Minecraft.getMinecraft().fontRenderer);
			GL11.glDisable(GL11.GL_LIGHTING);
		}
		GL11.glPopMatrix();

		GL11.glScaled(1 / Minecraft.getMinecraft().gameSettings.chatScale,
				1 / Minecraft.getMinecraft().gameSettings.chatScale, 1);
		if (lighting)
			GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_POLYGON_SMOOTH);
		GL11.glDisable(GL11.GL_POINT_SMOOTH);

		GL11.glPopMatrix();
	}

	@SuppressWarnings("rawtypes")
	private void renderEmotesForLine(ChatLine l, ChatLine l2, GuiNewChat chat, List chatLines,
			List chatLines2, boolean isOpen, RenderGameOverlayEvent ev) {

		// Make it an emote line :D
		if (!(l2.func_151461_a() instanceof ChatComponentEmote)) {
			ReflectionUtils.set(l2, "lineString", new ChatComponentEmote(l2.func_151461_a()));
		}

		IChatComponent cc = l.func_151461_a();

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int id = chatLines.indexOf(l);
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
				String unformatted = cc.getUnformattedText().trim();
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
								|| (s.startsWith(e.getEmote()) && s.length() == e.getEmote()
										.length())
								|| s.endsWith(" " + e.getEmote())
								|| (s.endsWith(e.getEmote()) && s.length() == e.getEmote().length())) {
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
						s = s.substring(s.indexOf(emote.getEmote()) + emote.getEmote().length());
					}

					if (found) {
						String before = unformatted.substring(lastIndex, index);
						translation += fr.getStringWidth(before) + 7;
						renderEmote(translation - 11, id, emote, opacity, ev);
					}
				} while (found);
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
				drawTexturedModalRect(0, 0, 0, 0, bi.getWidth(), bi.getHeight());
				GL11.glScaled(1D / mul, 1D / mul, 1);
			} catch (Exception e) {
			}
		}
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawTexturedModalRect(double x, double y, double u, double v, double width,
			double height) {

		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(0, 1);
			GL11.glVertex3d(x, y + height, 0);
			GL11.glTexCoord2d(1, 1);
			GL11.glVertex3d(x + width, y + height, 0);
			GL11.glTexCoord2d(1, 0);
			GL11.glVertex3d(x + width, y, 0);
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex3d(x, y, 0);
		}
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	private float zLevel = 0;

	public void drawHoveringText(List<String> text, int x, int y, FontRenderer font) {
		if (!text.isEmpty()) {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			int k = 0;
			Iterator<String> iterator = text.iterator();

			while (iterator.hasNext()) {
				String s = (String) iterator.next();
				int l = font.getStringWidth(s);

				if (l > k) {
					k = l;
				}
			}

			int j2 = x + 12;
			int k2 = y - 12;
			int i1 = 8;

			if (text.size() > 1) {
				i1 += 2 + (text.size() - 1) * 10;
			}

			this.zLevel = 300.0F;
			int j1 = -267386864;
			this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
			this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
			int k1 = 1347420415;
			int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
			this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
			this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
			this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

			for (int i2 = 0; i2 < text.size(); ++i2) {
				String s1 = (String) text.get(i2);
				font.drawStringWithShadow(s1, j2, k2, -1);

				if (i2 == 0) {
					k2 += 2;
				}

				k2 += 10;
			}

			this.zLevel = 0.0F;
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			RenderHelper.enableStandardItemLighting();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}

	protected void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
		float f = (float) (par5 >> 24 & 255) / 255.0F;
		float f1 = (float) (par5 >> 16 & 255) / 255.0F;
		float f2 = (float) (par5 >> 8 & 255) / 255.0F;
		float f3 = (float) (par5 & 255) / 255.0F;
		float f4 = (float) (par6 >> 24 & 255) / 255.0F;
		float f5 = (float) (par6 >> 16 & 255) / 255.0F;
		float f6 = (float) (par6 >> 8 & 255) / 255.0F;
		float f7 = (float) (par6 & 255) / 255.0F;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(f1, f2, f3, f);
		tessellator.addVertex((double) par3, (double) par2, (double) this.zLevel);
		tessellator.addVertex((double) par1, (double) par2, (double) this.zLevel);
		tessellator.setColorRGBA_F(f5, f6, f7, f4);
		tessellator.addVertex((double) par1, (double) par4, (double) this.zLevel);
		tessellator.addVertex((double) par3, (double) par4, (double) this.zLevel);
		tessellator.draw();
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

}
