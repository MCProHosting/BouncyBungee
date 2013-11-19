package com.mcprohosting.bouncybungee.servers;

import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * BouncyServerBeat! This stores the time that a server last sent a heartbeat call to BungeeCord. The handler is
 * responsible for removing servers that don't send a heartbeat often enough.
 */
public class BouncyServerBeat {
    /**
     * The Server that sent the ping.
     */
    @Getter private ServerInfo info;
    /**
     * The time the heartbeat was sent.
     */
    @Getter private Long timeHeartbeat;

    public BouncyServerBeat(ServerInfo info, Long timeHeartbeat) {
        this.info = info;
        this.timeHeartbeat = timeHeartbeat;
    }
    public ServerInfo getInfo() {
        return this.info;
    }
    public Long getTimeHeartbeat() {
        return this.timeHeartbeat;
    }
}
