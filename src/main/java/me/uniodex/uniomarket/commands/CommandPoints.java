package me.uniodex.uniomarket.commands;

import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPoints implements CommandExecutor {

    private UnioMarket plugin;

    public CommandPoints(UnioMarket plugin) {
        this.plugin = plugin;
        plugin.getCommand("points").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("uniomarket.points.control") || args.length == 0) {
                player.sendMessage(plugin.getMessage("command.points.see").replaceAll("%points%", String.valueOf(plugin.getPointsManager().getPoints(player.getName()))));
                return true;
            }
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("gör")) {
                String playerToSee = args[1];
                sender.sendMessage(plugin.getMessage("command.points.seeother").replaceAll("%player%", playerToSee).replaceAll("%points%", String.valueOf(plugin.getPointsManager().getPoints(playerToSee))));
                return true;
            }
        }

        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("ver")) {
                String playerToGive = args[1];
                Integer pointsToGive = Integer.valueOf(args[2]);
                if (plugin.getPointsManager().givePoints(playerToGive, pointsToGive)) {
                    sender.sendMessage(plugin.getMessage("command.points.given").replaceAll("%player%", playerToGive).replaceAll("%points%", String.valueOf(pointsToGive)));
                } else {
                    sender.sendMessage(plugin.getMessage("command.points.failed"));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("al")) {
                String playerToTake = args[1];
                Integer pointsToTake = Integer.valueOf(args[2]);
                if (plugin.getPointsManager().givePoints(playerToTake, pointsToTake)) {
                    sender.sendMessage(plugin.getMessage("command.points.taken").replaceAll("%player%", playerToTake).replaceAll("%points%", String.valueOf(pointsToTake)));
                } else {
                    sender.sendMessage(plugin.getMessage("command.points.failed"));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("ayarla")) {
                String playerToSet = args[1];
                Integer pointsToSet = Integer.valueOf(args[2]);
                if (plugin.getPointsManager().givePoints(playerToSet, pointsToSet)) {
                    sender.sendMessage(plugin.getMessage("command.points.set").replaceAll("%player%", playerToSet).replaceAll("%points%", String.valueOf(pointsToSet)));
                } else {
                    sender.sendMessage(plugin.getMessage("command.points.failed"));
                }
                return true;
            }
        }

        sender.sendMessage(plugin.getMessage("command.points.usage"));
        return true;
    }
}
