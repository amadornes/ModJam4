package com.amadornes.tbircme.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.amadornes.tbircme.network.packet.PacketGUI;
import com.amadornes.tbircme.ref.ModInfo;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class NetworkHandler {
    
    public static final SimpleNetworkWrapper NETWORK_WRAPPER              = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MODID);
    private static int                       lastDiscriminator = 0;
    
    public static void init() {
    
        registerPacket(PacketGUI.class, Side.CLIENT);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerPacket(Class packetHandler, Class packetType, Side side) {
    
        NETWORK_WRAPPER.registerMessage(packetHandler, packetType, lastDiscriminator++, side);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void registerPacket(Class packetType, Side side) {
    
        NETWORK_WRAPPER.registerMessage(packetType, packetType, lastDiscriminator++, side);
    }
    
    public static void sendToAll(IMessage message) {
    
        NETWORK_WRAPPER.sendToAll(message);
    }
    
    public static void sendTo(IMessage message, EntityPlayerMP player) {
    
        NETWORK_WRAPPER.sendTo(message, player);
    }
    
    public static void sendToAllAround(LocatedPacket message, World world, double range) {
    
        sendToAllAround(message, message.getTargetPoint(world, range));
    }
    
    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
    
        NETWORK_WRAPPER.sendToAllAround(message, point);
    }
    
    public static void sendToDimension(IMessage message, int dimensionId) {
    
        NETWORK_WRAPPER.sendToDimension(message, dimensionId);
    }
    
    public static void sendToServer(IMessage message) {
    
        NETWORK_WRAPPER.sendToServer(message);
    }
    
}
