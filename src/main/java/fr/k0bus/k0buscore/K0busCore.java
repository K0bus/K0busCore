package fr.k0bus.k0buscore;

import fr.k0bus.k0buscore.menu.MenuListener;
import fr.k0bus.k0buscore.updater.UpdateChecker;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class K0busCore extends JavaPlugin {

    private static K0busCore instance;
    private static MenuListener menuListener;
    private Logger logger;
    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    private final List<UUID> antiSpam = new ArrayList<>();
    public int antiSpamTick = 5;


    @Override
    public void onEnable() {
        // Plugin startup logic
        init();
        initVault();
    }

    public void init()
    {
        // Plugin startup logic
        instance = this;
        menuListener = new MenuListener();
        logger = new Logger(this, false);
        registerEvent(menuListener);
    }
    public void initVault()
    {
        if(this.getDescription().getSoftDepend().contains("Vault") || this.getDescription().getDepend().contains("Vault"))
            if(getServer().getPluginManager().isPluginEnabled("Vault"))
            {
                if(setupEconomy()) getLog().log("&2Vault Economy loaded successfully !");
                if(setupChat()) getLog().log("&2Vault Chat loaded successfully !");
                if(setupPermissions()) getLog().log("&2Vault Permission loaded successfully !");
            }
            else {
                getLog().log("&6Vault is not enabled on this server !");
            }
    }

    public void setMenuListener(JavaPlugin plugin)
    {
        menuListener = new MenuListener();
        plugin.getServer().getPluginManager()
                .registerEvents(menuListener, plugin);
    }

    protected void checkUpdate(int id)
    {
        UpdateChecker updateChecker = new UpdateChecker(this, id);
        if (updateChecker.isUpToDate()) {
            logger.log("&2" + this.getDescription().getName() + " &av" + this.getDescription().getVersion());
        } else {
            logger.log("&2" + this.getDescription().getName() + " &cv" + this.getDescription().getVersion() +
                    " (Update " + updateChecker.getVersion() + " available on SpigotMC)");
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

    public Logger getLog() {
        return logger;
    }

    public void setAntiSpamTick(int antiSpamTick) {
        this.antiSpamTick = antiSpamTick;
    }

    public Economy getEcon() {
        return econ;
    }

    public Chat getChat() {
        return chat;
    }

    public Permission getPerms() {
        return perms;
    }

    public boolean isVaultEnabled()
    {
        return getEcon() != null;
    }

    public void sendMessage(HumanEntity p, String str)
    {
        if(antiSpam.contains(p.getUniqueId())) return;
        antiSpam.add(p.getUniqueId());
        p.sendMessage(str);
        Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), ()->{
            antiSpam.remove(p.getUniqueId());
        }, antiSpamTick);
    }

}
