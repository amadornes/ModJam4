package com.amadornes.tbircme.exception;


public class IRCException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    private String            message          = "";
    private String            title            = "";
    private IRCExceptionLevel level            = IRCExceptionLevel.ERROR;
    
    public IRCException(String message) {
    
        this(message, IRCExceptionLevel.ERROR);
    }
    
    public IRCException(String message, String title) {
    
        this(message, IRCExceptionLevel.ERROR);
    }
    
    public IRCException(String message, IRCExceptionLevel level) {
    
        this(message, level, level.localize());
    }
    
    public IRCException(String message, IRCExceptionLevel level, String title) {
    
        this.message = message;
        this.level = level;
        this.title = title;
    }
    
    @Override
    public String getMessage() {
    
        return title + ": " + message;
    }
    
    public String getTitle() {
    
        return title;
    }
    
    public IRCExceptionLevel getLevel() {
    
        return level;
    }
    
}
