package fr.k0bus.k0buscore.menu;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a paged menu where content is split across multiple pages.
 * This class extends {@link Menu} and provides functionality for managing
 * content and navigating between pages.
 */
public class PagedMenu extends Menu {

    private int page;
    private int[] slots = null;
    private List<MenuItems> content = new ArrayList<>();

    /**
     * Creates a new {@link PagedMenu} with the specified size, name, and plugin.
     *
     * @param size The size of the menu.
     * @param name The name of the menu.
     * @param plugin The plugin to which the menu belongs.
     */
    public PagedMenu(int size, String name, JavaPlugin plugin) {
        super(size, name, plugin);
    }

    /**
     * Creates a new {@link PagedMenu} with a default size of 6, the specified name,
     * and plugin.
     *
     * @param name The name of the menu.
     * @param plugin The plugin to which the menu belongs.
     */
    public PagedMenu(String name, JavaPlugin plugin) {
        super(6, name, plugin);
    }

    /**
     * Initializes the menu by clearing the inventory.
     */
    @Override
    public void init() {
        getInventory().clear();
    }

    /**
     * Checks if there is a next page available.
     *
     * @return True if there is a next page, false otherwise.
     */
    public boolean hasNextPage() {
        return page < getMaxPage();
    }

    /**
     * Checks if there is a previous page available.
     *
     * @return True if there is a previous page, false otherwise.
     */
    public boolean hasPreviousPage() {
        return page > 0;
    }

    /**
     * Navigates to the next page if available and redraws the content.
     */
    public void next() {
        if (hasNextPage()) {
            page++;
            drawContent();
        }
    }

    /**
     * Navigates to the previous page if available and redraws the content.
     */
    public void previous() {
        if (hasPreviousPage()) {
            page--;
            drawContent();
        }
    }

    /**
     * Sets the slots in which menu items will be placed.
     *
     * @param slots The slots to set.
     */
    public void setSlots(int[] slots) {
        this.slots = slots;
    }

    /**
     * Adds a menu item to the content of the menu.
     *
     * @param menuItems The menu item to add.
     */
    public void add(MenuItems menuItems) {
        this.content.add(menuItems);
    }

    /**
     * Removes a menu item from the content of the menu.
     *
     * @param menuItems The menu item to remove.
     */
    public void remove(MenuItems menuItems) {
        this.content.remove(menuItems);
    }

    /**
     * Checks if the menu contains a specific menu item.
     *
     * @param menuItems The menu item to check for.
     * @return True if the menu contains the item, false otherwise.
     */
    public boolean contains(MenuItems menuItems) {
        return this.content.contains(menuItems);
    }

    /**
     * Gets the content (list of menu items) of the menu.
     *
     * @return A list of menu items in the menu.
     */
    public List<MenuItems> getContent() {
        return content;
    }

    /**
     * Clears the content in the specified slots of the inventory.
     */
    public void clearInventoryContent() {
        for (int n : slots) {
            setItem(n, null);
        }
    }

    /**
     * Clears both the inventory content and the list of menu items.
     */
    public void clearContent() {
        clearInventoryContent();
        content = new ArrayList<>();
    }

    /**
     * Draws the content for the current page, placing menu items in the specified slots.
     */
    public void drawContent() {
        clearInventoryContent();
        int i = page * slots.length;
        for (int n : slots) {
            if (i < content.size()) {
                setItem(n, content.get(i));
            }
            i++;
        }
    }

    /**
     * Calculates the maximum number of pages based on the number of menu items
     * and the available slots.
     *
     * @return The maximum number of pages.
     */
    public int getMaxPage() {
        return Math.max(((int) Math.ceil((double) content.size() / (double) slots.length)) - 1, 1);
    }

    /**
     * Gets the number of slots per page.
     *
     * @return The number of slots per page.
     */
    public int getSlotParPage() {
        return slots.length;
    }

    /**
     * Gets the current page number.
     *
     * @return The current page number.
     */
    public int getPage() {
        return page;
    }

    /**
     * Gets the array of slots used by the menu.
     *
     * @return The array of slots.
     */
    public int[] getSlots() {
        return slots;
    }
}
