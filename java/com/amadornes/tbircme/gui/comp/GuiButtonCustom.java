package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.gui.GuiButton;

public class GuiButtonCustom extends GuiButton {
    
    public GuiButtonCustom(int par1, int par2, int par3, String par4Str) {
    
        super(par1, par2, par3, par4Str);
    }
    
    public GuiButtonCustom(int par1, int par2, int par3, int par4, int par5, String par6Str) {
    
        super(par1, par2, par3, par4, par5, par6Str);
    }
    
    public void setWidth(int width) {
    
        this.width = width;
    }
    
}
