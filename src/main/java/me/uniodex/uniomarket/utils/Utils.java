package me.uniodex.uniomarket.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String getTimeAsHours() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static String getTimeAsYearMonthDay() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE);
    }

    public static boolean isThereEnoughSpace(ItemStack[] items, int spaceNeeded) {
        int space = 0;
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                space++;
            }
        }
        return space >= spaceNeeded;
    }

    public static String colorizeMessage(String message) {
        return ChatColor.translateAlternateColorCodes('&', message.replaceAll("%hataPrefix%", UnioMarket.hataPrefix).replaceAll("%bilgiPrefix%", UnioMarket.bilgiPrefix).replaceAll("%dikkatPrefix%", UnioMarket.dikkatPrefix).replaceAll("%prefix%", UnioMarket.bilgiPrefix));
    }

    public static List<String> colorizeMessages(List<String> messages) {
        List<String> newList = new ArrayList<>();
        for (String msg : messages) {
            newList.add(ChatColor.translateAlternateColorCodes('&', msg.replaceAll("%hataPrefix%", UnioMarket.hataPrefix).replaceAll("%bilgiPrefix%", UnioMarket.bilgiPrefix).replaceAll("%dikkatPrefix%", UnioMarket.dikkatPrefix).replaceAll("%prefix%", UnioMarket.bilgiPrefix)));
        }
        return newList;
    }

    public static String getStringLocation(final Location l) {
        if (l == null) {
            return "";
        }
        return l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
    }

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static boolean isLocationInMine(Location location) {
        List<String> regionIds = new ArrayList<>();
        RegionManager regionManager = WorldGuardPlugin.inst().getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);

        for (ProtectedRegion region : regionsAtLocation) {
            regionIds.add(region.getId());
        }

        //TODO Sync region names.
        return regionIds.contains("maden") || regionIds.contains("vipmaden") || regionIds.contains("uviphammadde") || regionIds.contains("moria") || regionIds.contains("madenkazi");
    }
}
