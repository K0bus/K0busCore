package fr.k0bus.k0buscore;

import fr.k0bus.k0buscore.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Logger {

    JavaPlugin plugin;
    boolean debug = false;

    String tag;

    public Logger(JavaPlugin plugin, boolean debug)
    {
        this.plugin = plugin;
        this.debug = debug;
        this.tag = "&f[&e" + plugin.getDescription().getName()+ "&f]&r ";
    }

    public void log(String msg) {
        msg = StringUtils.translateColor(tag + msg);
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    public void debug(String msg) {
        if (isDebug()) {
            log("&6&lDEBUG >>&r " + msg);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
