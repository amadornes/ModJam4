package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import com.amadornes.lib.render.RenderHelper;
import com.amadornes.tbircme.util.IChangeListener;

public class GuiCheckbox extends Gui implements IComponent {
    
    private IChangeListener parent;
    
    private int             x, y, size;
    private boolean         state     = false;
    private boolean         isEnabled = true;
    private String          label;
    
    private int             onColor   = -1774280;
    
    public GuiCheckbox(IChangeListener parent, int x, int y, int size) {
    
        this(parent, x, y, size, "");
    }
    
    public GuiCheckbox(IChangeListener parent, int x, int y, int size, String label) {
    
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.size = size;
        this.label = label;
    }
    
    public void drawCheckbox() {
    
        drawRect(x - 1, y - 1, x + size + 1, y + size + 1, isEnabled ? -6250336 : -16777216);
        drawRect(x, y, x + size, y + size, isEnabled ? -16777216 : 0);
        
        if (state) drawRect(x + 1, y + 1, x + size - 1, y + size - 1, isEnabled ? onColor : -0x858585);
        
        drawString(Minecraft.getMinecraft().fontRenderer, label, x + size + 3, y + ((int) Math.ceil(((size / 2) - (7D / 2D)))), isEnabled ? 16777215
                : -6250336);
    }
    
    public void setOnColor(int onColor) {
    
        this.onColor = -(-onColor);
    }
    
    public void mouseClick(int x, int y, int button) {
    
        if (x >= this.x && x < this.x + size && y >= this.y && y < this.y + size && isEnabled()) {
            state = !state;
            parent.onChange(this);
        }
    }
    
    public void setLabel(String label) {
    
        this.label = label;
    }
    
    public String getLabel() {
    
        return label;
    }
    
    public void setState(boolean state) {
    
        this.state = state;
    }
    
    public boolean getState() {
    
        return state;
    }
    
    public int getSize() {
    
        return size;
    }
    
    @Override
    public int getX() {
    
        return x;
    }
    
    @Override
    public int getY() {
    
        return y;
    }
    
    public void setSize(int size) {
    
        this.size = size;
    }
    
    public void setX(int x) {
    
        this.x = x;
    }
    
    public void setY(int y) {
    
        this.y = y;
    }
    
    public boolean isEnabled() {
    
        return isEnabled;
    }
    
    public void setEnabled(boolean isEnabled) {
    
        this.isEnabled = isEnabled;
    }
    
    private String tooltip = "";
    
    public void setTooltip(String tooltip) {
    
        this.tooltip = tooltip;
        if (this.tooltip == null) this.tooltip = "";
    }
    
    public String getTooltip() {
    
        return tooltip;
    }
    
    @SuppressWarnings("unchecked")
    public void renderTooltip(int mx, int my) {
    
        if (tooltip != null && tooltip.trim().length() > 0) if (mx >= x && mx < x + size && my >= y && my < y + size) RenderHelper.drawHoveringText(
                Minecraft.getMinecraft().fontRenderer.listFormattedStringToWidth(tooltip, 150), mx, my, Minecraft.getMinecraft().fontRenderer);
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        drawCheckbox();
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        mouseClick(x, y, button);
    }
    
    @Override
    public void onMouseDrag(int x, int y, int button) {
    
    }
    
    @Override
    public void onMouseUp(int x, int y, int button) {
    
    }
    
    @Override
    public void onKeyType(char c, int key) {
    
    }
    
    @Override
    public int getWidth() {
    
        return size + (label.trim().length() > 0 ? (3 + Minecraft.getMinecraft().fontRenderer.getStringWidth(label)) : 0);
    }
    
    @Override
    public int getHeight() {
    
        return size;
    }
    
}
