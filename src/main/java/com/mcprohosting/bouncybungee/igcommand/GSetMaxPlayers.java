package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.io.IOException;

public class GSetMaxPlayers extends Command {

    public GSetMaxPlayers() {
        super("gsetmaxplayers");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.setmaxplayers")) {
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }

        NetCommand.withName("gsetmaxplayers")
                .withArg("maxplayers", strings[0])
                .withArg("sender", sender.getName()).send();
    }

    public static void setMaxPlayers(String maxPlayers) {
        try {
            BouncyBungee.getInstance().editConfigProperty("maxplayers", maxPlayers);
        } catch (IOException e) {
            BouncyBungee.getInstance().getLogger().warning("Failed to save maxplayers to settings.properties");
        }
    }

}
