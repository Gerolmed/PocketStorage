package net.endrealm.pocketstorage.nms;

import lombok.Data;
import net.endrealm.pocketstorage.api.GeneratedPocketWorld;
import net.endrealm.pocketstorage.api.PocketWorld;
import net.endrealm.pocketstorage.core.math.Vector3;
import net.endrealm.pocketstorage.core.task.Task;
import net.endrealm.pocketstorage.core.task.TaskManager;
import org.bukkit.Location;

import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Data
public abstract class CraftProvider {

    private final Pattern supportPattern;
    private final TaskManager taskManager;

    public abstract void load();
    public abstract void unload();

    public boolean supports(String version) {
        return supportPattern.matcher(version).find();
    }

    public abstract void loadWorld(PocketWorld pocketWorld, Location location, Consumer<GeneratedPocketWorld> onFinish, Runnable onCancel);

}
