package com.amadornes.tbircme.gui.comp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.gui.Screen;
import com.amadornes.tbircme.util.IChangeListener;

public class ScrollableList extends ScrollablePanel {
    
    private List<ScrollableListItem> items         = new ArrayList<ScrollableListItem>();
    private int                      selectedIndex = -1;
    private boolean                  canSelect     = false;
    
    public ScrollableList(Screen parent, int x, int y, int width, int height, boolean canSelect) {
    
        super(parent, x, y, width - 4, height - 4);
        this.canSelect = canSelect;
    }
    
    @Override
    public void addComponent(IComponent comp) {
    
    }
    
    public void addItem(ScrollableListItem item) {
    
        items.add(item);
    }
    
    public void removeItem(ScrollableListItem item) {
    
        if (selectedIndex == indexOf(item)) selectedIndex = -1;
        items.remove(item);
    }
    
    public void removeItem(int item) {
    
        if (selectedIndex == item) selectedIndex = -1;
        items.remove(item);
    }
    
    public int indexOf(ScrollableListItem item) {
    
        return items.indexOf(item);
    }
    
    public ScrollableListItem get(int item) {
    
        return items.get(item);
    }
    
    public int getAmount() {
    
        return items.size();
    }
    
    public List<ScrollableListItem> getItems() {
    
        return items;
    }
    
    public int getSelectedIndex() {
    
        return selectedIndex;
    }
    
    public ScrollableListItem getSelected() {
    
        try {
            return get(selectedIndex);
        } catch (Exception ex) {
        }
        return null;
    }
    
    public void setSelected(int item) {
    
        setSelected(item, false);
    }
    
    public void setSelected(ScrollableListItem item) {
    
        setSelected(item, false);
    }
    
    public void setSelected(int item, boolean notifyUpdate) {
    
        selectedIndex = item;
        if (notifyUpdate) if (parent instanceof IChangeListener) ((IChangeListener) parent).onChange(this);
    }
    
    public void setSelected(ScrollableListItem item, boolean notifyUpdate) {
    
        setSelected(indexOf(item), notifyUpdate);
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        super.onClick(x, y, button);
        
        if (canSelect) {
            if (x >= getX() && x < getX() + getInnerWidth()) {
                int y2 = (int) -scrolled;
                for (ScrollableListItem i : items) {
                    int height = i.getHeight();
                    if (y >= y2 + getY() && y < y2 + getY() + height) {
                        selectedIndex = indexOf(i);
                        if (parent instanceof IChangeListener) ((IChangeListener) parent).onChange(this);
                        break;
                    }
                    
                    y2 += height;
                }
            }
        }
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        super.render(x, y, frame);
        
        GL11.glPushMatrix();
        {
            GL11.glTranslated(getX(), getY(), 0);
            
            drawGradientRect(-2, -2, getWidth() + 2, 0, 0xA0000000, 0xB0000000);
            drawGradientRect(-2, getHeight(), getWidth() + 2, getHeight() + 2, 0xA0000000, 0xB0000000);
            drawGradientRect(-2, 0, 0, getHeight(), 0xA0000000, 0xB0000000);
            drawGradientRect(getWidth(), 0, getWidth() + 2, getHeight(), 0xA0000000, 0xB0000000);
            
            if (renderBar) drawGradientRect(getWidth() - getBarWidth() - barMarginRight - 1, barMarginTop, getWidth() - getBarWidth()
                    - barMarginRight, getHeight() - barMarginTop, 0xA0000000, 0xB0000000);
            
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            double scaleMultiplier = (Minecraft.getMinecraft().displayWidth / parent.getMainGui().width);
            GL11.glScissor((int) ((getX() + (parent.getMainGui().width - parent.getWidth())) * scaleMultiplier), (int) ((parent.getHeight()
                    - getHeight() - getY() - 1) * scaleMultiplier), (int) (getInnerWidth() * scaleMultiplier), (int) (getHeight() * scaleMultiplier));
            
            GL11.glTranslated(0, -((int) scrolled), 0);
            
            for (ScrollableListItem i : items)
                i.render(x, y, frame);
            
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public int getContentSize() {
    
        if (items.size() > 0) return items.get(items.size() - 1).getY() + items.get(items.size() - 1).getHeight();
        
        return 0;
    }
    
    @Override
    public int getInnerWidth() {
    
        return renderBar ? super.getInnerWidth() - 1 : super.getInnerWidth();
    }
    
}
