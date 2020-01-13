package net.endrealm.nms.v1_15_R1;

import net.endrealm.pocketstorage.nms.CraftProvider;

import java.util.regex.Pattern;

public class CraftProviderV1_15 extends CraftProvider {

    public CraftProviderV1_15() {
        super(Pattern.compile("(1.15(.[0-9])?)"));
    }

    public void load() {

    }

    public void unload() {

    }
}
