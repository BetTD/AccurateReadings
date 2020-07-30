package com.sparkedhost.accuratereadings;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PerformanceCmd implements CommandExecutor {
    private final Main plugin;
    public PerformanceCmd(Main plugin) { this.plugin = plugin; }
    private final CooldownManager cooldown = new CooldownManager();

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        Thread checkStats = new CheckStats(plugin, sender, plugin.getConfig().getString("panelUrl"), plugin.getConfig().getString("apiKey"), plugin.getConfig().getString("serverId"));
        if(!(sender instanceof Player)) {
            new Thread(checkStats).start();
        } else {
            Player p = (Player) sender;
            if(p.hasPermission("readings.perf")) {
                long timeLeft = System.currentTimeMillis() - cooldown.getCooldown(p.getUniqueId());
                if(plugin.getConfig().getBoolean("cooldownEnable")) {
                    if(TimeUnit.MILLISECONDS.toSeconds(timeLeft) >= CooldownManager.DEFAULT_COOLDOWN) {
                        new Thread(checkStats).start();
                        cooldown.setCooldown(p.getUniqueId(), System.currentTimeMillis());
                    } else {
                        long timeremaining = TimeUnit.MILLISECONDS.toSeconds(timeLeft) - CooldownManager.DEFAULT_COOLDOWN;
                        p.sendMessage(Methods.convert("&cYou must wait "+timeremaining+" seconds before you can check the stats again."));
                    }
                } else {
                    new Thread(checkStats).start();
                }
            } else {
                p.sendMessage(Methods.convert(plugin.getConfig().getString("messages.no-permission")));
            }
        }
        return true;
    }
}
