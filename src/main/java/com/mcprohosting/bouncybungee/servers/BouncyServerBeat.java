package com.mcprohosting.bouncybungee.servers;

import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;

/**
 * BouncyServerBeat! This stores the time that a server last sent a heartbeat call to BungeeCord. The handler is
 * responsible for removing servers that don't send a heartbeat often enough.
 */
@RequiredArgsConstructor
public class BouncyServerBeat {
    /**
     * The Server that sent the ping.
     */
    @Getter @NotNull private ServerInfo info;
    /**
     * The time the heartbeat was sent.
     */
    @Getter @NotNull private Long timeHeartbeat;
}
