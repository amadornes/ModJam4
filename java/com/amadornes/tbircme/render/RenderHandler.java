package com.amadornes.tbircme.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

	@SubscribeEvent
	public void onRenderOverlayTick(RenderGameOverlayEvent ev) {
		if (ev.type != ElementType.CHAT)
			return;

		int emoteSize = 14;
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();

		boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4d(1, 0, 0, 1);
			// drawTexturedModalRect(20, ev.resolution.getScaledHeight_double()
			// - 8 - 32 - (emoteSize/2), 0, 0, emoteSize, emoteSize);
			// drawTexturedModalRect(20, ev.resolution.getScaledHeight_double()
			// - 32 - (emoteSize/2), 0, 0, emoteSize, emoteSize);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
		GL11.glPopMatrix();

		if (lighting)
			GL11.glEnable(GL11.GL_LIGHTING);
	}

	public void drawTexturedModalRect(double x, double y, double u, double v, double width,
			double height) {
		double zLevel = 0;
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) zLevel,
				(double) ((float) (u + 0) * f), (double) ((float) (v + height) * f1));
		tessellator.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel,
				(double) ((float) (u + width) * f), (double) ((float) (v + height) * f1));
		tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) zLevel,
				(double) ((float) (u + width) * f), (double) ((float) (v + 0) * f1));
		tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel,
				(double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1));
		tessellator.draw();
	}

}
