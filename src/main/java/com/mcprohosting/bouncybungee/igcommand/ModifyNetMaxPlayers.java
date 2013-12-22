package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class ModifyNetMaxPlayers {

    @NetCommandHandler(args = {"maxplayers", "sender"}, name = "gsetmaxplayers")
    public void onSetMaxPlayers(HashMap<String, Object> maxPlayersData) {

        try {
            Integer.parseInt((String) maxPlayersData.get("maxplayers"));
        } catch (NumberFormatException e) {
            BouncyBungee.getInstance().getLogger().warning("Invalid value specifed for maxplayers command.");
            return;
        }

        GSetMaxPlayers.setMaxPlayers((String) maxPlayersData.get("maxplayers"));
    }

}
