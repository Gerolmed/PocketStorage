package net.endrealm.nms.v1_15_R1;

import lombok.Data;
import net.endrealm.pocketstorage.api.PocketWorld;
import net.endrealm.pocketstorage.core.math.Vector3;
import net.endrealm.pocketstorage.nms.PaletteBuilder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;

import java.util.concurrent.atomic.AtomicInteger;

@Data
public class CraftPocketWorld implements PocketWorld {

    private final Vector3 bounds;
    private final int[] tiles;
    private final Material[] palette;

    public CraftPocketWorld(CraftWorld craftWorld, Vector3 origin, Vector3 bounds) {
        this. bounds = bounds;
        this.tiles = new int[bounds.getIterationSizeInt(1)];

        PaletteBuilder paletteBuilder = new PaletteBuilder();
        final AtomicInteger tileIndex = new AtomicInteger();
        origin.loop(bounds, 1,
            (vec) -> {
                Block block = craftWorld.getBlockAt(vec.getIntX(), vec.getIntY(), vec.getIntZ());
                int index = paletteBuilder.getId(block.getType());
                tiles[tileIndex.get()] = index;
                tileIndex.incrementAndGet();
            }
        );
        palette = paletteBuilder.getContents();
    }


    public Vector3 getSize() {
        return bounds;
    }

    public int[] getTiles() {
        return tiles;
    }

    public Material[] getPalette() {
        return palette;
    }
}
