package com.amadornes.tbircme;

import java.io.File;

public class Constants {
    
    public static final String LOC_BASE                   = "tbircme";
    
    public static final String LOC_EXCEPTION_LEVEL_INFO   = LOC_BASE + ".exception.level.info";
    public static final String LOC_EXCEPTION_LEVEL_ERROR  = LOC_BASE + ".exception.level.error";
    public static final String LOC_EXCEPTION_LEVEL_SEVERE = LOC_BASE + ".exception.level.severe";
    
    public static final String LOC_EXCEPTION_SAME_ID      = LOC_BASE + ".exception.sameid";
    
    public static final File   CONFIG_FOLDER              = new File("config/tbircme");
    public static final File   CONFIG_FILE_MAIN           = new File(CONFIG_FOLDER, "tbircme.cfg");
    public static final File   CONFIG_SERVERS_FOLDER      = new File(CONFIG_FOLDER, "servers");
    
    public static final File   EMOTE_FOLDER               = new File("tbircme/emotes");
    
}
