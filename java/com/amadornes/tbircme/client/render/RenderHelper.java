package com.amadornes.tbircme.client.render;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHelper {
    
    /**
     * @author K-4U
     * 
     */
    public static final void vertexWithTexture(double x, double y, double z, float tx, float ty) {
    
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex3d(x, y, z);
    }
    
    public static DoubleBuffer planeEquation(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
    
        double[] eq = new double[4];
        eq[0] = (y1 * (z2 - z3)) + (y2 * (z3 - z1)) + (y3 * (z1 - z2));
        eq[1] = (z1 * (x2 - x3)) + (z2 * (x3 - x1)) + (z3 * (x1 - x2));
        eq[2] = (x1 * (y2 - y3)) + (x2 * (y3 - y1)) + (x3 * (y1 - y2));
        eq[3] = -((x1 * ((y2 * z3) - (y3 * z2))) + (x2 * ((y3 * z1) - (y1 * z3))) + (x3 * ((y1 * z2) - (y2 * z1))));
        return fromArray(eq);
    }
    
    public static DoubleBuffer fromArray(double... eq) {
    
        DoubleBuffer b = BufferUtils.createDoubleBuffer(eq.length * 2).put(eq);
        b.flip();
        return b;
    }
    
    public static final void drawTexturedRect(double x, double y, double u, double v, double width, double height) {
    
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
    
    public static final void drawColoredRect(double x, double y, double width, double height, int color) {
    
        float red = (color >> 16 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float alpha = (color >> 24 & 255) / 255.0F;
        
        GL11.glPushMatrix();
        
        boolean isTextureEnabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        GL11.glColor4f(red, green, blue, alpha);
        
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex3d(x, y + height, 0);
            GL11.glVertex3d(x + width, y + height, 0);
            GL11.glVertex3d(x + width, y, 0);
            GL11.glVertex3d(x, y, 0);
        }
        GL11.glEnd();
        
        GL11.glColor4f(1, 1, 1, 1);
        
        if (isTextureEnabled) GL11.glEnable(GL11.GL_TEXTURE_2D);
        
        GL11.glPopMatrix();
    }
    
    private static float zLevel = 0;
    
    public static final void drawHoveringText(List<String> text, int x, int y, FontRenderer font) {
    
        if (!text.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator<String> iterator = text.iterator();
            
            while (iterator.hasNext()) {
                String s = iterator.next();
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
            
            zLevel = 300.0F;
            int j1 = -267386864;
            drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
            drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
            drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
            int k1 = 1347420415;
            int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
            drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
            drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
            drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);
            
            for (int i2 = 0; i2 < text.size(); ++i2) {
                String s1 = text.get(i2);
                font.drawStringWithShadow(s1, j2, k2, -1);
                
                if (i2 == 0) {
                    k2 += 2;
                }
                
                k2 += 10;
            }
            
            zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
    
    public static final void drawGradientRect(int x1, int y1, int x2, int y2, int c1, int c2) {
    
        float f = (c1 >> 24 & 255) / 255.0F;
        float f1 = (c1 >> 16 & 255) / 255.0F;
        float f2 = (c1 >> 8 & 255) / 255.0F;
        float f3 = (c1 & 255) / 255.0F;
        float f4 = (c2 >> 24 & 255) / 255.0F;
        float f5 = (c2 >> 16 & 255) / 255.0F;
        float f6 = (c2 >> 8 & 255) / 255.0F;
        float f7 = (c2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x2, y1, zLevel);
        tessellator.addVertex(x1, y1, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x1, y2, zLevel);
        tessellator.addVertex(x2, y2, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
