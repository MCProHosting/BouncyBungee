package com.mcprohosting.bouncybungee.servers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

/**
 * BouncyServerBeat! This stores the time that a server last sent a heartbeat call to BungeeCord. The handler is
 * responsible for removing servers that don't send a heartbeat often enough.
 */
@AllArgsConstructor
@ToString(of = {"info", "players"})
public class BouncyServerBeat {
    /**
     * The Server that sent the ping.
     */
    @Getter @NonNull private ServerInfo info;
    /**
     * The time the heartbeat was sent.
     */
    @Getter @NonNull private Long timeHeartbeat;
    /**
     * Players.
     */
    @Getter @NonNull private List<String> players;
}
