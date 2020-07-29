package me.bettd.accuratereadings;

import org.bukkit.ChatColor;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Methods {
    public static String convert(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static boolean validateURL(String url)
    {
        try {
            new URL(url).toURI();
            return true;
        }
        catch (URISyntaxException | MalformedURLException e) {
            return false;
        }
    }
}
