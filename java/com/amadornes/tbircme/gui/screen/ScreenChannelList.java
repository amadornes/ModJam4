package com.amadornes.tbircme.gui.screen;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.api.AConnectionManager;
import com.amadornes.tbircme.api.IIRCChannel;
import com.amadornes.tbircme.api.IIRCConnection;
import com.amadornes.tbircme.gui.IGui;
import com.amadornes.tbircme.gui.Screen;
import com.amadornes.tbircme.gui.comp.DropDownOption;
import com.amadornes.tbircme.gui.comp.GuiCheckbox;
import com.amadornes.tbircme.gui.comp.GuiDropDown;
import com.amadornes.tbircme.gui.comp.GuiTextFieldCustomizable;
import com.amadornes.tbircme.gui.comp.ScrollableList;
import com.amadornes.tbircme.gui.comp.ScrollableListItem;
import com.amadornes.tbircme.gui.comp.ScrollableListItemChannel;
import com.amadornes.tbircme.gui.comp.ScrollablePanel;
import com.amadornes.tbircme.ref.Names;

public class ScreenChannelList extends Screen {
    
    private IIRCConnection           connection;
    private ScrollableList           channelList;
    private GuiButton                buttonAdd;
    private GuiButton                buttonRemove;
    
    private ScrollablePanel          container;
    private GuiTextFieldCustomizable fieldChannel;
    private GuiDropDown              fieldDropDownConnection;
    private GuiCheckbox              fieldCheckShowGameJoins;
    private GuiCheckbox              fieldCheckShowGameParts;
    private GuiCheckbox              fieldCheckShowIRCJoins;
    private GuiCheckbox              fieldCheckShowIRCParts;
    private GuiCheckbox              fieldCheckShowDeaths;
    private GuiCheckbox              fieldCheckShowAchievements;
    private GuiCheckbox              fieldCheckReceiveMessages;
    private GuiCheckbox              fieldCheckSendMessages;
    private GuiCheckbox              fieldCheckOverrideServer;
    
    private boolean                  initialized         = false;
    private boolean                  isEditing           = false;
    private boolean                  isEditingNewChannel = false;
    
    public ScreenChannelList(IGui parent, IIRCConnection connection) {
    
        super(parent);
        this.connection = connection;
    }
    
    public ScreenChannelList(IGui parent) {
    
        this(parent, null);
    }
    
