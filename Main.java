import scheduler.MultiLevelScheduler;
import scheduler.Priority;
import scheduler.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    private static ArrayList<Task> tasks   = new ArrayList<>();
    private static ArrayList<Task> history = new ArrayList<>();
    private static MultiLevelScheduler scheduler = new MultiLevelScheduler();

    public static void main(String[] args) {
        /* Read tasks from standard input */
        String current_line;
        BufferedReader buffered_reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            current_line = buffered_reader.readLine();
            while (current_line != null && !current_line.equals("")) {
                String[] input = current_line.split(",");
                Priority prio = (input[1].equals("1")) ? Priority.HIGH_PRIORITY : Priority.LOW_PRIORITY;
                Task task = new Task(input[0], prio, Integer.parseInt(input[2]), Integer.parseInt(input[3]));
                tasks.add(task);
                current_line = buffered_reader.readLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        tasks.sort(Comparator.comparing(Task::get_time_start));

        /* Run scheduler */
        // Case: Didn't read any tasks
        if (tasks.isEmpty()) {
            System.out.println("Didn't receive any tasks to execute.");
            return;
        }
        // Execute tasks : If tasks > 0 and if schedulers aren't empty
        for (int i = 0; tasks.size() > 0 || !scheduler.isEmpty(); i++) {
            for (int j = 0; j < tasks.size(); j++) {
                if (tasks.get(j).get_time_start() == i) {
                    scheduler.add(tasks.get(j));
                    history.add(tasks.get(j));
                    tasks.remove(j);
                    j--;
                }
            }
            scheduler.tick();
        }

        /* Write results to standard output*/
        System.out.println(MultiLevelScheduler.get_log());
        boolean first = true;   // Epic trick c:
        for (Task task : history) {
            System.out.print((first ? "" : ",") + task.get_name() + ":" + task.get_wait());
            first = false;
        }
    }
}
