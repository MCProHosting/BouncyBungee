package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

public class TransferNet {
    @NetCommandHandler(args = {"transfer", "sender"}, name = "gtransfer")
    public void onTransfer(HashMap<String, Object> alertData) {
        GTransfer.transferPlayer((String) alertData.get("sender"), (String) alertData.get("transfer"));
    }
}
