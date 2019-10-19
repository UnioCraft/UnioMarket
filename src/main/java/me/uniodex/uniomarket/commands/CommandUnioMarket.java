package me.uniodex.uniomarket.commands;

import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnioMarket implements CommandExecutor {

    private UnioMarket plugin;

    public CommandUnioMarket(UnioMarket plugin) {
        this.plugin = plugin;
        plugin.getCommand("uniomarket").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("uniomarket.reload")) {
            plugin.reload();
            sender.sendMessage(plugin.getMessage("plugin.reloaded"));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("activatecratesfor") && sender.hasPermission("uniomarket.activate.others")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) return true;
            plugin.getActivationManager().activateCrates(player);
            sender.sendMessage(plugin.getMessage("activation.cratesActivatedFor").replaceAll("%player%", player.getName()));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("command.playerOnly"));
            return true;
        }
        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage(plugin.getMessage("command.uniomarket.wrongUsage"));
            return true;
        }

        if (args[0].equalsIgnoreCase("activate")) {
            String itemName = args[1];
            if (!plugin.getActivationManager().checkItemExist(itemName)) {
                player.sendMessage(plugin.getMessage("command.uniomarket.itemCouldntFind"));
                return true;
            }

            if (!plugin.getActivationManager().checkPlayerForItem(player.getName(), itemName)) {
                player.sendMessage(plugin.getMessage("activation.youHaveToBuyFirst"));
                return true;
            }

            if (!plugin.getActivationManager().isThereEnoughSpace(player, itemName)) {
                player.sendMessage(plugin.getMessage("activation.notEnoughSpace"));
                return true;
            }

            if (plugin.getActivationManager().activate(player.getName(), itemName)) {
                player.sendMessage(plugin.getMessage("activation.itemActivated").replaceAll("%item%", plugin.getActivationManager().getItemDisplayName(itemName)));
            } else {
                player.sendMessage(plugin.getMessage("activation.itemActivationFailed").replaceAll("%item%", plugin.getActivationManager().getItemDisplayName(itemName)));
            }
            return true;
        }

        sender.sendMessage(plugin.getMessage("command.uniomarket.wrongUsage"));
        return true;
    }
}
