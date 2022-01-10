package com.sparkedhost.accuratereadings;

import com.stanjg.ptero4j.PteroUserAPI;
import com.stanjg.ptero4j.entities.objects.server.ServerLimits;
import com.stanjg.ptero4j.entities.objects.server.ServerUsage;
import com.stanjg.ptero4j.entities.panel.user.UserServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class CheckStats extends Thread {
    private final String panelUrl;
    private final String apiKey;
    private final String serverId;
    private final CommandSender sender;

    public CheckStats(CommandSender sender, String panelUrl, String apiKey, String serverId) {
        this.panelUrl = panelUrl;
        this.apiKey = apiKey;
        this.serverId = serverId;
        this.sender = sender;
    }

    String emptyLine = "&r &r";

    public void run() {
        // Alert the sender that the plugin is going to attempt to establish a panel connection
        sender.sendMessage(Methods.convert("&7&oEstablishing connection to the panel..."));

        // Init user API to interact with the panel
        PteroUserAPI api = new PteroUserAPI(panelUrl, apiKey);

        // Find server matching the ID set in the config
        UserServer server = api.getServersController().getServer(serverId);

        // Store the different objects as local variables for ease of use
        ServerUsage usage = server.getServerUsage();
        ServerLimits limits = server.getLimits();

        StringBuilder outputBuilder = new StringBuilder();

        if (!server.isOwner() && !Main.getInstance().getConfig().getString("messages.api-key-not-owner").isEmpty()) {
            outputBuilder.append(Main.getInstance().getConfig().getString("messages.api-key-not-owner")).append("\n");
        }

        char cpuUsageColorCode;
        String diskUsage = usage.getDiskUsage() + " MB";
        String diskLimit = limits.getDisk() + " MB";
        String memoryUsage = usage.getMemoryUsage() + " MB";
        String memoryLimit = limits.getMemory() + " MB";

        /*
         * Changes CPU usage color depending on its value.
         * I don't know a better way to do this.
         */
        if (usage.getCpuUsage() < 50) {
            cpuUsageColorCode = 'a';
        } else if (usage.getCpuUsage() >= 50 && usage.getCpuUsage() < 80) {
            cpuUsageColorCode = 'e';
        } else {
            cpuUsageColorCode = 'c';
        }

        // The following block of if statements just changes the unit to GBs when sizes are over 1000 MBs

        if (usage.getMemoryUsage() >= 1000) {
            memoryUsage = usage.getMemoryUsage() / 1000 + " GB";
        }

        if (limits.getMemory() >= 1000) {
            diskUsage = limits.getMemory() / 1000 + " GB";
        }

        if (usage.getDiskUsage() >= 1000) {
            diskUsage = usage.getDiskUsage() / 1000 + " GB";
        }

        if (limits.getDisk() >= 1000) {
            diskUsage = limits.getDisk() / 1000 + " GB";
        }

        // If a stats title is set in the config, append it
        if (!Main.getInstance().getConfig().getString("messages.stats-title").isEmpty()) {
            outputBuilder.append(Main.getInstance().getConfig().getString("messages.stats-title")).append("\n");
        }

        // Append base message
        outputBuilder.append(String.join("\n",
                emptyLine,
                "&6&l- CPU: &e{CURRENTCPU}% &7(limit: {MAXCPU})",
                "&6&l- RAM: &e{CURRENTRAM} &7(limit: {MAXRAM})",
                "&6&l- Disk: &e{CURRENTDISK} &7(limit: {MAXDISK}",
                "&6&l- Players: &e{PLAYERCOUNT}&7/&f{PLAYERLIMIT}",
                "&6&l- Server ID: &e{SERVERID}",
                emptyLine));

        // Convert output to a string and replace variables with their actual values
        String output = outputBuilder.toString()
                .replace("{CURRENTCPU}", "&" + cpuUsageColorCode + usage.getCpuUsage())
                .replace("{MAXCPU}", String.valueOf(limits.getCpu()))
                .replace("{CURRENTRAM}", memoryUsage)
                .replace("{MAXRAM}", memoryLimit)
                .replace("{CURRENTDISK}", diskUsage)
                .replace("{MAXDISK}", diskLimit)
                .replace("{PLAYERCOUNT}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                .replace("{PLAYERLIMIT}", String.valueOf(Bukkit.getMaxPlayers()));

        // If a post command is set in the config, make the sender execute it
        if (!Main.getInstance().getSettings().perf_postCommand.isEmpty()) {
            Bukkit.getServer().dispatchCommand(sender, Main.getInstance().getSettings().perf_postCommand);
        }

        // Finally, send response to sender
        sender.sendMessage(Methods.convert(output));
    }
}