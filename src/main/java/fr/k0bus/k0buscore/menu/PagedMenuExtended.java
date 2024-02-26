package fr.k0bus.k0buscore.menu;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PagedMenuExtended extends MenuExtended {

    int page;
    int[] slots = null;
    List<MenuItems> content = new ArrayList<>();

    public PagedMenuExtended(int size, String name, JavaPlugin plugin) {
        super(size, name, plugin);
    }
    public PagedMenuExtended(String name, JavaPlugin plugin) {
        super(6, name, plugin);
    }

    @Override
    public void init() {
        getInventory().clear();
    }

    public boolean hasNextPage()
    {
        return page< getMaxPage();
    }
    public boolean hasPreviousPage()
    {
        return page>0;
    }

    public void next()
    {
        if(hasNextPage())
        {
            page++;
            drawContent();
        }
    }
    public void previous()
    {
        if(hasPreviousPage())
        {
            page--;
            drawContent();
        }
    }

    public void setSlots(int[] slots) {
        this.slots = slots;
    }

    public void add(MenuItems menuItems)
    {
        this.content.add(menuItems);
    }

    public void remove(MenuItems menuItems)
    {
        this.content.remove(menuItems);
    }
    public boolean contains(MenuItems menuItems)
    {
        return this.content.contains(menuItems);
    }

    public List<MenuItems> getContent() {
        return content;
    }

    public void clearInventoryContent()
    {
        for(int n: slots)
        {
            setItem(n, null);
        }
    }

    public void clearContent()
    {
        clearInventoryContent();
        content = new ArrayList<>();
    }

    public void drawContent()
    {
        clearInventoryContent();
        int i = page * slots.length;
        for (int n:slots) {
            if(i < content.size())
            {
                setItem(n, content.get(i));
            }
            i++;
        }
    }

    public int getMaxPage()
    {
        return Math.max(((int) Math.ceil((double)content.size() / (double)slots.length))-1, 1);
    }

    public int getSlotParPage()
    {
        return slots.length;
    }

    public int getPage() {
        return page;
    }

    public int[] getSlots() {
        return slots;
    }
}
