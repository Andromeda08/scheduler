package scheduler;

import java.util.ArrayDeque;

public class RoundRobin {
    private static final int TIME_SLICE = 2;
    private ArrayDeque<Task> queue = new ArrayDeque<>();
    private Task active = null;
    private int running = 0;

    // Add task to Scheduler queue
    public void add(Task task) {
        queue.addLast(task);
    }

    // Scheduler tick
    public void tick() {
        running++;
        // Grab next task
        if (queue.size() > 0 && (running == TIME_SLICE || active == null)) {
            if (active != null) {
                queue.addLast(active);
            }
            active = queue.removeFirst();
            MultiLevelScheduler.log_task(active.name);
            running = 0;
        }
        // Reset running counter so the scheduler know to change tasks
        if (running == TIME_SLICE) {
            running = 0;
        }
        // Step tasks
        for (Task task : queue) task.wait_one();
        if (active != null) {
            active.run_one();
            // If task done
            if (active.time_remaining == 0) {
                active = null;
            }
        }
    }

    // Is the scheduler empty?
    public boolean empty() {
        return queue.isEmpty() && active == null;
    }
}
