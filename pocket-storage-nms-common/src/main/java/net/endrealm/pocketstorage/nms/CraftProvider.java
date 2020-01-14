package net.endrealm.pocketstorage.nms;

import lombok.Data;
import net.endrealm.pocketstorage.api.PocketWorld;
import net.endrealm.pocketstorage.core.math.Vector3;
import org.bukkit.Location;

import java.util.regex.Pattern;

@Data
public abstract class CraftProvider {

    private final Pattern supportPattern;

    public abstract void load();
    public abstract void unload();

    public boolean supports(String version) {
        return supportPattern.matcher(version).find();
    }

    public void loadWorld(PocketWorld pocketWorld, Location location) {
        pocketWorld.spawnAt(location.getWorld(), new Vector3(location.getX(), location.getY(), location.getZ()));
    }

}
