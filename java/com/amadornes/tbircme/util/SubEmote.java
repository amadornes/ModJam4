package com.amadornes.tbircme.util;

import java.util.ArrayList;
import java.util.List;

public class SubEmote extends Emote {

	private String channel;
	
	public SubEmote(String name, String channel, String url) {
		super(name, url);
		this.channel = channel;
	}
	
	public String getChannel() {
		return channel;
	}
	
	@Override
	public List<String> getToolTip() {
		if(tooltip == null){
			tooltip = new ArrayList<String>();
			tooltip.add(getEmote());
			tooltip.add(getChannel());
		}
		
		return tooltip;
	}

}
