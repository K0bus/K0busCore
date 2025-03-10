package fr.k0bus.k0buscore.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 * A utility class that provides methods for handling PlaceHolderAPI placeholders.
 * It includes methods to parse strings with placeholders and replace them with actual values.
 * <p>
 * This class checks if the PlaceHolderAPI plugin is enabled and applies the placeholders accordingly.
 */
public class Utils {

    /**
     * Parses the provided string to replace any placeholders with the appropriate values.
     * If PlaceHolderAPI is installed and enabled, it will replace placeholders for the given player.
     *
     * @param str The string containing placeholders to be parsed.
     * @param player The player whose data will be used to replace placeholders (may be null).
     * @return The string with placeholders replaced by actual values, or the original string if PlaceHolderAPI is not enabled.
     */
    public static String PAPIParse(String str, OfflinePlayer player) {
        // Check if PlaceHolderAPI is enabled
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceHolderAPI")) {
            // If enabled, replace placeholders for the provided player
            return PlaceholderAPI.setPlaceholders(player, str);
        }
        // If PlaceHolderAPI is not enabled, return the original string
        return str;
    }

    /**
     * Parses the provided string to replace any placeholders with the appropriate values.
     * This method defaults to using a null player (i.e., general placeholders).
     *
     * @param str The string containing placeholders to be parsed.
     * @return The string with placeholders replaced by actual values, or the original string if PlaceHolderAPI is not enabled.
     */
    public static String PAPIParse(String str) {
        // Call the method with a null player
        return PAPIParse(str, null);
    }
}
