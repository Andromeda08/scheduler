package scheduler;

import java.util.ArrayList;
import java.util.Comparator;

public class SRTF {
    private ArrayList<Task> queue = new ArrayList<>();
    private Task active = null;
    private boolean has_shorter = true;
    private boolean enabled = true;

    // Start scheduler
    public void start() {
        enabled = true;
    }

    // Stop scheduler
    public void stop() {
        enabled = false;
        if (active != null) {
            queue.add(active);
        }
        active = null;
        has_shorter = true;
    }

    // Add task to Scheduler queue
    public void add(Task task) {
        queue.add(task);
        if (active == null || task.time_remaining < active.time_remaining) {
            has_shorter = true;
        }
    }

    // Scheduler tick
    public void tick() {
        // If empty return
        if (empty()) {
            return;
        }
        // If not enabled all tasks wait
        if (!enabled) {
            for (Task task : queue) {
                task.wait_one();
            }
            return;
        }
        // If task with shorter remaining time exists pass execution
        if (has_shorter) {
            queue.sort(Comparator.comparing((Task t) -> t.time_remaining));
            if (active != null) {
                queue.add(active);
            }
            active = queue.get(0);
            queue.remove(0);

            MultiLevelScheduler.log_task(active.name);

            has_shorter = false;
        }
        // Step tasks
        for (Task task : queue) {
            task.wait_one();
        }
        active.run_one();
        // Task done?
        if (active.time_remaining == 0) {
            has_shorter = true;
            active = null;
        }
    }

    // Is the scheduler empty?
    public boolean empty() {
        return queue.isEmpty() && active == null;
    }
}
