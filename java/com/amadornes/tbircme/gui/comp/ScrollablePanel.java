package com.amadornes.tbircme.gui.comp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.gui.Screen;

public class ScrollablePanel extends Gui implements IComponent {
    
    protected List<IComponent> components        = new ArrayList<IComponent>();
    
    protected Screen           parent;
    
    private int                marginBottom      = 0;
    
    private boolean            scrolling         = false;
    protected double           scrolled          = 0;
    private double             barY              = 0;
    private double             barSize           = 0;
    protected int              barMarginTop      = 0;
    protected int              barMarginBottom   = 0;
    protected int              barMarginRight    = 0;
    private int                barWidth          = 6;
    protected boolean          renderBar         = false;
    
    private int                x                 = 0;
    private int                y                 = 0;
    private int                width             = 0;
    private int                height            = 0;
    
    private int                contentSize       = 0;
    
    private boolean            scrollingInverted = false;
    
    private int                lastMouseY;
    
    private boolean            drawBackground    = true;
    
    public ScrollablePanel(Screen parent, int x, int y, int width, int height) {
    
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void setX(int x) {
    
        this.x = x;
    }
    
    public void setY(int y) {
    
        this.y = y;
    }
    
    public void setWidth(int width) {
    
        this.width = width;
    }
    
    public void setHeight(int height) {
    
        this.height = height;
    }
    
    @Override
    public int getX() {
    
        return x;
    }
    
    @Override
    public int getY() {
    
        return y;
    }
    
    @Override
    public int getWidth() {
    
        return width;
    }
    
    @Override
    public int getHeight() {
    
        return height;
    }
    
    public int getInnerWidth() {
    
        return getWidth() - (renderBar ? getBarWidth() : 0);
    }
    
    public void setMarginBottom(int marginBottom) {
    
        this.marginBottom = marginBottom;
    }
    
    public int getMarginBottom() {
    
        return marginBottom;
    }
    
    public void addComponent(IComponent comp) {
    
        components.add(comp);
    }
    
    public void drawBackground() {
    
        drawGradientRect(0, 0, width, height, 0x7010100F, 0x8010100F);
    }
    
    public boolean shouldDrawBackground() {
    
        return drawBackground;
    }
    
    public void setDrawBackground(boolean drawBackground) {
    
        this.drawBackground = drawBackground;
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, this.y, 0);
        
        if (shouldDrawBackground()) drawBackground();
        
        // Size calculations
        {
            for (IComponent c : components)
                contentSize = Math.max(getContentSize(), c.getY() + c.getHeight() + marginBottom);
            
            if (getContentSize() > getHeight()) {
                barSize = ((height / (double) getContentSize())) * (height - 1 + barMarginTop + barMarginBottom);
                renderBar = true;
                if (barY > height - 1 - barSize) {
                    barY = height - 1 - barSize;
                }
            } else {
                scrolled = 0;
                renderBar = false;
                barSize = 0;
            }
        }
        
        // Rendering itself
        GL11.glPushMatrix();
        {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            double scaleMultiplier = (Minecraft.getMinecraft().displayWidth / parent.getMainGui().width);
            GL11.glScissor((int) ((getX() + (parent.getMainGui().width - parent.getWidth())) * scaleMultiplier), (int) ((parent.getHeight()
                    - getHeight() - getY() - 1) * scaleMultiplier), (int) (getWidth() * scaleMultiplier), (int) (getHeight() * scaleMultiplier));
            GL11.glTranslated(0, -scrolled, 0);
            for (IComponent c : components)
                c.render(x - this.x, (int) ((y - this.y) + scrolled), frame);
            
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        GL11.glPopMatrix();
        
        if (renderBar) {
            drawGradientRect(width - getBarWidth() - barMarginRight, barMarginTop, width - barMarginRight, height - barMarginTop, 0xA0000000,
                    0xB0000000);
            drawGradientRect(width - getBarWidth() + 1 - barMarginRight, (int) barY + 1 + barMarginTop, width - 1 - barMarginRight, (int) (barY
                    + barSize + barMarginTop), 0xAFFFFFFF, 0xBFFFFFFF);
        }
        
        GL11.glPopMatrix();
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        if (!(x >= this.x && x < this.x + width && y >= this.y && y < this.y + height)) return;
        
        for (IComponent c : components)
            c.onClick(x - this.x, (int) ((y - this.y) + scrolled), button);
        
        if (x >= this.x + width - getBarWidth() + 1 - barMarginRight && x < this.x + width - 1 - barMarginRight && y >= this.y + barY + barMarginTop
                && y < this.y + barY + barSize + barMarginBottom) {
            scrolling = true;
            scrollingInverted = false;
        } else if (x < this.x + width - getBarWidth() - barMarginRight) {
            scrolling = true;
            scrollingInverted = true;
        }
        
        lastMouseY = y;
    }
    
    @Override
    public void onMouseDrag(int x, int y, int button) {
    
        if (!scrolling) return;
        
        double diff = y - lastMouseY;
        if (scrollingInverted) {
            diff = -diff;
            scrolled = Math.max(0, Math.min(getContentSize(), scrolled + diff));
            barY = (scrolled / getContentSize()) * (height - 1 - barSize);
        } else {
            barY = Math.max(0, Math.min(height - 1 - barSize, barY + diff));
            scrolled = (barY / height) * getContentSize();
        }
        
        lastMouseY = y;
    }
    
    @Override
    public void onMouseUp(int x, int y, int button) {
    
        scrolling = false;
    }
    
    @Override
    public void onKeyType(char c, int key) {
    
        for (IComponent comp : components)
            comp.onKeyType(c, key);
    }
    
    public int getContentSize() {
    
        return contentSize;
    }
    
    public int getBarWidth() {
    
        return barWidth;
    }
    
}
