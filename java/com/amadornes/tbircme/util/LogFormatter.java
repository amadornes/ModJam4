package com.amadornes.tbircme.util;

import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {
    
    @Override
    public String format(LogRecord r) {
    
        return "[" + r.getLoggerName() + " " + r.getLevel().getName() + "] " + r.getMessage() + "\r\n";
    }
    
    @Override
    public String getHead(Handler paramHandler) {
    
        return "";
    }
    
    @Override
    public String getTail(Handler paramHandler) {
    
        return "";
    }
    
}
