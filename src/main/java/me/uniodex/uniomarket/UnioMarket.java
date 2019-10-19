package me.uniodex.uniomarket;

import lombok.Getter;
import me.uniodex.unioessentials.UnioEssentials;
import me.uniodex.unioessentials.managers.SQLManager;
import me.uniodex.uniomarket.commands.CommandCredits;
import me.uniodex.uniomarket.commands.CommandPoints;
import me.uniodex.uniomarket.commands.CommandUnioMarket;
import me.uniodex.uniomarket.enums.ServerType;
import me.uniodex.uniomarket.hooks.JobsHook;
import me.uniodex.uniomarket.hooks.PlaceholderAPIHook;
import me.uniodex.uniomarket.listeners.*;
import me.uniodex.uniomarket.managers.*;
import me.uniodex.uniomarket.managers.ConfigManager.Config;
import me.uniodex.uniomarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class UnioMarket extends JavaPlugin {

    @Getter
    private static ServerType serverType;
    @Getter
    private ActivationManager activationManager;
    @Getter
    private CreditsManager creditsManager;
    @Getter
    private LogManager logManager;
    @Getter
    private PointsManager pointsManager;
    @Getter
    private SQLManager sqlManager;
    @Getter
    private ConfigManager configManager;
    @Getter
    private JobsHook jobsHook;
    @Getter
    private UnioEssentials unioEssentials;

    public static String hataPrefix;
    public static String dikkatPrefix;
    public static String bilgiPrefix;
    public static String consolePrefix;

    public void onEnable() {
        // Config
        configManager = new ConfigManager(this);

        serverType = ServerType.valueOf(getConfig().getString("server").toUpperCase());
        initializePrefixes();

        // Plugins
        if ((unioEssentials = (UnioEssentials) Bukkit.getPluginManager().getPlugin("UnioEssentials")) == null) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Jobs")) {
            jobsHook = new JobsHook(this);
        }

        // Managers
        sqlManager = unioEssentials.getSqlManager();
        activationManager = new ActivationManager(this);
        creditsManager = new CreditsManager(this);
        pointsManager = new PointsManager(this);
        logManager = new LogManager(this);

        // Placeholders
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(this).register();
        }

        // Listeners
        new ActivationListeners(this);
        new LegacyListeners(this);
        new LogListeners(this);
        new PointsListeners(this);
        new ProtectionListeners(this);

        // Commands
        new CommandUnioMarket(this);
        new CommandCredits(this);
        new CommandPoints(this);
    }

    public void onDisable() {
        sqlManager.onDisable();
    }

    private void initializePrefixes() {
        bilgiPrefix = getMessage("prefix.bilgiPrefix");
        dikkatPrefix = getMessage("prefix.dikkatPrefix");
        hataPrefix = getMessage("prefix.hataPrefix");
        consolePrefix = getMessage("prefix.consolePrefix");
    }

    public void reload() {
        // TODO
        reloadConfig();
    }

    public String getMessage(String configSection) {
        if (configManager.getConfig(Config.LANG).getString(configSection) == null) return null;

        return Utils.colorizeMessage(configManager.getConfig(Config.LANG).getString(configSection));
    }

    public List<String> getMessages(String configSection) {
        if (configManager.getConfig(Config.LANG).getStringList(configSection) == null) return null;

        return Utils.colorizeMessages(configManager.getConfig(Config.LANG).getStringList(configSection));
    }
}
