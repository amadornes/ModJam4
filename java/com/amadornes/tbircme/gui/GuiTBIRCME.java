package com.amadornes.tbircme.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.gui.screen.ScreenClientConfig;
import com.amadornes.tbircme.gui.screen.ScreenMain;

public class GuiTBIRCME extends GuiScreen implements IGui {
    
    private int          marginLeftMax  = 75;
    private int          marginLeftBig  = 115;
    private double       marginLeft     = 0;
    
    private double       miniMarginVert = 10;
    private double       miniMarginHor  = 7;
    private double       miniSepVert    = 8;
    
    private List<Screen> screens        = new ArrayList<Screen>();
    
    private boolean      isIngame       = false;
    
    public GuiTBIRCME() {
    
        isIngame = true;
    }
    
    public GuiTBIRCME(GuiScreen parent) {
    
        isIngame = false;
    }
    
    public void addScreen(Screen screen) {
    
        if (screen == null) return;
        if (screens.contains(screen)) return;
        
        screens.add(screen);
        screen.onOpen();
    }
    
    public void removeScreen(Screen screen) {
    
        if (screen == null) return;
        if (!screens.contains(screen)) return;
        
        List<Screen> removed = new ArrayList<Screen>();
        
        for (int i = screens.indexOf(screen); i < screens.size(); i++)
            removed.add(screens.get(i));
        for (Screen s : removed) {
            screens.remove(s);
            s.onClose();
        }
    }
    
    public void select(Screen screen) {
    
        if (screen == null) {
            screens.clear();
            return;
        }
        if (!screens.contains(screen)) return;
        
        int index = screens.indexOf(screen);
        removeScreen(screens.get(index + 1));
    }
    
    public Screen getCurrentScreen() {
    
        if (screens.isEmpty()) return null;
        return screens.get(screens.size() - 1);
    }
    
    @Override
    public IGui getParent() {
    
        return null;
    }
    
