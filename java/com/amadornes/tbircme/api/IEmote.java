package com.amadornes.tbircme.api;

import java.io.File;
import java.net.URL;

public interface IEmote {
    
    public String getName();
    
    public URL getURL();
    
    public File getCached();
    
    public void downloadIfNeeded();
    
    public void bindTexture();
    
    public int getWidth();
    
    public int getHeight();
    
    public String[] getTooltip();
    
}
