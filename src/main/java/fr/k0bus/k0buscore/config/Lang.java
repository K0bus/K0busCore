package fr.k0bus.k0buscore.config;

import fr.k0bus.k0buscore.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles loading and managing language configuration files.
 * It loads the language files (.yml) and ensures that all required keys
 * are available, with a fallback to the default language if necessary.
 */
public class Lang extends Configuration {

    /**
     * Constructs a new Lang object that loads the language configuration file.
     * It attempts to load the specified language file, or falls back to 'en_US.yml' if the specified file is not found.
     * Then, it ensures all keys from the default configuration are copied to the current configuration if missing.
     *
     * @param lang    The language code (e.g., "en_US").
     * @param instance The JavaPlugin instance to access plugin resources.
     */
    public Lang(String lang, JavaPlugin instance) {
        super(lang + ".yml", instance, "lang");
        FileConfiguration defaultConfig = null;

        // Try to load the specified language file
        InputStream is = getPlugin().getResource("lang/" + lang + ".yml");
        if (is != null) {
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        } else {
            // Fallback to the default language file
            is = getPlugin().getResource("lang/en_US.yml");
            if (is != null) {
                defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
            } else {
                getPlugin().getLogger().warning("Unable to find language file for " + lang + ". Falling back to default language.");
            }
        }

        // Copy missing keys from the default configuration to the current configuration
        if (defaultConfig != null) {
            for (String key : defaultConfig.getKeys(false)) {
                if (defaultConfig.isConfigurationSection(key)) {
                    checkSection(defaultConfig.getConfigurationSection(key));
                }
                String fullKey = defaultConfig.getCurrentPath() + "." + key;
                if (!getConfiguration().contains(fullKey)) {
                    getConfiguration().set(fullKey, defaultConfig.get(fullKey));
                }
            }
        }
    }

    /**
     * Recursively checks and copies missing configuration sections.
     * This ensures that all sections and keys from the default configuration are available.
     *
     * @param section The configuration section to check and copy.
     */
    public void checkSection(ConfigurationSection section) {
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                checkSection(section.getConfigurationSection(key));
            }
            String fullKey = section.getCurrentPath() + "." + key;
            if (!getConfiguration().contains(fullKey)) {
                getConfiguration().set(fullKey, section.get(fullKey));
            }
        }
    }

    /**
     * Retrieves a string value from the configuration and applies color translation.
     * The string is processed to replace color codes and other placeholders with their respective values.
     *
     * @param path The path to the string in the configuration file.
     * @return The translated string, with color codes applied.
     */
    public String getString(String path) {
        String value = getConfiguration().getString(path);
        if (value == null) {
            return "";
        }
        return StringUtils.translateColor(value);
    }

    /**
     * Retrieves a string value from the configuration and applies color translation.
     * Additionally, replaces placeholders in the string using the provided map.
     *
     * @param path        The path to the string in the configuration file.
     * @param replaceMap  A map of placeholders and their replacement values.
     * @return The translated string with placeholders replaced and color codes applied.
     */
    public String getString(String path, HashMap<String, String> replaceMap) {
        String s = getConfiguration().getString(path);
        if (s == null) {
            return "";
        }

        // Replace placeholders in the string with their corresponding values
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }

        // Return the final string with color codes applied
        return StringUtils.translateColor(s);
    }
}
