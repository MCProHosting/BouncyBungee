package com.mcprohosting.bouncybungee.command;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommandHandler;
import com.mcprohosting.bouncybungee.event.BouncyJoinEvent;
import com.mcprohosting.bouncybungee.event.BouncyLeaveEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

/**
 * Base Receiver, gets all incoming NetCommand calls from this base plugin.
 */
public class BaseReceiver {
    @NetCommandHandler(args = {"player", "server"}, name = "send")
    public void onSend(HashMap<String, Object> args) {
        Object p = args.get("player");
        Object s = args.get("server");
        if (!(p instanceof String) || !(s instanceof String)) return;
        String player = (String)p;
        String server = (String)s;
        ProxiedPlayer player1 = ProxyServer.getInstance().getPlayer(player);
        if (player1 == null) return;
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);
        if (serverInfo == null) {
            player1.sendMessage(BouncyBungee.getInstance().getFormat("server-not-online"));
            return;
        }
        if (player1.getServer().getInfo().getName().equals(server)) {
            player1.sendMessage(BouncyBungee.getInstance().getFormat("already-connected"));
            return;
        }
        player1.sendMessage
                (BouncyBungee.getInstance().getFormat("connecting", true, true, new String[]{"<server>", server}));
        player1.connect(serverInfo);
    }
    @NetCommandHandler(args = {"player", "server"}, name = "join")
    public void onJoin(HashMap<String, Object> args) {
        Object p = args.get("player");
        Object s = args.get("s");
        if (!(p instanceof String) || !(s instanceof String)) return;
        BouncyJoinEvent event = new BouncyJoinEvent((String)p, (String)s);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
    }
    @NetCommandHandler(args = {"player"}, name = "quit")
    public void onLeave(HashMap<String, Object> args) {
        Object p = args.get("player");
        if (!(p instanceof String)) return;
        BouncyLeaveEvent event = new BouncyLeaveEvent((String)p);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
    }
}
