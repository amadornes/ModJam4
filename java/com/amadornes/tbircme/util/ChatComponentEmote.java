package com.amadornes.tbircme.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.emote.Emote;
import com.amadornes.tbircme.emote.EmoteWrapper;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("rawtypes")
public class ChatComponentEmote implements IChatComponent {

	private static String getTextForWidth(int width) {
		String s = "   ";

		return s;
	}

	private IChatComponent comp;
	private List<EmoteWrapper> emotes;

	public ChatComponentEmote(IChatComponent comp) {
		this.comp = comp;
	}

	private String unformattedForChatOriginal = "";
	private String unformattedForChat = null;

	@Override
	public String getUnformattedTextForChat() {

		String str = comp.getUnformattedTextForChat();
		if (unformattedForChat == null || !unformattedForChatOriginal.equals(str)) {
			unformattedForChatOriginal = str;
			String s = str;
			for (Emote e : TheBestIRCModEver.emotes) {
				s.replace(" " + e.getEmote() + " ", getTextForWidth(14) + "  ");
				if (s.startsWith(e.getEmote()))
					s.replaceFirst(e.getEmote(), getTextForWidth(14));
				if (s.endsWith(e.getEmote()))
					s = s.substring(0, s.length() - e.getEmote().length() - 1);
			}

			unformattedForChat = s;
		}
		if (!Config.emotesEnabled)
			return unformattedForChatOriginal;

		return unformattedForChat;
	}

	public String getOriginalText() {
		return comp.getUnformattedTextForChat();
	}

	protected List siblings = Lists.newArrayList();
	private ChatStyle style;

	public IChatComponent appendSibling(IChatComponent sibling) {
		comp.appendSibling(sibling);
		return this;
	}

	public List getSiblings() {
		return comp.getSiblings();
	}

	public IChatComponent appendText(String text) {
		return comp.appendText(text);
	}

	public IChatComponent setChatStyle(ChatStyle style) {

		return comp.setChatStyle(style);
	}

	public ChatStyle getChatStyle() {
		return comp.getChatStyle();
	}

	public Iterator iterator() {
		return comp.iterator();
	}

	private String unformattedOriginal = "";
	private String unformatted = null;

	public final String getUnformattedText() {

		String str = comp.getUnformattedTextForChat();
		if (unformatted == null || !unformattedOriginal.equals(str)) {
			unformattedOriginal = str;
			String s = str;
			for (Emote e : TheBestIRCModEver.emotes) {
				s.replace(" " + e.getEmote() + " ", getTextForWidth(14) + "  ");
				if (s.startsWith(e.getEmote()))
					s.replaceFirst(e.getEmote(), getTextForWidth(14));
				if (s.endsWith(e.getEmote()))
					s = s.substring(0, s.length() - e.getEmote().length() - 1);
			}

			unformatted = s;
		}
		if (!Config.emotesEnabled)
			return unformattedOriginal;

		return unformatted;
	}

	private String formattedOriginal = "";
	private String formatted = null;

	@SideOnly(Side.CLIENT)
	public final String getFormattedText() {
		String str = comp.getFormattedText();
		if (formatted == null || !formattedOriginal.equals(str)) {
			formattedOriginal = str;
			
			String s = comp.getFormattedText();
			for (Emote e : TheBestIRCModEver.emotes) {
				s = s.replace(" " + e.getEmote() + " ", getTextForWidth(14) + " ");
				s = s.replace("\u00A7r" + e.getEmote() + " ", getTextForWidth(14));
				s = s.replace(" " + e.getEmote() + "\u00A7r ", getTextForWidth(14) + " ");
				s = s.replace(" " + e.getEmote() + "\u00A7r", getTextForWidth(14));
				s = s.replace("\u00A7r" + e.getEmote() + "\u00A7r ", getTextForWidth(14));
				s = s.replace("\u00A7r" + e.getEmote() + "\u00A7r", getTextForWidth(14));
				if (s.startsWith(e.getEmote()))
					s.replaceFirst(e.getEmote(), getTextForWidth(14));
				if (s.endsWith(e.getEmote()))
					s = s.substring(0, s.length() - e.getEmote().length() - 1);
			}

			formatted = s;
		}

		if (!Config.emotesEnabled)
			return formattedOriginal;

		return formatted;
	}

	@SuppressWarnings("unchecked")
	public static Iterator createDeepCopyIterator(Iterable p_150262_0_) {
		Iterator iterator = Iterators.concat(Iterators.transform(p_150262_0_.iterator(),
				new Function() {

					public Iterator apply(IChatComponent p_150665_1_) {
						return p_150665_1_.iterator();
					}

					public Object apply(Object par1Obj) {
						return this.apply((IChatComponent) par1Obj);
					}
				}));
		iterator = Iterators.transform(iterator, new Function() {

			public IChatComponent apply(IChatComponent p_150666_1_) {
				IChatComponent ichatcomponent1 = p_150666_1_.createCopy();
				ichatcomponent1.setChatStyle(ichatcomponent1.getChatStyle().createDeepCopy());
				return ichatcomponent1;
			}

			public Object apply(Object par1Obj) {
				return this.apply((IChatComponent) par1Obj);
			}
		});
		return iterator;
	}

	public boolean equals(Object par1Obj) {
		if (this == par1Obj) {
			return true;
		} else if (!(par1Obj instanceof ChatComponentStyle)) {
			return false;
		} else {
			ChatComponentStyle chatcomponentstyle = (ChatComponentStyle) par1Obj;
			return this.siblings.equals(chatcomponentstyle.getSiblings())
					&& this.getChatStyle().equals(chatcomponentstyle.getChatStyle());
		}
	}

	public int hashCode() {
		return 31 * this.style.hashCode() + this.siblings.hashCode();
	}

	public String toString() {
		return "EmoteComponent" + comp.toString().substring(comp.toString().indexOf("{"));
	}

	public String getChatComponentText_TextValue() {
		return this.getUnformattedTextForChat();
	}

	public ChatComponentEmote createCopy() {
		return new ChatComponentEmote(comp);
	}

	public List<EmoteWrapper> getEmotes() {
		return emotes;
	}

	public void setEmotes(List<EmoteWrapper> emotes) {
		this.emotes = emotes;
	}

}
