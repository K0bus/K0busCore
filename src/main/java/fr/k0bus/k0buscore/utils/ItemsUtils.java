package fr.k0bus.k0buscore.utils;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemsUtils {
    public static ItemStack fromConfiguration(@Nullable ConfigurationSection cs, Player player)
    {
        if(cs == null) return new ItemStack(Material.AIR);
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
                    finalLore.add(Utils.PAPIParse(StringUtils.translateColor(s), player));
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
            if(!finalLore.isEmpty())
                itemMeta.setLore(finalLore);
            itemStack.setItemMeta(itemMeta);
        }
        if(cs.isConfigurationSection("leather-color") && itemStack.getItemMeta() instanceof LeatherArmorMeta)
        {
            ConfigurationSection csColor = cs.getConfigurationSection("leather-color");
            if(csColor != null)
            {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) itemStack.getItemMeta();
                leatherArmorMeta.setColor(
                        Color.fromRGB(
                                csColor.getInt("red"),
                                csColor.getInt("green"),
                                csColor.getInt("blue")
                        )
                );
                itemStack.setItemMeta(leatherArmorMeta);
            }
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
