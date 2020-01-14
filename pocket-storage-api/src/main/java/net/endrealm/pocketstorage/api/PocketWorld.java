package net.endrealm.pocketstorage.api;

import net.endrealm.pocketstorage.core.math.Vector3;
import org.bukkit.Material;
import org.bukkit.World;

public interface PocketWorld {
    Vector3 getSize();
    int[] getTiles();
    Material[] getPalette();

    void spawnAt(World world, Vector3 origin);
}
