package fr.k0bus.k0buscore.menu;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuItems extends ItemStack {

    private Consumer<InventoryClickEvent> consumer;
    private Sound sound = Sound.UI_BUTTON_CLICK;

    public MenuItems(Material m, int size)
    {
        super(m, size);
    }
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer)
    {
        this(m, size);
        this.consumer = consumer;
    }
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer, Sound sound)
    {
        this(m, size, consumer);
        this.sound = sound;
    }
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer)
    {
        this(m, 1, consumer);
    }
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer, Sound sound)
    {
        this(m, 1, consumer, sound);
    }

    public boolean isClickable() {
        return this.consumer != null;
    }

    public void onClick(InventoryClickEvent e)
    {
        if(!isClickable()) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        p.playSound(p.getLocation(), sound, 0.5f, 1);
        consumer.accept(e);
    }

}
