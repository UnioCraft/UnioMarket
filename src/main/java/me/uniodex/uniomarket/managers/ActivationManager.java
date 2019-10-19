package me.uniodex.uniomarket.managers;

import me.uniodex.unioessentials.enums.SQLAction;
import me.uniodex.unioessentials.objects.DatabaseInfo;
import me.uniodex.unioessentials.objects.SQLProcess;
import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.objects.ActivatableItem;
import me.uniodex.uniomarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivationManager {

    private UnioMarket plugin;

    private Map<String, DatabaseInfo> databaseInfos = new HashMap<>();
    private Map<String, ActivatableItem> activatableItems = new HashMap<>();

    public ActivationManager(UnioMarket plugin) {
        this.plugin = plugin;

        initializeDatabaseInfos();
        initializeActivatableItems();
    }

    private void initializeDatabaseInfos() {
        ConfigurationSection localDatabaseInfos = plugin.getConfig().getConfigurationSection("databaseInfos");
        for (String databaseInfo : localDatabaseInfos.getKeys(false)) {
            String dbName = plugin.getConfig().getString("databaseInfos." + databaseInfo + ".dbName");
            String tableName = plugin.getConfig().getString("databaseInfos." + databaseInfo + ".tableName");
            String usernameColumn = plugin.getConfig().getString("databaseInfos." + databaseInfo + ".usernameColumn");

            databaseInfos.put(databaseInfo, new DatabaseInfo(databaseInfo, dbName, tableName, usernameColumn));
        }
    }

    private void initializeActivatableItems() {
        ConfigurationSection localActivatableItems = plugin.getConfig().getConfigurationSection("items");
        for (String itemName : localActivatableItems.getKeys(false)) {
            String displayName = plugin.getConfig().getString("items." + itemName + ".displayName");
            String databaseInfoName = plugin.getConfig().getString("items." + itemName + ".database");
            DatabaseInfo databaseInfo = databaseInfos.get(databaseInfoName);
            String itemColumn = plugin.getConfig().getString("items." + itemName + ".itemColummn");
            int spaceNeeded = plugin.getConfig().getInt("items." + itemName + ".spaceNeeded");
            List<String> rewardCommands = plugin.getConfig().getStringList("items." + itemName + ".rewardCommands");

            activatableItems.put(itemName, new ActivatableItem(itemName, displayName, databaseInfo, itemColumn, spaceNeeded, rewardCommands));
        }
    }

    public boolean checkItemExist(String item) {
        return activatableItems.containsKey(item);
    }

    public boolean checkPlayerForItem(String player, String item) {
        if (!checkItemExist(item)) return false;

        ActivatableItem activatableItem = activatableItems.get(item);
        return checkPlayerForItem(player, activatableItem);
    }

    private boolean checkPlayerForItem(String player, ActivatableItem item) {
        DatabaseInfo databaseInfo = item.getDatabaseInfo();

        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.GET, 1, player, databaseInfo.getDbName(), databaseInfo.getTableName(), item.getItemColumn(), databaseInfo.getUsernameColumn(), false);
        int amount = sqlProcess.get();
        return amount >= 1;
    }

    public boolean isThereEnoughSpace(Player player, String item) {
        if (checkItemExist(item)) {
            return isThereEnoughSpace(player, activatableItems.get(item));
        }
        return false;
    }

    private boolean isThereEnoughSpace(Player player, ActivatableItem item) {
        return Utils.isThereEnoughSpace(player.getInventory().getContents(), item.getSpaceNeeded());
    }

    public boolean activate(String player, String item) {
        // TODO test with random item name
        return activate(player, activatableItems.get(item));
    }

    private boolean activate(String player, ActivatableItem item) {
        return activate(Bukkit.getPlayer(player), item);
    }

    private boolean activate(Player player, ActivatableItem item) {
        if (player == null || item == null) {
            return false;
        }

        if (!checkPlayerForItem(player.getName(), item)) {
            return false;
        }

        if (!isThereEnoughSpace(player, item)) {
            return false;
        }

        DatabaseInfo databaseInfo = item.getDatabaseInfo();
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.REMOVE, 1, player.getName(), databaseInfo.getDbName(), databaseInfo.getTableName(), item.getItemColumn(), databaseInfo.getUsernameColumn(), false);
        if (sqlProcess.isSuccessful()) {
            for (String command : item.getRewardCommands()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("%player%", player.getName()));
            }
            plugin.getLogManager().logItemActivationSuccess(player.getName(), item.getDisplayName());
            return true;
        }
        plugin.getLogManager().logItemActivationError(player.getName(), item.getDisplayName(), "SQL error");
        return false;
    }

    public String getItemDisplayName(String item) {
        if (activatableItems.containsKey(item)) {
            return activatableItems.get(item).getDisplayName();
        }
        return null;
    }

    public DatabaseInfo getDatabaseInfo(String name) {
        return databaseInfos.get(name);
    }

    public void activateCrates(Player player) {
        int activated = 0;

        for (String crate : plugin.getConfig().getStringList("crates")) {
            while (plugin.getActivationManager().checkPlayerForItem(player.getName(), crate)) {
                plugin.getActivationManager().activate(player.getName(), crate);
                activated++;
            }
        }

        if (activated > 0) {
            player.sendMessage(plugin.getMessage("activation.cratesActivated"));
        }
    }
}
