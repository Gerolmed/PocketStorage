package net.endrealm.pocketstorage.nms;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class PaletteBuilder {
    private List<Material> palette;

    public PaletteBuilder() {
        palette = new ArrayList<>();
    }

    public int getId(Material material) {
        int index = palette.indexOf(material);

        if(index != -1) {
            return index;
        }
        palette.add(material);
        return palette.size()-1;
    }

    public Material[] getContents() {
        return palette.toArray(new Material[0]);
    }
}
