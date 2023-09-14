package ekud.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ekud.exceptions.EkudException;
import ekud.exceptions.EkudIllegalArgException;
import ekud.exceptions.EkudInvalidCommandException;

/**
 * TaskList handles task modifications and invalid user inputs for
 * modifying tasks, and is a key component of the ekud.Ekud chatbot.
 */
public class TaskList {
    // Actual list storing the tasks
    private List<Task> tasks;
    // Cached list before clearing the main list
    private List<Task> cachedTasks;

    /**
     * Constructor for TaskList which initialises its arrays.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        this.cachedTasks = new ArrayList<>();
    }

    /**
     * Returns the tasks as a string.
     * @return String response message for user.
     */
    public String showTasks() {
        if (this.tasks.isEmpty()) {
            return "Your to-do list is currently empty :o";
        }
        StringBuilder output = new StringBuilder("Here is your to-do list:\n");
        int len = this.tasks.size();
        for (int i = 0; i < len; i++) {
            output.append(String.format("%d. %s\n", i + 1, this.tasks.get(i).toString()));
        }
        return output.toString();
    }

    /**
     * Marks a specific task as done and returns a confirmation message.
     * @param index Index number of task supplied by user.
     * @return String response message for user.
     * @throws EkudIllegalArgException Illegal arg for index number.
     */
    public String markTaskAsDone(int index) throws EkudIllegalArgException {
        if (index >= this.tasks.size()) {
            throw new EkudIllegalArgException("Task index number is out of bounds :/");
        }
        Task task = this.tasks.get(index);
        task.markAsDone();
        return "The following task is marked done, sheeesh:\n" + task;
    }

    /**
     * Same as the markTaskAsDone() function, but used when loading saved tasks
     * from the hard disk, specifically by the storage object.
     * @param index
     */
    public void markDoneOnStart(int index) {
        this.tasks.get(index).markAsDone();
    }

    /**
     * Marks a specific task as not done and returns a confirmation messsage.
     * @param index Index number of task supplied by user.
     * @return String response message for user.
     * @throws EkudIllegalArgException Illegal arg for index number.
     */
    public String markTaskAsNotDone(int index) throws EkudIllegalArgException {
        if (index >= this.tasks.size()) {
            throw new EkudIllegalArgException("Task index number is out of bounds :/");
        }
        Task task = this.tasks.get(index);
        task.markAsNotDone();
        return "The following task is marked as not done yet:\n" + task;
    }

    public String changePriority(Priority priority, int index) throws EkudIllegalArgException {
        if (index >= this.tasks.size()) {
            throw new EkudIllegalArgException("Task index number is out of bounds :/");
        }
        Task task = this.tasks.get(index);
        task.changePriority(priority);
        return String.format(
                "The following task's priority is set to '%s':\n%s",
                task.getPriority(),
                task);
    }

    /**
     * Returns the confirmation message for having added a task.
     * @param task
     * @return String response message for user.
     */
    private String confirmAddedTask(Task task) {
        return String.format(
                "Got it! I've added this task:\n%s\nNow you have %d task(s) in the list.",
                task.toString(),
                this.tasks.size());
    }

    /**
     * Adds an already initialised task to this TaskList, used for loading
     * saved tasks onto this TaskList on startup, specifically by the
     * storage object.
     * @param task
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Adds a to-do task to this TaskList.
     * @param description Description of the to-do task.
     * @return String response message for user.
     * @throws EkudIllegalArgException Illegal arg for to-do task.
     */
    public String addToDo(String description) {
        ToDo newToDo = new ToDo(description, Priority.MEDIUM);
        this.tasks.add(newToDo);
        return this.confirmAddedTask(newToDo);
    }

    /**
     * Adds a deadline task to this TaskList.
     * @param description Description of the deadline task.
     * @param dateTime Date and time to complete this task by.
     * @return String response message for user.
     * @throws EkudIllegalArgException Illegal arg(s) for deadline task.
     */
    public String addDeadline(String description, LocalDateTime dateTime) {
        Deadline newDeadline = new Deadline(description, dateTime, Priority.MEDIUM);
        this.tasks.add(newDeadline);
        return this.confirmAddedTask(newDeadline);
    }

    /**
     * Adds an event task to this TaskList and returns a confirmation message.
     * @param description Description of the event task.
     * @param dateTimes Date and Time this event starts and ends.
     * @return String response for user.
     */
    public String addEvent(String description, LocalDateTime ... dateTimes) {
        Event newEvent = new Event(description, dateTimes[0], dateTimes[1], Priority.MEDIUM);
        this.tasks.add(newEvent);
        return this.confirmAddedTask(newEvent);
    }

    /**
     * Deletes a task from this TaskList and returns a confirmation message.
     * @param index Index number of task to be deleted as supplied by user.
     * @return String response message for user.
     */
    public String deleteTask(int index) throws EkudException {
        if (this.tasks.isEmpty()) {
            throw new EkudInvalidCommandException("You cannot delete from an empty task list :/");
        }
        if (index >= this.tasks.size()) {
            throw new EkudIllegalArgException("Task index number is out of bounds :/");
        }
        Task task = this.tasks.get(index);
        this.tasks.remove(index);
        return String.format(
                "Alright, this task has been removed:\n%s\nNow you have %d task(s) in the list.",
                task.toString(),
                tasks.size());
    }

    /**
     * Finds a list of tasks matching the user's keyword search and returns it as a String.
     * @param keyword
     * @return String response of tasks for user.
     */
    public String findTasks(String keyword) {
        List<String> matchingTasks = new ArrayList<>();
        for (Task task: this.tasks) {
            if (task.description.contains(keyword)) {
                matchingTasks.add(task.toString());
            }
        }
        if (matchingTasks.isEmpty()) {
            return String.format(
                   "No tasks matching the keyword '%s' were found :(",
                    keyword);
        }
        StringBuilder output = new StringBuilder("Here are the matching tasks in your list: :>\n");
        for (String task: matchingTasks) {
            output.append(task + "\n");
        }
        return output.toString();
    }

    /**
     * Helper function used by the storage object to retrieve the number of tasks to store
     * into the hard disk.
     * @return Number of tasks to be saved.
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Helper function used by the storage object to format all tasks to be saved.
     * @param i Index number of task to be formatted.
     * @return Task formatted for saving.
     */
    public String getSaveTaskFormat(int i) {
        return this.tasks.get(i).getSaveFormat();
    }
    public String clear() {
        this.cachedTasks = this.tasks;
        this.tasks = new ArrayList<>();
        return "Task list has been reset :o";
    }
    public String undoClear() {
        this.tasks = this.cachedTasks;
        return "Cleared task list has been restored :o";
    }
}
