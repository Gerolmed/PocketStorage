package net.endrealm.pocketstorage.core.task;


import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TaskManager {
    private final TreeSet<Task> tasks;
    private final List<TaskGroup> groups;
    private final List<Integer> usedIds;

    private final List<BiConsumer<TaskGroup, Boolean>> finishListeners;

    private static final int cpuTime = 60;

    public TaskManager() {
        groups = new ArrayList<>();
        tasks = new TreeSet<>((o1,o2)-> o2.getTotalWeight()-o1.getTotalWeight());
        finishListeners = new ArrayList<>();
        usedIds = new ArrayList<>();
    }

    public void execute() {
        long stopTime = System.currentTimeMillis() + cpuTime;

        while (System.currentTimeMillis() < stopTime) {
            Task task = tasks.pollFirst();

            if(task == null)
                break;
            task.getJob().run();
            freeId(task.getTaskId());
        }

        cleanGroups(true);
    }

    public void cancelTask(int taskId) {

        tasks.removeIf(e->e.getTaskId()==taskId);

        cleanGroups(false);
    }

    public void cancelGroup(TaskGroup taskGroup) {
        tasks.removeIf(e->e.getGroup()==taskGroup);
        cleanGroups(false);
    }

    private void cleanGroups(boolean success) {
        List<TaskGroup> removedGroups = new ArrayList<>(groups); // Select all groups to removed groups without tasks
        // Remove groups from "Removed/Finished groups" list if still have a running task
        for (Task remainingTask: tasks) {
            removedGroups.remove(remainingTask.getGroup());
        }

        for(TaskGroup group : removedGroups) {
            finishGroup(group, success);
        }
    }

    private void finishGroup(TaskGroup group, boolean success) {
        groups.remove(group);

        group.getOnFinish().accept(success);

        finishListeners.forEach(e -> e.accept(group, success));
    }

    public void addFinishListener(BiConsumer<TaskGroup, Boolean> listener) {
        finishListeners.add(listener);
    }

    /**
     * Creates a new task <b>AND SCHEDULES</b> it!
     */
    public Task createTask(Runnable job, int weight, TaskGroup group) {
        Task task = new Task(newTaskId(), group, weight, job);

        if(task.getGroup() == null)
            throw new RuntimeException("Task must have a group assigned!");

        tasks.add(task);
        groups.add(task.getGroup());


        return task;
    }

    /**
     * Schedule multiple tasks. Do <b>not use</b> this together with {@link #createTask(Runnable, int, TaskGroup)}
     * @param tasks tasks to schedule
     */
    public void scheduleTasks(Task... tasks) {

        if(tasks == null)
            return;

        for (Task task : tasks) {

            if(task.getGroup() == null)
                throw new RuntimeException("Task must have a group assigned!");

            this.tasks.add(task);
            groups.add(task.getGroup());
        }
    }

    public TaskGroup createTaskGroup(Consumer<Boolean> onFinish, int weight) {
        return new TaskGroup(weight, onFinish);
    }

    private int newTaskId() {
        int id = 0;

        while (usedIds.contains(id))
            id++;

        return id;
    }

    private void freeId(int id) {
        usedIds.remove(id);
    }

    public void cancelAll() {
        for (TaskGroup taskGroup : new ArrayList<>(groups)) {
            cancelGroup(taskGroup);
        }
    }
}
