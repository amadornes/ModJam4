package com.amadornes.tbircme.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.amadornes.tbircme.util.IChangeListener;

public class Screen implements IGui, IChangeListener {
    
    private IGui              parent;
    
    private double            miniWidth  = 0;
    private double            miniHeight = 0;
    
    protected List<GuiButton> buttonList = new ArrayList<GuiButton>();
    
    public Screen(IGui parent) {
    
        this.parent = parent;
    }
    
    public final GuiTBIRCME getMainGui() {
    
        IGui gui = this;
        
        while (gui.getParent() != null)
            gui = gui.getParent();
        
        return (GuiTBIRCME) gui;
    }
    
    @Override
    public IGui getParent() {
    
        return parent;
    }
    
    public void onClick(int x, int y, int button) {
    
        List<GuiButton> buttons = new ArrayList<GuiButton>();
        buttons.addAll(buttonList);
        for (GuiButton b : getButtons())
            buttons.add(b);
        
        if (button == 0) {
            for (int l = 0; l < buttons.size(); ++l) {
                GuiButton guibutton = buttons.get(l);
                
                if (guibutton.mousePressed(getMc(), x, y)) {
                    ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(getMainGui(), guibutton, buttons);
                    if (MinecraftForge.EVENT_BUS.post(event)) break;
                    event.button.func_146113_a(getMc().getSoundHandler());
                    actionPerformed(event.button);
                    if (equals(getMc().currentScreen)) MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(getMainGui(), event.button,
                            buttons));
                }
            }
        }
    }
    
    public void onMouseDrag(int x, int y, int button) {
    
    }
    
    public void onMouseUp(int x, int y, int button) {
    
    }
    
    protected void actionPerformed(GuiButton button) {
    
    }
    
    public void onKeyPress(int key, char c) {
    
    }
    
    public List<String> getTooltip() {
    
        return null;
    }
    
    public void renderBackground(int mx, int my, float partialTick) {
    
    }
    
    public void renderComponents(int mx, int my, float partialTick) {
    
    }
    
    public void renderForeground(int mx, int my, float partialTick) {
    
    }
    
    public void onOpen() {
    
    }
    
    public void onClose() {
    
    }
    
    public final int getWidth() {
    
        return getMainGui().getCurrentScreen() == this ? getMainGui().getScreenWidth() : getMainGui().width;
    }
    
    public final int getHeight() {
    
        return getMainGui().getCurrentScreen() == this ? getMainGui().getScreenHeight() : getMainGui().height;
    }
    
    public final void setMiniWidth(double miniWidth) {
    
        this.miniWidth = miniWidth;
    }
    
    public final void setMiniHeight(double miniHeight) {
    
        this.miniHeight = miniHeight;
    }
    
    public final double getMiniWidth() {
    
        return miniWidth;
    }
    
    public final double getMiniHeight() {
    
        return miniHeight;
    }
    
    public FontRenderer getFontRenderer() {
    
        return getMc().fontRenderer;
    }
    
    public Minecraft getMc() {
    
        return Minecraft.getMinecraft();
    }
    
    @Override
    public void onChange(Object component) {
    
    }
    
    public GuiButton[] getButtons() {
    
        return new GuiButton[] {};
    }
    
    protected void drawGradientRect(double x1, double y1, double x2, double y2, int colorFrom, int colorTo) {
    
        float f = (colorFrom >> 24 & 255) / 255.0F;
        float f1 = (colorFrom >> 16 & 255) / 255.0F;
        float f2 = (colorFrom >> 8 & 255) / 255.0F;
        float f3 = (colorFrom & 255) / 255.0F;
        float f4 = (colorTo >> 24 & 255) / 255.0F;
        float f5 = (colorTo >> 16 & 255) / 255.0F;
        float f6 = (colorTo >> 8 & 255) / 255.0F;
        float f7 = (colorTo & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x2, y1, 0);
        tessellator.addVertex(x1, y1, 0);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x1, y2, 0);
        tessellator.addVertex(x2, y2, 0);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    protected void drawColoredRect(double x1, double y1, double x2, double y2, int color) {
    
        drawGradientRect(x1, y1, x2, y2, color, color);
    }
    
    protected void drawHoveringText(double x, double y, FontRenderer fontRenderer, String... text) {
    
        List<String> text2 = new ArrayList<String>();
        
        for (String s : text)
            text2.add(s);
        
        if (!text2.isEmpty()) {
            GL11.glTranslated(0, 0, 300);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;
            Iterator<String> iterator = text2.iterator();
            
            while (iterator.hasNext()) {
                String s = iterator.next();
                int l = fontRenderer.getStringWidth(s);
                
                if (l > k) {
                    k = l;
                }
            }
            
            double j2 = x + 12;
            double k2 = y - 12;
            int i1 = 8;
            
            if (text2.size() > 1) {
                i1 += 2 + (text2.size() - 1) * 10;
            }
            
            if (j2 + k > getWidth()) {
                j2 -= 28 + k;
            }
            
            if (k2 + i1 + 6 > getHeight()) {
                k2 = getHeight() - i1 - 6;
            }
            
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
            
            for (int i2 = 0; i2 < text2.size(); ++i2) {
                String s1 = text2.get(i2);
                GL11.glPushMatrix();
                {
                    GL11.glTranslated(j2, k2, 0);
                    fontRenderer.drawStringWithShadow(s1, 0, 0, -1);
                }
                GL11.glPopMatrix();
                
                if (i2 == 0) {
                    k2 += 2;
                }
                
                k2 += 10;
            }
            
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslated(0, 0, -300);
        }
    }
    
    protected void drawHorizontalLine(double x, double y, double length, int color) {
    
        length += x;
        
        if (y < x) {
            double i1 = x;
            x = y;
            y = i1;
        }
        
        drawColoredRect(x, length, y + 1, length + 1, color);
    }
    
    protected void drawVerticalLine(double x, double y, double length, int color) {
    
        length += y;
        
        if (length < y) {
            double i1 = y;
            y = length;
            length = i1;
        }
        
        drawColoredRect(x, y + 1, x + 1, length, color);
    }
    
    protected void drawCenteredString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
    
        par1FontRenderer.drawStringWithShadow(par2Str, par3 - par1FontRenderer.getStringWidth(par2Str) / 2, par4, par5);
    }
    
    protected void drawString(FontRenderer par1FontRenderer, String par2Str, int par3, int par4, int par5) {
    
        par1FontRenderer.drawStringWithShadow(par2Str, par3, par4, par5);
    }
    
    protected void drawTexturedRect(double x1, double y1, double x2, double y2, double u, double v) {
    
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x1 + 0, y1 + v, 0, (float) (x2 + 0) * f, (float) (y2 + v) * f1);
        tessellator.addVertexWithUV(x1 + u, y1 + v, 0, (float) (x2 + u) * f, (float) (y2 + v) * f1);
        tessellator.addVertexWithUV(x1 + u, y1 + 0, 0, (float) (x2 + u) * f, (float) (y2 + 0) * f1);
        tessellator.addVertexWithUV(x1 + 0, y1 + 0, 0, (float) (x2 + 0) * f, (float) (y2 + 0) * f1);
        tessellator.draw();
    }
    
    protected void drawTexturedRectFromIcon(double x1, double y1, IIcon icon, double width, double height) {
    
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x1 + 0, y1 + height, 0, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x1 + width, y1 + height, 0, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x1 + width, y1 + 0, 0, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x1 + 0, y1 + 0, 0, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }
}
