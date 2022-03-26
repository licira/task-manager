package taskmanager;

import process.Process;

import java.util.LinkedList;

/**
 * @author liviuc
 */
public class FifoTaskManager extends TaskManager {

    public FifoTaskManager(final int capacity) {
        super(capacity);
        this.processList = new LinkedList<>();
    }

    public boolean add(final Process p) {
        if (this.processList.size() == capacity) {
            ((LinkedList) this.processList).poll();
        }
        return super.add(p);
    }

    public void initProcessList() {
        this.processList = new LinkedList<>();
    }
}
