package fr.k0bus.k0buscore.menu;

import fr.k0bus.k0buscore.utils.InventoryUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MenuExtended extends Menu{

    HashMap<Integer, MenuItems> playerMenuItemsHashMap = new HashMap<>();
    
    public MenuExtended(int size, String name, JavaPlugin plugin) {
        super(size, name, plugin);
    }

    @Override
    public void init() {

    }

    public void setPlayerItem(int slot, MenuItems menuItems) {
        playerMenuItemsHashMap.put(slot, menuItems);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();
        String inventoryString = InventoryUtils.toBase64(p.getInventory());
        NamespacedKey key = new NamespacedKey(getPlugin(), "gui-inventory");
        p.getPersistentDataContainer().set(key, PersistentDataType.STRING, inventoryString);
        ItemStack[] armorContent = p.getInventory().getArmorContents();
        p.getInventory().setContents(new ItemStack[36]);
        p.saveData();
        for (Map.Entry<Integer, MenuItems> entry:playerMenuItemsHashMap.entrySet()) {
            p.getInventory().setItem(entry.getKey(), entry.getValue());
        }
        super.onOpen(e);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        try {
            getPlugin().getLogger().info("TEST");
            NamespacedKey key = new NamespacedKey(getPlugin(), "gui-inventory");
            p.getInventory().setContents(
                    InventoryUtils.itemStackArrayFromBase64(p.getPersistentDataContainer().get(key, PersistentDataType.STRING))
            );
            p.getPersistentDataContainer().remove(key);
            p.saveData();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        super.onClose(e);
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory() instanceof PlayerInventory)
        {
            if(playerMenuItemsHashMap.containsKey(e.getSlot()))
            {
                MenuItems menuItems = playerMenuItemsHashMap.get(e.getSlot());
                if(menuItems != null)
                    menuItems.onClick(e);
            }
        }
        else
            super.onClick(e);
    }
}
