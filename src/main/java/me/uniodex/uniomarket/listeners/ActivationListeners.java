package me.uniodex.uniomarket.listeners;

import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ActivationListeners implements Listener {

    private UnioMarket plugin;

    public ActivationListeners(UnioMarket plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        plugin.getActivationManager().activateCrates(event.getPlayer());
    }
}
