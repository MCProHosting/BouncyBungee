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

        NetCommand.withName("gsetmotd")
                .withArg("motd", strings);
    }

    public static void setMOTD(String[] motd) {
        String message = "";
        for (String string : motd) {
            message += string + " ";
        }
        message.trim();

        try {
            BouncyBungee.getInstance().editConfigProperty("motd", message);
        } catch (IOException e) {
            BouncyBungee.getInstance().getLogger().warning("Failed to save motd to settings.properties");
            e.printStackTrace();
        }
    }

}
