package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class GSetMOTD extends Command {

    public GSetMOTD() {
        super("gsetmotd");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.setmotd")) {
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }

        if (strings.length == 0) {
            sender.sendMessage(ChatColor.RED + "You must specify a message for the motd.");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s).append(" ");
        }

        NetCommand.withName("gsetmotd")
                .withArg("motd", builder.toString())
                .withArg("sender", sender.getName());
        sender.sendMessage(ChatColor.GREEN + "Setting the motd of the network!");
    }

    public static void setMOTD(String motd) {
        try {
            BouncyBungee.getInstance().editConfigProperty("motd", motd);
        } catch (IOException e) {
            BouncyBungee.getInstance().getLogger().warning("Failed to save motd to settings.properties");
            e.printStackTrace();
        }
    }

}
