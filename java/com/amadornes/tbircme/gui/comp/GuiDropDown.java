package com.amadornes.tbircme.gui.comp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.render.RenderHelper;
import com.amadornes.tbircme.util.IChangeListener;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDropDown extends Gui {
	private final FontRenderer fontRenderer;
	public int xPosition;
	public int yPosition;

	public int width;
	public int height;
	private int dropDownHeight = 0;

	private boolean enableBackgroundDrawing = true;
	private boolean isEnabled = true;

	private int enabledColor = 14737632;
	private int disabledColor = 7368816;
	private int placeholderColor = 0x5C5C5C;
	private int placeholderDisabledColor = 0x2E2E2E;

	private boolean visible = true;

	private String placeholder = "";
	private boolean password = false;

	private IChangeListener parent;

	private int maxWidth;

	private List<DropDownOption> options = new ArrayList<DropDownOption>();
	private int selected = -1;

	private boolean renderDropDown = false;

	public GuiDropDown(IChangeListener parent, FontRenderer par1FontRenderer, int par2, int par3,
			int par4, int par5) {
		this.parent = parent;
		this.fontRenderer = par1FontRenderer;
		this.xPosition = par2;
		this.yPosition = par3;
		this.width = par4;
		this.height = par5;
	}

	public GuiDropDown(IChangeListener parent, FontRenderer par1FontRenderer, int par2, int par3,
			int par4, int par5, int maxWidth) {
		this.parent = parent;
		this.fontRenderer = par1FontRenderer;
		this.xPosition = par2;
		this.yPosition = par3;
		this.width = par4;
		this.height = par5;
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
		if (placeholder == null)
			this.placeholder = "";
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
		if (tooltip != null && tooltip.trim().length() > 0)
			if (mx >= xPosition && mx < xPosition + width && my >= yPosition
					&& my < yPosition + height)
				RenderHelper.drawHoveringText(
						fontRenderer.listFormattedStringToWidth(this.tooltip, 150), mx, my,
						fontRenderer);
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

	@SuppressWarnings("unused")
	public void mouseClicked(int mx, int my, int btn) {
		boolean clickedMain = mx >= this.xPosition && mx < this.xPosition + this.width
				&& my >= this.yPosition && my < this.yPosition + this.height;

		if (clickedMain)
			renderDropDown = !renderDropDown;

		if(renderDropDown){
			int y = 0;
			int i2 = 0;
			for (DropDownOption o : options) {
				y += 5;

				if (mx >= this.xPosition && mx < this.xPosition + this.width
						&& my >= this.yPosition + this.height + 1 + y - 5
						&& my < this.yPosition + this.height + 1 + y + fontRenderer.FONT_HEIGHT + 5 - 1) {
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

		if (!(mx >= xPosition && mx < xPosition + width && my >= yPosition && my < yPosition
				+ height + dropDownHeight + 1)
				&& renderDropDown)
			renderDropDown = false;

		if (!this.getVisible())
			return;

		if (this.getEnableBackgroundDrawing()) {
			drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1,
					this.yPosition + this.height + 1, -6250336);
			drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition
					+ this.height, -16777216);
			drawRect(this.xPosition - 1 + this.width - this.height, this.yPosition - 1,
					this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
			drawRect(this.xPosition + this.width - this.height, this.yPosition, this.xPosition
					+ this.width, this.yPosition + this.height, -16777216);
		}

		int i = this.isEnabled ? this.enabledColor : this.disabledColor;
		if (getSelectedOption() == null)
			i = this.isEnabled ? this.placeholderColor : this.placeholderDisabledColor;
		String s = this.fontRenderer.trimStringToWidth(
				(getSelectedOption() != null ? getSelectedOption().getText() : placeholder),
				this.getWidth() - this.height);

		this.fontRenderer
				.drawStringWithShadow(s, this.xPosition + 4, (int) (this.yPosition + Math
						.ceil((this.height - fontRenderer.FONT_HEIGHT) / 2)), i);

		GL11.glPushMatrix();
		{
			int size = 10;
			int x2 = (int) (this.xPosition + this.width - this.height + ((this.height - size) / 2D));
			int y2 = (int) (this.yPosition + ((this.height - (size / 2D)) / 2D)) + 1;

			for (int y = 0; y < (size / 2); y++) {
				drawRect(x2 + y, y2 + y, x2 + size - y, y2 + y + 1, -6250336);
			}
		}
		GL11.glPopMatrix();
	}

	public void drawDropDownMenu(int mx, int my, float frame) {
		if (!this.getVisible())
			return;
		if (!this.renderDropDown)
			return;

		drawRect(this.xPosition, this.yPosition + height + 1, this.xPosition + this.width,
				this.yPosition + height + 1 + this.dropDownHeight, -16777216);

		int i = this.isEnabled ? this.enabledColor : this.disabledColor;
		if (getSelectedOption() == null)
			i = this.isEnabled ? this.placeholderColor : this.placeholderDisabledColor;

		int y = 0;
		int i2 = 0;
		for (DropDownOption o : options) {
			y += 5;

			boolean selected = false;

			if (my >= this.yPosition + this.height + 1 + y - 5
					&& my < this.yPosition + this.height + 1 + y + fontRenderer.FONT_HEIGHT + 5 - 1) {
				drawRect(this.xPosition, this.yPosition + this.height + 1 + y - 5, this.xPosition
						+ this.width, this.yPosition + this.height + 1 + y
						+ fontRenderer.FONT_HEIGHT + 5 - 1, -(0xFFFFFF - 0xBAD6F7));
				selected = true;
			}

			String s = this.fontRenderer.trimStringToWidth(o.getText(), this.getWidth()
					- this.height);
			if (selected) {
				this.fontRenderer.drawString(s, this.xPosition + 4, this.yPosition + this.height
						+ 1 + y, 0);
			} else {
				this.fontRenderer.drawStringWithShadow(s, this.xPosition + 4, this.yPosition
						+ this.height + 1 + y, i);
			}

			y += 5 + fontRenderer.FONT_HEIGHT;
			i2++;

			if (i2 < options.size()) {
				drawRect(this.xPosition + 4, this.yPosition + this.height + y, this.xPosition - 4
						+ width, this.yPosition + this.height + y + 1, -(0xFFFFFF - 0x333333));
			}
		}
	}

	public boolean getEnableBackgroundDrawing() {
		return this.enableBackgroundDrawing;
	}

	public void setEnableBackgroundDrawing(boolean p_146185_1_) {
		this.enableBackgroundDrawing = p_146185_1_;
	}

	public void setTextColor(int p_146193_1_) {
		this.enabledColor = p_146193_1_;
	}

	public void setDisabledTextColour(int p_146204_1_) {
		this.disabledColor = p_146204_1_;
	}

	public void setEnabled(boolean p_146184_1_) {
		this.isEnabled = p_146184_1_;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public int getWidth() {
		return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
	}

	public boolean getVisible() {
		return this.visible;
	}

	public void setVisible(boolean p_146189_1_) {
		this.visible = p_146189_1_;
	}

	public void addOption(DropDownOption option) {
		options.add(option);
	}

	public void setSelected(int selected) {
		this.selected = selected;
	}
}