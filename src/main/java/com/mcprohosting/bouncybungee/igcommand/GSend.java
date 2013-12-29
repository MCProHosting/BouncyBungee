package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.command.NetCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class GSend extends Command {

    public GSend() {
        super("gsend");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.gsend")) {
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }

        NetCommand.withName("gsend")
                .withArg("message", strings);
    }

    public static void sendMessage(String[] strings) {
        String message = "";
        for (String string : strings) {
            message += string + " ";
        }
        message.trim();

        for (ProxiedPlayer p : ProxyServer.getInstance().getPlayers()) {
            if (p.getServer().getInfo().getName().toLowerCase().contains("hub") ||
                    p.getServer().getInfo().getName().toLowerCase().contains("lobby")) {
                p.sendMessage(message);
            }
        }
    }
}
