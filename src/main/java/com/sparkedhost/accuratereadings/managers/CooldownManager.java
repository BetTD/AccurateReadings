// SOURCE: https://www.spigotmc.org/wiki/feature-command-cooldowns/

package com.sparkedhost.accuratereadings.managers;

import com.sparkedhost.accuratereadings.Main;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
    This is very legacy code... I don't see a real legitimate use for a cooldown feature anymore, since the resource
    usage checks have been moved to an async routine. Cooldown is disabled by default for that very reason, but I'll
    keep this code here just in case someone wants to use the feature.
 */

public class CooldownManager {

    private final Map<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public static final int DEFAULT_COOLDOWN = Main.getInstance().getSettings().cooldown_time;

    public void setCooldown(UUID player, long time){
        if (time < 1) {
            cooldowns.remove(player);
            return;
        }

        cooldowns.put(player, time);
    }

    public Long getCooldown(UUID player){
        return cooldowns.getOrDefault(player, (long) 0);
    }
}
