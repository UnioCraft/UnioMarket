package me.uniodex.uniomarket.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.uniodex.uniomarket.UnioMarket;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final String VERSION = getClass().getPackage().getImplementationVersion();

    private UnioMarket plugin;

    public PlaceholderAPIHook(UnioMarket plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean register() {
        return super.register();
    }

    @Override
    public String getAuthor() {
        return "UnioDex";
    }

    @Override
    public String getIdentifier() {
        return "uniomarket";
    }

    @Override
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) return null;

        if (identifier.startsWith("points_see")) {
            String playerName = identifier.split("_")[2];
            return String.valueOf(plugin.getPointsManager().getPoints(playerName));
        }

        return null;
    }


}