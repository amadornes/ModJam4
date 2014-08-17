package com.amadornes.tbircme.emote;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.amadornes.tbircme.api.IEmote;
import com.amadornes.tbircme.ref.ModInfo;
import com.amadornes.tbircme.util.ChatComponentEmote;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EmoteApi {
    
    private static List<IEmote> emotes = new ArrayList<IEmote>();
    
    public static void init() {
    
        loadGlobalEmotes();
        loadSubEmotes();
    }
    
    private static final void loadGlobalEmotes() {
    
        try {
            // URL u = new URL("http://www.twitchemotes.com/global.json");
            
            InputStream is = EmoteApi.class.getResourceAsStream("/assets/" + ModInfo.MODID + "/emotes/global.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = "";
            String l = null;
            while ((l = br.readLine()) != null)
                s += l;
            br.close();
            is.close();
            JsonObject emotes = (JsonObject) new JsonParser().parse(s);
            // Hardcode them to a file because they can't be loaded from the
            // website
            
            for (Entry<String, JsonElement> e : emotes.entrySet()) {
                String name = e.getKey();
                String url = ((JsonObject) e.getValue()).get("url").getAsString();
                
                registerEmote(name, url);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static final void loadSubEmotes() {
    
        try {
            // URL u = new URL("http://www.twitchemotes.com/subscriber.json");
            
            InputStream is = EmoteApi.class.getResourceAsStream("/assets/" + ModInfo.MODID + "/emotes/subscriber.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = "";
            String l = null;
            while ((l = br.readLine()) != null)
                s += l;
            br.close();
            is.close();
            JsonObject channels = (JsonObject) new JsonParser().parse(s);
            // Hardcode them to a file because they can't be loaded from the
            // website
            
            for (Entry<String, JsonElement> c : channels.entrySet()) {
                String channel = c.getKey();
                JsonObject emotes = (JsonObject) ((JsonObject) c.getValue()).get("emotes");
                for (Entry<String, JsonElement> e2 : emotes.entrySet()) {
                    String emote = e2.getKey();
                    String url = e2.getValue().getAsString();
                    
                    registerSubEmote(emote, url, channel);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void registerEmote(IEmote emote) {
    
        if (emote == null) return;
        
        IEmote e = null;
        for (IEmote e2 : emotes)
            if (e2.getName().equals(emote.getName())) {
                e = e2;
                break;
            }
        if (e != null) emotes.remove(e);
        
        emotes.add(emote);
    }
    
    public static void registerEmote(String name, String url) {
    
        registerEmote(new Emote(name, url));
    }
    
    public static void registerSubEmote(String name, String url, String channel) {
    
        registerEmote(new EmoteSub(name, url, channel));
    }
    
    public static List<Object> lookForEmotes(String string) {
    
        List<Object> l = new ArrayList<Object>();
        
        String s = "";
        for (int i = 0; i < string.length(); i++) {
            IEmote found = null;
            for (IEmote e : emotes) {
                if (string.substring(i).indexOf(e.getName()) == 0 && (found == null || e.getName().length() > found.getName().length())) {
                    found = e;
                }
            }
            if (found != null) {
                l.add(s);
                s = "";
                l.add(new ChatComponentEmote(found));
                i += found.getName().length() - 1;
            } else {
                s += string.substring(i, i + 1);
            }
        }
        l.add(s);
        
        return l;
    }
    
}
