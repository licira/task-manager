package taskmanager;

import org.junit.Test;
import process.Process;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static process.ListCriterion.*;
import static process.Priority.*;

/**
 * @author liviuc
 */
public class TaskManagerTest {

    @Test
    public void testAddTaskManager() {
        final TaskManager target = new TaskManager(3);

        assertTrue(target.add(new Process(1L, LOW)));
        assertTrue(target.add(new Process(2L, MEDIUM)));
        assertTrue(target.add(new Process(3L, HIGH)));

        assertFalse(target.add(new Process(4L, HIGH)));
    }

    @Test
    public void testKillExistingTaskManager() {
        final TaskManager target = new TaskManager(3);

        assertTrue(target.add(new Process(1L, LOW)));
        assertTrue(target.add(new Process(2L, MEDIUM)));
        assertTrue(target.add(new Process(3L, HIGH)));

        assertTrue(target.kill(3L));

        assertTrue(target.add(new Process(4L, HIGH)));
    }

    @Test
    public void testKillMissingTaskManager() {
        final TaskManager target = new TaskManager(3);

        assertTrue(target.add(new Process(1L, LOW)));
        assertTrue(target.add(new Process(2L, MEDIUM)));
        assertTrue(target.add(new Process(3L, HIGH)));

        assertFalse(target.kill(10L));

        assertFalse(target.add(new Process(4L, HIGH)));
        assertEquals(3, target.list().size());
    }

    @Test
    public void testKillGroupTaskManager() {
        final TaskManager target = new TaskManager(6);

        assertTrue(target.add(new Process(1L, LOW)));
        assertTrue(target.add(new Process(2L, LOW)));
        assertTrue(target.add(new Process(3L, MEDIUM)));
        assertTrue(target.add(new Process(4L, MEDIUM)));
        assertTrue(target.add(new Process(5L, HIGH)));
        assertTrue(target.add(new Process(6L, HIGH)));

        target.killGroup(LOW);

        final Collection<Process> processList = target.list();

        assertTrue(processList.size() == 4);
        for (final Process p : processList) {
            assertFalse(p.getPid() == 1L);
            assertFalse(p.getPid() == 2L);
            assertFalse(p.getPriority() == LOW);
        }
    }

    @Test
    public void testKillAll() {
        final TaskManager target = new TaskManager(3);

        assertTrue(target.add(new Process(1L, LOW)));
        assertTrue(target.add(new Process(2L, MEDIUM)));
        assertTrue(target.add(new Process(3L, HIGH)));

        target.killAll();

        assertTrue(target.list().size() == 0);
    }

    @Test
    public void testAddWithFifoTaskManager() {
        final TaskManager target = new FifoTaskManager(3);

        final Process oldest = new Process(1L, LOW);
        assertTrue(target.add(oldest));
        assertTrue(target.add(new Process(2L, MEDIUM)));
        assertTrue(target.add(new Process(3L, HIGH)));

        final Process newest = new Process(4L, HIGH);
        assertTrue(target.add(newest));

        final Collection<Process> processList = target.list();
        assertEquals(processList.size(), 3);
        assertTrue(processList.contains(newest));
        assertFalse(processList.contains(oldest));
    }

    @Test
    public void testAddWithPriorityTaskManagerWhenHigherPriority() {
        final TaskManager target = new PriorityTaskManager(3);

        final Process oldest = new Process(1L, LOW);
        target.add(oldest);
        target.add(new Process(2L, MEDIUM));
        target.add(new Process(3L, HIGH));

        final Process newest = new Process(4L, HIGH);
        assertTrue(target.add(newest));

        final Collection<Process> processList = target.list();
        assertEquals(processList.size(), 3);
        assertTrue(processList.contains(newest));
        assertFalse(processList.contains(oldest));
    }

    @Test
    public void testAddWithPriorityTaskManagerWhenLowerPriority() {
        final TaskManager target = new PriorityTaskManager(3);

        final Process oldest = new Process(1L, HIGH);
        target.add(oldest);
        target.add(new Process(2L, HIGH));
        target.add(new Process(3L, HIGH));

        final Process newest = new Process(4L, LOW);
        assertTrue(target.add(newest));

        final Collection<Process> processList = target.list();
        assertEquals(processList.size(), 3);
        assertFalse(processList.contains(newest));
        assertTrue(processList.contains(oldest));
    }

    @Test
    public void testAddWithPriorityTaskManagerWhenEqualPriority() {
        final TaskManager target = new PriorityTaskManager(3);

        final Process oldest = new Process(1L, HIGH);
        target.add(oldest);
        target.add(new Process(2L, HIGH));
        target.add(new Process(3L, HIGH));

        final Process newest = new Process(4L, HIGH);
        assertTrue(target.add(newest));

        final Collection<Process> processList = target.list();
        assertEquals(processList.size(), 3);
        assertTrue(processList.contains(newest));
        assertFalse(processList.contains(oldest));
    }

    @Test
    public void testListByCreation() {
        final TaskManager target = new TaskManager(5);

        final Process p1 = add(target, new Process(1L, HIGH));
        final Process p2 = add(target, new Process(2L, MEDIUM));
        final Process p3 = add(target, new Process(3L, LOW));
        final Process p4 = add(target, new Process(4L, MEDIUM));
        final Process p5 = add(target, new Process(5L, HIGH));

        final List<Process> expected = List.of(p1, p2, p3, p4, p5);

        final Collection<Process> actual = target.list(CREATION);

        assertEquals(expected, actual);
    }

    @Test
    public void testListByPriority() {
        final TaskManager target = new TaskManager(5);

        final Process p1 = add(target, new Process(1L, HIGH));
        final Process p2 = add(target, new Process(2L, MEDIUM));
        final Process p3 = add(target, new Process(3L, LOW));
        final Process p4 = add(target, new Process(4L, MEDIUM));
        final Process p5 = add(target, new Process(5L, HIGH));

        final List<Process> expected = List.of(p3, p2, p4, p1, p5);

        final Collection<Process> actual = target.list(PRIORITY);

        assertEquals(expected, actual);
    }

    @Test
    public void testListById() {
        final TaskManager target = new TaskManager(5);

        final Process p1 = add(target, new Process(5L, HIGH));
        final Process p2 = add(target, new Process(4L, MEDIUM));
        final Process p3 = add(target, new Process(3L, LOW));
        final Process p4 = add(target, new Process(2L, MEDIUM));
        final Process p5 = add(target, new Process(1L, HIGH));

        final List<Process> expected = List.of(p5, p4, p3, p2, p1);

        final Collection<Process> actual = target.list(ID);

        assertEquals(expected, actual);
    }

    private Process add(final TaskManager target,
                        final Process p) {
        target.add(p);
        try {
            // to allow time to elapse between timestamp of processes
            // to get more clarity for time based sorting
            Thread.sleep(100);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return p;
    }
}
