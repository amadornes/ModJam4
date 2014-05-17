package com.amadornes.tbircme.util;

import java.net.URL;
import java.net.URLConnection;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EmoteAPI {

	private static JsonObject emotes;

	public static final void init() {
		try {
			URL u = new URL("http://twitchemotes.com/global.json");
			URLConnection con = u.openConnection();
			con.connect();
			emotes = (JsonObject) new JsonParser().parse(IOUtils.toString(con.getInputStream()));

			for (Entry<String, JsonElement> e : emotes.entrySet()) {
				String name = e.getKey();
				String url = ((JsonObject) e.getValue()).get("url").getAsString();
				TheBestIRCModEver.emotes.add(new Emote(name, url));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

}
