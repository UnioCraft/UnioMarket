package me.uniodex.uniomarket.managers;

import me.uniodex.unioessentials.enums.SQLAction;
import me.uniodex.unioessentials.objects.DatabaseInfo;
import me.uniodex.unioessentials.objects.SQLProcess;
import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.managers.ConfigManager.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CreditsManager {

    private UnioMarket plugin;

    // Hacky Stuff
    private String creditCheckStartsWith;

    public CreditsManager(UnioMarket plugin) {
        this.plugin = plugin;

        creditCheckStartsWith = plugin.getConfigManager().getConfig(Config.LANG).getString("items.creditCheck.name");
        creditCheckStartsWith = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', creditCheckStartsWith)).split("%amount%")[0];
    }

    public boolean isCreditCheck(ItemStack item) {
        return item != null ||
                item.getType().equals(Material.PAPER) ||
                item.getItemMeta().getDisplayName() != null ||
                item.getItemMeta().getDisplayName().startsWith(creditCheckStartsWith);
    }

    public int getAmountFromCheck(ItemStack item) {
        return Integer.valueOf(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceAll("[^\\d]", ""));
    }

    public ItemStack getCreditCheck(String player, int amount) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(plugin.getMessage("items.creditCheck.name")
                .replaceAll("%amount%", String.valueOf(amount)));
        List<String> lore = plugin.getMessages("items.creditCheck.lore");
        lore.replaceAll(s -> s.replaceAll("%player%", player).replaceAll("%amount%", String.valueOf(amount)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public boolean giveCredit(String player, int amount) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("credit.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.ADD, amount, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("credit.creditColumnName"), databaseInfo.getUsernameColumn(), true);
        return sqlProcess.isSuccessful();
    }

    public boolean takeCredit(String player, int amount) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("credit.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.REMOVE, amount, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("credit.creditColumnName"), databaseInfo.getUsernameColumn(), false);
        return sqlProcess.isSuccessful();
    }

    public int getCredit(String player) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("credit.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.GET, 0, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("credit.creditColumnName"), databaseInfo.getUsernameColumn(), false);
        return sqlProcess.get();
    }
}
