// SOURCE: https://www.spigotmc.org/wiki/feature-command-cooldowns/

package com.sparkedhost.accuratereadings;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public static final int DEFAULT_COOLDOWN = Bukkit.getPluginManager().getPlugin("AccurateReadings").getConfig().getInt("cooldownTime");

    public void setCooldown(UUID player, long time){
        if(time < 1) {
            cooldowns.remove(player);
        } else {
            cooldowns.put(player, time);
        }
    }

    public Long getCooldown(UUID player){
        return cooldowns.getOrDefault(player, (long) 0);
    }
}
