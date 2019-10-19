package me.uniodex.uniomarket.managers;

import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogManager {

    private UnioMarket plugin;

    public LogManager(UnioMarket plugin) {
        this.plugin = plugin;
    }

    public void logPlace(Player player, String itemName) {
        logInfo(plugin.getMessage("logging.place")
                .replaceAll("%player%", player.getName())
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", ""))
                .replaceAll("%location%", Utils.getStringLocation(player.getLocation())));
    }

    public void logBreak(Player player, String itemName) {
        logInfo(plugin.getMessage("logging.break")
                .replaceAll("%player%", player.getName())
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", ""))
                .replaceAll("%location%", Utils.getStringLocation(player.getLocation())));
    }

    public void logPickup(Player player, String itemName) {
        logInfo(plugin.getMessage("logging.pickup")
                .replaceAll("%player%", player.getName())
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", ""))
                .replaceAll("%location%", Utils.getStringLocation(player.getLocation())));
    }

    public void logDrop(Player player, String itemName) {
        logInfo(plugin.getMessage("logging.drop")
                .replaceAll("%player%", player.getName())
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", ""))
                .replaceAll("%location%", Utils.getStringLocation(player.getLocation())));
    }

    public void logDeathDrop(Player player, String itemName) {
        logInfo(plugin.getMessage("logging.deathdrop")
                .replaceAll("%player%", player.getName())
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", ""))
                .replaceAll("%location%", Utils.getStringLocation(player.getLocation())));
    }

    public void logTradeResult(String player1, String player2, String itemName) {
        logInfo(plugin.getMessage("logging.trade")
                .replaceAll("%player1%", player1)
                .replaceAll("%player2%", player2)
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", "")));
    }

    public void logItemActivationSuccess(String player, String itemName) {
        logInfo(plugin.getMessage("logging.activationSuccess")
                .replaceAll("%player%", player)
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", "")));
    }

    public void logItemActivationError(String player, String itemName, String reason) {
        logError(plugin.getMessage("logging.activationError")
                .replaceAll("%player%", player)
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", "")));
    }

    public void logIllegalItem(String player, String itemName) {
        logError(plugin.getMessage("logging.illegalItem")
                .replaceAll("%player%", player)
                .replaceAll("%item%", ChatColor.stripColor(itemName).replaceAll("§:", "").replaceAll("§;", "")));
    }

    public void logCreditDeposit(String player, Integer amount) {
        plugin.getSqlManager().updateSQL("INSERT INTO `genel`.`krediLogs` (`id`, `player`, `islem`, `kredi`, `tarih`) VALUES (NULL, '" + player + "', '" + Utils.capitalize(UnioMarket.getServerType().toString().toLowerCase()) + " Çek Bozdurma', '" + amount + "', '" + (System.currentTimeMillis() / 1000) + "');");
        logInfo(plugin.getMessage("logging.creditDeposited")
                .replaceAll("%player%", player)
                .replaceAll("%amount%", String.valueOf(amount)));
    }

    public void logCreditWithdraw(String player, Integer amount) {
        plugin.getSqlManager().updateSQL("INSERT INTO `genel`.`krediLogs` (`id`, `player`, `islem`, `kredi`, `tarih`) VALUES (NULL, '" + player + "', '" + Utils.capitalize(UnioMarket.getServerType().toString().toLowerCase()) + " Çek Alma', '" + amount + "', '" + (System.currentTimeMillis() / 1000) + "');");
        logInfo(plugin.getMessage("logging.creditWithdrawn")
                .replaceAll("%player%", player)
                .replaceAll("%amount%", String.valueOf(amount)));
    }

    public void logCreditDepositFailure(String player, Integer amount) {
        logError(plugin.getMessage("logging.creditDepositFailed")
                .replaceAll("%player%", player)
                .replaceAll("%amount%", String.valueOf(amount)));
    }

    public void logCreditWithdrawFailure(String player, Integer amount) {
        logError(plugin.getMessage("logging.creditWithdrawFailed")
                .replaceAll("%player%", player)
                .replaceAll("%amount%", String.valueOf(amount)));
    }

    public void logInfo(String message) {
        String messageToLog = "[" + Utils.getTimeAsHours() + "] " + message;

        try {
            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/logs/" + Utils.getTimeAsYearMonthDay() + ".info.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(messageToLog);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logError(String message) {
        String messageToLog = "[" + Utils.getTimeAsHours() + "] " + message;

        try {
            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/logs/" + Utils.getTimeAsYearMonthDay() + ".error.log");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(messageToLog);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
