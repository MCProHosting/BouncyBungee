package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class ModifyNetMOTD {

    @NetCommandHandler(args = {"motd", "sender"}, name = "gsetmotd")
    public void onSetMaxPlayers(HashMap<String, Object> maxPlayersData) {
        GSetMOTD.setMOTD((String[]) maxPlayersData.get("motd"));
    }

}
