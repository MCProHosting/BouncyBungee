package com.mcprohosting.bouncybungee.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.md_5.bungee.api.plugin.Event;

@Data
@AllArgsConstructor
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class BouncyLeaveEvent extends Event {
    /**
     * The name of the player whom connected
     */
    private final String player;
}
