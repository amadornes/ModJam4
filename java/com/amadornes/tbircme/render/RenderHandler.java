package com.amadornes.tbircme.render;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.util.ChatComponentEmote;
import com.amadornes.tbircme.util.ReflectionUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

	@SuppressWarnings("rawtypes")
	@SubscribeEvent
	public void onRenderOverlayTick(RenderGameOverlayEvent ev) {
		if (ev.type != ElementType.CHAT)
			return;

		if (Minecraft.getMinecraft().gameSettings.chatVisibility == EnumChatVisibility.HIDDEN)
			return;

		GL11.glPushMatrix();

		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glScaled(Minecraft.getMinecraft().gameSettings.chatScale,
				Minecraft.getMinecraft().gameSettings.chatScale, 1);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_POLYGON_SMOOTH);
		GL11.glEnable(GL11.GL_POINT_SMOOTH);

		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		List chatLines = (List) ReflectionUtils.get(chat, "chatLines");
		boolean isOpen = chat.getChatOpen();

		if (chatLines == null)
			return;

		for (Object o : chatLines) {
			ChatLine l = (ChatLine) o;
			renderEmotesForLine(l, chat, chatLines, isOpen, ev);
		}

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
	private void renderEmotesForLine(ChatLine l, GuiNewChat chat, List chatLines, boolean isOpen,
			RenderGameOverlayEvent ev) {

		// Make it an emote line :D
		if (!(l.func_151461_a() instanceof ChatComponentEmote))
			ReflectionUtils.set(l, "lineString", new ChatComponentEmote(l.func_151461_a()));

		ChatComponentEmote cc = (ChatComponentEmote) l.func_151461_a();

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int id = chatLines.indexOf(l);
		int id2 = (id - ((int) ReflectionUtils.get(chat, "field_146250_j")));
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
				String unformatted = cc.getOriginalText().trim();
				String s = unformatted;
				int index = 0;

				boolean found = false;
				do {
					found = false;
					String emote = "";
					for (String e : TheBestIRCModEver.emotes) {
						if (s.contains(" " + e + " ") || s.startsWith(e + " ")
								|| (s.startsWith(e) && s.length() == e.length())
								|| s.endsWith(" " + e)
								|| (s.endsWith(e) && s.length() == e.length())) {
							emote = e;
							found = true;
							index += s.indexOf(e) + e.length();
							s = s.substring(s.indexOf(e) + e.length());
							break;
						}
					}

					if (found) {
						String before = unformatted.substring(0, index - emote.length() - 1);
						int w = fr.getStringWidth(before);
						renderEmote(w, id, emote, opacity, ev);
					}
				} while (found);
			}
		}
	}

	public void renderEmote(double x, int line, String emote, double opacity,
			RenderGameOverlayEvent ev) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		int emoteSize = 28;
		GL11.glPushMatrix();
		{
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID
					+ ":emotes/" + emote.toLowerCase() + ".png"));
			GL11.glColor4d(1, 1, 1, opacity);
			GL11.glTranslated(x + 3, ev.resolution.getScaledHeight_double() - (9 * line) - 32
					- (emoteSize / 4D), 0);
			GL11.glScaled(0.5, 0.5, 1);
			drawTexturedModalRect(0, 0, 0, 0, emoteSize, emoteSize);
			GL11.glScaled(2, 2, 1);
		}
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
	}

	public void drawTexturedModalRect(double x, double y, double u, double v, double width,
			double height) {

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
	}

}
