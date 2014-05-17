package com.amadornes.tbircme.render;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RenderHandler {

	@SubscribeEvent
	public void onRenderOverlayTick(RenderGameOverlayEvent ev) {
		if (ev.type != ElementType.CHAT)
			return;
		GL11.glPushMatrix();
		{
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor4d(1, 0, 0, 1);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2d(10, 10);
				GL11.glVertex2d(100, 10);
				GL11.glVertex2d(100, 100);
				GL11.glVertex2d(10, 100);
			}
			GL11.glEnd();
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

		}
		GL11.glPopMatrix();
	}

}
