package com.amadornes.tbircme.emote;

import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.amadornes.tbircme.ModInfo;
import com.amadornes.tbircme.TheBestIRCModEver;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EmoteAPI {

	public static final void init() {
		loadGlobalEmotes();
		loadSubEmotes();
	}

	private static final void loadGlobalEmotes() {
		try {
			// URL u = new URL("http://www.twitchemotes.com/global.json");

			JsonObject emotes = (JsonObject) new JsonParser().parse(IOUtils.toString(Class.class
					.getResourceAsStream("/assets/" + ModInfo.MODID + "/emotes/global.json")));
			// Hardcode them to a file because they can't be loaded from the
			// website

			for (Entry<String, JsonElement> e : emotes.entrySet()) {
				String name = e.getKey();
				String url = ((JsonObject) e.getValue()).get("url").getAsString();
				TheBestIRCModEver.emotes.add(new Emote(name, url));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static final void loadSubEmotes() {
		try {
			// URL u = new URL("http://www.twitchemotes.com/subscriber.json");

			JsonObject channels = (JsonObject) new JsonParser().parse(IOUtils.toString(Class.class
					.getResourceAsStream("/assets/" + ModInfo.MODID + "/emotes/subscriber.json")));
			// Hardcode them to a file because they can't be loaded from the
			// website

			for (Entry<String, JsonElement> c : channels.entrySet()) {
				String channel = c.getKey();
				JsonObject emotes = (JsonObject) ((JsonObject) c.getValue()).get("emotes");
				for (Entry<String, JsonElement> e2 : emotes.entrySet()) {
					String emote = e2.getKey();
					String url = e2.getValue().getAsString();

					TheBestIRCModEver.emotes.add(new SubEmote(emote, channel, url));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
