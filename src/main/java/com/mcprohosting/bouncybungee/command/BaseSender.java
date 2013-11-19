package com.mcprohosting.bouncybungee.command;

import net.md_5.bungee.api.event.PostLoginEvent;
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
    public void onPlayerJoin(PostLoginEvent event) {
        NetCommand.
                withName("join").
                withArg("player", event.getPlayer().getName()).
                withArg("server", event.getPlayer().getServer().getInfo().getName())
                .send();
    }
}
