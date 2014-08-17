package com.amadornes.tbircme.util;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amadornes.tbircme.ref.ModInfo;

public class Log {
    
    private static class LevelCrash extends Level {
        
        private static final long serialVersionUID = 1L;
        
        protected LevelCrash() {
        
            super("CRASH", 10000);
        }
        
        @Override
        public String getName() {
        
            return "CRASH";
        }
        
    }
    
    private static Map<String, Logger> loggers = new HashMap<String, Logger>();
    
    private static Level               crash   = new LevelCrash();
    
    private static boolean             debug   = false;
    
    public static final void crash(Exception exception, String msg, boolean debug, Object... extra) {
    
        String[] ex = StringUtils.exceptionToString(exception);
        log(crash, msg);
        for (String s : ex) {
            log(crash, s, debug);
        }
        for (int i = 0; i < extra.length; i += 2) {
            try {
                log(crash, extra[i] + "=" + extra[i + 1].toString());
            } catch (Exception e) {
            }
        }
    }
    
    public static final void crash(Exception exception, String msg, Object... extra) {
    
        crash(exception, msg, true, extra);
    }
    
    public static final void disableDebug() {
    
        debug = false;
    }
    
    public static final void enableDebug() {
    
        debug = true;
    }
    
    public static final void info(String msg) {
    
        log(Level.INFO, msg);
    }
    
    public static final void info(String msg, boolean debug) {
    
        log(Level.INFO, msg, debug);
    }
    
    public static final boolean isDebugEnabled() {
    
        return debug;
    }
    
    public static final void log(Level level, String msg) {
    
        log(level, msg, false);
    }
    
    public static final void log(Level lvl, String msg, boolean debug) {
    
        Logger l = null;
        
        String name;
        
        if (debug) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            String clazz = "";
            int i = 3;
            do {
                clazz = stackTraceElements[i].getClassName();
                i++;
            } while (clazz.equals(Log.class.getName()));
            
            clazz = clazz.substring("com.vertex.".length());
            if (clazz.contains("$")) {
                clazz = clazz.substring(0, clazz.indexOf("$"));
            }
            
            if (isDebugEnabled()) {
                name = clazz;
            } else {
                if (clazz.contains(".")) {
                    clazz = clazz.substring(0, clazz.lastIndexOf("."));
                } else {
                    clazz = "";
                }
                name = StringUtils.convertPacketToString(clazz);
            }
        } else {
            name = ModInfo.MODID;
        }
        
        try {
            l = loggers.get(name);
        } catch (Exception ex) {
        }
        if (l == null) {
            l = Logger.getLogger(name == ModInfo.MODID ? name : (ModInfo.MODID + (name.trim().length() == 0 ? "" : "|" + name)));
            loggers.put(name, l);
            l.setUseParentHandlers(false);
            ConsoleHandler handler = new ConsoleHandler();
            handler.setFormatter(new LogFormatter());
            l.addHandler(handler);
        }
        
        if (debug && !isDebugEnabled()) { return; }
        
        l.log(lvl, msg);
    }
    
    public static final void severe(String msg) {
    
        log(Level.SEVERE, msg);
    }
    
    public static final void severe(String msg, boolean debug) {
    
        log(Level.SEVERE, msg, debug);
    }
    
    public static final void warning(String msg) {
    
        log(Level.WARNING, msg);
    }
    
    public static final void warning(String msg, boolean debug) {
    
        log(Level.WARNING, msg, debug);
    }
    
}
