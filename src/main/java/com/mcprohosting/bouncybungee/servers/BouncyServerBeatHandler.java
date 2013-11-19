package com.mcprohosting.bouncybungee.servers;

import com.mcprohosting.bouncybungee.BouncyBungee;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * BouncyServerBeatHandler is responsible for removing servers that are no longer sending their heartbeat NetCommands.
 */
public class BouncyServerBeatHandler implements Runnable {
    /**
     * The amount of time that a server has to send a heartbeat before it's removed.
     */
    private static final Long TIME_EXPIRE = TimeUnit.SECONDS.toMillis(30);
    /**
     * Stores previous heartbeats.
     */
    private Map<ServerInfo, BouncyServerBeat> heartbeats;

    /**
     * Creates a new handler, and schedules it in the BungeeCord scheduler.
     */
    public BouncyServerBeatHandler() {
        this.heartbeats = new HashMap<>();
        BouncyServerHandler serverHandler = new BouncyServerHandler();
        BouncyBungee.getInstance().getDispatch().registerNetCommands(serverHandler);
        schedule();
    }

    /**
     * Call this method when a server sends a heartbeat.
     * @param info The server that a heartbeat was received for.
     */
    public void heartbeatRecieved(ServerInfo info, List playerList) {
        ArrayList<String> players = new ArrayList<>();
        for (Object p : playerList) {
            if (!(p instanceof String)) continue;
            players.add((String) p);
        }
        this.heartbeats.put(info, new BouncyServerBeat(info, Calendar.getInstance().getTimeInMillis(), players));
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            BouncyServerBeat bouncyServerBeat = heartbeats.get(info);
            if (Calendar.getInstance().getTimeInMillis()-bouncyServerBeat.getTimeHeartbeat() > BouncyServerBeatHandler.TIME_EXPIRE) {
                ProxyServer.getInstance().getServers().remove(bouncyServerBeat.getInfo().getName());
                BouncyServerHandler.disconnectAll(bouncyServerBeat.getInfo(), BouncyBungee.getInstance().getFormat("updating-routing",false));
                this.heartbeats.remove(info);
            }
        }
        schedule();
    }

    /**
     * Reschedule this in the scheduler for execution.
     */
    private void schedule() {
        ProxyServer.getInstance().getScheduler().schedule(BouncyBungee.getInstance(), this, 30, TimeUnit.SECONDS);
    }

    /**
     * Get players online
     */
    public static Integer getPlayersOnline() {
        Collection<BouncyServerBeat> values = BouncyBungee.getInstance().getBeatHandler().heartbeats.values();
        Integer count = 0;
        for (BouncyServerBeat beat : values) {
            count = count + beat.getPlayers().size();
        }
        return count;
    }
}
