package com.amadornes.tbircme.gui.screen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.gui.IGui;
import com.amadornes.tbircme.gui.Screen;
import com.amadornes.tbircme.ref.Names;

public class ScreenClientConfig extends Screen {
    
    private boolean initialized = false;
    
    private GuiButton btnServers, btnChannels;
    
    int               buttonSeparation = 10;
    int               buttonHeight     = 20;
    int               buttonWidth      = 150;
    
    public ScreenClientConfig(IGui parent) {
    
        super(parent);
    }
    
    @Override
    public void onOpen() {
    
        buttonList.add(btnServers = new GuiButton(0, 0, 0, buttonWidth, buttonHeight, I18n.format(Names.GuiNames.SERVERS_TITLE)));
        buttonList.add(btnChannels = new GuiButton(1, 0, 0, buttonWidth, buttonHeight, I18n.format(Names.GuiNames.CHANNELS_TITLE)));
        
        initialized = true;
    }
    
    @Override
    public void renderComponents(int mx, int my, float frame) {
    
        drawCenteredString(getFontRenderer(), I18n.format(Names.GuiNames.CLIENT_TITLE), getWidth() / 2, 85, 16777215);
        
        if (!initialized) return;
        
        btnServers.xPosition = btnChannels.xPosition = (getWidth() - buttonWidth) / 2;
        btnServers.yPosition = ((getHeight() - buttonHeight - buttonSeparation) / 2);
        btnChannels.yPosition = ((getHeight() + buttonHeight + buttonSeparation) / 2);
        
        // Draw other buttons
        {
            if (btnServers != null) btnServers.drawButton(getMc(), mx, my);
            if (btnChannels != null) btnChannels.drawButton(getMc(), mx, my);
        }
    }
    
    @Override
    protected void actionPerformed(GuiButton btn) {
    
        if (btn == btnServers) {
            getMainGui().addScreen(new ScreenServerList(this));
            // Minecraft.getMinecraft().displayGuiScreen(new GuiServerList(this, mainMenu));
        } else if (btn == btnChannels) {
            getMainGui().addScreen(new ScreenChannelList(this));
        }
    }
    
    @Override
    public List<String> getTooltip() {
    
        return Arrays.asList(I18n.format(Names.GuiNames.CLIENT_TITLE));
    }
    
}
