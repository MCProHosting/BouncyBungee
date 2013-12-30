package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommand;
import com.mcprohosting.bouncybungee.tasks.TransferHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;

public class GTransfer extends Command {

    public GTransfer() {
        super("gtransfer");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.transfer")) {
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }

        if (strings.length != 2) {
            sender.sendMessage(ChatColor.RED + "You must specify a source and target server.");
            return;
        }

        boolean foundSource = false;
        boolean foundTarget = false;
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            if (info.getName().equalsIgnoreCase(strings[0])) {
                foundSource = true;
            }

            if (info.getName().equalsIgnoreCase(strings[1])) {
                foundTarget = true;
            }
        }

        if (foundSource == false) {
            sender.sendMessage(ChatColor.RED + "Invalid source server.");
            return;
        }

        if (foundTarget == false) {
            sender.sendMessage(ChatColor.RED + "Invalid target server.");
            return;
        }

        NetCommand.withName("gtransfer")
                .withArg("source", strings[0])
                .withArg("target", strings[1])
                .withArg("sender", sender.getName()).send();
        sender.sendMessage(ChatColor.GREEN + "Transferring players from source server to target server!");
    }

    public static void transferPlayer(String source, String target) {
        ServerInfo info = ProxyServer.getInstance().getServerInfo(source);

        ProxyServer.getInstance().getScheduler().schedule(BouncyBungee.getInstance(),
                new TransferHandler(info.getPlayers(), target), 1000, 50, TimeUnit.MILLISECONDS);
    }

}
