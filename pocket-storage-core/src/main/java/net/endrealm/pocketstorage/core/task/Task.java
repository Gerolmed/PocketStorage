package net.endrealm.pocketstorage.core.task;

import lombok.Data;

@Data
public class Task {
    private final int taskId;
    private final TaskGroup group;
    private final int weight;
    private final Runnable job;

    public int getTotalWeight() {
        return weight + group.getWeight();
    }
}
