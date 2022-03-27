package taskmanager;

import process.ListCriterion;
import process.Priority;
import process.Process;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static process.ListCriterion.CREATION;

/**
 * @author liviuc
 */
public class TaskManager {

    protected static final Comparator<Process> PRIORITY_COMPARATOR =
            Comparator.comparing(Process::getPriority)
                    .thenComparing(Process::getTimestamp);
    private static final Comparator<Process> CREATION_COMPARATOR =
            Comparator.comparing(Process::getTimestamp);
    private static final Comparator<Process> ID_COMPARATOR =
            Comparator.comparing(Process::getPid);

    protected int capacity;
    protected Collection<Process> processList;

    public TaskManager(final int capacity) {
        initProcessList();
        this.capacity = capacity;
    }

    public boolean add(final Process p) {
        if (this.processList.size() < capacity) {
            setTimestamp(p);
            return this.processList.add(p.clone());
        }
        return false;
    }

    public boolean kill(final long pid) {
        final Iterator<Process> each = this.processList.iterator();
        while (each.hasNext()) {
            // we can break the loop since the pid is unique
            if (each.next().getPid() == pid) {
                each.remove();
                return true;
            }
        }
        return false;

    }

    public boolean killAll() {
        initProcessList();
        return true;
    }

    public boolean killGroup(final Priority priority) {
        return processList.removeIf(p -> p.getPriority() == priority);
    }

    public Collection<Process> list() {
        return list(CREATION);
    }

    public Collection<Process> list(final ListCriterion criterion) {
        final List<Process> sorted = new LinkedList<>(); // to get O(1) addition at the tail
        for (final Process p : this.processList) {
            sorted.add(p.clone());
        }

        switch (criterion) {
            case CREATION:
                sorted.sort(CREATION_COMPARATOR);
                break;
            case PRIORITY:
                sorted.sort(PRIORITY_COMPARATOR);
                break;
            case ID:
                sorted.sort(ID_COMPARATOR);
                break;
        }
        return sorted;
    }

    protected void initProcessList() {
        processList = new ArrayList<>(capacity);
    }

    protected void setTimestamp(final Process p) {
        p.setTimestamp(new Timestamp(Instant.now().toEpochMilli()));
    }
}
