package com.amadornes.tbircme.exception;

import net.minecraft.client.resources.I18n;

import com.amadornes.tbircme.Constants;

public enum IRCExceptionLevel {
    
    INFO, //
    ERROR, //
    SEVERE;
    
    public String localize() {
    
        switch (this) {
            case INFO:
                return I18n.format(Constants.LOC_EXCEPTION_LEVEL_INFO);
            case ERROR:
                return I18n.format(Constants.LOC_EXCEPTION_LEVEL_ERROR);
            case SEVERE:
                return I18n.format(Constants.LOC_EXCEPTION_LEVEL_SEVERE);
        }
        return "";
    }
}
