package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.Main;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPIManager extends PlaceholderExpansion {
    final PterodactylManager manager = Main.getInstance().getPteroApi();

    @Override
    public @NotNull String getAuthor() {
        return "BetTD";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "readings";
    }

    @Override
    public @NotNull String getVersion() {
        return Main.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        switch (params) {
            case "serverid":
                return manager.getServerId();
            case "cpuusage":
                return manager.getCpuUsage() + "%";
            case "cpulimit":
                return manager.getCpuLimit() + "%";
            case "memoryusage":
                return manager.getMemoryUsageString();
            case "memorylimit":
                return manager.getMemoryLimitString();
            case "diskusage":
                return manager.getDiskUsageString();
            case "disklimit":
                return manager.getDiskLimitString();
        }

        // Requested placeholder does not exist in this expansion
        return null;
    }
}
