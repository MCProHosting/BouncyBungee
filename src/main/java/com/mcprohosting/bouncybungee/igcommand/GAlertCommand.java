package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

/**
 * #SloppyCode
 *
 * I have an annotation based command engine.
 *
 * I'll put it to use later.
 *
 * <3
 */
public class GAlertCommand extends Command {
    public GAlertCommand() {
        super("galert");
    }
    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.alert")){
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s).append(" ");
        }
        NetCommand.withName("galert").withArg("alert", builder.toString()).withArg("sender", sender.getName()).send();
        sender.sendMessage(ChatColor.GREEN + "Alert sent.");
    }
    public static void broadcastAlert(String alert) {
        ProxyServer.getInstance().broadcast(BouncyBungee.getInstance().getFormat("alert", true, true, new String[]{"<alert>", alert}));
    }
}
