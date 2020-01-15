package net.endrealm.nms.v1_15_R1;

import net.endrealm.pocketstorage.api.GeneratedPocketWorld;
import net.endrealm.pocketstorage.api.PocketWorld;
import net.endrealm.pocketstorage.core.math.Vector3;
import net.endrealm.pocketstorage.core.task.Task;
import net.endrealm.pocketstorage.core.task.TaskGroup;
import net.endrealm.pocketstorage.core.task.TaskManager;
import net.endrealm.pocketstorage.nms.CraftGeneratedPocketWorld;
import net.endrealm.pocketstorage.nms.CraftProvider;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class CraftProviderV1_15 extends CraftProvider {

    public CraftProviderV1_15(TaskManager taskManager) {
        super(Pattern.compile("(1.15(.[0-9])?)"), taskManager);
    }

    public void load() {

    }

    public void unload() {

    }

    @Override
    public void loadWorld(PocketWorld pocketWorld, Location location, Consumer<GeneratedPocketWorld> onFinish, Runnable onCancel) {

        final TaskGroup taskGroup = getTaskManager().createTaskGroup(
                (success)-> {

                    if(!success) {
                        onCancel.run();
                        return;
                    }

                    final GeneratedPocketWorld result = new CraftGeneratedPocketWorld(pocketWorld, location);
                    onFinish.accept(result);

        }, 0);

        List<Task> taskList = getTileTasks(pocketWorld, location.getWorld(), new Vector3(location.getX(), location.getY(), location.getZ()));
        taskList.forEach(task -> task.setGroup(taskGroup));

        getTaskManager().scheduleTasks(taskList);
    }

    private List<Task> getTileTasks(PocketWorld pocketWorld, World world, Vector3 origin) {
        final AtomicInteger tileIndex = new AtomicInteger();
        final List<Task> tasks = new ArrayList<>();
        origin.loop(pocketWorld.getSize(), 1,
                (vec) -> {
                    final Block block = world.getBlockAt(vec.getIntX(), vec.getIntY(), vec.getIntZ());
                    final int index = pocketWorld.getTiles()[tileIndex.get()];

                    tasks.add(new Task(
                            ()-> {
                                block.setType(pocketWorld.getPalette()[index]);
                            }, tileIndex.get()));

                    tileIndex.incrementAndGet();
                }
        );

        return tasks;
    }
}
