package com.amadornes.tbircme.gui.comp;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import com.amadornes.tbircme.render.RenderHelper;
import com.amadornes.tbircme.util.IChangeListener;

public class GuiCheckbox extends Gui {
	
	private IChangeListener parent;
	
	private int x, y, size;
	private boolean state = false;
	private boolean isEnabled = true;
	private String label;

	public GuiCheckbox(IChangeListener parent, int x, int y, int size) {
		this(parent, x, y, size, "");
	}

	public GuiCheckbox(IChangeListener parent, int x, int y, int size, String label) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.size = size;
		this.label = label;
	}

	public void drawCheckbox() {
		drawRect(this.x - 1, this.y - 1, this.x + this.size + 1, this.y + this.size + 1,
				isEnabled ? -6250336 : -16777216);
		drawRect(this.x, this.y, this.x + this.size, this.y + this.size, isEnabled ? -16777216 : 0);

		if (state)
			drawRect(this.x + 1, this.y + 1, this.x + this.size - 1, this.y + this.size - 1,
					isEnabled ? -1774280 : -6250336);
		
		drawString(Minecraft.getMinecraft().fontRenderer, label, x + size + 3, y + ((int)Math.ceil(((size/2) - (7D/2D)))), isEnabled ? 16777215 : -6250336);
	}

	public void mouseClick(int x, int y, int button) {
		if (x >= this.x && x < this.x + size && y >= this.y && y < this.y + size){
			state = !state;
			parent.onChange(this);
		}
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return state;
	}

	public int getSize() {
		return size;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	private String tooltip = "";

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getTooltip() {
		return tooltip;
	}

	@SuppressWarnings("unchecked")
	public void renderTooltip(int mx, int my) {
		if (tooltip != null && tooltip.trim().length() > 0)
			if (mx >= x && mx < x + size && my >= y && my < y + size)
				RenderHelper.drawHoveringText(Minecraft.getMinecraft().fontRenderer
						.listFormattedStringToWidth(this.tooltip, 150), mx, my, Minecraft
						.getMinecraft().fontRenderer);
	}

}
