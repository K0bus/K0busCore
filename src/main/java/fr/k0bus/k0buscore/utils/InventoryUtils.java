package fr.k0bus.k0buscore.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utility class for handling the serialization and deserialization of player inventories and item stacks to and from Base64 strings.
 * This class provides methods to convert player inventories and item arrays into Base64-encoded strings, and vice versa.
 */
public class InventoryUtils {

    /**
     * Converts a player's inventory into an array of Base64 strings. The first string represents the main content, and the second one represents the armor.
     *
     * @param playerInventory The player's inventory to convert into Base64 strings.
     * @return An array of strings, where the first element is the content and the second element is the armor.
     * @throws IllegalStateException If unable to serialize the inventory to Base64.
     */
    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        // Get the main content part, this doesn't return the armor
        String content = toBase64(playerInventory);
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());

        return new String[] { content, armor };
    }

    /**
     * Serializes an array of {@link ItemStack} objects to a Base64 string.
     *
     * @param items The array of ItemStacks to serialize.
     * @return The Base64 string representing the serialized item array.
     * @throws IllegalStateException If unable to serialize the item stacks to Base64.
     */
    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.length);

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Serializes an {@link Inventory} to a Base64 string.
     * This method serializes the contents of the entire inventory into Base64.
     *
     * @param inventory The inventory to serialize.
     * @return The Base64 string representing the serialized inventory.
     * @throws IllegalStateException If unable to serialize the inventory to Base64.
     */
    public static String toBase64(Inventory inventory) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    /**
     * Deserializes a Base64-encoded string into an {@link Inventory}.
     *
     * @param data The Base64 string containing the serialized inventory.
     * @return The deserialized Inventory object.
     * @throws IOException If an I/O error occurs while decoding the inventory data.
     */
    public static Inventory fromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();
            return inventory;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

    /**
     * Deserializes a Base64-encoded string into an array of {@link ItemStack} objects.
     *
     * @param data The Base64 string containing the serialized item array.
     * @return The deserialized array of ItemStacks.
     * @throws IOException If an I/O error occurs while decoding the item data.
     */
    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
