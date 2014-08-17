package com.amadornes.tbircme.gui.screen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.gui.IGui;
import com.amadornes.tbircme.gui.Screen;
import com.amadornes.tbircme.gui.comp.GuiCheckbox;
import com.amadornes.tbircme.gui.comp.GuiTextFieldCustomizable;
import com.amadornes.tbircme.gui.comp.ScrollableList;
import com.amadornes.tbircme.gui.comp.ScrollableListItemServer;
import com.amadornes.tbircme.gui.comp.ScrollablePanel;
import com.amadornes.tbircme.ref.Names;

public class ScreenServerList extends Screen {
    
    private ScrollableList           serverList;
    private GuiButton                buttonAdd;
    private GuiButton                buttonRemove;
    
    private GuiButton                buttonChannels;
    private GuiButton                buttonToggleConnection;
    
    private ScrollablePanel          container;
    private GuiTextFieldCustomizable fieldName;
    private GuiTextFieldCustomizable fieldHost;
    private GuiTextFieldCustomizable fieldUsername;
    private GuiTextFieldCustomizable fieldServerPass;
    private GuiTextFieldCustomizable fieldNickservPass;
    private GuiCheckbox              fieldCheckShowGameJoins;
    private GuiCheckbox              fieldCheckShowGameParts;
    private GuiCheckbox              fieldCheckShowIRCJoins;
    private GuiCheckbox              fieldCheckShowIRCParts;
    private GuiCheckbox              fieldCheckShowDeaths;
    private GuiCheckbox              fieldCheckShowAchievements;
    
    private boolean                  initialized        = false;
    private boolean                  isEditing          = false;
    private boolean                  isEditingNewServer = false;
    
    public ScreenServerList(IGui parent) {
    
        super(parent);
    }
    
    @Override
    public void onOpen() {
    
        serverList = new ScrollableList(this, 10, 35, 100, getHeight() - 40 - 10 - 20 - 20 - 5, true);
        for (IIRCConnection c : AConnectionManager.inst().getConnections())
            serverList.addItem(new ScrollableListItemServer(serverList, c));
        
        buttonAdd = new GuiButton(0, 10, serverList.getY() + serverList.getHeight() + 5, serverList.getWidth(), 20,
                I18n.format(Names.GuiNames.Buttons.ADD));
        buttonRemove = new GuiButton(1, 10, serverList.getY() + serverList.getHeight() + 5 + 20 + 5, serverList.getWidth(), 20,
                I18n.format(Names.GuiNames.Buttons.REMOVE));
        
        buttonList.add(buttonAdd);
        buttonList.add(buttonRemove);
        
        // Buttons
        {
            int btnSize = 20;
            int btnDist = 10;
            int btnSep = 5;
            buttonList.add(buttonChannels = new GuiButton(2, getWidth() - btnSize - btnDist, btnDist, btnSize, btnSize, "CH"));
            buttonList.add(buttonToggleConnection = new GuiButton(2, getWidth() - btnSize - btnDist - btnSize - btnSep, btnDist, btnSize, btnSize,
                    "C"));
        }
        
        // Options
        container = new ScrollablePanel(this, serverList.getX() + serverList.getWidth() + 10, serverList.getY(), getWidth() - serverList.getX()
                - serverList.getWidth() - 10 - 20, getHeight() - serverList.getY() - 10);
        container.setMarginBottom(5);
        container.setDrawBackground(false);
        
        int y = 5;
        fieldName = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, 5, 100, 20, 1000);
        fieldName.setPlaceholder("Server name");
        container.addComponent(fieldName);
        
