package com.amadornes.tbircme.gui.comp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.client.render.RenderHelper;
import com.amadornes.tbircme.util.IChangeListener;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDropDown extends Gui implements IComponent {
    
    private final FontRenderer   fontRenderer;
    public int                   xPosition;
    public int                   yPosition;
    
    public int                   width;
    public int                   height;
    private int                  dropDownHeight           = 0;
    
    private boolean              enableBackgroundDrawing  = true;
    private boolean              isEnabled                = true;
    
    private int                  enabledColor             = 14737632;
    private int                  disabledColor            = 7368816;
    private int                  placeholderColor         = 0x5C5C5C;
    private int                  placeholderDisabledColor = 0x2E2E2E;
    
    private boolean              visible                  = true;
    
    private String               placeholder              = "";
    private boolean              password                 = false;
    
    private IChangeListener      parent;
    
    private int                  maxWidth;
    
    private List<DropDownOption> options                  = new ArrayList<DropDownOption>();
    private int                  selected                 = -1;
    
    private boolean              renderDropDown           = false;
    
    public GuiDropDown(IChangeListener parent, FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5) {
    
        this.parent = parent;
        fontRenderer = par1FontRenderer;
        xPosition = par2;
        yPosition = par3;
        width = par4;
        height = par5;
        
        maxWidth = 1000;
    }
    
    public GuiDropDown(IChangeListener parent, FontRenderer par1FontRenderer, int par2, int par3, int par4, int par5, int maxWidth) {
    
        this.parent = parent;
        fontRenderer = par1FontRenderer;
        xPosition = par2;
        yPosition = par3;
        width = par4;
        height = par5;
        this.maxWidth = maxWidth;
    }
    
    public void setMaxWidth(int maxWidth) {
    
        this.maxWidth = maxWidth;
    }
    
    public int getMaxWidth() {
    
        return maxWidth;
    }
    
    public void setWidth(int width) {
    
        this.width = Math.min(width, maxWidth);
    }
    
    public void setPlaceholder(String placeholder) {
    
        this.placeholder = placeholder;
        if (placeholder == null) this.placeholder = "";
    }
    
    public String getPlaceholder() {
    
        return placeholder;
    }
    
    private String tooltip = "";
    
    public void setTooltip(String tooltip) {
    
        this.tooltip = tooltip;
    }
    
    public String getTooltip() {
    
        return tooltip;
    }
    
    public void setPassword(boolean password) {
    
        this.password = password;
    }
    
    public boolean isPassword() {
    
        return password;
    }
    
    @SuppressWarnings("unchecked")
    public void renderTooltip(int mx, int my) {
    
        if (tooltip != null && tooltip.trim().length() > 0) if (mx >= xPosition && mx < xPosition + width && my >= yPosition
                && my < yPosition + height) RenderHelper
                .drawHoveringText(fontRenderer.listFormattedStringToWidth(tooltip, 150), mx, my, fontRenderer);
    }
    
    public DropDownOption getSelectedOption() {
    
        try {
            return options.get(selected);
        } catch (Exception ex) {
        }
        return null;
    }
    
    public int getSelectedOptionID() {
    
        return selected;
    }
    
    public void mouseClicked(int mx, int my, int btn) {
    
        boolean clickedMain = mx >= xPosition && mx < xPosition + width && my >= yPosition && my < yPosition + height;
        
        if (clickedMain) renderDropDown = !renderDropDown;
        
        if (renderDropDown) {
            int y = 0;
            int i2 = 0;
            for (DropDownOption o : options) {
                y += 5;
                
                if (mx >= xPosition && mx < xPosition + width && my >= yPosition + height + 1 + y - 5
                        && my < yPosition + height + 1 + y + fontRenderer.FONT_HEIGHT + 5 - 1 && o.canBeSelected()) {
                    renderDropDown = false;
                    selected = i2;
                    parent.onChange(this);
                    break;
                }
                
                y += 5 + fontRenderer.FONT_HEIGHT;
                i2++;
            }
        }
    }
    
    @SuppressWarnings("unused")
    public void drawDropDown(int mx, int my, float frame) {
    
        dropDownHeight = 0;
        for (DropDownOption o : options)
            dropDownHeight += 5 + fontRenderer.FONT_HEIGHT + 5;
        dropDownHeight = Math.max(dropDownHeight, 10);
        
        if (!(mx >= xPosition && mx < xPosition + width && my >= yPosition && my < yPosition + height + dropDownHeight + 1) && renderDropDown) renderDropDown = false;
        
        if (!getVisible()) return;
        
        if (getEnableBackgroundDrawing()) {
            drawRect(xPosition - 1, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
            drawRect(xPosition, yPosition, xPosition + width, yPosition + height, -16777216);
            drawRect(xPosition - 1 + width - height, yPosition - 1, xPosition + width + 1, yPosition + height + 1, -6250336);
            drawRect(xPosition + width - height, yPosition, xPosition + width, yPosition + height, -16777216);
        }
        
        int i = isEnabled ? enabledColor : disabledColor;
        if (getSelectedOption() == null) i = isEnabled ? placeholderColor : placeholderDisabledColor;
        String s = fontRenderer.trimStringToWidth((getSelectedOption() != null ? getSelectedOption().getText() : placeholder), getWidth() - height);
        
        fontRenderer.drawStringWithShadow(s, xPosition + 4, (int) (yPosition + Math.ceil((height - fontRenderer.FONT_HEIGHT) / 2)), i);
        
        GL11.glPushMatrix();
        {
            int size = 10;
            int x2 = (int) (xPosition + width - height + ((height - size) / 2D));
            int y2 = (int) (yPosition + ((height - (size / 2D)) / 2D)) + 1;
            
            for (int y = 0; y < (size / 2); y++) {
                drawRect(x2 + y, y2 + y, x2 + size - y, y2 + y + 1, -6250336);
            }
        }
        GL11.glPopMatrix();
    }
    
    public void drawDropDownMenu(int mx, int my, float frame) {
    
        if (!getVisible()) return;
        if (!renderDropDown) return;
        
        drawRect(xPosition, yPosition + height + 1, xPosition + width, yPosition + height + 1 + dropDownHeight, -16777216);
        
        int i = isEnabled ? enabledColor : disabledColor;
        if (getSelectedOption() == null) i = isEnabled ? placeholderColor : placeholderDisabledColor;
        
        int y = 0;
        int i2 = 0;
        for (DropDownOption o : options) {
            y += 5;
            
            boolean selected = false;
            
            if (my >= yPosition + height + 1 + y - 5 && my < yPosition + height + 1 + y + fontRenderer.FONT_HEIGHT + 5 - 1) {
                drawRect(xPosition, yPosition + height + 1 + y - 5, xPosition + width, yPosition + height + 1 + y + fontRenderer.FONT_HEIGHT + 5 - 1,
                        o.canBeSelected() ? 0xFFBAD6F7 : 0xFF555555);
                selected = true;
            }
            
            String s = fontRenderer.trimStringToWidth(o.getText(), getWidth() - height);
            if (selected) {
                fontRenderer.drawString(s, xPosition + 4, yPosition + height + 1 + y, 0);
            } else {
                fontRenderer.drawStringWithShadow(s, xPosition + 4, yPosition + height + 1 + y, i);
            }
            
            y += 5 + fontRenderer.FONT_HEIGHT;
            i2++;
            
            if (i2 < options.size()) {
                drawRect(xPosition + 4, yPosition + height + y, xPosition - 4 + width, yPosition + height + y + 1, -(0xFFFFFF - 0x333333));
            }
        }
    }
    
    public boolean getEnableBackgroundDrawing() {
    
        return enableBackgroundDrawing;
    }
    
    public void setEnableBackgroundDrawing(boolean p_146185_1_) {
    
        enableBackgroundDrawing = p_146185_1_;
    }
    
    public void setTextColor(int p_146193_1_) {
    
        enabledColor = p_146193_1_;
    }
    
    public void setDisabledTextColour(int p_146204_1_) {
    
        disabledColor = p_146204_1_;
    }
    
    public void setEnabled(boolean p_146184_1_) {
    
        isEnabled = p_146184_1_;
    }
    
    public boolean isEnabled() {
    
        return isEnabled;
    }
    
    public boolean getVisible() {
    
        return visible;
    }
    
    public void setVisible(boolean p_146189_1_) {
    
        visible = p_146189_1_;
    }
    
    public void addOption(DropDownOption option) {
    
        options.add(option);
    }
    
    public void setSelected(int selected) {
    
        this.selected = selected;
    }
    
    @Override
    public void render(int x, int y, float frame) {
    
        drawDropDown(x, y, frame);
        
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0, 1);
        drawDropDownMenu(x, y, frame);
        GL11.glPopMatrix();
    }
    
    @Override
    public void onClick(int x, int y, int button) {
    
        mouseClicked(x, y, button);
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
    
        return xPosition;
    }
    
    @Override
    public int getY() {
    
        return yPosition;
    }
    
    @Override
    public int getHeight() {
    
        return height;
    }
    
    @Override
    public int getWidth() {
    
        return getEnableBackgroundDrawing() ? width - 8 : width;
    }
    
    public List<DropDownOption> getOptions() {
    
        return options;
    }
}
