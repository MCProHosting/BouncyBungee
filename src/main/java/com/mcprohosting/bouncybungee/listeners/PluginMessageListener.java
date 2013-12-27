package com.mcprohosting.bouncybungee.listeners;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PluginMessageListener implements Listener {

    @EventHandler
    public void onMessageReceived(PluginMessageEvent event) throws IOException {
        if (event.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
            String channel = in.readUTF();

            if (channel.equalsIgnoreCase("Connect") == true) {
                String server = in.readUTF();

                if (ProxyServer.getInstance().getServers().containsKey(server) == false) {
                    event.setCancelled(true);

                    ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
                    player.sendMessage("The server is currently unavailable. Try again soon.");
                }
            }
        }
    }

}
