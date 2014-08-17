package com.amadornes.tbircme.emote;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.TextureUtil;

import org.apache.commons.io.FileUtils;
import org.lwjgl.opengl.GL11;

import com.amadornes.tbircme.Constants;
import com.amadornes.tbircme.api.IEmote;
import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

public class Emote implements IEmote {
    
    private String        name;
    private URL           url;
    private File          cached;
    private BufferedImage bi;
    private int           textureId = -1;
    
    public Emote(String name, String url) {
    
        this.name = name;
        try {
            this.url = new URL(url);
        } catch (Exception e) {
        }
        cached = new File(Constants.EMOTE_FOLDER, name + ".png");
    }
    
    @Override
    public String getName() {
    
        return name;
    }
    
    @Override
    public URL getURL() {
    
        return url;
    }
    
    @Override
    public File getCached() {
    
        return cached;
    }
    
    @Override
    public void downloadIfNeeded() {
    
        boolean shouldDownload = false;
        if (!getCached().exists()) {
            shouldDownload = true;
        } else {
            try {
                HashCode file = Files.hash(getCached(), Hashing.md5());
                HashCode online = Hashing.md5().newHasher().putString(getURL().getContent().toString(), Charsets.UTF_8).hash();
                if (!file.equals(online)) shouldDownload = true;
            } catch (IOException e) {
                shouldDownload = true;
            }
        }
        if (getCached().exists()) getCached().delete();
        
        if (!shouldDownload) return;
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
            
                try {
                    FileUtils.copyURLToFile(getURL(), getCached());
                    bi = ImageIO.read(getURL());
                    textureId = TextureUtil.uploadTextureImage(GL11.glGenTextures(), bi);
                } catch (Exception e) {
                }
            }
        }).start();
    }
    
    @Override
    public void bindTexture() {
    
        if (textureId >= 0) GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }
    
    @Override
    public int getWidth() {
    
        if (bi == null) return 14;
        
        return bi.getWidth();
    }
    
    @Override
    public int getHeight() {
    
        if (bi == null) return 14;
        
        return bi.getHeight();
    }
    
    @Override
    public String[] getTooltip() {
    
        return new String[] { getName() };
    }
    
}
