package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class ModifyNetMaxPlayers {

    @NetCommandHandler(args = {"maxplayers", "sender"}, name = "gsetmaxplayers")
    public void onSetMaxPlayers(HashMap<String, Object> maxPlayersData) {
        GSetMaxPlayers.setMaxPlayers((String) maxPlayersData.get("maxplayers"));
    }

}