        fieldHost = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, y += 20 + 5, 100, 20, 1000);
        fieldHost.setPlaceholder("Host");
        container.addComponent(fieldHost);
        
        fieldUsername = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, y += 20 + 5, 100, 20, 1000);
        fieldUsername.setPlaceholder("Username");
        container.addComponent(fieldUsername);
        
        fieldServerPass = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, y += 20 + 5, 100, 20, 1000);
        fieldServerPass.setPlaceholder("Server password");
        fieldServerPass.setPassword(true);
        container.addComponent(fieldServerPass);
        
        fieldNickservPass = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, y += 20 + 5, 100, 20, 1000);
        fieldNickservPass.setPlaceholder("Nickserv password");
        fieldNickservPass.setPassword(true);
        container.addComponent(fieldNickservPass);
        
        fieldCheckShowGameJoins = new GuiCheckbox(this, 5, y += 20 + 5, 10, "Show game joins");
        fieldCheckShowGameJoins.setState(true);
        container.addComponent(fieldCheckShowGameJoins);
        
        fieldCheckShowGameParts = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Show game parts");
        fieldCheckShowGameParts.setState(true);
        container.addComponent(fieldCheckShowGameParts);
        
        fieldCheckShowIRCJoins = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Show IRC joins");
        fieldCheckShowIRCJoins.setState(true);
        container.addComponent(fieldCheckShowIRCJoins);
        
        fieldCheckShowIRCParts = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Show IRC parts");
        fieldCheckShowIRCParts.setState(true);
        container.addComponent(fieldCheckShowIRCParts);
        
        fieldCheckShowDeaths = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Show deaths");
        fieldCheckShowDeaths.setState(true);
        container.addComponent(fieldCheckShowDeaths);
        
        fieldCheckShowAchievements = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Show achievements [WIP]");
        fieldCheckShowAchievements.setState(true);
        container.addComponent(fieldCheckShowAchievements);
        
        // Mark as initialized
        initialized = true;
    }
    
    @Override
    public void renderComponents(int mx, int my, float partialTick) {
    
        drawCenteredString(getFontRenderer(), I18n.format(Names.GuiNames.SERVERS_TITLE), getWidth() / 2, 15, 16777215);
        
        if (!initialized) return;
        
        // Update componenents
        serverList.setHeight(getHeight() - 40 - 10 - 20 - 20 - 5);
        
        buttonAdd.yPosition = serverList.getY() + serverList.getHeight() + 5;
        buttonRemove.yPosition = serverList.getY() + serverList.getHeight() + 5 + 20 + 5;
        if (isEditing) {
            buttonAdd.displayString = I18n.format(Names.GuiNames.Buttons.SAVE);
            buttonRemove.displayString = I18n.format(Names.GuiNames.Buttons.DISCARD);
        } else {
            buttonAdd.displayString = I18n.format(Names.GuiNames.Buttons.ADD);
            buttonRemove.displayString = I18n.format(Names.GuiNames.Buttons.REMOVE);
        }
        
        buttonAdd.enabled = !isEditing || (isEditing && isNameValid() && isHostValid() && isUsernameValid());
        buttonRemove.enabled = serverList.getSelected() != null || isEditing;
        
        buttonChannels.enabled = !isEditing && serverList.getSelected() != null
                && !((ScrollableListItemServer) serverList.getSelected()).getConnection().isConnecting();
        buttonToggleConnection.enabled = !isEditing && serverList.getSelected() != null
                && !((ScrollableListItemServer) serverList.getSelected()).getConnection().isConnecting();
        
        fieldName.setEnabled(serverList.getSelected() != null || isEditing);
        fieldName.setValid(!fieldName.isEnabled() || isNameValid());
        fieldHost.setEnabled(serverList.getSelected() != null || isEditing);
        fieldHost.setValid(!fieldHost.isEnabled() || isHostValid());
        fieldUsername.setEnabled(serverList.getSelected() != null || isEditing);
        fieldUsername.setValid(!fieldUsername.isEnabled() || isUsernameValid());
        fieldServerPass.setEnabled(serverList.getSelected() != null || isEditing);
        fieldNickservPass.setEnabled(serverList.getSelected() != null || isEditing);
        
        fieldCheckShowGameJoins.setEnabled(serverList.getSelected() != null || isEditing);
        fieldCheckShowGameParts.setEnabled(serverList.getSelected() != null || isEditing);
        fieldCheckShowIRCJoins.setEnabled(serverList.getSelected() != null || isEditing);
        fieldCheckShowIRCParts.setEnabled(serverList.getSelected() != null || isEditing);
        fieldCheckShowDeaths.setEnabled(serverList.getSelected() != null || isEditing);
        fieldCheckShowAchievements.setEnabled(serverList.getSelected() != null || isEditing);
        
        container.setX(serverList.getX() + serverList.getWidth() + 10);
        container.setY(serverList.getY());
        container.setWidth(getWidth() - serverList.getX() - serverList.getWidth() - 10 - 20);
        container.setHeight(getHeight() - serverList.getY() - 10);
        
        fieldName.setWidth(container.getInnerWidth() - 5 - 5);
        fieldHost.setWidth(container.getInnerWidth() - 5 - 5);
        fieldUsername.setWidth(container.getInnerWidth() - 5 - 5);
        fieldServerPass.setWidth(container.getInnerWidth() - 5 - 5);
        fieldNickservPass.setWidth(container.getInnerWidth() - 5 - 5);
        
        // Render components
        serverList.render(mx, my, partialTick);
        
        container.render(mx, my, partialTick);
        
        buttonAdd.drawButton(getMc(), mx, my);
        buttonRemove.drawButton(getMc(), mx, my);
        
        buttonChannels.xPosition = getWidth() - 20 - 10;
        buttonChannels.drawButton(getMc(), mx, my);
        
        buttonToggleConnection.xPosition = getWidth() - 20 - 10 - 20 - 5;
        buttonToggleConnection.drawButton(getMc(), mx, my);
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        super.onClick(x, y, button);
        
        serverList.onClick(x, y, button);
        container.onClick(x, y, button);
    }
    
    @Override
    public void onMouseDrag(int x, int y, int button) {
    
        super.onMouseDrag(x, y, button);
        
        serverList.onMouseDrag(x, y, button);
        container.onMouseDrag(x, y, button);
    }
    
    @Override
    public void onMouseUp(int x, int y, int button) {
    
        super.onMouseUp(x, y, button);
        
        serverList.onMouseUp(x, y, button);
        container.onMouseUp(x, y, button);
    }
    
    @Override
    public void onKeyPress(int key, char c) {
    
        super.onKeyPress(key, c);
        
        container.onKeyType(c, key);
    }
    
    @Override
    public List<String> getTooltip() {
    
        return Arrays.asList(I18n.format(Names.GuiNames.SERVERS_TITLE));
    }
    
    @Override
    protected void actionPerformed(GuiButton button) {
    
        if (button == buttonAdd) {
            if (isEditing) {
                // Save
                IIRCConnection c;
                if (isEditingNewServer) {
                    c = AConnectionManager.inst().newConnection();
                    c.setId(fieldName.getText().toLowerCase().replace(" ", ""));
                } else {
                    c = ((ScrollableListItemServer) serverList.getSelected()).getConnection();
                }
                isEditing = false;
                isEditingNewServer = false;
                
                c.getConfig().setName(fieldName.getText());
                String host = fieldHost.getText().contains(":") ? fieldHost.getText().substring(0, fieldHost.getText().indexOf(":")) : fieldHost
                        .getText();
                int port = fieldHost.getText().contains(":") ? Integer.parseInt(fieldHost.getText().substring(fieldHost.getText().indexOf(":") + 1))
                        : 6667;
                c.getConfig().setHost(host);
                c.getConfig().setPort(port);
                c.getConfig().setPass(fieldServerPass.getText());
                
                c.getConfig().setShowGameJoins(fieldCheckShowGameJoins.getState());
                c.getConfig().setShowGameParts(fieldCheckShowGameParts.getState());
                c.getConfig().setShowIRCJoins(fieldCheckShowIRCJoins.getState());
                c.getConfig().setShowIRCParts(fieldCheckShowIRCParts.getState());
                c.getConfig().setShowDeaths(fieldCheckShowDeaths.getState());
                c.getConfig().setShowAchievements(fieldCheckShowAchievements.getState());
                
                c.saveConfig();
                if (isEditingNewServer) {
                    ScrollableListItemServer i = new ScrollableListItemServer(serverList, c);
                    serverList.addItem(i);
                    serverList.setSelected(i);
                }
            } else {
                // Add
                isEditing = true;
                isEditingNewServer = true;
                resetFields();
                serverList.setSelected(-1);
                
                fieldName.setText("New Server");
                fieldHost.setText("your.server.com");
                
                fieldCheckShowGameJoins.setState(true);
                fieldCheckShowGameParts.setState(true);
                fieldCheckShowIRCJoins.setState(true);
                fieldCheckShowIRCParts.setState(true);
                fieldCheckShowDeaths.setState(true);
                fieldCheckShowAchievements.setState(true);
            }
        } else if (button == buttonRemove) {
            if (isEditing) {
                // Discard
                resetFields();
                if (serverList.getSelected() != null) setFields(((ScrollableListItemServer) serverList.getSelected()).getConnection());
                isEditing = false;
                isEditingNewServer = false;
            } else {
                // Remove
                ScrollableListItemServer selected = (ScrollableListItemServer) serverList.getSelected();
                if (selected != null) serverList.removeItem(serverList.getSelectedIndex());
                resetFields();
                
                selected.getConnection().disconnect();
                AConnectionManager.inst().removeConnection(selected.getConnection());
            }
        } else if (button == buttonChannels) {
            getMainGui().addScreen(new ScreenChannelList(this, ((ScrollableListItemServer) serverList.getSelected()).getConnection()));
        } else if (button == buttonToggleConnection) {
            final IIRCConnection c = ((ScrollableListItemServer) serverList.getSelected()).getConnection();
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                
                    if (!c.isConnected()) {
                        c.connect();
                    } else {
                        c.disconnect();
                    }
                }
            }).start();
        }
    }
    
    @Override
    public void onChange(Object component) {
    
        if (component == serverList) {
            ScrollableListItemServer selected = (ScrollableListItemServer) serverList.getSelected();
            if (selected != null) {
                resetFields();
                setFields(selected.getConnection());
                isEditing = false;
                isEditingNewServer = false;
            }
        } else {
            isEditing = true;
        }
    }
    
    private void resetFields() {
    
        fieldName.setText("");
        fieldHost.setText("");
        fieldUsername.setText("");
        fieldServerPass.setText("");
        fieldNickservPass.setText("");
        
        fieldCheckShowGameJoins.setState(false);
        fieldCheckShowGameParts.setState(false);
        fieldCheckShowIRCJoins.setState(false);
        fieldCheckShowIRCParts.setState(false);
        fieldCheckShowDeaths.setState(false);
        fieldCheckShowAchievements.setState(false);
    }
    
    private void setFields(IIRCConnection connection) {
    
        fieldName.setText(connection.getName());
        fieldHost.setText(connection.getConfig().getHost() + (connection.getConfig().getPort() != 6667 ? connection.getConfig().getHost() : ""));
        fieldUsername.setText(connection.getConfig().getNick());
        fieldServerPass.setText(connection.getConfig().getPass());
        fieldNickservPass.setText("");
        
        fieldCheckShowGameJoins.setState(connection.getConfig().isShowGameJoins());
        fieldCheckShowGameParts.setState(connection.getConfig().isShowGameParts());
        fieldCheckShowIRCJoins.setState(connection.getConfig().isShowIRCJoins());
        fieldCheckShowIRCParts.setState(connection.getConfig().isShowIRCParts());
        fieldCheckShowDeaths.setState(connection.getConfig().isShowDeaths());
        fieldCheckShowAchievements.setState(connection.getConfig().isShowAchievements());
    }
    
    private boolean isNameValid() {
    
        if (fieldName.getText().trim().isEmpty()) return false;
        
        for (IIRCConnection c : AConnectionManager.inst().getConnections()) {
            if (serverList.getSelected() != null) if (((ScrollableListItemServer) serverList.getSelected()).getConnection() == c) continue;
            if (c.getId().equalsIgnoreCase(fieldName.getText().toLowerCase().replace(" ", ""))) return false;
        }
        
        return true;
    }
    
    private boolean isHostValid() {
    
        if (fieldHost.getText().trim().isEmpty()) return false;
        if (fieldHost.getText().trim().contains(" ")) return false;
        if (fieldHost.getText().trim().contains(":") && fieldHost.getText().trim().indexOf(":") != fieldHost.getText().trim().lastIndexOf(":")) return false;
        
        return true;
    }
    
    private boolean isUsernameValid() {
    
        if (fieldUsername.getText().trim().isEmpty()) return false;
        if (fieldHost.getText().trim().contains(" ")) return false;
        
        return true;
    }
    
}
