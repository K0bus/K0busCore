package fr.k0bus.k0buscore.updater;

import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * A class responsible for checking if the current plugin version is up to date
 * by querying the Spigot API and comparing it with the version available for
 * the specified resource on SpigotMC.
 */
public class UpdateChecker {

    private final JavaPlugin plugin;
    private final Version version;
    private final Version spigotVersion;
    private final int resourceId;

    /**
     * Creates an instance of the {@link UpdateChecker} class, which checks for updates
     * for the plugin based on the provided resource ID.
     *
     * @param plugin The plugin instance to check for updates.
     * @param resourceId The resource ID of the plugin on SpigotMC.
     */
    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.version = new Version(this.plugin.getDescription().getVersion());
        this.resourceId = resourceId;
        this.spigotVersion = getVersion();
    }

    /**
     * Fetches the latest version of the plugin from the SpigotMC API using the
     * provided resource ID. This method communicates with the Spigot API and returns
     * the version of the plugin available for download.
     *
     * @return The latest version of the plugin from SpigotMC, or a version with the
     *         value "X" if the update check fails.
     */
    public Version getVersion() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            Scanner scanner = new Scanner(connection.getInputStream());
            if(scanner.hasNext())
            {
                return new Version(scanner.next());
            }
        } catch (Exception exception) {
            this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
        return new Version("X");
    }

    /**
     * Checks if the current plugin version is up to date by comparing it with the
     * version available on SpigotMC. If the current version is greater than or equal
     * to the SpigotMC version, it returns true, otherwise false.
     *
     * @return True if the plugin is up to date, otherwise false.
     */
    public boolean isUpToDate() {
        if(spigotVersion != null)
            return spigotVersion.compareTo(this.version) <= 0;
        return false;
    }

    /**
     * Checks if the current plugin version is stable based on its version type.
     *
     * @return True if the plugin version is stable, otherwise false.
     */
    public boolean isStable() {
        return this.version.getType().isStable();
    }
}
