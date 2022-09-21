package fr.k0bus.k0buscore;

import fr.k0bus.k0buscore.menu.MenuListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class K0busCore extends JavaPlugin {

    public static K0busCore instance;
    public static MenuListener menuListener;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        menuListener = new MenuListener();
        registerEvent(menuListener);
    }

    public static void registerEvent(Listener listener)
    {
        getInstance().getServer().getPluginManager()
                .registerEvents(listener, getInstance());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static K0busCore getInstance() {
        return instance;
    }

    public static MenuListener getMenuListener() {
        return menuListener;
    }
}
