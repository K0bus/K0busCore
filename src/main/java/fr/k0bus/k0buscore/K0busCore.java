package fr.k0bus.k0buscore;

import fr.k0bus.k0buscore.menu.MenuListener;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class K0busCore extends JavaPlugin {

    public static K0busCore instance;
    public static MenuListener menuListener;
    private static Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        menuListener = new MenuListener();
        registerEvent(menuListener);
        if(getServer().getPluginManager().isPluginEnabled("Vault"))
        {
            if(setupEconomy()) getLogger().info("Vault Economy loaded successfully !");
            if(setupChat()) getLogger().info("Vault Chat loaded successfully !");
            if(setupPermissions()) getLogger().info("Vault Permission loaded successfully !");
        }
        else {
            getLogger().info("Vault is not enabled on this server !");
        }
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

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp != null)
            econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if(rsp != null)
            chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if(rsp != null)
            perms = rsp.getProvider();
        return perms != null;
    }

    public static K0busCore getInstance() {
        return instance;
    }

    public static MenuListener getMenuListener() {
        return menuListener;
    }

    public static Economy getEcon() {
        return econ;
    }

    public static Chat getChat() {
        return chat;
    }

    public static Permission getPerms() {
        return perms;
    }

    public static boolean isVaultEnabled()
    {
        return getEcon() != null;
    }
}
