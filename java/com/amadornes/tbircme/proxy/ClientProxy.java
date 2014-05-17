package com.amadornes.tbircme.proxy;

import com.amadornes.tbircme.render.RenderHandler;

import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenders() {
		MinecraftForge.EVENT_BUS.register(new RenderHandler());
	}
	
}
