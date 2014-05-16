package com.amadornes.tbircme.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.exception.REAlreadyConnected;
import com.amadornes.tbircme.exception.REErrorConnecting;
import com.amadornes.tbircme.exception.RENotConnected;
import com.amadornes.tbircme.exception.RENullHost;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IRCConnection {

	private String host = null;

	private Socket socket = null;
	private BufferedReader r = null;
	private BufferedWriter w = null;

	private String nick = null;

	private boolean shouldDisconnect = false;

	private boolean connected = false;
	
	private List<String> channels = new ArrayList<String>();

	public IRCConnection(String host, String nick) {
		if (host == null)
			throw new RENullHost();
		this.host = host;
		this.nick = nick;
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void connect() {
		if (!isConnected()) {
			String h = host.contains(":") ? host.substring(0, host.indexOf(":")) : host;
			int p = host.contains(":") ? Integer.parseInt(host.substring(host.indexOf(":"))) : 6667;

			TheBestIRCModEver.log.log(Level.INFO, "Connecting to " + h + " on port " + p + "!");

			try {
				socket = new Socket(h, p);

				r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				// Clear incoming messages
				{
					boolean received = false;
					String line = "";
					while ((line = r.readLine()) != null && (r.ready() || !received)) {
						System.out.println("RECEIVED!!!! " + line);
						received = true;
					}
				}

				boolean tryAgain = false;
				int id = 0;
				do {
					sendRaw("NICK " + nick + (id > 0 ? id : ""), true, false);
					sendRaw("USER " + nick + " 8 * : The Best IRC Mod Ever!");

					tryAgain = false;

					String line = "";
					while ((line = r.readLine()) != null && r.ready()) {
						if (line.indexOf("433") >= 0) {
							tryAgain = true;
						}
						if (line.indexOf("004") >= 0) {
							connected = true;
							break;
						}
					}
				} while (tryAgain && id < 100);
				TheBestIRCModEver.log.log(Level.INFO, "Connected as: " + nick + (id > 0 ? id : ""));

				// Start listening for pings, messages, etc...
				startConnectionTicker();
			} catch (Exception e) {
				throw new REErrorConnecting(h, p);
			}
		} else {
			throw new REAlreadyConnected();
		}
	}

	public void waitUntilConnected() {
		while (!connected)
			Thread.yield();
	}

	public void disconnect() {
		shouldDisconnect = true;
	}

	public boolean isConnected() {
		return socket != null && !socket.isClosed();
	}

	public void sendRaw(String str) {
		sendRaw(str, true, true);
	}

	public void sendRaw(String str, boolean carriageReturn, boolean flush) {
		try {
			String s = str + (carriageReturn ? "\r\n" : "");
			w.write(s);
			if (flush)
				w.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void join(String channel) {
		if (!isConnected())
			throw new RENotConnected();

		sendRaw("JOIN #" + channel);
		if(!channels.contains(channel.toLowerCase()))
			channels.add(channel.toLowerCase());
	}

	public void part(String channel) {
		if (!isConnected())
			throw new RENotConnected();

		sendRaw("PART #" + channel);
		if(channels.contains(channel.toLowerCase()))
			channels.remove(channel.toLowerCase());
	}

	public void chat(String channel, String message) {
		if (!isConnected())
			throw new RENotConnected();

		sendRaw("PRIVMSG #" + channel + " :" + message);
	}

	private void onMessage(String s) {

		TheBestIRCModEver.log.log(Level.INFO, "[IRC] " + s);

		if (s.startsWith("PING")) {
			onPing(s);
			return;
		}
		if (s.indexOf("004") >= 0 && !connected) {
			connected = true;
			return;
		}
		if (s.indexOf(" PRIVMSG ") >= 0) {
			String st = s.substring(s.indexOf(" PRIVMSG ") + " PRIVMSG ".length());
			String sender = s.substring(s.indexOf(":") + 1, s.indexOf("!"));
			String channel = st.substring(0, st.indexOf(" "));
			String msg = st.substring(st.indexOf(" ") + 2);

			if (channel.startsWith("#"))
				onChatMessage(channel.substring(1), sender, msg);
		}
	}

	protected void onPing(String s) {
		// Send PONG with the received args
		sendRaw(s.replace("PING", "PONG"));
	}

	protected void onChatMessage(String channel, String sender, String message) {
		if (MinecraftServer.getServer() != null && MinecraftServer.getServer().isServerRunning()) {
			for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
				EntityPlayer p = (EntityPlayer) o;
				p.addChatMessage(new ChatComponentText("[" + channel + "] <" + sender + "> " + message));
			}
		}
	}

	@SubscribeEvent
	public void onIngameChat(ServerChatEvent ev) {
		for(String c : channels){
			chat(c, "<" + ev.username + "> " + ev.message);
		}
	}

	private void startConnectionTicker() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (!shouldDisconnect) {
					if (isConnected()) {
						String line = "";
						try {
							while ((line = r.readLine()) != null) {
								onMessage(line);
							}
						} catch (IOException e) {
						}
						if (shouldDisconnect) {
							shouldDisconnect = false;
							sendRaw("QUIT");
							try {
								socket.close();
							} catch (IOException e) {
							}
						}
					}
				}
			}
		}, "IRC Connection - " + host).start();
	}

	public String getHost() {
		return host;
	}

}
