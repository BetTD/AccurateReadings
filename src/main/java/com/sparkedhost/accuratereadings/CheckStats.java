package com.sparkedhost.accuratereadings;

import com.mattmalec.pterodactyl4j.client.entities.ClientServer;
import com.mattmalec.pterodactyl4j.entities.Limit;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckStats extends Thread {
    private final CommandSender sender;

    public CheckStats(CommandSender sender) {
        this.sender = sender;
    }

    String emptyLine = "&r &r";

    public void run() {
        // Alert the sender that the plugin is going to attempt to establish a panel connection
        sender.sendMessage(Methods.convert("&7&oEstablishing connection to the panel..."));

        PterodactylManager manager = Main.getInstance().pteroAPI;
        ClientServer server = manager.getServer();

        // Store Limits object as local variables for ease of use
        Limit limits = server.getLimits();

        StringBuilder outputBuilder = new StringBuilder();

        if (!server.isServerOwner() && !Main.getInstance().getConfig().getString("messages.api-key-not-owner").isEmpty()) {
            outputBuilder.append(Main.getInstance().getConfig().getString("messages.api-key-not-owner")).append("\n");
        }

        char cpuUsageColorCode;
        String diskUsage = manager.getDiskUsage() + " MB";
        String diskLimit = limits.getDisk() + " MB";
        String memoryUsage = manager.getMemoryUsage() + " MB";
        String memoryLimit = limits.getMemory() + " MB";

        /*
         * Changes CPU usage color depending on its value.
         * I don't know a better way to do this.
         */
        if (manager.getCpuUsage() < 50) {
            cpuUsageColorCode = 'a';
        } else if (manager.getCpuUsage() >= 50 && manager.getCpuUsage() < 80) {
            cpuUsageColorCode = 'e';
        } else {
            cpuUsageColorCode = 'c';
        }

        // The following block of if statements just changes the unit to GBs when applicable

        if (manager.getMemoryUsage() >= 1024000) {
            memoryUsage = manager.getMemoryUsage() / 1024000 + " GB";
        }

        if (limits.getMemoryLong() >= 1024) {
            memoryLimit = limits.getMemoryLong() / 1024 + " GB";
        }

        if (manager.getDiskUsage() >= 1024000) {
            diskUsage = manager.getDiskUsage() / 1024000 + " GB";
        }

        if (limits.getDiskLong() >= 1024) {
            diskLimit = limits.getDiskLong() / 1024 + " GB";
        }

        // If a stats title is set in the config, append it
        if (!Main.getInstance().getConfig().getString("messages.stats-title").isEmpty()) {
            outputBuilder.append(Main.getInstance().getConfig().getString("messages.stats-title")).append("\n");
        }

        // Append base message
        outputBuilder.append(String.join("\n",
                emptyLine,
                "&6&l- CPU: &e{CURRENTCPU}% &7(limit: {MAXCPU}%)",
                "&6&l- RAM: &e{CURRENTRAM} &7(limit: {MAXRAM})",
                "&6&l- Disk: &e{CURRENTDISK} &7(limit: {MAXDISK})",
                "&6&l- Players: &e{PLAYERCOUNT}&7/&f{PLAYERLIMIT}",
                "&6&l- Server ID: &e{SERVERID}",
                emptyLine));

        // Convert output to a string and replace variables with their actual values
        String output = outputBuilder.toString()
                .replace("{CURRENTCPU}", "&" + cpuUsageColorCode + manager.getCpuUsage())
                .replace("{MAXCPU}", String.valueOf(limits.getCPU()))
                .replace("{CURRENTRAM}", memoryUsage)
                .replace("{MAXRAM}", memoryLimit)
                .replace("{CURRENTDISK}", diskUsage)
                .replace("{MAXDISK}", diskLimit)
                .replace("{PLAYERCOUNT}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                .replace("{PLAYERLIMIT}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{SERVERID}", server.getIdentifier());

        // If a post command is set in the config, make the sender execute it
        if (!Main.getInstance().getSettings().perf_postCommand.isEmpty()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Bukkit.getServer().dispatchCommand(sender, Main.getInstance().getSettings().perf_postCommand);
                }
            }.runTask(Main.getInstance());

        }

        // Finally, send response to sender
        sender.sendMessage(Methods.convert(output));
    }
}