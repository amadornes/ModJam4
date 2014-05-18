package com.amadornes.tbircme.emote;

public class EmoteWrapper {

	private Emote emote;
	private int line;
	private double x;

	public EmoteWrapper(Emote emote, int line, double x) {
		this.emote = emote;
		this.line = line;
		this.x = x;
	}

	public Emote getEmote() {
		return emote;
	}

	public int getLine() {
		return line;
	}

	public double getX() {
		return x;
	}

}
