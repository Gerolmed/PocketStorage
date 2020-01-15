package net.endrealm.pocketstorage.nms;

import lombok.Data;
import net.endrealm.pocketstorage.api.GeneratedPocketWorld;
import net.endrealm.pocketstorage.api.PocketWorld;
import org.bukkit.Location;

@Data
public class CraftGeneratedPocketWorld implements GeneratedPocketWorld {
    private final PocketWorld world;
    private final Location origin;
}
