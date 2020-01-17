package net.endrealm.pocketstorage;

import lombok.Getter;
import net.endrealm.nms.v1_15_R1.CraftProviderV1_15;
import net.endrealm.pocketstorage.api.GeneratedPocketWorld;
import net.endrealm.pocketstorage.api.PocketStorage;
import net.endrealm.pocketstorage.api.StorageBackend;
import net.endrealm.pocketstorage.core.task.TaskManager;
import net.endrealm.pocketstorage.nms.CraftProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.function.Consumer;

@Getter
public final class PocketStoragePlugin extends JavaPlugin implements PocketStorage {

    private final CraftProvider[] providers;
    private final TaskManager taskManager;

    private final Configuration configuration;
    private final HashMap<String, StorageBackend> storageBackends;


    public PocketStoragePlugin() throws IOException {
        taskManager = new TaskManager();
        storageBackends = new HashMap<>();
        providers = new CraftProvider[] {
                new CraftProviderV1_15(taskManager)
        };
        configuration = new Configuration(this, "config");
    }

    private CraftProvider craftProvider;
    private World storageWorld;

    @Override
    public void onEnable() {

        prepareBackend();

        configuration.reloadConfig();

        initStorageWorld();

        selectProvider();
        Bukkit.getScheduler().runTaskTimer(this, taskManager::execute, 1, 1);
    }

    private void prepareBackend() {
        storageBackends.clear();

        //TODO: add backend from file
    }

    private void initStorageWorld() {
        String tempWorldName = configuration.get("storage-world-name");

        if(Bukkit.getWorld(tempWorldName) != null) {
            Bukkit.unloadWorld(tempWorldName, false);
        }

        File worldFolder = new File(Bukkit.getWorldContainer(), tempWorldName);

        if(worldFolder.exists()) {
            getLogger().warning("Found existing storage world! This world will be removed in 5 seconds!");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(worldFolder.delete()) {
                getLogger().info("Removed old storage world!");
            } else {
                getLogger().severe("Could not remove old storage mode. Exiting to prevent any errors!");
                System.exit(1);
            }
        }

        Bukkit.createWorld(new WorldCreator(tempWorldName).generator("PocketStorage"));
    }

    private void selectProvider() {

        CraftProvider selected = null;

        for(CraftProvider craftProvider : providers) {
            if(craftProvider.supports(Bukkit.getVersion())) {
                selected = craftProvider;
                break;
            }
        }

        if(selected == null) {
            getLogger().severe("Could not find handler for version " + Bukkit.getVersion());
            System.exit(1);
            return;
        }

        craftProvider = selected;
        craftProvider.load();
    }

    @Override
    public void onDisable() {
        deselectProvider();
        taskManager.cancelAll();

        unloadStorageWorld();
    }

    private void unloadStorageWorld() {

        for(Player player : storageWorld.getPlayers()) {
            player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
        }

        Bukkit.unloadWorld(storageWorld, false);
    }

    private void deselectProvider() {
        craftProvider.unload();
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new VoidGenerator();
    }

    @Override
    public StorageBackend getBackend(String backendId) {
        return storageBackends.get(backendId);
    }

    @Override
    public StorageBackend getDefaultBackend() {
        return storageBackends.get("file"); //TODO replace by config value
    }

    @Override
    public void registerBackend(StorageBackend storageBackend) {
        storageBackends.put(storageBackend.getBackendId(), storageBackend);
    }

    @Override
    public void generate(StorageBackend storageBackend, Consumer<GeneratedPocketWorld> onFinish) {

    }
}
