package scheduler;

public class Task {
    protected final String name;
    protected final Priority priority;
    protected final Integer time_start;

    protected Integer time_remaining;
    protected Integer time_waiting;

    public Task(String _n, Priority _p, Integer _s, Integer _l) {
        this.name = _n;
        this.priority = _p;
        this.time_start = _s;
        this.time_remaining = _l;
        this.time_waiting = 0;
    }

    public void wait_one() { time_waiting++; }
    public void run_one() { time_remaining--; }

    public String get_name() { return name; }
    public Integer get_time_start() { return time_start; }
    public Integer get_wait() { return time_waiting; }
}
