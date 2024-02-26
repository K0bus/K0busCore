package fr.k0bus.k0buscore.updater;

import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final Version version;
    private final Version spigotVersion;
    private final int resourceId;

    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.version = new Version(this.plugin.getDescription().getVersion());
        this.resourceId = resourceId;
        this.spigotVersion = getVersion();
    }

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
        }catch (Exception exception)
        {
            this.plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
        }
        return new Version("X");
    }
    public boolean isUpToDate()
    {
        if(spigotVersion != null)
            return spigotVersion.compareTo(this.version)<=0;
        return false;
    }
    public boolean isStable()
    {
        return this.version.type.isStable();
    }
}
