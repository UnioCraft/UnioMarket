package me.uniodex.uniomarket.listeners;

import me.Zrips.TradeMe.Events.TradeFinishEvent;
import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LogListeners implements Listener {

    private UnioMarket plugin;

    public LogListeners(UnioMarket plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getPluginManager().registerEvents(new TradeListener(), plugin);
    }

    private boolean isItemSpecial(String displayName) {
        if (displayName != null) {
            if (displayName.contains("§")) {
                for (String contains : plugin.getConfig().getStringList("logging.ignoredNamesContains")) {
                    if (displayName.contains(contains)) {
                        return false;
                    }
                }
                for (String contains : plugin.getConfig().getStringList("logging.ignoredNamesEquals")) {
                    if (displayName.equals(contains)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack item = event.getItem().getItemStack();
        String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

        if (isItemSpecial(displayName)) {
            plugin.getLogManager().logPickup(player, displayName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

        if (isItemSpecial(displayName)) {
            plugin.getLogManager().logDrop(player, displayName);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

        if (isItemSpecial(displayName)) {
            plugin.getLogManager().logPlace(player, displayName);
        }
    }

    // TODO Move block break logging to each plugin's source code. Because these plugins cancelling block break event. Or fix cancelling. Or throw a custom event.
    /*@EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (block.getType() == Material.CHEST) {
            if (plugin.uciPlugin.sellChest.isVoidChest(block.getLocation())) ;
            {
                plugin.logManager.logBreak(player, "Satış Sandığı");
            }
        }

        try {
            if (block.getType() == Material.HOPPER) {
                if (EpicHoppersAPI.getImplementation().getHopperManager().isHopper(block.getLocation())) {
                    if (EpicHoppersAPI.getImplementation().getHopperManager().getHopper(block).getLevel().getLevel() == 1) {
                        plugin.logManager.logBreak(player, "Satış Hunisi");
                    } else if (EpicHoppersAPI.getImplementation().getHopperManager().getHopper(block).getLevel().getLevel() == 1) {
                        plugin.logManager.logBreak(player, "Blok Kırma Hunisi");
                    }
                }
            }
        } catch (Exception e) {
        }

        if (block.getType() == Material.MOB_SPAWNER) {
            if (plugin.unioSpawners == null) {
                plugin.logManager.logBreak(player, ((CreatureSpawner) block.getState()).getCreatureTypeName() + " Spawner");
            } else {
                Spawner spawner = UnioSpawnersAPI.getSpawnerManager().getSpawnerFromWorld(block.getLocation());
                plugin.logManager.logBreak(player, spawner.getDisplayName() + " Spawner");
            }
        }
    }*/

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void on(PlayerDeathEvent event) {
        Player player = event.getEntity();

        for (ItemStack item : event.getDrops()) {
            String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

            if (isItemSpecial(displayName)) {
                plugin.getLogManager().logDeathDrop(player, displayName);
            }
        }
    }

    public class TradeListener implements Listener {

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        public void on(TradeFinishEvent event) {
            Player player1 = event.getPlayer1();
            Player player2 = event.getPlayer2();

            List<ItemStack> items = new ArrayList<>();
            items.addAll(event.getTradeResultsP1().getItems());
            items.addAll(event.getTradeResultsP2().getItems());

            for (ItemStack item : items) {
                String displayName = item.getItemMeta() != null ? item.getItemMeta().getDisplayName() : null;

                if (isItemSpecial(displayName)) {
                    plugin.getLogManager().logTradeResult(player1.getName(), player2.getName(), displayName);
                }
            }
        }
    }
}
