package fr.k0bus.k0buscore.menu;

import fr.k0bus.k0buscore.K0busCore;
import fr.k0bus.k0buscore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Abstract class representing a customizable menu with interactive items.
 */
public abstract class Menu {

    private final Inventory inventory;
    private final HashMap<Integer, MenuItems> menuItemsHashMap = new HashMap<>();
    private final JavaPlugin plugin;

    /**
     * Constructs a menu with a specific size and title.
     *
     * @param size  Number of rows in the menu (1-6)
     * @param name  Display name of the menu
     * @param plugin The plugin instance associated with this menu
     */
    public Menu(int size, String name, JavaPlugin plugin) {
        inventory = Bukkit.createInventory(null, 9 * size, Utils.PAPIParse(name));
        this.plugin = plugin;
        K0busCore.getMenuListener().add(this);
    }

    /**
     * Constructs a menu with a default size of 6 rows.
     *
     * @param name  Display name of the menu
     * @param plugin The plugin instance associated with this menu
     */
    public Menu(String name, JavaPlugin plugin) {
        this(6, name, plugin);
    }

    /**
     * Gets the plugin associated with this menu.
     *
     * @return The JavaPlugin instance
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

    /**
     * Initializes the menu contents. Must be implemented in subclasses.
     */
    public abstract void init();

    /**
     * Sets an interactive item in the menu at a specific slot.
     *
     * @param slot      The slot position (0-based index)
     * @param menuItems The menu item to be placed
     */
    public void setItem(int slot, MenuItems menuItems) {
        inventory.setItem(slot, menuItems);
        menuItemsHashMap.put(slot, menuItems);
    }

    /**
     * Opens the menu for a player.
     *
     * @param player The player to open the menu for
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    /**
     * Closes the menu for a player.
     *
     * @param player The player whose menu should be closed
     */
    public void close(Player player) {
        if (player.getOpenInventory().getTopInventory().equals(inventory)) {
            player.closeInventory();
        }
        K0busCore.getMenuListener().remove(this);
    }

    /**
     * Gets the inventory associated with this menu.
     *
     * @return The Bukkit Inventory instance
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Handles item click events inside the menu.
     *
     * @param event The InventoryClickEvent triggered
     */
    public void onClick(InventoryClickEvent event) {
        if (menuItemsHashMap.containsKey(event.getSlot())) {
            MenuItems menuItems = menuItemsHashMap.get(event.getSlot());
            if (menuItems != null) {
                menuItems.onClick(event);
            }
        }
    }

    /**
     * Handles item drag events inside the menu.
     *
     * @param event The InventoryDragEvent triggered
     */
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handles inventory open events.
     *
     * @param event The InventoryOpenEvent triggered
     */
    public void onOpen(InventoryOpenEvent event) {
    }

    /**
     * Handles inventory close events.
     *
     * @param event The InventoryCloseEvent triggered
     */
    public void onClose(InventoryCloseEvent event) {
    }

    /**
     * Handles general inventory interaction events.
     *
     * @param event The InventoryInteractEvent triggered
     */
    public void onInteract(InventoryInteractEvent event) {
    }
}
