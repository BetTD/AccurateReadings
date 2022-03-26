package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.Main;
import org.jetbrains.annotations.NotNull;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderAPIManager extends PlaceholderExpansion {
    final PterodactylManager manager = Main.getInstance().pteroAPI;

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
                String memoryUsage = (manager.getMemoryUsage() / 1024000) + " MB";

                if (manager.getMemoryUsage() >= 1024000000) {
                    memoryUsage = manager.getMemoryUsage() / 1024000000 + " GB";
                }

                return memoryUsage;
            case "memorylimit":
                String memoryLimit = manager.getMemoryLimit() + " MB";

                if (manager.getMemoryLimit() >= 1024) {
                    memoryLimit = manager.getMemoryLimit() / 1024 + " GB";
                }

                return memoryLimit;
            case "diskusage":
                String diskUsage = (manager.getDiskUsage() / 1024000) + " MB";

                if (manager.getDiskUsage() >= 1024000000) {
                    diskUsage = manager.getDiskUsage() / 1024000000 + " GB";
                }

                return diskUsage;
            case "disklimit":
                String diskLimit = manager.getDiskLimit() + " MB";

                if (manager.getDiskLimit() >= 1024) {
                    diskLimit = manager.getDiskLimit() / 1024 + " GB";
                }

                return diskLimit;
        }

        // Requested placeholder does not exist in this expansion
        return null;
    }
}