    @Override
    protected void mouseClicked(int x, int y, int button) {
    
        super.mouseClicked(x, y, button);
        
        if (!screens.isEmpty()) {
            if (x >= marginLeft) {
                screens.get(screens.size() - 1).onClick(x - (int) marginLeft, y, button);
            } else {
                for (int i = 0; i < screens.size() - 1; i++) {
                    Screen sc = screens.get(i);
                    double x2 = (int) miniMarginHor;
                    double y2 = getY(sc);
                    
                    if (x >= x2 && x < x2 + sc.getMiniWidth() && y >= y2 && y < y2 + sc.getMiniHeight()) {
                        select(sc);
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    protected void mouseClickMove(int x, int y, int button, long duration) {
    
        super.mouseClickMove(x, y, button, duration);
        
        if (!screens.isEmpty()) {
            if (x >= marginLeft) {
                screens.get(screens.size() - 1).onMouseDrag(x - (int) marginLeft, y, button);
            }
        }
        
    }
    
    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
    
        super.mouseMovedOrUp(x, y, button);
        
        if (!screens.isEmpty() && button >= 0) {
            if (x >= marginLeft) {
                screens.get(screens.size() - 1).onMouseUp(x - (int) marginLeft, y, button);
            }
        }
    }
    
    @Override
    protected void keyTyped(char par1, int par2) {
    
        super.keyTyped(par1, par2);
        
        if (getCurrentScreen() != null) {
            getCurrentScreen().onKeyPress(par2, par1);
        }
    }
    
    @Override
    public void drawScreen(int mx, int my, float frame) {
    
        updateMarginSize(mx, my);
        
        drawGradientRect(0, 0, width, height, 0xC010100F, 0xD010100F);
        
        drawGradientRect(((int) marginLeft) - 2, 0, (int) marginLeft, height, 0xA0000000, 0xB0000000);
        drawGradientRect(0, 0, ((int) marginLeft) - 2, height, 0xA010100F, 0xB010100F);
        
        if (!screens.isEmpty()) {
            Screen s = screens.get(screens.size() - 1);
            List<String> tip = null;
            
            GL11.glPushMatrix();
            {
                GL11.glTranslated(marginLeft, 0, 0);
                s.renderBackground(mx - (int) marginLeft, my, frame);
                s.renderComponents(mx - (int) marginLeft, my, frame);
                s.renderForeground(mx - (int) marginLeft, my, frame);
            }
            GL11.glPopMatrix();
            
            for (int i = 0; i < screens.size() - 1; i++) {
                Screen sc = screens.get(i);
                double x = (int) miniMarginHor;
                double y = getY(sc);
                
                if (mx >= miniMarginHor && mx < miniMarginHor + sc.getMiniWidth() && my >= y && my < y + sc.getMiniHeight()) tip = sc.getTooltip();
                
                double marginSize = 1;
                int borderFrom = 0xA0FFFFFF;
                int borderTo = 0xA0FFFFFF;
                
                double t = (((marginLeft - (2 * miniMarginHor)) - sc.getMiniWidth()) / 2);
                
                drawGradientRect(t + x - marginSize, y, t + x, sc.getMiniHeight() + y, borderFrom, borderTo);
                drawGradientRect(t + sc.getMiniWidth() + x, y, t + sc.getMiniWidth() + x + marginSize, sc.getMiniHeight() + y, borderFrom, borderTo);
                drawGradientRect(t + x - marginSize, y - marginSize, t + x + sc.getMiniWidth() + marginSize, y, borderFrom, borderTo);
                drawGradientRect(t + x - marginSize, y + sc.getMiniHeight(), t + x + sc.getMiniWidth() + marginSize, y + sc.getMiniHeight()
                        + marginSize, borderFrom, borderTo);
                
                GL11.glPushMatrix();
                {
                    GL11.glTranslated(x + t, y, 0);
                    GL11.glScaled((sc.getMiniWidth() / width), (sc.getMiniHeight() / height), 1);
                    drawGradientRect(0, 0, width, height, 0xA0000000, 0xB0000000);
                    
                    sc.renderBackground(0, 0, frame);
                    sc.renderComponents(0, 0, frame);
                    sc.renderForeground(0, 0, frame);
                }
                GL11.glPopMatrix();
            }
            
            if (tip != null) {
                GL11.glPushMatrix();
                {
                    GL11.glTranslated(mx, my, 500);
                    GL11.glScaled(0.75, 0.75, 1);
                    drawHoveringText(tip, 0, 0, mc.fontRenderer);
                }
                GL11.glPopMatrix();
            }
        }
    }
    
    @Override
    public void initGui() {
    
        Keyboard.enableRepeatEvents(true);
        
        if (screens.isEmpty()) {
            if (isIngame) addScreen(new ScreenMain(this));
            else addScreen(new ScreenClientConfig(this));
            
            for (Screen s : screens)
                s.onOpen();
        }
    }
    
    @Override
    public void onGuiClosed() {
    
        Keyboard.enableRepeatEvents(false);
        
        for (Screen s : screens)
            s.onOpen();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
    
        return true;
    }
    
    // Custom methods
    
    public boolean shouldDisplayLeftBar() {
    
        return screens.size() > 1;
    }
    
    public void updateMarginSize(int mx, int my) {
    
        if (!screens.isEmpty()) {
            Screen s = screens.get(screens.size() - 1);
            s.setMiniWidth(0);
            s.setMiniHeight(0);
        }
        
        if (!shouldDisplayLeftBar()) {
            for (int i = 0; i < screens.size() - 1; i++) {
                Screen s = screens.get(i);
                if (s.getMiniWidth() > 0) {
                    if (s.getMiniWidth() > 1) {
                        s.setMiniWidth(s.getMiniWidth() - 1);
                    } else {
                        s.setMiniWidth(0);
                    }
                }
            }
            if (marginLeft > 0) {
                if (marginLeft > 1) {
                    marginLeft--;
                } else {
                    marginLeft = 0;
                }
            }
        } else {
            for (int i = 0; i < screens.size() - 1; i++) {
                Screen s = screens.get(i);
                double y = getY(s);
                
                boolean isHovering = false;
                if (mx >= miniMarginHor && mx < miniMarginHor + s.getMiniWidth() && my >= y && my < y + s.getMiniHeight()) isHovering = true;
                
                if (isHovering && s.getMiniWidth() >= (marginLeftMax - (miniMarginHor * 2))) {
                    if (s.getMiniWidth() < (marginLeftBig - (miniMarginHor * 2))) s.setMiniWidth(s.getMiniWidth() + 0.75);
                    if (s.getMiniWidth() > (marginLeftBig - (miniMarginHor * 2))) s.setMiniWidth(s.getMiniWidth() - 1);
                } else {
                    if (s.getMiniWidth() < (marginLeftMax - (miniMarginHor * 2))) s.setMiniWidth(s.getMiniWidth() + 0.5);
                    if (s.getMiniWidth() > (marginLeftMax - (miniMarginHor * 2))) s.setMiniWidth(s.getMiniWidth() - 0.75);
                }
                
                s.setMiniHeight((((double) (height)) / ((double) (width))) * s.getMiniWidth());
            }
            
            marginLeft = 0;
            for (int i = 0; i < screens.size() - 1; i++)
                marginLeft = Math.max(marginLeft, screens.get(i).getMiniWidth() + (miniMarginHor * 2));
        }
    }
    
    private double getY(Screen s) {
    
        double y = miniMarginVert;
        
        for (int i = 0; i < screens.size() - 1; i++) {
            Screen sc = screens.get(i);
            if (sc == s) break;
            
            y += sc.getMiniHeight() + miniSepVert;
        }
        
        return y;
    }
    
    public int getScreenHeight() {
    
        return height;
    }
    
    public int getScreenWidth() {
    
        int w = width;
        
        if (shouldDisplayLeftBar()) w -= marginLeft;
        
        return w;
    }
    
    protected void drawGradientRect(double par1, double par2, double par3, double par4, int par5, int par6) {
    
        float f = (par5 >> 24 & 255) / 255.0F;
        float f1 = (par5 >> 16 & 255) / 255.0F;
        float f2 = (par5 >> 8 & 255) / 255.0F;
        float f3 = (par5 & 255) / 255.0F;
        float f4 = (par6 >> 24 & 255) / 255.0F;
        float f5 = (par6 >> 16 & 255) / 255.0F;
        float f6 = (par6 >> 8 & 255) / 255.0F;
        float f7 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(par3, par2, zLevel);
        tessellator.addVertex(par1, par2, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(par1, par4, zLevel);
        tessellator.addVertex(par3, par4, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
}
