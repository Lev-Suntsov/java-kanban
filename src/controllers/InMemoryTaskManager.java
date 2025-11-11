package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;
    private Scanner scanner = new Scanner(System.in);
    StartTimeComporator comporator = new StartTimeComporator();
    private TreeSet<Task> sortTask = new TreeSet(comporator);
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public void addNewTask(Task task) throws IOException {
        if (!intersectionStartTime(task)) {
            final int id = ++generatorId;
            task.setId(id);
            tasks.put(id, task);
            sortTask.add(task);
        }
    }

    @Override
    public void addNewEpic(Epic epic) throws IOException {
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        if (!subtasks.isEmpty()) {
            LocalDateTime startTime = subtasks.get(epic.getSubtaskIds().getFirst()).startTime;
            Duration duration = Duration.ofDays(0);
            for (int i : epic.getSubtaskIds()) {
                if (subtasks.containsValue(i)) {
                    if (subtasks.get(i).startTime.isBefore(startTime)) {
                        startTime = subtasks.get(i).startTime;
                    }
                    duration = subtasks.get(i).duration.plusDays(duration.toDays()).plusHours(
                            duration.toHours()).plusMinutes(duration.toMinutes());
                }
            }
            epic.startTime = startTime;
            epic.duration = duration;
        }
        if (!subtasks.isEmpty()) {
            epic.setEndTime(subtasks.get(epic.getSubtaskIds().getLast()).getEndTime());
        }
    }

    @Override
    public void addNewSubtask(Subtask subtask) throws IOException {
        if (!intersectionStartTime(subtask)) {
            final int id = ++generatorId;
            subtask.setId(id);
            sortTask.add(subtask);
            subtasks.put(id, subtask);
            epics.get(subtask.getEpicId()).getSubtaskIds().add(id);
            updateEpicStatus(subtask.getEpicId());
            if (epics.get(subtask.getEpicId()).getSubtaskIds().size() == 1) {
                epics.get(subtask.getEpicId()).startTime = subtask.startTime;
                epics.get(subtask.getEpicId()).setEndTime(subtask.getEndTime());
            } else if (subtask.startTime.isBefore(epics.get(subtask.getEpicId()).startTime)) {
                epics.get(subtask.getEpicId()).startTime = subtask.startTime;
            } else if (subtask.getEndTime().isAfter(epics.get(subtask.getEpicId()).getEndTime())) {
                epics.get(subtask.getEpicId()).setEndTime(subtask.getEndTime());
            }
        }
    }

    @Override
    public ArrayList<Task> getTasksValues() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicsValues() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksValues() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        if (historyManager.getHistory().contains(id)) {
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) throws IOException {
        for (int subtaskId : subtasks.keySet()) {
            for (int removedId : epics.get(id).getSubtaskIds()) {
                if (subtaskId == removedId) {
                    subtasks.remove(subtaskId);
                }
            }
        }
        epics.remove(id);
        if (historyManager.getHistory().contains(id)) {
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(int id, int epicId) throws IOException {
        subtasks.remove(id);
        for (int i = 0; i < epics.get(epicId).getSubtaskIds().size(); i++) {
            if (epics.get(epicId).getSubtaskIds().get(i) == id) {
                epics.get(epicId).getSubtaskIds().remove(i);
            }
        }
        updateEpicStatus(epicId);
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
        return tasks.get(id);

    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(tasks.get(id));
        }
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
        }
        return subtasks.get(id);
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            epic.setStatus(Status.TaskStatus.NEW);
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void updateTask(int id, String name, String description, Status.TaskStatus status) {
        tasks.get(id).setStatus(status);
        tasks.get(id).setName(name);
        tasks.get(id).setDescription(description);
    }

    @Override
    public void updateSubtask(int id, String name, String description, int epicId, Status.TaskStatus status) {
        subtasks.get(id).setName(name);
        subtasks.get(id).setDescription(description);
        subtasks.get(id).setStatus(status);
        updateEpicStatus(epicId);
    }

    @Override
    public void updateEpic(int id, String name, String decdription) {
        epics.get(id).setName(name);
        epics.get(id).setDescription(decdription);
    }

    @Override
    public Status.TaskStatus updateEpicStatus(int id) {
        for (int i = 0; i < epics.get(id).getSubtaskIds().size(); i++) {
            if (subtasks.get(epics.get(id).getSubtaskIds().get(i)).getStatus() == Status.TaskStatus.NEW) {
                epics.get(id).setStatus(Status.TaskStatus.NEW);
                if (i + 1 < epics.get(id).getSubtaskIds().size()) {
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() == Status.TaskStatus.NEW) {
                        epics.get(id).setStatus(Status.TaskStatus.NEW);
                    } else {
                        epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                }
            } else if (subtasks.get(epics.get(id).getSubtaskIds().get(i)).getStatus() == Status.TaskStatus.DONE) {
                if (i + 1 < epics.get(id).getSubtaskIds().size()) {
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() == Status.TaskStatus.DONE) {
                        epics.get(id).setStatus(Status.TaskStatus.DONE);
                    } else {
                        epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                }
            } else {
                epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                break;
            }
        }
        return epics.get(id).getStatus();
    }

    @Override
    public ArrayList getHistory() {
        ArrayList history = new ArrayList<>();
        for (int i = 0; i < historyManager.getHistory().size(); i++) {
            history.add(historyManager.getHistory().get(i));
        }
        return history;
    }

    @Override
    public String toString() {
        return "controllers.InMemoryTaskManager";
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return sortTask;
    }

    @Override
    public boolean intersectionStartTime(Task task) {
        boolean isintersection = false;
        if (!getPrioritizedTasks().isEmpty()) {
            for (Task e : getPrioritizedTasks()) {
                if ((e.startTime.isAfter(task.startTime) && e.getEndTime().isBefore(task.getEndTime())) || e.startTime.equals(task.startTime)) {
                    isintersection = true;
                    break;
                }
            }
        }
        return isintersection;
    }
}