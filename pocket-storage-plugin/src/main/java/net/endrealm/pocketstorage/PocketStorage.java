package net.endrealm.pocketstorage;

import net.endrealm.nms.v1_15_R1.CraftProviderV1_15;
import net.endrealm.pocketstorage.nms.CraftProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PocketStorage extends JavaPlugin {

    private static final CraftProvider[] providers = new CraftProvider[] {
            new CraftProviderV1_15()
    };

    private CraftProvider craftProvider;

    @Override
    public void onEnable() {
        selectProvider();

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
            //TODO: crash server _> error
            return;
        }

        craftProvider = selected;
        craftProvider.load();
    }

    @Override
    public void onDisable() {
        deselectProvider();
    }

    private void deselectProvider() {
        craftProvider.unload();
    }
}
