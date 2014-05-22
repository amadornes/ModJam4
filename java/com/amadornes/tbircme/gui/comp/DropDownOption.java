package com.amadornes.tbircme.gui.comp;

public class DropDownOption {

	private String text;
	private boolean canBeSelected = false;

	public DropDownOption(String text, boolean canBeSelected) {
		this.text = text;
		this.canBeSelected = canBeSelected;
	}

	public String getText() {
		return text;
	}

	public boolean canBeSelected() {
		return canBeSelected;
	}

	public void setText(String text) {
		this.text = text;
	}

}
