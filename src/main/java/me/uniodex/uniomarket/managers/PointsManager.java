package me.uniodex.uniomarket.managers;

import me.uniodex.unioessentials.enums.SQLAction;
import me.uniodex.unioessentials.objects.DatabaseInfo;
import me.uniodex.unioessentials.objects.SQLProcess;
import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.managers.ConfigManager.Config;

import java.util.Date;

public class PointsManager {

    private UnioMarket plugin;

    public PointsManager(UnioMarket plugin) {
        this.plugin = plugin;
    }

    public boolean givePoints(String player, int amount) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("points.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.ADD, amount, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("points.pointColumnName"), databaseInfo.getUsernameColumn(), true);
        return sqlProcess.isSuccessful();
    }

    public boolean takePoints(String player, int amount) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("points.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.REMOVE, amount, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("points.pointColumnName"), databaseInfo.getUsernameColumn(), false);
        return sqlProcess.isSuccessful();
    }

    public boolean setPoints(String player, int amount) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("points.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.SET, amount, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("points.pointColumnName"), databaseInfo.getUsernameColumn(), true);
        return sqlProcess.isSuccessful();
    }

    public int getPoints(String player) {
        DatabaseInfo databaseInfo = plugin.getActivationManager().getDatabaseInfo(plugin.getConfig().getString("points.databaseName"));
        SQLProcess sqlProcess = new SQLProcess(plugin.getUnioEssentials(), SQLAction.GET, 0, player, databaseInfo.getDbName(), databaseInfo.getTableName(), plugin.getConfig().getString("points.pointColumnName"), databaseInfo.getUsernameColumn(), false);
        return sqlProcess.get();
    }

    public long getLastPaid(String player) {
        return plugin.getConfigManager().getConfig(Config.DAILYPOINTS).getLong(player);
    }

    public boolean isPaidToday(String player) {
        Date lastpaid = new Date(getLastPaid(player) * 1000);
        Date now = new Date();
        return lastpaid.getDay() == now.getDay() && lastpaid.getMonth() == now.getMonth() && lastpaid.getYear() == now.getYear();
    }

    public void setLastPaid(String player) {
        plugin.getConfigManager().getConfig(Config.DAILYPOINTS).set(player, (System.currentTimeMillis() / 1000));
        plugin.getConfigManager().saveConfig(Config.DAILYPOINTS);
    }

    public void payPlayer(String player) {
        givePoints(player, plugin.getConfig().getInt("points.pointsToGiveDaily"));
        setLastPaid(player);
    }
}
