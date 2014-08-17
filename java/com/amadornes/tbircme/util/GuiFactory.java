package com.amadornes.tbircme.util;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.amadornes.tbircme.gui.GuiTBIRCME;

import cpw.mods.fml.client.IModGuiFactory;

public class GuiFactory implements IModGuiFactory {
    
    @Override
    public void initialize(Minecraft minecraftInstance) {
    
    }
    
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
    
        return GuiTBIRCME.class;
    }
    
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
    
        return null;
    }
    
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
    
        return null;
    }
    
}
