package com.amadornes.tbircme.ref;

public class Names {
    
    private static final String BASE     = ModInfo.MODID + ".";
    
    public static final String  MOD_NAME = BASE + "name";
    
    public static class GuiNames {
        
        private static final String BASE           = Names.BASE + "gui.";
        
        /*
         * Main GUI
         */
        
        public static final String  MAIN_TITLE     = BASE + "title";
        
        public static final String  EMOTES_ENABLE  = BASE + "emotes.enable";
        public static final String  EMOTES_DISABLE = BASE + "emotes.disable";
        
        public static final String  PAUSE_ENABLE   = BASE + "pause.enable";
        public static final String  PAUSE_DISABLE  = BASE + "pause.disable";
        
        /*
         * Client settings
         */
        
        public static final String  CLIENT_TITLE   = BASE + "client.title";
        
        /*
         * Server settings
         */
        
        public static final String  SERVER_TITLE   = BASE + "server.title";
        
        /*
         * Channel settings
         */
        
        public static final String  CHANNELS_TITLE = BASE + "channels.title";
        
        /*
         * Server settings
         */
        
        public static final String  SERVERS_TITLE  = BASE + "servers.title";
        
        public static class Buttons {
            
            public static final String ADD     = Names.GuiNames.BASE + "buttons.add";
            public static final String REMOVE  = Names.GuiNames.BASE + "buttons.remove";
            public static final String SAVE    = Names.GuiNames.BASE + "buttons.save";
            public static final String DISCARD = Names.GuiNames.BASE + "buttons.discard";
            
        }
        
    }
    
    public static class Status {
        
        private static final String BASE         = Names.BASE + "status.";
        
        public static final String  CONNECTED    = BASE + "connected";
        public static final String  DISCONNECTED = BASE + "disconnected";
        public static final String  CONNECTING   = BASE + "connecting";
        
    }
    
}
