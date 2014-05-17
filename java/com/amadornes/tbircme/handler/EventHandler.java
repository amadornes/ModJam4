package com.amadornes.tbircme.handler;

import java.util.ArrayList;
import java.util.List;

import com.amadornes.tbircme.Config;
import com.amadornes.tbircme.network.Server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	
	private List<String> players = new ArrayList<String>();

	@SubscribeEvent
	public void onPlayerJoin(EntityJoinWorldEvent ev) {
		if(ev.entity instanceof EntityPlayer){
			EntityPlayer p = ((EntityPlayer)ev.entity);
			if(!players.contains(p.getCommandSenderName())){
				players.add(p.getCommandSenderName());
				for(Server s : Config.servers)
					s.getConnection().onPlayerJoin(ev);
			}
		}
	}

}
