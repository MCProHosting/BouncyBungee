package com.mcprohosting.bouncybungee;

import com.mcprohosting.bouncybungee.servers.BouncyServerBeatHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 11/19/13
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class MOTDFeature implements Listener {
    @EventHandler
    public void onServerPing(ProxyPingEvent event) {
        event.setResponse(new ServerPing(
                ProxyServer.getInstance().getProtocolVersion(),
                ProxyServer.getInstance().getGameVersion(),
                event.getResponse().getMotd(), //Keep this the same.
                BouncyServerBeatHandler.getPlayersOnline(),
                BouncyServerBeatHandler.getPlayersOnline()+1
        ));
    }
}
