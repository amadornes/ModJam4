package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.ref.Names;

public class ScrollableListItemServer extends ScrollableListItem {
    
    private IIRCConnection connection;
    private int            minHeight  = 35;
    private int            maxHeight  = 100;      // 90
    private int            height     = minHeight;
    private int            msToResize = 500;
    private long           start      = 0;
    
    public ScrollableListItemServer(ScrollableList parent, IIRCConnection connection) {
    
        super(parent);
        this.connection = connection;
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        // Max height calculation
        {
            maxHeight = 42 + (connection.getChannels().size() > 0 ? 3 : 0)
                    + (int) ((Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT / 2D) * connection.getChannels().size());
        }
        
        // Resizing
        if (isSelected()) {
            if (height < maxHeight) {
                if (start == 0) start = System.currentTimeMillis();
                height = (int) (minHeight + ((maxHeight - minHeight) * ((System.currentTimeMillis() - start) / (double) (msToResize))));
            } else {
                start = 0;
            }
        } else {
            if (height > minHeight) {
                if (start == 0) start = System.currentTimeMillis();
                height = (int) (maxHeight - ((maxHeight - minHeight) * ((System.currentTimeMillis() - start) / (double) (msToResize))));
            } else {
                start = 0;
            }
        }
        
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
                double y2 = 0;
                FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
                fr.drawStringWithShadow(connection.getName(), 0, 0, 0xFFFFFF);
                GL11.glTranslated(2, fr.FONT_HEIGHT + 1, 0);
                y2 += fr.FONT_HEIGHT + 1;
                GL11.glScaled(0.75, 0.75, 0.75);
                if (connection.isConnected()) {
                    fr.drawStringWithShadow("Connected!", 0, 0, 0x00FF00);
                } else {
                    if (connection.isConnecting()) {
                        fr.drawStringWithShadow("Connecting", 0, 0, 0xFFFF00);
                    } else {
                        fr.drawStringWithShadow("Disconnected!", 0, 0, 0xFF0000);
                    }
                }
                GL11.glScaled(1 / 0.75, 1 / 0.75, 1 / 0.75);
                GL11.glTranslated(1, (fr.FONT_HEIGHT + 1) * 0.75 + 1, 0);
                y2 += (fr.FONT_HEIGHT + 1) * 0.75 + 1;
                GL11.glScaled(0.5, 0.5, 0.5);
                fr.drawSplitString(connection.getConfig().getHost() + ":" + connection.getConfig().getPort(), 1, 1,
                        (getWidth() - marginHor - marginHor) * 2, 0x222222);
                fr.drawSplitString(connection.getConfig().getHost() + ":" + connection.getConfig().getPort(), 0, 0,
                        (getWidth() - marginHor - marginHor) * 2, 0xBBBBBB);
                GL11.glScaled(1 / 0.5, 1 / 0.5, 1 / 0.5);
                GL11.glTranslated(0, (fr.FONT_HEIGHT + 1) * 0.75 + 1, 0);
                y2 += (fr.FONT_HEIGHT + 1) * 0.75 + 1;
                if (isSelected()) {
                    GL11.glScaled(0.5, 0.5, 0.5);
                    fr.drawSplitString(I18n.format(Names.GuiNames.CHANNELS_TITLE) + ":", 1, 1, getWidth() - marginHor - marginHor, 0x222222);
                    fr.drawSplitString(I18n.format(Names.GuiNames.CHANNELS_TITLE) + ":", 0, 0, getWidth() - marginHor - marginHor, 0xBBBBBB);
                    GL11.glScaled(1 / 0.5, 1 / 0.5, 1 / 0.5);
                    GL11.glTranslated(0, (fr.FONT_HEIGHT + 1) * 0.75, 0);
                    y2 += (fr.FONT_HEIGHT + 1) * 0.75;
                    GL11.glScaled(0.5, 0.5, 0.5);
                    double left = (getHeight() - marginVert - marginVert - y2) * 2 + (minHeight / 1.5D);
                    double amt = (fr.FONT_HEIGHT + 1) * 0.75 + 1;
                    int times = (int) Math.floor(left / amt) - 1;
                    int count = Math.min(times, connection.getChannels().size());
                    for (int i = 0; i < count; i++) {
                        IIRCChannel c = connection.getChannels().get(i);
                        fr.drawStringWithShadow(" - #" + c.getName(), 0, 0, 0xBBBBBB);
                        GL11.glTranslated(0, amt, 0);
                    }
                    if (times < count) {
                        fr.drawStringWithShadow(" - ...", 0, 0, 0x888888);
                    }
                }
            }
            GL11.glPopMatrix();
            
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            if (parent.getSelected() == this) {
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
    
        return height;
    }
    
    public IIRCConnection getConnection() {
    
        return connection;
    }
}
