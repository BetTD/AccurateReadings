package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.CooldownManager;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceCmd extends BaseCommand {
    private final CooldownManager cooldown = new CooldownManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Player-specific code: permissions & cooldown checking
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!p.hasPermission("readings.perf")) {
                p.sendMessage(Utils.colorize(Main.getInstance().getSettings().messages_noPerms));
                return false;
            }

            // Code to execute if cooldown is enabled in the plugin config
            if (Main.getInstance().getSettings().cooldown_enabled) {
                // Calculate difference (in millis) between current time and cooldown time
                // TODO Replace cooldown system with something more modern...
                long difference = System.currentTimeMillis() - cooldown.getCooldown(p.getUniqueId());

                // If the difference is smaller than the default cooldown value, deny access to the command
                if (TimeUnit.MILLISECONDS.toSeconds(difference) < CooldownManager.DEFAULT_COOLDOWN) {
                    long timeRemaining = Math.abs(TimeUnit.MILLISECONDS.toSeconds(difference) - CooldownManager.DEFAULT_COOLDOWN);
                    p.sendMessage(Utils.colorize("&cYou must wait " + timeRemaining + " seconds before you can check the stats again."));
                    return false;
                }

                // Player can execute the command, store cooldown!
                cooldown.setCooldown(p.getUniqueId(), System.currentTimeMillis());
            }
        }

        // Sender is able to execute the command, continue execution

        PterodactylManager manager = Main.getInstance().pteroAPI;

        StringBuilder outputBuilder = new StringBuilder();

        char cpuUsageColorCode;

        // Resource usage is reported in bytes, while actual limits are returned in megabytes, which means we have to do
        // some maths.

        String diskUsage = (manager.getDiskUsage() / 1024000) + " MB";
        String diskLimit = manager.getDiskLimit() + " MB";
        String memoryUsage = (manager.getMemoryUsage() / 1024000) + " MB";
        String memoryLimit = manager.getMemoryLimit() + " MB";

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

        if (manager.getMemoryUsage() >= 1024000000) {
            memoryUsage = manager.getMemoryUsage() / 1024000000 + " GB";
        }

        if (manager.getMemoryLimit() >= 1024) {
            memoryLimit = manager.getMemoryLimit() / 1024 + " GB";
        }

        if (manager.getDiskUsage() >= 1024000000) {
            diskUsage = manager.getDiskUsage() / 1024000000 + " GB";
        }

        if (manager.getDiskLimit() >= 1024) {
            diskLimit = manager.getDiskLimit() / 1024 + " GB";
        }

        // Append base message
        outputBuilder.append(Main.getInstance().getSettings().messages_statsMessage);

        // Convert output to a string and replace variables with their actual values, incl. PlaceholderAPI placeholders.
        String output = Utils.parsePlaceholdersIfPresent(sender instanceof Player ? (Player) sender : null, outputBuilder.toString()
                .replace("{CURRENTCPU}", "&" + cpuUsageColorCode + manager.getCpuUsage())
                .replace("{MAXCPU}", String.valueOf(manager.getCpuLimit()))
                .replace("{CURRENTRAM}", memoryUsage)
                .replace("{MAXRAM}", memoryLimit)
                .replace("{CURRENTDISK}", diskUsage)
                .replace("{MAXDISK}", diskLimit)
                .replace("{PLAYERCOUNT}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                .replace("{MAXPLAYERS}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{SERVERID}", manager.getServerId())
                .replace("{UPTIME}", manager.getUptime()));

        // If a post command is set in the config, make the sender execute it
        if (!Main.getInstance().getSettings().perf_postCommand.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Bukkit.getServer().dispatchCommand(sender, Main.getInstance().getSettings().perf_postCommand), 1);
        }

        // Finally, send response to sender
        sender.sendMessage(Utils.colorize(output));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
