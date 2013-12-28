package com.mcprohosting.bouncybungee.igcommand;

import com.mcprohosting.bouncybungee.BouncyBungee;
import com.mcprohosting.bouncybungee.command.NetCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class GBan extends Command {

    public GBan() {
        super("gban");
    }

    @Override
    public void execute(CommandSender sender, String[] strings) {
        if (!sender.hasPermission("ssundee.banplayer")) {
            sender.sendMessage(ChatColor.RED + "You can't perform this command!");
            return;
        }

        NetCommand.withName("gban")
                .withArg("ban", strings[0])
                .withArg("sender", sender.getName()).send();
    }

    public static void banPlayer(String player) {
        BouncyBungee.getInstance().addPlayerToBans(player);
    }
}
