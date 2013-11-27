package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommandHandler;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Joey
 * Date: 11/26/13
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class AlertNet {
    @NetCommandHandler(args = {"alert", "sender"}, name = "galert")
    public void onAlert(HashMap<String, Object> alertData) {
        GAlertCommand.broadcastAlert((String) alertData.get("alert"));
    }
}
