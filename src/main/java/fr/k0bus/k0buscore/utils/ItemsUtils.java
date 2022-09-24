package fr.k0bus.k0buscore.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemsUtils {
    public static ItemStack fromConfiguration(ConfigurationSection cs, Player player)
    {
        ItemStack itemStack = fromItemsAdder(cs.getString("items-adder"));

        if(itemStack == null)
        {
            Material material = Material.PAPER;
            int stackSize = 1;
            if(cs.isString("material"))
            {
                String str = cs.getString("material");
                if(str != null)
                {
                    material = Material.getMaterial(str);
                }
                if(material == null)
                    material = Material.PAPER;
            }
            if(cs.isInt("stack-size"))
            {
                stackSize = cs.getInt("stack-size");
                if(stackSize<=0)
                    stackSize = 1;
            }
            itemStack = new ItemStack(material, stackSize);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null)
        {
            String name = null;
            List<String> lore;
            List<String> finalLore = new ArrayList<>();
            int model = 0;
            if(cs.isString("name"))
            {
                name = Utils.PAPIParse(StringUtils.translateColor(cs.getString("name")), player);
            }
            if(cs.isList("lore"))
            {
                lore = cs.getStringList("lore");
                for (String s:lore) {
                    finalLore.add(Utils.PAPIParse(StringUtils.translateColor(s), null));
                }
            }
            if(cs.isInt("model-data"))
            {
                model = cs.getInt("model-data");
            }

            if(name != null)
                itemMeta.setDisplayName(name);
            if(model > 0)
                itemMeta.setCustomModelData(model);
            if(finalLore.size()>0)
                itemMeta.setLore(finalLore);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }
    public ItemStack fromConfiguration(ConfigurationSection cs)
    {
        return fromConfiguration(cs, null);
    }

    public static ItemStack fromItemsAdder(String str)
    {
        if(str != null)
            if(Bukkit.getServer().getPluginManager().isPluginEnabled("ItemsAdder"))
            {
                CustomStack customStack = CustomStack.getInstance(str);
                if(customStack != null)
                    return customStack.getItemStack();
            }
        return null;
    }
}
