# task manager

The purpose of the current project is to provide a task manager implementation according 
the following specifications: 
- add process
- kill process/all/group
- list proceses
- a process is an immutable object defined by its id, priority (low, medium, high) and creation time
- the task manager has a predefined size

Add process:
1. Size based
2. FIFO
3. Priority based (priority + creation time)

The reasoning behind the current implementation was based on offering a comprehensive yet simple implementation
of the specs without overengineering.

The assumption was that we had an initial implementation, and then just as in real life, clients would come with
new requirements. Thus, when performing the design at first I implemented the size based task manager and 
afterwards in the SOLID spirit I extended the *TaskManager* class trying to modify as less as possible the initial
implementation (in order to avoid potential error) and in exchange extend the existing *TaskManager* by creating 
2 new classes: *FifoTaskManager* and *PriorityTaskManager*, each one answering to a specific use case.

### Task Manager Design
Basically the task manger's state is defined by it's capacity which represent the capacity of the collection holding
the processes added.

1.Size Based
The underlying data structure is the array list with induced fixed size (i.e. never grows beyond the capacity).
Simplicity in holding a fixed number of entries was what led to the choice of the array list.

add() -> O(1)

2.FIFO
The underlying data structure is the queue with induced fixed size (i.e. never grows beyond the capacity).
The reason behind this choice is that the queue (initialized as linked list in Java) employs the proper 
implementation for a FIFO mechanism.
add() -> O(1) (a linked list was used for implementing a que and in Java linked list's implementation the tail node
gets updated when adding - i.e. queueing)

3.Priority based (priority + creation time)
The underlying data structure is the priority queue (with its underlying heap data structure) 
with induced fixed size (i.e. never grows beyond capacity + 1).
This data structure was chosen as it holds data sorted by custom criteria.
add() -> log(n) (when adding an element, if the capacity is reached before addition we add the element, and the perform
a poll() in order to assure the removal of the first element according to the sorting criteria)

We ensure the priority criteria of the task manager by making use of Java's custom comparator based on process 
priority level and creation time. 

For all types of task managers the following operations are the same:

kill() -> O(n) (iterate through all the list and remove the process matching a given pid)

killGroup() -> O(n)

killAll() -> O(1)

list() -> O(nlogn) (we make use of Java's sorting implementation based on custom comparators that are based on:
creation time, priority level + creation time or pid);


### Setup

Import the current project into Intellij (or eclipse).

Set Java 12 as project SDK.

Set Java 12 as programming language level.

Set target bytcode level 12 for Java compiler

Go to *TaskManagerTest* and right click on the name of the class in the .java file and hit 'Run TaskManagerTest'.

The successful tests reflect the expected functionality as described in the specs.