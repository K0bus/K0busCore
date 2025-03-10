package fr.k0bus.k0buscore;

import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A simple logger utility class for the plugin, designed to handle standard logging
 * as well as debug messages. It formats the log messages with the plugin's name and
 * tag for consistency and clarity.
 */
public class Logger {

    private JavaPlugin plugin;
    private boolean debug = false;
    private String tag;

    /**
     * Constructor for the Logger class.
     *
     * @param plugin The plugin instance, used to get the plugin name.
     * @param debug Whether the debug mode is enabled.
     */
    public Logger(JavaPlugin plugin, boolean debug) {
        this.plugin = plugin;
        this.debug = debug;
        this.tag = "&f[&e" + plugin.getDescription().getName() + "&f]&r ";
    }

    /**
     * Logs a standard message to the console.
     * The message will be prefixed with the plugin's name and formatted with colors.
     *
     * @param msg The message to send to the console.
     */
    public void log(String msg) {
        msg = StringUtils.translateColor(tag + msg);
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    /**
     * Logs a debug message to the console.
     * This message will only be logged if debug mode is enabled.
     *
     * @param msg The debug message to send to the console.
     */
    public void debug(String msg) {
        if (isDebug()) {
            log("&6&lDEBUG >>&r " + msg);
        }
    }

    /**
     * Returns whether the debug mode is enabled.
     *
     * @return True if debug mode is enabled, false otherwise.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Sets the debug mode for the logger.
     *
     * @param debug The debug value to set.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
