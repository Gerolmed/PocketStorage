package net.endrealm.pocketstorage.nms;

import lombok.Data;

import java.util.regex.Pattern;

@Data
public abstract class CraftProvider {

    private final Pattern supportPattern;

    public abstract void load();
    public abstract void unload();

    public boolean supports(String version) {
        return supportPattern.matcher(version).find();
    }

}
