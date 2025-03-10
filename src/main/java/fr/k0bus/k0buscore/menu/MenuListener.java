package fr.k0bus.k0buscore.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

/**
 * Listens for various inventory events related to custom menus and handles them accordingly.
 * The class manages a mapping between inventories and their corresponding menu objects.
 */
public class MenuListener implements Listener {

    private final HashMap<Inventory, Menu> menuMap = new HashMap<>();

    /**
     * Adds a menu to the listener.
     * The menu will be associated with its inventory, and events for that inventory will be handled.
     *
     * @param menu The menu to add.
     */
    public void add(Menu menu) {
        if (menuMap.containsKey(menu.getInventory())) return;
        menuMap.put(menu.getInventory(), menu);
    }

    /**
     * Removes a menu from the listener.
     * The menu will be removed if its inventory is not currently open by any players.
     *
     * @param menu The menu to remove.
     */
    public void remove(Menu menu) {
        if (!menuMap.containsKey(menu.getInventory())) return;
        if (!menu.getInventory().getViewers().isEmpty()) return;
        menuMap.remove(menu.getInventory());
    }

    /**
     * Handles a click event in an inventory.
     * If the inventory is associated with a menu, the click event is passed to the menu's click handler.
     *
     * @param e The inventory click event.
     */
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onClick(e);
        }
    }

    /**
     * Handles a drag event in an inventory.
     * If the inventory is associated with a menu, the drag event is passed to the menu's drag handler.
     *
     * @param e The inventory drag event.
     */
    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onDrag(e);
        }
    }

    /**
     * Handles an open event for an inventory.
     * If the inventory is associated with a menu, the open event is passed to the menu's open handler.
     *
     * @param e The inventory open event.
     */
    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onOpen(e);
        }
    }

    /**
     * Handles a close event for an inventory.
     * If the inventory is associated with a menu, the close event is passed to the menu's close handler.
     *
     * @param e The inventory close event.
     */
    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onClose(e);
        }
    }

    /**
     * Handles an interact event for an inventory.
     * If the inventory is associated with a menu, the interact event is passed to the menu's interact handler.
     *
     * @param e The inventory interact event.
     */
    @EventHandler
    public void onInteract(InventoryInteractEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onInteract(e);
        }
    }
}
