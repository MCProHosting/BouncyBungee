package com.mcprohosting.bouncybungee.servers;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommandHandler;
import net.md_5.bungee.BungeeServerInfo;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * This will listen for the heartbeat command from the other servers to keep servers in sync across all BungeeCord instances.
 * This will require the Bukkit plugin to be on the server end, along with a proper public facing IP, name, and port to
 * be sent from the Bukkit plugin (based on configuration and the Bukkit port, with configuration overrides)
 */
public class BouncyServerHandler {
    @NetCommandHandler(name = "heartbeat", args = {"name", "ip", "port"})
    public void onHeartbeat(HashMap<String, Object> args) {
        Object i = args.get("ip");
        Object n = args.get("name");
        Object p = args.get("port");
        /*
        Why get this? Because you don't want to do HashMap.get more than you have to. You can't be sure which type the
        object is, thus you assign it to "Object" as a type, and then check if these are instances of the right types,
        and then cast the Object to the appropriate type.

        This may be improved in the future, instead catching the Cast Exception while attempting to cast the direct
        result of HashMap.get
         */
        if (!(i instanceof String) ||
                !(n instanceof String) ||
                !(p instanceof Integer))
            return;
        String ip = (String)i;
        String name = (String)n;
        Integer port = (Integer)p;
        InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(name);
        if (serverInfo != null) {
            if (serverInfo.getAddress().equals(socketAddress)) return;
            disconnectAll(serverInfo, BouncyBungee.getInstance().getFormat("updating-routing",false));
        }
        BungeeServerInfo info = new BungeeServerInfo(name, socketAddress, BouncyBungee.getInstance().getFormat("default-motd", false), false);
        ProxyServer.getInstance().getServers().put(name, info);
        BouncyBungee.getInstance().getBeatHandler().heartbeatRecieved(info);
    }
    @NetCommandHandler(name = "disconnect", args = {"name"})
    public void onDisconnect(HashMap<String, Object> args) {
        Object n = args.get("name");
        if (!(n instanceof String)) return;
        String name = (String)n;
        disconnectAll(ProxyServer.getInstance().getServerInfo(name), BouncyBungee.getInstance().getFormat("updating-routing",false));
        ProxyServer.getInstance().getServers().remove(name);
    }

    /**
     * Disconnect all players from the server.
     * @param info The server to disconnect players from.
     * @param dcMessage The message to disconnect with.
     */
    public static void disconnectAll(ServerInfo info, String dcMessage) {
        for (ProxiedPlayer player : info.getPlayers()) {
            player.disconnect(dcMessage);
        }
    }
}
