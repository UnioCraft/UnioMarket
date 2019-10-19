package me.uniodex.uniomarket.commands;

import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandCredits implements CommandExecutor {

    private UnioMarket plugin;
    private List<String> cooldowns = new ArrayList<>();

    public CommandCredits(UnioMarket plugin) {
        this.plugin = plugin;
        plugin.getCommand("credits").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessage("command.playerOnly"));
            return true;
        }

        Player player = (Player) sender;
        if (checkCooldown(player.getName())) {
            player.sendMessage(plugin.getMessage("command.credits.cooldown"));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("al")) {
            int amount = 0;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.getMessage("command.credits.wrongNumber"));
                return true;
            }

            if (amount > 100 || amount < 1) {
                player.sendMessage(plugin.getMessage("command.credits.mustBeBetween1and100"));
                return true;
            }

            if (!Utils.isThereEnoughSpace(player.getInventory().getContents(), 1)) {
                player.sendMessage(plugin.getMessage("activation.notEnoughSpace"));
                return true;
            }

            if (plugin.getCreditsManager().getCredit(player.getName()) < amount) {
                addCooldown(player.getName());
                player.sendMessage(plugin.getMessage("command.credits.notEnoughCredit"));
                return true;
            }

            if (plugin.getCreditsManager().takeCredit(player.getName(), amount)) {
                player.getInventory().addItem(plugin.getCreditsManager().getCreditCheck(player.getName(), amount));
                plugin.getLogManager().logCreditWithdraw(player.getName(), amount);
                player.sendMessage(plugin.getMessage("command.credits.success"));
                return true;
            } else {
                addCooldown(player.getName());
                player.sendMessage(plugin.getMessage("command.credits.failure"));
                plugin.getLogManager().logCreditWithdrawFailure(player.getName(), amount);
                return true;
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("bozdur")) {
            ItemStack itemInHand;
            try {
                itemInHand = player.getInventory().getItemInMainHand();
            } catch (Exception e) {
                itemInHand = player.getInventory().getItemInHand();
            }

            if (!plugin.getCreditsManager().isCreditCheck(itemInHand)) {
                player.sendMessage(plugin.getMessage("command.credits.mustBeCreditCheck"));
                return true;
            }

            Integer amount = plugin.getCreditsManager().getAmountFromCheck(itemInHand);

            if (itemInHand.getAmount() > 1) {
                itemInHand.setAmount(itemInHand.getAmount() - 1);
            } else {
                player.getInventory().remove(itemInHand);
            }

            if (plugin.getCreditsManager().giveCredit(player.getName(), amount)) {
                addCooldown(player.getName());
                player.sendMessage(plugin.getMessage("command.credits.deposited").replaceAll("%amount%", String.valueOf(amount)));
                plugin.getLogManager().logCreditDeposit(player.getName(), amount);
                return true;
            } else {
                addCooldown(player.getName());
                player.sendMessage(plugin.getMessage("command.credits.depositFailed"));
                plugin.getLogManager().logCreditDepositFailure(player.getName(), amount);
                return true;
            }
        }

        player.sendMessage(plugin.getMessage("command.credits.usage").replaceAll("%credit%", String.valueOf(plugin.getCreditsManager().getCredit(player.getName()))));
        return true;
    }

    private boolean checkCooldown(String player) {
        return cooldowns.contains(player);
    }

    private void addCooldown(String player) {
        cooldowns.add(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            cooldowns.remove(player);
        }, 200L);
    }
}
