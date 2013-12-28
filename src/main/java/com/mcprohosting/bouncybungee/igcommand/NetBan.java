package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class NetBan {

    @NetCommandHandler(args = {"ban", "sender"}, name = "gban")
    public void onAlert(HashMap<String, Object> alertData) {
        GBan.banPlayer((String) alertData.get("ban"));
    }

}
