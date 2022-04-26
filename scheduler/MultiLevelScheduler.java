package scheduler;

import java.util.ArrayList;

public class MultiLevelScheduler {
    /* Task execution order log */
    private static String task_log = "";
    protected static void log_task(String name) {
        task_log += name;
    }

    /* Schedulers */
    private final SRTF srtf     = new SRTF();
    private final RoundRobin rr = new RoundRobin();

    // Add task to the correct scheduler based on Priority
    public void add(Task task) {
        if (task.priority == Priority.HIGH_PRIORITY) {
            rr.add(task);
        }
        if (task.priority == Priority.LOW_PRIORITY) {
            srtf.add(task);
        }
    }

    // Scheduler tick
    public void tick() {
        if (!rr.empty()) srtf.stop();
        rr.tick();
        srtf.tick();
        if (rr.empty()) srtf.start();
    }

    // Get execution order log
    public static String get_log() { return task_log; }

    // Are both schedulers empty?
    public boolean isEmpty() {
        return rr.empty() && srtf.empty();
    }
}
