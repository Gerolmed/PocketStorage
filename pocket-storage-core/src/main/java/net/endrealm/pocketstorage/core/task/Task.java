package net.endrealm.pocketstorage.core.task;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {
    private int taskId;
    private TaskGroup group;
    private final int weight;
    private final Runnable job;

    public Task(Runnable job, int weight) {
        this(-1, null, weight, job);
    }

    public int getTotalWeight() {
        return weight + group.getWeight();
    }
}
