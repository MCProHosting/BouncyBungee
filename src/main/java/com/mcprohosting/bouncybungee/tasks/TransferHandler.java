package com.mcprohosting.bouncybungee.tasks;

import com.mcprohosting.bouncybungee.BouncyBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Collection;

public class TransferHandler implements ScheduledTask, Runnable {

    public static boolean transferInProgress = false;
    public static TransferHandler handler;
    public static ArrayList<ProxiedPlayer> players = null;

    private String destination;

    public TransferHandler(Collection<ProxiedPlayer> list, String destination) {
        if (transferInProgress) {
            handler.cancel();
        }

        transferInProgress = true;
        handler = this;
        players = new ArrayList<>(list);

        this.destination = destination;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Plugin getOwner() {
        return BouncyBungee.getInstance();
    }

    @Override
    public Runnable getTask() {
        return this;
    }

    @Override
    public void cancel() {
        transferInProgress = false;
        handler = null;
        players = null;
    }

    @Override
    public void run() {
        ProxiedPlayer p;

        if (players == null) {
            this.cancel();
            return;
        }

        if (players.isEmpty()) {
            this.cancel();
            return;
        }

        p = players.get(0);

        p.connect(ProxyServer.getInstance().getServers().get(destination));
        p.sendMessage(ChatColor.GREEN + "You've been transferred to another server by an admin!");

        players.remove(p);
    }
}
