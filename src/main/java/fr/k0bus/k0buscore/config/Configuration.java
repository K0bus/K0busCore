package fr.k0bus.k0buscore.config;

import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Manages YAML configuration files for a Bukkit plugin.
 */
public class Configuration {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration configuration;
    private final String filename;

    /**
     * Creates a new configuration file instance.
     *
     * @param filename Name of the configuration file
     * @param instance Plugin instance
     */
    public Configuration(String filename, JavaPlugin instance) {
        this.plugin = instance;
        this.filename = filename;
        this.file = new File(instance.getDataFolder(), filename);
        loadConfig();
    }

    /**
     * Creates a new configuration file in a specific directory.
     *
     * @param filename Name of the configuration file
     * @param instance Plugin instance
     * @param dirName  Directory name where the file is stored
     */
    public Configuration(String filename, JavaPlugin instance, String dirName) {
        this.plugin = instance;
        this.filename = filename;
        File dir = new File(plugin.getDataFolder(), dirName);
        if (!dir.exists()) dir.mkdirs();
        this.file = dir.isDirectory() ? new File(dir, filename) : new File(dir.getParentFile(), filename);
        loadConfig();
    }

    /**
     * Loads the configuration file.
     */
    public void loadConfig() {
        if (!file.exists()) {
            if (plugin.getResource(filename) != null) {
                plugin.saveResource(filename, false);
            } else {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            this.configuration = loadConfiguration(this.file);
        } catch (InvalidConfigurationException e) {
            plugin.getLogger().log(Level.SEVERE, filename + " can't be loaded! Check file syntax first!");
            plugin.getLogger().log(Level.SEVERE, e.getMessage());
            File renamed = new File(file.getParentFile(), filename + ".old");
            if (renamed.exists()) renamed.delete();
            file.renameTo(renamed);
            loadConfig();
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Can't read file " + filename);
            this.configuration = new YamlConfiguration();
        }
    }

    /**
     * Loads a YAML configuration file.
     *
     * @param file File to load
     * @return Parsed YAML configuration
     * @throws IOException                  If file cannot be read
     * @throws InvalidConfigurationException If file contains invalid syntax
     */
    private static YamlConfiguration loadConfiguration(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        return config;
    }

    /**
     * Saves the configuration to the file.
     */
    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the configuration file with default values if missing.
     *
     * @param cfg    Configuration file name
     * @param plugin Plugin instance
     */
    public static void updateConfig(String cfg, JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), cfg);
        file.getParentFile().mkdirs();
        if (!file.exists()) plugin.saveResource(cfg, false);

        FileConfiguration defaultConf = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(cfg)));
        FileConfiguration conf;
        try {
            conf = loadConfiguration(file);
        } catch (InvalidConfigurationException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, cfg + " can't be loaded! Check file syntax first!");
            return;
        }

        for (String path : defaultConf.getKeys(true)) {
            if (!conf.contains(path) || !conf.get(path).getClass().getName().equals(defaultConf.get(path).getClass().getName())) {
                plugin.getLogger().log(Level.WARNING, path + " added to " + cfg);
                conf.set(path, defaultConf.get(path));
            }
        }

        try {
            conf.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the configuration object.
     *
     * @return FileConfiguration object
     */
    public FileConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Gets the configuration file.
     *
     * @return Configuration file
     */
    public File getFile() {
        return file;
    }

    /**
     * Gets the plugins instance
     *
     * @return Bukkit plugins instance
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
