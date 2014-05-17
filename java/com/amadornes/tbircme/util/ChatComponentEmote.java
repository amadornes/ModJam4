package com.amadornes.tbircme.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings("rawtypes")
public class ChatComponentEmote implements IChatComponent {

	private static String getTextForWidth(int width) {
		String s = "";
		int w = 0;

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

		while (w < width) {
			s += "i";
			w = fr.getStringWidth(s);
		}

		return s;
	}

	private IChatComponent comp;

	public ChatComponentEmote(IChatComponent comp) {
		this.comp = comp;
	}

	@Override
	public String getUnformattedTextForChat() {
		String s = comp.getUnformattedTextForChat();
		for (String e : TheBestIRCModEver.emotes) {
			s.replace(" " + e + " ", getTextForWidth(14) + "  ");
			if (s.startsWith(e))
				s.replaceFirst(e, getTextForWidth(14));
			if (s.endsWith(e))
				s = s.substring(0, s.length() - e.length() - 1);
		}
		
		System.out.println(s);
		return s;
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

	public final String getUnformattedText() {

		String s = comp.getUnformattedText();
		for (String e : TheBestIRCModEver.emotes) {
			s.replace(" " + e + " ", getTextForWidth(14) + "  ");
			if (s.startsWith(e))
				s.replaceFirst(e, getTextForWidth(14));
			if (s.endsWith(e))
				s = s.substring(0, s.length() - e.length() - 1);
		}

		return s;
	}

	@SideOnly(Side.CLIENT)
	public final String getFormattedText() {
		String s = comp.getFormattedText();
		for (String e : TheBestIRCModEver.emotes) {
			s.replace(" " + e + " ", getTextForWidth(14) + "  ");
			if (s.startsWith(e))
				s.replaceFirst(e, getTextForWidth(14));
			if (s.endsWith(e))
				s = s.substring(0, s.length() - e.length() - 1);
		}

		return s;
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
		return "EmoteComponent{style=" + this.style + ", siblings=" + this.siblings + '}';
	}

	public String getChatComponentText_TextValue() {
		return this.getUnformattedTextForChat();
	}

	public ChatComponentEmote createCopy() {
		return new ChatComponentEmote(comp);
	}

}