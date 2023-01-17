package com.sparkedhost.accuratereadings.commands;

import com.sparkedhost.accuratereadings.Main;
import com.sparkedhost.accuratereadings.Utils;
import com.sparkedhost.accuratereadings.managers.CooldownManager;
import com.sparkedhost.accuratereadings.managers.PterodactylManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class StatsCommand extends BaseCommand {
    private final CooldownManager cooldown = new CooldownManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        // Player-specific code: permissions & cooldown checking
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (!p.hasPermission("readings.stats")) {
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

        // Append base message
        outputBuilder.append(Main.getInstance().getSettings().messages_statsMessage);

        // Convert output to a string and replace variables with their actual values, incl. PlaceholderAPI placeholders.
        String output = Utils.parsePlaceholdersIfPresent(sender instanceof Player ? (Player) sender : null, outputBuilder.toString()
                .replace("{CURRENTCPU}", "&" + cpuUsageColorCode + manager.getCpuUsage())
                .replace("{MAXCPU}", String.valueOf(manager.getCpuLimit()))
                .replace("{CURRENTRAM}", manager.getMemoryUsageString())
                .replace("{MAXRAM}", manager.getMemoryLimitString())
                .replace("{CURRENTDISK}", manager.getDiskUsageString())
                .replace("{MAXDISK}", manager.getDiskLimitString())
                .replace("{PLAYERCOUNT}", String.valueOf(Bukkit.getServer().getOnlinePlayers().size()))
                .replace("{MAXPLAYERS}", String.valueOf(Bukkit.getMaxPlayers()))
                .replace("{SERVERID}", manager.getServerId())
                .replace("{UPTIME}", manager.getUptime()));

        // If a post command is set in the config, make the sender execute it
        if (!Main.getInstance().getSettings().output_postCommand.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Bukkit.getServer().dispatchCommand(sender, Main.getInstance().getSettings().output_postCommand), 1);
        }

        // Finally, send response to sender
        sender.sendMessage(Utils.colorize(output));

        return true;
    }
}
