package com.mcprohosting.bouncybungee.command;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.servers.BouncyServerBeatHandler;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 11/18/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseSender implements Listener {
    @EventHandler
    public void onPlayerLogin(PreLoginEvent event) {
        if (BouncyBungee.getInstance().containsPlayer(event.getConnection().getName())) {
            event.setCancelReason("You have been banned from this network!");
            event.setCancelled(true);
            return;
        }

        if (BouncyServerBeatHandler.getPlayersOnline() >=
                Integer.parseInt((String) BouncyBungee.getInstance().getSettings().get("maxplayers"))) {
            event.setCancelReason("Our network is currently full, try again shortly!");
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        NetCommand.
                withName("join").
                withArg("player", event.getPlayer().getName()).
                send();
    }
    @EventHandler
    public void onPlayerLeave(PlayerDisconnectEvent event) {
        NetCommand.
                withName("quit").
                withArg("player", event.getPlayer().getName()).
                send();
    }
}
