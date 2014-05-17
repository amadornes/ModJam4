package com.amadornes.tbircme.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.amadornes.tbircme.network.Server;
import com.amadornes.tbircme.util.Config;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class EventHandler {

	private List<String> players = new ArrayList<String>();

	@SubscribeEvent
	public void onTick(ServerTickEvent ev) {
		List<String> nPlayers = new ArrayList<String>();

		if (ev.side.isServer()) {
			for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
				EntityPlayer p = (EntityPlayer) o;
				nPlayers.add(p.getCommandSenderName());
			}

			// Loop through the old list
			for (String p : players) {
				// If the new one doesn't contain that player it means that
				// he/she left
				if (!nPlayers.contains(p))
					for (Server s : Config.servers)
						if (s.shouldShowIngameParts())
							s.getConnection().onPlayerLeave(p);
			}
			// Loop through the new list
			for (String p : nPlayers) {
				// If the old one doesn't contain that player it means that
				// he/she joined
				if (!players.contains(p))
					for (Server s : Config.servers)
						if (s.shouldShowIngameJoins())
							s.getConnection().onPlayerJoin(p);
			}

			players.clear();
			players.addAll(nPlayers);
			nPlayers.clear();
			nPlayers = null;
		}
	}

	@SubscribeEvent
	public void onPlayerDie(LivingDeathEvent ev) {
		if (ev.entity instanceof EntityPlayer) {
			for (Server s : Config.servers)
				if (s.shouldShowDeaths())
					s.getConnection().onPlayerDie(ev.entity.getCommandSenderName(), ev);
		}
	}

}