    @Override
    public void onOpen() {
    
        channelList = new ScrollableList(this, 10, 35, 100, getHeight() - 40 - 10 - 20 - 20 - 5, true);
        if (connection == null) {
            for (IIRCConnection c : AConnectionManager.inst().getConnections())
                for (IIRCChannel channel : c.getChannels())
                    channelList.addItem(new ScrollableListItemChannel(channelList, channel));
        } else {
            for (IIRCChannel channel : connection.getChannels())
                channelList.addItem(new ScrollableListItemChannel(channelList, channel));
        }
        
        buttonAdd = new GuiButton(0, 10, channelList.getY() + channelList.getHeight() + 5, channelList.getWidth(), 20,
                I18n.format(Names.GuiNames.Buttons.ADD));
        buttonRemove = new GuiButton(1, 10, channelList.getY() + channelList.getHeight() + 5 + 20 + 5, channelList.getWidth(), 20,
                I18n.format(Names.GuiNames.Buttons.REMOVE));
        
        buttonList.add(buttonAdd);
        buttonList.add(buttonRemove);
        
        // Options
        container = new ScrollablePanel(this, channelList.getX() + channelList.getWidth() + 10, channelList.getY(), getWidth() - channelList.getX()
                - channelList.getWidth() - 10 - 20, getHeight() - channelList.getY() - 10);
        container.setMarginBottom(5);
        container.setDrawBackground(false);
        
        int y = 5;
        fieldChannel = new GuiTextFieldCustomizable(this, getFontRenderer(), 5, 5, 100, 20, 1000);
        fieldChannel.setPlaceholder("Channel name");
        container.addComponent(fieldChannel);
        
        if (connection == null) {
            fieldDropDownConnection = new GuiDropDown(this, getFontRenderer(), 5, y += 20 + 7, 100, 20, 1000);
            fieldDropDownConnection.addOption(new DropDownOption("Select a server", false));
            for (IIRCConnection c : AConnectionManager.inst().getConnections())
                fieldDropDownConnection.addOption(new DropDownOption(c.getName(), true));
            fieldDropDownConnection.setPlaceholder("Select a server");
            container.addComponent(fieldDropDownConnection);
        }
        
        fieldCheckOverrideServer = new GuiCheckbox(this, 5, y += 20 + 5, 10, "Override server config");
        fieldCheckOverrideServer.setState(false);
        fieldCheckOverrideServer.setOnColor(0xFFFF5555);
        container.addComponent(fieldCheckOverrideServer);
        
        fieldCheckShowGameJoins = new GuiCheckbox(this, 10, y += 10 + 10, 10, "Show game joins");
        fieldCheckShowGameJoins.setState(true);
        container.addComponent(fieldCheckShowGameJoins);
        
        fieldCheckShowGameParts = new GuiCheckbox(this, 10, y += 10 + 5, 10, "Show game parts");
        fieldCheckShowGameParts.setState(true);
        container.addComponent(fieldCheckShowGameParts);
        
        fieldCheckShowIRCJoins = new GuiCheckbox(this, 10, y += 10 + 5, 10, "Show IRC joins");
        fieldCheckShowIRCJoins.setState(true);
        container.addComponent(fieldCheckShowIRCJoins);
        
        fieldCheckShowIRCParts = new GuiCheckbox(this, 10, y += 10 + 5, 10, "Show IRC parts");
        fieldCheckShowIRCParts.setState(true);
        container.addComponent(fieldCheckShowIRCParts);
        
        fieldCheckShowDeaths = new GuiCheckbox(this, 10, y += 10 + 5, 10, "Show deaths");
        fieldCheckShowDeaths.setState(true);
        container.addComponent(fieldCheckShowDeaths);
        
        fieldCheckShowAchievements = new GuiCheckbox(this, 10, y += 10 + 5, 10, "Show achievements [WIP]");
        fieldCheckShowAchievements.setState(true);
        container.addComponent(fieldCheckShowAchievements);
        
        fieldCheckReceiveMessages = new GuiCheckbox(this, 5, y += 10 + 15, 10, "Receive IRC messages");
        fieldCheckReceiveMessages.setState(true);
        fieldCheckReceiveMessages.setOnColor(0xFF00AAFF);
        container.addComponent(fieldCheckReceiveMessages);
        
        fieldCheckSendMessages = new GuiCheckbox(this, 5, y += 10 + 5, 10, "Send chat messages");
        fieldCheckSendMessages.setState(true);
        fieldCheckSendMessages.setOnColor(0xFF00AAFF);
        container.addComponent(fieldCheckSendMessages);
        
        // Mark as initialized
        initialized = true;
    }
    
    @Override
    public void renderComponents(int mx, int my, float partialTick) {
    
        drawCenteredString(getFontRenderer(), I18n.format(Names.GuiNames.CHANNELS_TITLE), getWidth() / 2, 15, 16777215);
        
        if (!initialized) return;
        
        // Update componenents
        channelList.setHeight(getHeight() - 40 - 10 - 20 - 20 - 5);
        
        buttonAdd.yPosition = channelList.getY() + channelList.getHeight() + 5;
        buttonRemove.yPosition = channelList.getY() + channelList.getHeight() + 5 + 20 + 5;
        if (isEditing) {
            buttonAdd.displayString = I18n.format(Names.GuiNames.Buttons.SAVE);
            buttonRemove.displayString = I18n.format(Names.GuiNames.Buttons.DISCARD);
        } else {
            buttonAdd.displayString = I18n.format(Names.GuiNames.Buttons.ADD);
            buttonRemove.displayString = I18n.format(Names.GuiNames.Buttons.REMOVE);
        }
        
        buttonAdd.enabled = !isEditing || (isEditing && isNameValid());
        buttonRemove.enabled = channelList.getSelected() != null || isEditing;
        
        fieldChannel.setEnabled(channelList.getSelected() != null || isEditing);
        fieldChannel.setValid(!fieldChannel.isEnabled() || isNameValid());
        
        fieldCheckOverrideServer.setEnabled(channelList.getSelected() != null || isEditing);
        fieldCheckShowGameJoins.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckShowGameParts.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckShowIRCJoins.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckShowIRCParts.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckShowDeaths.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckShowAchievements.setEnabled((channelList.getSelected() != null || isEditing) && fieldCheckOverrideServer.getState());
        fieldCheckReceiveMessages.setEnabled(channelList.getSelected() != null || isEditing);
        fieldCheckSendMessages.setEnabled(channelList.getSelected() != null || isEditing);
        if (fieldDropDownConnection != null) fieldDropDownConnection.setEnabled(channelList.getSelected() != null
                || (isEditing && isEditingNewChannel));
        
        container.setX(channelList.getX() + channelList.getWidth() + 10);
        container.setY(channelList.getY());
        container.setWidth(getWidth() - channelList.getX() - channelList.getWidth() - 10 - 20);
        container.setHeight(getHeight() - channelList.getY() - 10);
        
        fieldChannel.setWidth(container.getInnerWidth() - 5 - 5);
        if (fieldDropDownConnection != null) fieldDropDownConnection.setWidth(container.getInnerWidth() - 5 - 5);
        
        // Render components
        channelList.render(mx, my, partialTick);
        
        container.render(mx, my, partialTick);
        
        buttonAdd.drawButton(getMc(), mx, my);
        buttonRemove.drawButton(getMc(), mx, my);
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        super.onClick(x, y, button);
        
        channelList.onClick(x, y, button);
        container.onClick(x, y, button);
    }
    
