package net.endrealm.pocketstorage;

import net.endrealm.nms.v1_15_R1.CraftProviderV1_15;
import net.endrealm.pocketstorage.core.task.TaskManager;
import net.endrealm.pocketstorage.nms.CraftProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PocketStorage extends JavaPlugin {

    private final CraftProvider[] providers;
    private final TaskManager taskManager;


    public PocketStorage() {
        taskManager = new TaskManager();
        providers = new CraftProvider[] {
                new CraftProviderV1_15(taskManager)
        };
    }

    private CraftProvider craftProvider;

    @Override
    public void onEnable() {
        selectProvider();
        Bukkit.getScheduler().runTaskTimer(this, taskManager::execute, 1, 1);
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
            //TODO: crash server --> error
            return;
        }

        craftProvider = selected;
        craftProvider.load();
    }

    @Override
    public void onDisable() {
        deselectProvider();
        taskManager.cancelAll();
    }

    private void deselectProvider() {
        craftProvider.unload();
    }
}
