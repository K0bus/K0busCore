package fr.k0bus.k0buscore.menu;

import java.util.ArrayList;
import java.util.List;

public class PagedMenu extends Menu {

    int page;
    int[] slots = null;
    List<MenuItems> content = new ArrayList<>();

    public PagedMenu(int size, String name) {
        super(size, name);
    }
    public PagedMenu(String name) {
        super(6, name);
    }

    @Override
    public void init() {
        getInventory().clear();
    }

    public boolean hasNextPage(Object[] objects)
    {
        return page< getMaxPage(objects);
    }
    public boolean hasPreviousPage()
    {
        return page>0;
    }

    public void next(Object[] objects)
    {
        if(hasNextPage(objects))
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

    public int getMaxPage(Object[] objects)
    {
        return (int) Math.ceil(objects.length / slots.length);
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
