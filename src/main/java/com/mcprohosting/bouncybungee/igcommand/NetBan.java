package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class NetBan {

    @NetCommandHandler(args = {"ban", "sender"}, name = "gban")
    public void onBan(HashMap<String, Object> banData) {
        GBan.banPlayer((String) banData.get("ban"));
    }

}
