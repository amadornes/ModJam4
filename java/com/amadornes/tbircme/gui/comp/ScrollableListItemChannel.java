package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.api.IIRCChannel;

public class ScrollableListItemChannel extends ScrollableListItem {
    
    private IIRCChannel channel;
    
    public ScrollableListItemChannel(ScrollableList parent, IIRCChannel channel) {
    
        super(parent);
        this.channel = channel;
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        GL11.glPushMatrix();
        {
            GL11.glTranslated(getX(), getY(), 0);
            
            if (parent.getSelected() == this) {
                drawGradientRect(0, 0, getWidth(), getHeight() + 1, 0x7FCCCCCC, 0x8F666666);
            }
            
            GL11.glPushMatrix();
            {
                GL11.glTranslated(marginHor, marginVert, 0);
                
                // Draw text
                FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
                fr.drawStringWithShadow("#" + channel.getName(), 0, 0, 0xFFFFFF);
                GL11.glTranslated(2, fr.FONT_HEIGHT + 1, 0);
                GL11.glScaled(0.75, 0.75, 0.75);
                if (channel.getConnection().isConnected()) {
                    fr.drawStringWithShadow("Connected!", 0, 0, 0x00FF00);
                } else {
                    if (channel.getConnection().isConnecting()) {
                        fr.drawStringWithShadow("Connecting", 0, 0, 0xFFFF00);
                    } else {
                        fr.drawStringWithShadow("Disconnected!", 0, 0, 0xFF0000);
                    }
                }
                GL11.glTranslated(0, fr.FONT_HEIGHT + 1, 0);
                GL11.glScaled(1 / 0.75, 1 / 0.75, 1 / 0.75);
                GL11.glScaled(0.5, 0.5, 0.5);
                fr.drawStringWithShadow("Server: " + channel.getConnection().getName(), 0, 0, 0xBBBBBB);
            }
            GL11.glPopMatrix();
            
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            if (isSelected()) {
                int y2 = getY();
                int y3 = y2;
                int maxY = y2 + getHeight() + 1;
                maxY = Math.min(maxY, ((int) parent.scrolled) + parent.getHeight());
                y2 = Math.max(((int) parent.scrolled) - y3, 0);
                drawGradientRect(-1, y2 - 1, getWidth() + 1, y2, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(-1, maxY - y3, getWidth() + 1, maxY - y3 + 1, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(-1, y2, 0, maxY - y3, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(getWidth(), y2, getWidth() + 1, maxY - y3, 0xFFDDDDDD, 0xFFDDDDDD);
            }
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }
        GL11.glPopMatrix();
    }
    
    private boolean isSelected() {
    
        return parent.getSelected() == this;
    }
    
    @Override
    public int getHeight() {
    
        return 34;
    }
    
    public IIRCChannel getChannel() {
    
        return channel;
    }
    
    public void setChannel(IIRCChannel c) {
    
        channel = c;
    }
}
