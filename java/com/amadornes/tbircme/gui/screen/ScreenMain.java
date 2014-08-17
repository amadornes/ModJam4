package com.amadornes.tbircme.gui.screen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.gui.IGui;
import com.amadornes.tbircme.gui.Screen;
import com.amadornes.tbircme.gui.comp.GuiButtonToggle;
import com.amadornes.tbircme.ref.ModInfo;
import com.amadornes.tbircme.ref.Names;

public class ScreenMain extends Screen {
    
    public ScreenMain(IGui parent) {
    
        super(parent);
    }
    
    private boolean         initialized      = false;
    
    private GuiButtonToggle btnEmotes, btnPause;
    private GuiButton       btnConfigClient, btnConfigServer;
    
    private int             buttonWidth      = 150;
    private int             buttonHeight     = 20;
    private int             buttonSeparation = 30;
    
    @Override
    public void onOpen() {
    
        // Emote button
        {
            int btnEmotesSize = 20;
            int btnEmotesDist = 10;
            buttonList.add(btnEmotes = new GuiButtonToggle(0, getWidth() - btnEmotesSize - btnEmotesDist, btnEmotesDist, btnEmotesSize,
                    btnEmotesSize, ""));
            btnEmotes.setState(true);// FIXME Config.emotesEnabled);
        }
        
        // Pause button
        {
            int btnPauseSize = 20;
            int btnPauseDist = 10;
            buttonList.add(btnPause = new GuiButtonToggle(1, btnPauseDist, btnPauseDist, btnPauseSize, btnPauseSize, ""));
            btnPause.setState(true);// FIXME Config.shouldConfigGuiPauseGame);
        }
        
        buttonList.add(btnConfigClient = new GuiButton(2, 0, 0, buttonWidth, buttonHeight, I18n.format(Names.GuiNames.CLIENT_TITLE)));
        buttonList.add(btnConfigServer = new GuiButton(3, 0, 0, buttonWidth, buttonHeight, I18n.format(Names.GuiNames.SERVER_TITLE)));
        
        initialized = true;
    }
    
    @Override
    public void renderComponents(int mx, int my, float frame) {
    
        GL11.glPushMatrix();
        GL11.glTranslated(getWidth() / 2, 40, 0);
        GL11.glScaled(3, 3, 1);
        drawCenteredString(getFontRenderer(), I18n.format(Names.MOD_NAME), 0, 0, 0x96F2F2);
        GL11.glPopMatrix();
        drawCenteredString(getFontRenderer(), I18n.format(Names.GuiNames.MAIN_TITLE), getWidth() / 2, 85, 16777215);
        
        if (!initialized) return;
        
        btnConfigClient.yPosition = btnConfigServer.yPosition = Math.max((getHeight() - buttonHeight) / 2, 50);
        btnConfigClient.xPosition = ((getWidth() - buttonSeparation) / 2) - buttonWidth;
        btnConfigServer.xPosition = (getWidth() + buttonSeparation) / 2;
        
        // Draw other buttons
        {
            if (btnConfigClient != null) btnConfigClient.drawButton(getMc(), mx, my);
            if (btnConfigServer != null) btnConfigServer.drawButton(getMc(), mx, my);
        }
        
        // Draw emote toggle button
        {
            btnEmotes.xPosition = getWidth() - 20 - 10;
            btnEmotes.drawButton(getMc(), mx, my);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID + ":emotes/kappa.png"));
            drawTexturedRect(btnEmotes.xPosition + 3, btnEmotes.yPosition + 3, 0, 0, 14, 14);
            if (!btnEmotes.getState()) {
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID + ":others/nope.png"));
                drawTexturedRect(btnEmotes.xPosition + 2, btnEmotes.yPosition + 2, 0, 0, btnEmotes.getButtonWidth() - 4,
                        btnEmotes.getButtonWidth() - 4);
            }
            
            GL11.glEnable(GL11.GL_LIGHTING);
            if (mx >= btnEmotes.xPosition && mx < btnEmotes.xPosition + btnEmotes.getButtonWidth() && my >= btnEmotes.yPosition
                    && my < btnEmotes.yPosition + btnEmotes.getButtonWidth()) {
                drawHoveringText(mx, my, getFontRenderer(),
                        btnEmotes.getState() ? I18n.format(Names.GuiNames.EMOTES_DISABLE) : I18n.format(Names.GuiNames.EMOTES_ENABLE));
            }
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        
        // Draw pause toggle button
        {
            btnPause.drawButton(getMc(), mx, my);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID + ":others/pause.png"));
            drawTexturedRect(btnPause.xPosition + 3, btnPause.yPosition + 3, 0, 0, 14, 14);
            if (!btnPause.getState()) {
                Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID + ":others/nope.png"));
                drawTexturedRect(btnPause.xPosition + 2, btnPause.yPosition + 2, 0, 0, btnPause.getButtonWidth() - 4, btnPause.getButtonWidth() - 4);
            }
            
            GL11.glEnable(GL11.GL_LIGHTING);
            if (mx >= btnPause.xPosition && mx < btnPause.xPosition + btnPause.getButtonWidth() && my >= btnPause.yPosition
                    && my < btnPause.yPosition + btnPause.getButtonWidth()) {
                drawHoveringText(mx, my, getFontRenderer(),
                        btnPause.getState() ? I18n.format(Names.GuiNames.PAUSE_DISABLE) : I18n.format(Names.GuiNames.PAUSE_ENABLE));
            }
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        
    }
    
    @Override
    public void actionPerformed(GuiButton btn) {
    
        if (btn == btnEmotes) {
            // btnEmotes.setState(Config.emotesEnabled = !btnEmotes.getState());
            // ConfigHandler.saveMainConfig();
        } else if (btn == btnPause) {
            // btnPause.setState(Config.shouldConfigGuiPauseGame = !btnPause.getState());
            // ConfigHandler.saveMainConfig();
            // Minecraft.getMinecraft().displayGuiScreen(new GuiConfig(mainMenu));
        } else if (btn == btnConfigClient) {
            getMainGui().addScreen(new ScreenClientConfig(this));
            // Minecraft.getMinecraft().displayGuiScreen(new GuiClientConfig(this));
        } else if (btn == btnConfigServer) {
            
        }
    }
    
    @Override
    public List<String> getTooltip() {
    
        return Arrays.asList(I18n.format(Names.MOD_NAME));
    }
    
}
