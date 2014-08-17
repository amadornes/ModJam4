package com.amadornes.tbircme.api;

public class IRCMode {
    
    public static final int OP    = 0xF0;
    public static final int VOICE = 0x0F;
    
    public static boolean isOp(int value) {
    
        return hasMode(value, OP);
    }
    
    public static boolean hasVoice(int value) {
    
        return hasMode(value, VOICE);
    }
    
    public static boolean hasMode(int value, int mode) {
    
        return Math.abs(value & Math.abs(mode)) > 0;
    }
    
}
