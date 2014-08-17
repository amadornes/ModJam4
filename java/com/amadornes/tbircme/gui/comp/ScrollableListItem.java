package com.amadornes.tbircme.gui.comp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

public class ScrollableListItem extends Gui implements IComponent {
    
    protected ScrollableList parent;
    
    protected int            marginHor  = 5;
    protected int            marginVert = 7;
    
    protected List<String>   content    = new ArrayList<String>();
    
    public ScrollableListItem(ScrollableList parent, String... content) {
    
        this.parent = parent;
        this.content.addAll(Arrays.asList(content));
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void render(int x, int y, float frame) {
    
        GL11.glPushMatrix();
        {
            GL11.glTranslated(getX(), getY(), 0);
            
            if (parent.getSelected() == this) {
                drawGradientRect(2, 2, getWidth() - 2, getHeight() - 2, 0x7FCCCCCC, 0x8F666666);
                
                drawGradientRect(1, 1, getWidth() - 1, 2, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(1, getHeight() - 2, getWidth() - 1, getHeight() - 1, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(1, 2, 2, getHeight() - 2, 0xFFDDDDDD, 0xFFDDDDDD);
                drawGradientRect(getWidth() - 2, 2, getWidth() - 1, getHeight() - 2, 0xFFDDDDDD, 0xFFDDDDDD);
            }
            
            GL11.glTranslated(marginHor, marginVert, 0);
            
            // Draw text
            FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
            int y2 = 0;
            for (String s : content) {
                List l = fr.listFormattedStringToWidth(s, getWidth() - marginHor - marginHor);
                for (Object o : l) {
                    fr.drawStringWithShadow((String) o, 0, y2, 0xFFFFFF);
                    y2 += fr.FONT_HEIGHT;
                }
            }
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
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
    public int getX() {
    
        return 0;
    }
    
    @Override
    public int getY() {
    
        int y = 0;
        for (ScrollableListItem i : parent.getItems()) {
            if (i == this) break;
            y += i.getHeight();
        }
        return y;
    }
    
    @Override
    public int getWidth() {
    
        return parent.getInnerWidth();
    }
    
    @Override
    public int getHeight() {
    
        FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
        int lines = 0;
        
        for (String s : content)
            lines += fr.listFormattedStringToWidth(s, getWidth() - marginHor - marginHor).size();
        
        return marginVert + (fr.FONT_HEIGHT * lines) + marginVert;
    }
}
