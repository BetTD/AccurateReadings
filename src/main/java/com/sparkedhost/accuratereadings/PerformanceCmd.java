package com.sparkedhost.accuratereadings;

import com.sparkedhost.accuratereadings.managers.CooldownManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PerformanceCmd implements CommandExecutor {
    private Main plugin;
    public PerformanceCmd(Main plugin) { this.plugin = plugin; }
    private final CooldownManager cooldown = new CooldownManager();
    private final String panelUrl = plugin.getSettings().pterodactyl_panelUrl;
    private final String apiKey = plugin.getSettings().pterodactyl_apiKey;
    private final String serverId = plugin.getSettings().pterodactyl_serverId;

    @Override
    public boolean onCommand(CommandSender sender, Command c, String s, String[] args) {
        Thread checkStats = new CheckStats(plugin, sender, panelUrl, apiKey, serverId);

        if (!(sender instanceof Player)) {
            new Thread(checkStats).start();
            return true;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("readings.perf")) {
            p.sendMessage(Methods.convert(Main.getInstance().getSettings().messages_noPerms));
            return false;
        }

        long timeLeft = System.currentTimeMillis() - cooldown.getCooldown(p.getUniqueId());

        if (plugin.getSettings().cooldown_enabled && TimeUnit.MILLISECONDS.toSeconds(timeLeft) < CooldownManager.DEFAULT_COOLDOWN) {
            long timeRemaining = Math.abs(TimeUnit.MILLISECONDS.toSeconds(timeLeft) - CooldownManager.DEFAULT_COOLDOWN);
            p.sendMessage(Methods.convert("&cYou must wait " + timeRemaining + " seconds before you can check the stats again."));
            return false;
        }

        if (plugin.getSettings().cooldown_enabled) cooldown.setCooldown(p.getUniqueId(), System.currentTimeMillis());
        new Thread(checkStats).start();

        return true;
    }
}
