package process;

import java.sql.Timestamp;

/**
 * @author liviuc
 */
public class Process {

    private Long pid;
    private Priority priority;
    private Timestamp timestamp;

    public Process(final Long pid,
                   final Priority priority) {
        this.pid = pid;
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    public Long getPid() {
        return pid;
    }

    public void setTimestamp(final Timestamp timestamp) {
        this.timestamp = (this.timestamp == null) ? timestamp : this.timestamp;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
