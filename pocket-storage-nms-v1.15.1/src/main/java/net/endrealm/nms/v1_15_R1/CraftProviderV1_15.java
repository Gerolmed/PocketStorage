package net.endrealm.nms.v1_15_R1;

import net.endrealm.pocketstorage.core.task.TaskManager;
import net.endrealm.pocketstorage.nms.CraftProvider;

import java.util.regex.Pattern;

public class CraftProviderV1_15 extends CraftProvider {

    public CraftProviderV1_15(TaskManager taskManager) {
        super(Pattern.compile("(1.15(.[0-9])?)"), taskManager);
    }

    public void load() {

    }

    public void unload() {

    }
}