    @Override
    public void onMouseDrag(int x, int y, int button) {
    
        super.onMouseDrag(x, y, button);
        
        channelList.onMouseDrag(x, y, button);
        container.onMouseDrag(x, y, button);
    }
    
    @Override
    public void onMouseUp(int x, int y, int button) {
    
        super.onMouseUp(x, y, button);
        
        channelList.onMouseUp(x, y, button);
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
                IIRCChannel c = null;
                IIRCConnection con = null;
                for (IIRCConnection conn : AConnectionManager.inst().getConnections()) {
                    if (fieldDropDownConnection.getSelectedOption().getText().equals(conn.getName())) {
                        con = conn;
                        break;
                    }
                }
                if (isEditingNewChannel) {
                    if (connection != null) {
                        c = connection.join(fieldChannel.getText());
                    } else {
                        c = con.join(fieldChannel.getText());
                    }
                } else {
                    c = ((ScrollableListItemChannel) channelList.getSelected()).getChannel();
                }
                if (c == null) return;
                IIRCChannel oldChannel = c;
                c = c.setName(fieldChannel.getText());
                
                // FIXME
                
                c.saveConfig();
                if (isEditingNewChannel) {
                    ScrollableListItemChannel i = new ScrollableListItemChannel(channelList, c);
                    channelList.addItem(i);
                    channelList.setSelected(i);
                } else {
                    if (oldChannel != c) {
                        for (ScrollableListItem i : channelList.getItems()) {
                            if (((ScrollableListItemChannel) i).getChannel() == oldChannel) {
                                ((ScrollableListItemChannel) i).setChannel(c);
                            }
                        }
                    }
                }
                isEditing = false;
                isEditingNewChannel = false;
            } else {
                // Add
                isEditing = true;
                isEditingNewChannel = true;
                resetFields();
            }
        } else if (button == buttonRemove) {
            if (isEditing) {
                // Discard
                resetFields();
                if (channelList.getSelected() != null) setFields(((ScrollableListItemChannel) channelList.getSelected()).getChannel());
                isEditing = false;
                isEditingNewChannel = false;
            } else {
                // Remove
                ScrollableListItemChannel selected = (ScrollableListItemChannel) channelList.getSelected();
                if (selected != null) channelList.removeItem(channelList.getSelectedIndex());
                resetFields();
                
                selected.getChannel().getConnection().part(selected.getChannel().getName());
            }
        }
    }
    
    @Override
    public void onChange(Object component) {
    
        if (component == channelList) {
            ScrollableListItemChannel selected = (ScrollableListItemChannel) channelList.getSelected();
            if (selected != null) {
                resetFields();
                setFields(selected.getChannel());
                isEditing = false;
                isEditingNewChannel = false;
            }
        } else {
            isEditing = true;
        }
    }
    
    private void resetFields() {
    
        fieldChannel.setText("");
    }
    
    private void setFields(IIRCChannel channel) {
    
        fieldChannel.setText(channel.getName());
    }
    
    private boolean isNameValid() {
    
        if (fieldChannel.getText().trim().isEmpty()) return false;
        
        ScrollableListItemChannel sel = ((ScrollableListItemChannel) channelList.getSelected());
        if (sel == null) return true;
        IIRCChannel selected = sel.getChannel();
        for (IIRCChannel c : selected.getConnection().getChannels()) {
            if (c == selected) continue;
            if (c.getName().equalsIgnoreCase(fieldChannel.getText())) return false;
        }
        
        return true;
    }
    
}
