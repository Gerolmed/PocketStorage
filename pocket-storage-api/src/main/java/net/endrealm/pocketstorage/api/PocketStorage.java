package net.endrealm.pocketstorage.api;

import net.endrealm.pocketstorage.core.task.TaskManager;
import org.bukkit.World;

import java.util.function.Consumer;

public interface PocketStorage {
    World getStorageWorld();

    StorageBackend getBackend(String backendId);
    StorageBackend getDefaultBackend();
    void registerBackend(StorageBackend storageBackend);
    void generate(StorageBackend storageBackend, Consumer<GeneratedPocketWorld> onFinish);
    TaskManager getTaskManager();
}
