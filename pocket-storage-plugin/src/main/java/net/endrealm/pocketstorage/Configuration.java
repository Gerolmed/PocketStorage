package net.endrealm.pocketstorage;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

@Data
public class Configuration {
    private final File file;
    private final FileConfiguration configuration;

    private final Logger LOGGER;

    public Configuration(JavaPlugin plugin, String fileName) throws IOException {
        this(plugin, fileName, false);
    }

    public Configuration(JavaPlugin plugin, String fileName, boolean removeRedundantKeys) throws IOException {

        LOGGER = plugin.getLogger();

        File parentDirectory = plugin.getDataFolder();

        if(!parentDirectory.exists()) {
            LOGGER.info("Missing a config directory. Attempting to create...");

            if(parentDirectory.mkdirs())
                LOGGER.info("Constructed config directories");
            else
                LOGGER.info("Failed to create config directories");
        }

        // Add file ending if needed
        if(!(fileName.endsWith(".yml") || fileName.endsWith(".yaml"))) {
            fileName+=".yml";
        }

        file = new File(parentDirectory, fileName);

        //Create file if not existent
        if(!file.exists()) {
            if(file.createNewFile())
                LOGGER.info("Constructed config file");
            else
                LOGGER.info("Failed to create config file");

            loadFromResources(plugin, fileName);
        }

        configuration = new YamlConfiguration();

        reloadConfig(removeRedundantKeys);

    }

    /**
     * Reloads the config. Does <b>not</b> remove redundant keys
     */
    public void reloadConfig() {
        reloadConfig(false);
    }

    /**
     * Reloads the config from the file
     * @param removeRedundantKeys should redundant keys be removed
     */
    public void reloadConfig(boolean removeRedundantKeys) {
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if(removeRedundantKeys)
            removeRedundantKeys();
    }

    private void removeRedundantKeys() {
        //TODO: remove redundant keys
        LOGGER.warning("Removing redundant keys is not implemented yet!");
    }

    private void loadFromResources(JavaPlugin plugin, String fileName) throws IOException {
        InputStream inputStream = getResourceStream(plugin, fileName);

        if(inputStream == null) {
            throw new IOException("Please add "+ fileName+ " to the resources!");
        }

        byte[] buffer = new byte[inputStream.available()];
        //noinspection ResultOfMethodCallIgnored
        inputStream.read(buffer);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(buffer);
    }

    private InputStream getResourceStream(JavaPlugin plugin, String fileName) {
        return getResource(plugin, fileName);
    }

    /**
     * Saves the config to the file
     */
    public void save() {
        try {
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) getConfiguration().get(key);
    }

    private InputStream getResource(JavaPlugin plugin, String resourcePath) {

        if(resourcePath == null || resourcePath.isEmpty())
            return null;

        resourcePath = resourcePath.replace('\\', '/');

        try {
            URL url = plugin.getClass().getClassLoader().getResource(resourcePath);
            if (url == null) {
                return null;
            } else {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            }
        } catch (IOException var4) {
            return null;
        }
    }
}
