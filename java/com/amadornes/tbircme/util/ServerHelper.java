package com.amadornes.tbircme.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class ServerHelper {
    
    public static EntityPlayer getPlayer(String player) {
    
        for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList)
            if (((EntityPlayer) o).getDisplayName().equalsIgnoreCase(player)) return (EntityPlayer) o;
        return null;
    }
    
    public static World getWorld(int dimId) {
    
        for (World w : MinecraftServer.getServer().worldServers)
            if (w.provider.dimensionId == dimId) return w;
        
        return null;
    }
    
}
