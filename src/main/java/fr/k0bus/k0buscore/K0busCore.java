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

/**
 * The main class for the K0busCore plugin. This class handles plugin initialization,
 * interaction with Vault for economy, chat, and permission systems, and manages the
 * anti-spam functionality.
 * <p>
 * This class is responsible for loading dependencies like Vault, registering event listeners,
 * and checking for updates.
 * </p>
 */
public class K0busCore extends JavaPlugin {

    private static K0busCore instance;
    private static MenuListener menuListener;
    private Logger logger;
    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;

    private final List<UUID> antiSpam = new ArrayList<>();
    /**
     * The time in tick before sending another message
     */
    public int antiSpamTick = 5;

    /**
     * Called when the plugin is enabled. Initializes necessary components, including Vault.
     */
    @Override
    public void onEnable() {
        init();
        initVault();
    }

    /**
     * Initializes the plugin components, including logger and menu listener.
     */
    public void init() {
        instance = this;
        menuListener = new MenuListener();
        logger = new Logger(this, false);
        registerEvent(menuListener);
    }

    /**
     * Initializes Vault services (Economy, Chat, and Permissions).
     */
    public void initVault() {
        if (this.getDescription().getSoftDepend().contains("Vault") || this.getDescription().getDepend().contains("Vault")) {
            if (getServer().getPluginManager().isPluginEnabled("Vault")) {
                if (setupEconomy()) getLog().log("&2Vault Economy loaded successfully !");
                if (setupChat()) getLog().log("&2Vault Chat loaded successfully !");
                if (setupPermissions()) getLog().log("&2Vault Permission loaded successfully !");
            } else {
                getLog().log("&6Vault is not enabled on this server !");
            }
        }
    }

    /**
     * Sets the {@link MenuListener} for the plugin and registers it.
     *
     * @param plugin The plugin instance used to register the listener.
     */
    public void setMenuListener(JavaPlugin plugin) {
        menuListener = new MenuListener();
        plugin.getServer().getPluginManager()
                .registerEvents(menuListener, plugin);
    }

    /**
     * Checks if the plugin is up to date by comparing the version with the latest available version.
     *
     * @param id The ID used for update checking (usually the SpigotMC resource ID).
     */
    protected void checkUpdate(int id) {
        UpdateChecker updateChecker = new UpdateChecker(this, id);
        if (updateChecker.isUpToDate()) {
            logger.log("&2" + this.getDescription().getName() + " &av" + this.getDescription().getVersion());
        } else {
            logger.log("&2" + this.getDescription().getName() + " &cv" + this.getDescription().getVersion() +
                    " (Update " + updateChecker.getVersion() + " available on SpigotMC)");
        }
    }

    /**
     * Registers a plugin event listener.
     *
     * @param listener The listener to be registered.
     */
    public static void registerEvent(Listener listener) {
        getInstance().getServer().getPluginManager()
                .registerEvents(listener, getInstance());
    }

    /**
     * Called when the plugin is disabled. Can be used for cleanup tasks.
     */
    @Override
    public void onDisable() {
        // Plugin shutdown logic (currently empty)
    }

    /**
     * Sets up the Vault Economy service.
     *
     * @return True if the Economy service is successfully set up.
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null)
            econ = rsp.getProvider();
        return econ != null;
    }

    /**
     * Sets up the Vault Chat service.
     *
     * @return True if the Chat service is successfully set up.
     */
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null)
            chat = rsp.getProvider();
        return chat != null;
    }

    /**
     * Sets up the Vault Permissions service.
     *
     * @return True if the Permissions service is successfully set up.
     */
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null)
            perms = rsp.getProvider();
        return perms != null;
    }

    /**
     * Returns the instance of this plugin.
     *
     * @return The plugin instance.
     */
    public static K0busCore getInstance() {
        return instance;
    }

    /**
     * Returns the {@link MenuListener} instance for the plugin.
     *
     * @return The MenuListener instance.
     */
    public static MenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Returns the logger instance used by the plugin.
     *
     * @return The logger instance.
     */
    public Logger getLog() {
        return logger;
    }

    /**
     * Sets the anti-spam tick delay.
     *
     * @param antiSpamTick The number of ticks to delay before sending a message again.
     */
    public void setAntiSpamTick(int antiSpamTick) {
        this.antiSpamTick = antiSpamTick;
    }

    /**
     * Returns the Vault Economy service.
     *
     * @return The Economy service.
     */
    public Economy getEcon() {
        return econ;
    }

    /**
     * Returns the Vault Chat service.
     *
     * @return The Chat service.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Returns the Vault Permissions service.
     *
     * @return The Permissions service.
     */
    public Permission getPerms() {
        return perms;
    }

    /**
     * Checks if Vault is enabled and set up.
     *
     * @return True if Vault is enabled.
     */
    public boolean isVaultEnabled() {
        return getEcon() != null;
    }

    /**
     * Sends a message to a {@link HumanEntity}, preventing spam based on the anti-spam delay.
     *
     * @param p   The recipient of the message.
     * @param str The message to be sent.
     */
    public void sendMessage(HumanEntity p, String str) {
        if (antiSpam.contains(p.getUniqueId())) return;
        antiSpam.add(p.getUniqueId());
        p.sendMessage(str);
        Bukkit.getScheduler().runTaskLaterAsynchronously(getInstance(), () -> {
            antiSpam.remove(p.getUniqueId());
        }, antiSpamTick);
    }
}
