package fr.k0bus.k0buscore.menu;

import fr.k0bus.k0buscore.K0busCore;
import fr.k0bus.k0buscore.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public abstract class Menu {

    private final Inventory inventory;
    private final HashMap<Integer, MenuItems> menuItemsHashMap = new HashMap<>();

    private final JavaPlugin plugin;

    public Menu(int size, String name, JavaPlugin plugin)
    {
        inventory = Bukkit.createInventory(null,9*size, Utils.PAPIParse(name));
        this.plugin = plugin;
        K0busCore.getMenuListener().add(this);
    }
    public Menu(String name, JavaPlugin pl)
    {
        this(6, name, pl);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract void init();


    public void setItem(int slot, MenuItems menuItems)
    {
        inventory.setItem(slot, menuItems);
        menuItemsHashMap.put(slot, menuItems);
    }
    public void open(Player p)
    {
        p.openInventory(inventory);
    }
    public void close(Player p)
    {
        if(p.getOpenInventory().getTopInventory().equals(inventory))
            p.closeInventory();
        K0busCore.getMenuListener().remove(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent e) {
        if(menuItemsHashMap.containsKey(e.getSlot()))
        {
            MenuItems menuItems = menuItemsHashMap.get(e.getSlot());
            if(menuItems != null)
                menuItems.onClick(e);
        }
    }
    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }
    public void onOpen(InventoryOpenEvent e) {

    }
    public void onClose(InventoryCloseEvent e) {}
    public void onInteract(InventoryInteractEvent e) {}
}
