package fr.k0bus.k0buscore.utils;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for handling Minecraft version-related operations.
 * This class provides methods to retrieve and parse the version of the running Minecraft server.
 * <p>
 * It specifically retrieves the minor version number of the Minecraft server (e.g., "1.16" -> "16").
 * This can be useful for checking compatibility with certain Minecraft versions.
 * <p>
 * This class is abstract and cannot be instantiated.
 */
public abstract class VersionUtils {

    // Private constructor to prevent instantiation
    private VersionUtils() {}

    /**
     * Retrieves the minor version of the Minecraft server.
     * It parses the server's version string (e.g., "MC: 1.16") and extracts the minor version number.
     *
     * @return The minor version number of the Minecraft server (e.g., for "MC: 1.16", it returns 16).
     * @throws IllegalArgumentException If the version string cannot be parsed correctly.
     */
    public static int getMCVersion() {
        // Get the server version
        String version = Bukkit.getVersion();

        // Define the regex pattern to match "MC: 1.16"
        Matcher matcher = Pattern.compile("MC: \\d\\.(\\d+)").matcher(version);

        // If a match is found, extract the minor version number
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            // If the version string doesn't match the expected format, throw an exception
            throw new IllegalArgumentException("Failed to parse server version from: " + version);
        }
    }
}
