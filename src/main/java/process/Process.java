package process;

import java.sql.Timestamp;

/**
 * @author liviuc
 */
public class Process implements Cloneable {

    private Long pid;
    private Priority priority;
    private Timestamp timestamp;

    public Process(final Long pid,
                   final Priority priority) {
        this.pid = pid;
        this.priority = priority;
    }

    public Process clone() {
        final Process clone = new Process(this.pid, this.priority);
        clone.setTimestamp(timestamp);
        return clone;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getPid() {
        return pid;
    }

    public Timestamp getTimestamp() {
        return (Timestamp) timestamp.clone();
    }

    public void setTimestamp(final Timestamp timestamp) {
        this.timestamp = (this.timestamp == null) ? (Timestamp) timestamp.clone() : this.timestamp;
    }

    public boolean equals(final Object o) {
        if (o instanceof Process) {
            final Process that = (Process) o;
            return this.pid.equals(that.pid) &&
                    this.priority == that.priority &&
                    this.timestamp.equals(that.timestamp);
        }
        return false;
    }
}
