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

        if (strings.length != 1) {
            sender.sendMessage(ChatColor.RED + "You must specify a server to teleport players to!");
            return;
        }

        boolean found = false;
        for (ServerInfo info : ProxyServer.getInstance().getServers().values()) {
            if (info.getName().equalsIgnoreCase(strings[0])) {
                found = true;
            }
        }

        if (found == false) {
            sender.sendMessage(ChatColor.RED + "That is not a valid server.");
            return;
        }

        NetCommand.withName("gtransfer")
                .withArg("transfer", strings[0])
                .withArg("sender", sender.getName()).send();
        sender.sendMessage(ChatColor.GREEN + "Transferring players in your server to specified server!");
    }

    public static void transferPlayer(String sender, String destination) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(sender);
        Server server = player.getServer();

        ProxyServer.getInstance().getScheduler().schedule(BouncyBungee.getInstance(),
                new TransferHandler(server.getInfo().getPlayers(), destination), 1000, 50, TimeUnit.MILLISECONDS);
    }

}
