package me.uniodex.uniomarket.listeners;

import me.uniodex.uniomarket.UnioMarket;
import me.uniodex.uniomarket.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

public class PointsListeners implements Listener {

    private UnioMarket plugin;

    public PointsListeners(UnioMarket plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getPluginManager().registerEvents(new JobsListeners(), plugin);
    }

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (plugin.getConfig().getBoolean("points.giveDaily")) {
            if (!plugin.getPointsManager().isPaidToday(player.getName())) {
                plugin.getPointsManager().payPlayer(player.getName());
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player != null) {
                        player.sendMessage(plugin.getMessage("points.dailyPointsGiven"));
                    }
                }, 30L);
            }
        }
    }

    public class JobsListeners implements Listener {

        @EventHandler(ignoreCancelled = true)
        public void on(BlockBreakEvent event) {
            Player player = event.getPlayer();
            Location location = event.getBlock().getLocation();

            if (!plugin.getJobsHook().isPlayerMiner(player)) return;


            // TODO Adjust chances
            int chance = 500;
            // TODO Increase chance if multiblock
            /*
            if (plugin.itemManager.isItemNamed(player.getItemInHand()) &&
                    plugin.itemManager.getItem(Items.multiblock).getItemMeta().getDisplayName().equals(player.getItemInHand().getItemMeta().getDisplayName())) {
                chance = 1000;
            }*/

            int val = new Random().nextInt(chance) + 1;
            if (val == 1) {
                if (Utils.isLocationInMine(location)) {
                    int pointsToGive = plugin.getConfig().getInt("points.pointsToGiveMiners");
                    if (plugin.getPointsManager().givePoints(player.getName(), pointsToGive)) {
                        player.sendMessage(plugin.getMessage("points.youFoundPointsWhileMining").replaceAll("%points%", String.valueOf(pointsToGive)));
                    }
                }
            }
        }

        @EventHandler
        public void on(EntityDeathEvent event) {
            if (!(event.getEntity() instanceof Monster)) return;
            if (event.getEntity().getKiller() == null) return;
            Player player = event.getEntity().getKiller();
            if (!plugin.getJobsHook().isPlayerHunter(player)) return;

            // TODO Adjust chances
            int chance = 100;

            int val = new Random().nextInt(chance) + 1;
            if (val == 1) {
                int pointsToGive = plugin.getConfig().getInt("points.pointsToGiveHunters");
                if (plugin.getPointsManager().givePoints(player.getName(), pointsToGive)) {
                    player.sendMessage(plugin.getMessage("points.youFoundPointsWhileHunting").replaceAll("%points%", String.valueOf(pointsToGive)));
                }
            }
        }
    }
}
