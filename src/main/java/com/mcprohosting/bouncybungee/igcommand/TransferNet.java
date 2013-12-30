package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class TransferNet {
    @NetCommandHandler(args = {"source", "target", "sender"}, name = "gtransfer")
    public void onTransfer(HashMap<String, Object> alertData) {
        GTransfer.transferPlayer((String) alertData.get("source"), (String) alertData.get("target"));
    }
}
