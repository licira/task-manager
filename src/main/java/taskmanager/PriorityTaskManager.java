package taskmanager;

import process.Process;

import java.util.PriorityQueue;

/**
 * @author liviuc
 */
public class PriorityTaskManager extends TaskManager {

    public PriorityTaskManager(final int capacity) {
        super(capacity);
    }

    public boolean add(final Process p) {
        final PriorityQueue<Process> q =
                (PriorityQueue<Process>) this.processList;

        boolean result;
        setTimestamp(p);
        if (q.size() == this.capacity) {
            result = q.add(p);
            q.poll();
        } else {
            result = q.add(p);
        }
        return result;
    }

    public void initProcessList() {
        this.processList = new PriorityQueue<>(PRIORITY_COMPARATOR);
    }
}
