package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.lang.Object;import java.lang.String;import java.util.HashMap;

public class NetSend {

    @NetCommandHandler(args = {"message"}, name = "gsend")
    public void onSend(HashMap<String, Object> sendData) {
        GSend.sendMessage((String) sendData.get("message"));
    }

}
