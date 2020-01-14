package net.endrealm.pocketstorage.core.task;

import lombok.Data;

import java.util.function.Consumer;

@Data
public class TaskGroup {
    private final int weight;
    private final Consumer<Boolean> onFinish;
}
