package me.uniodex.uniomarket.listeners;

import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class ProtectionListeners implements Listener {

    private UnioMarket plugin;

    public ProtectionListeners(UnioMarket plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void on(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack leftItem = anvilInventory.getItem(0);
        if (leftItem != null && leftItem.getType().equals(Material.PAPER)) {
            event.setResult(new ItemStack(Material.AIR));
            for (HumanEntity player : event.getViewers()) {
                player.sendMessage(plugin.getMessage("protections.youCantPutPaperToAnvil"));
            }
        }
    }
}
