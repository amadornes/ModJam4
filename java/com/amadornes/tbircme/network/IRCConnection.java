package com.amadornes.tbircme.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import com.amadornes.tbircme.TheBestIRCModEver;
import com.amadornes.tbircme.compat.bukkit.BukkitStuff;
import com.amadornes.tbircme.exception.REAlreadyConnected;
import com.amadornes.tbircme.exception.REErrorConnecting;
import com.amadornes.tbircme.exception.RENullHost;
import com.amadornes.tbircme.permissions.User;
import com.amadornes.tbircme.util.Config;
import com.amadornes.tbircme.util.Util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class IRCConnection {

	private Server server;

	private String host = null;
	private String password = null;
	private String nickservPass = null;

	private Socket socket = null;
	private BufferedReader r = null;
	private BufferedWriter w = null;

	private String nick = null;

	private boolean shouldDisconnect = false;

	private boolean connected = false;

	private IRCCommandSender commandSender;

	public IRCConnection(Server server, final String host, String nick, String password,
			String nickservPass) {
		if (host == null)
			throw new RENullHost();
		this.server = server;
		this.host = host;
		this.nick = nick;
		this.password = password;
		this.nickservPass = nickservPass;

		commandSender = new IRCCommandSender(this, host);

		// Subscribe to chat events
		if (Util.isMCPCInstalled()) {
			BukkitStuff.waitUntilEnabled();
			BukkitStuff.registerChatEventsForConnection(this);
		} else {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	public Server getServer() {
		return server;
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

				if (password != null && password.trim().length() != 0) {
					sendRaw("PASS " + password);
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

	public void ragequit() {
		if (!isConnected())
			return;

		sendRaw("QUIT");
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	public boolean isConnected() {
		return socket != null && !socket.isClosed();
	}

	public void sendRaw(String str) {
		sendRaw(str, true, true);
	}

	public void cmd(String cmd) {

	}

	public void sendRaw(String str, boolean carriageReturn, boolean flush) {
		if (!isConnected())
			return;
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
			return;

		sendRaw("JOIN #" + channel);
		if (!getChannels().contains(channel.toLowerCase()))
			getChannels().add(new Channel(server, channel.toLowerCase()));
	}

	public void join(Channel channel) {
		if (!isConnected())
			return;

		sendRaw("JOIN #" + channel);
		if (!getChannels().contains(channel))
			getChannels().add(channel);
	}

	public void part(String channel) {
		if (!isConnected())
			return;

		sendRaw("PART #" + channel);
		if (getChannels().contains(channel.toLowerCase()))
			getChannels().remove(channel.toLowerCase());
	}

	public void chat(String channel, String message) {
		if (!isConnected())
			return;

		sendRaw("PRIVMSG #" + channel + " :" + message);
	}

	public void broadcast(String message) {
		for (Channel c : getChannels()) {
			chat(c.getChannel(), message);
		}
	}

	private void onMessage(final String s) {
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

			if (channel.startsWith("#")) {
				if (MinecraftServer.getServer() != null
						&& MinecraftServer.getServer().isServerRunning()) {
					if (msg.startsWith("!")) {
						onCommand(channel.substring(1), sender, msg.substring(1));
					} else {
						onChatMessage(channel.substring(1), sender, msg);
					}
				}
			}
			return;
		}

		if (s.indexOf(" KICK ") == s.indexOf(" ")) {
			if (s.substring(0, s.lastIndexOf(":")).contains(nick)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
						}
						String st = s.substring(s.indexOf(" ") + " KICK ".length());
						join(st.substring(1, st.indexOf(" ")));
					}
				}).start();
			} else {
				String who = s.substring(s.indexOf(" ") + 1);
				who = s.substring(s.indexOf(" ") + 1);
				who = s.substring(s.indexOf(" ") + 1, s.indexOf(" :"));
				if (server.shouldShowIRCParts()) {
					for (Channel c : getChannels()) {
						chat(c.getChannel(), c.formatIRCConnection(who, false));
					}
				}
			}
			return;
		}

		if (s.indexOf(" MODE ") == s.indexOf(" ")) {
			if (!s.contains(nick)) {
				System.out.println("MODE: " + s);
				String st = s.substring(s.indexOf(" ") + 1);
				st = st.substring(st.indexOf(" ") + 1);

				String ch = st.substring(0, st.indexOf(" "));
				if (!ch.startsWith("#"))
					return;
				ch = ch.substring(1);
				Channel channel = null;
				for (Channel c : getChannels())
					if (c.getChannel().equalsIgnoreCase(ch))
						channel = c;
				st = st.substring(st.indexOf(" ") + 1);
				String perm = st.substring(0, st.indexOf(" "));
				st = st.substring(st.indexOf(" ") + 1);
				String who = st;

				User u = channel.getUser(who);
				for (int i = 0; i < perm.length(); i += 2) {
					String mod = perm.substring(i, i + 1);
					String p = perm.substring(i + 1, i + 2);

					boolean state = mod == "+";
					if (p.equals("v"))
						u.setVoice(state);
					if (p.equals("o"))
						u.setOp(state);
				}
			}

			return;
		}

		if (s.indexOf(" JOIN ") == s.indexOf(" ") || s.indexOf(" PART ") == s.indexOf(" ")) {
			String st = s.substring(s.indexOf(" ") + 1);
			st = st.substring(st.indexOf(" ") + 1);

			String ch = st.substring(1, st.contains(" ") ? st.indexOf(" ") : st.length());
			Channel channel = null;
			for (Channel c : getChannels())
				if (c.getChannel().equalsIgnoreCase(ch))
					channel = c;
			String who = s.substring(s.indexOf(":") + 1, s.indexOf("!"));
			if (who.equalsIgnoreCase(nick))
				return;
			User u = channel.getUser(who);

			if (s.indexOf(" JOIN ") == s.indexOf(" ")) {
				u.setOnline(true);
				if (server.shouldShowIRCJoins())
					for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
						EntityPlayer p = (EntityPlayer) o;
						p.addChatMessage(new ChatComponentText(channel.formatIRCConnection(who,
								true)));
					}
			}
			if (s.indexOf(" PART ") == s.indexOf(" ")) {
				u.setOnline(false);
				if (server.shouldShowIRCParts())
					for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
						EntityPlayer p = (EntityPlayer) o;
						p.addChatMessage(new ChatComponentText(channel.formatIRCConnection(who,
								false)));
					}
			}

			return;
		}

		if (s.indexOf(" 353 ") == s.indexOf(" ")) {
			String st = s.substring(s.indexOf(" ") + 1);
			st = st.substring(st.indexOf(" ") + 1);
			st = st.substring(st.indexOf(" ") + 1);
			st = st.substring(st.indexOf(" ") + 1);

			String ch = st.substring(1, st.contains(" ") ? st.indexOf(" ") : st.length());
			Channel channel = null;
			for (Channel c : getChannels())
				if (c.getChannel().equalsIgnoreCase(ch))
					channel = c;
			st = st.substring(st.indexOf(" ") + 1);
			StringTokenizer strt = new StringTokenizer(st, " ");
			while (strt.hasMoreTokens()) {
				String us = strt.nextToken();
				boolean op = us.contains("@");
				boolean voice = us.contains("+");

				String username = us.substring(Math.max(0,
						Math.max(us.indexOf("@") + 1, us.indexOf("+") + 1)));
				User u = new User(channel, username, true, op, voice);

				channel.addUser(u);
			}
		}

		if (s.indexOf(" NICK ") == s.indexOf(" ")) {
			String st = s.substring(s.indexOf(" ") + 1);
			st = st.substring(st.indexOf(" ") + 1);

			String nick = st.substring(1, st.contains(" ") ? st.indexOf(" ") : st.length());
			String who = s.substring(s.indexOf(":"), s.indexOf("!"));

			for (Channel c : getChannels())
				c.getUser(who).setUsername(nick);

			return;
		}

		if (s.indexOf(" 474 ") == s.indexOf(" ")) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
					String st = s.substring(s.indexOf(" ") + (" 474 " + nick + " ").length());
					join(st.substring(1, st.indexOf(" ")));
				}
			}).start();
			return;
		}
	}

	protected void onPing(String s) {
		// Send PONG with the received args
		sendRaw(s.replace("PING", "PONG"));
	}

	protected void onChatMessage(String channel, String sender, String message) {
		Channel c = null;
		for (Channel ch : getChannels())
			if (ch.getChannel().equalsIgnoreCase(channel))
				c = ch;

		String msg = c.formatIRCMessage(sender, message);
		if (message.trim().startsWith("ACTION ") && !message.startsWith("ACTION ")) {
			msg = c.formatIRCEmote(sender, message);
		}

		for (Object o : MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
			EntityPlayer p = (EntityPlayer) o;
			p.addChatMessage(new ChatComponentText(msg));
		}
	}

	@SubscribeEvent
	public void onIngameChat(ServerChatEvent ev) {
		onIngameChat(ev.username, ev.message);
	}

	public void onIngameChat(String username, String message) {
		for (Channel c : getChannels())
			chat(c.getChannel(), c.formatChatMessage(username, message));
	}

	@SubscribeEvent
	public void onIngameCommand(CommandEvent ev) {
		if (ev.command.getCommandName().equalsIgnoreCase("me")) {
			String msg = "";
			for (String s : ev.parameters)
				msg += s + " ";
			msg = msg.trim();
			for (Channel c : getChannels())
				chat(c.getChannel(), c.formatChatEmote(ev.sender.getCommandSenderName(), msg));
		}
	}

	public void onPlayerJoin(String p) {
		if (server.shouldShowIngameJoins())
			for (Channel c : getChannels())
				chat(c.getChannel(), c.formatChatConnection(p, true));
	}

	public void onPlayerLeave(String p) {
		if (server.shouldShowIngameParts())
			for (Channel c : getChannels())
				chat(c.getChannel(), c.formatChatConnection(p, false));
	}

	public void onPlayerDie(String p, LivingDeathEvent ev) {
		try {
			if (server.shouldShowDeaths())
				for (Channel c : getChannels())
					chat(c.getChannel(), c.formatDeath(ev.source.func_151519_b(ev.entityLiving)
							.getUnformattedTextForChat()));
		} catch (Exception ex) {
		}
	}

	protected void onCommand(String channel, String sender, String command) {
		if (command.toLowerCase().startsWith(Config.command.toLowerCase())) {
			String realCommand = command.substring(Config.command.length()).trim();

			List<String> arguments = new ArrayList<String>();
			if (realCommand.indexOf(" ") > 0) {
				StringTokenizer st = new StringTokenizer(realCommand.substring(
						realCommand.indexOf(" ")).trim(), " ");
				while (st.hasMoreTokens())
					arguments.add(st.nextToken());
			}

			String cmd = realCommand.contains(" ") ? realCommand.substring(0,
					realCommand.indexOf(" ")).trim() : realCommand.trim();
			String[] args = arguments.toArray(new String[] {});

			arguments.clear();

			ICommandManager manager = MinecraftServer.getServer().getCommandManager();
			for (Object o : manager.getCommands().values()) {
				ICommand c = (ICommand) o;
				boolean is = false;
				if (c.getCommandName().equalsIgnoreCase(cmd))
					is = true;
				if (!is)
					if (c.getCommandAliases() != null)
						for (Object o2 : c.getCommandAliases()) {
							String s = (String) o2;
							if (s.equalsIgnoreCase(cmd)) {
								is = true;
								break;
							}
						}
				if (is) {
					try {
						Channel ch = null;
						for (Channel c2 : getChannels())
							if (c2.getChannel().equalsIgnoreCase(channel))
								ch = c2;

						if (!ch.getUser(sender).canRunCommand(command)) {
							chat(channel,
									sender
											+ " > ERROR! You don't have the permission to run this command! < ");
							break;
						}

						commandSender.setSender("#" + channel);
						c.processCommand(commandSender, args);
					} catch (Exception ex) {
						chat(channel, sender + " > ERROR! " + ex.getMessage() + " < ");
					}
					break;
				}
			}
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

	public List<Channel> getChannels() {
		return server.getChannels();
	}

}
