package com.amadornes.tbircme.util;

import k4unl.minecraft.colorchat.lib.User;
import k4unl.minecraft.colorchat.lib.Users;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Utils {
    
    public static String formatForIRC(String string) {
    
        string = string.replace(EnumChatFormatting.BOLD + "", "\u0002");
        string = string.replace(EnumChatFormatting.UNDERLINE + "", "\u0002");
        string = string.replace(EnumChatFormatting.ITALIC + "", "\u0002");
        string = string.replace(EnumChatFormatting.RESET + "", "\u000F");
        string = string.replace("\u00a7", "\u00030A");
        
        return string;
    }
    
    public static String getUsernameForIRC(EntityPlayer player) {
    
        return formatForIRC(getUsername(player));
    }
    
    public static String getUsername(EntityPlayer player) {
    
        if (Loader.isModLoaded("colorchat")) {
            String usr = getUsernameColorChat(player);
            if (usr != null) return usr;
        }
        return player.getGameProfile().getName();
    }
    
    @Optional.Method(modid = "colorchat")
    public static String getUsernameColorChat(EntityPlayer player) {
    
        User u = Users.getUserByName(player.getGameProfile().getName());
        
        if (u.hasNick()) return "~" + u.getNick() + EnumChatFormatting.RESET;
        
        return null;
    }
    
    public static boolean isLocal() {
    
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) { return isLocal(true); }
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    private static boolean isLocal(boolean unused) {
    
        return MinecraftServer.getServer() instanceof IntegratedServer;
    }
    
}
