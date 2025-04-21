package controllers;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int generatorId = 0;
    InMemoryHistoryManager iistoryManager = new InMemoryHistoryManager();

    @Override
    public int addNewTask(Task task) {
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }
    @Override
    public int addNewEpic(Epic epic) {
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }
    @Override
    public int addNewSubtask(Subtask subtask, int epicsid) {
        final int id = ++generatorId;
        subtask.setId(id);
        subtask.setStatus(Status.TaskStatus.NEW);
        subtasks.put(id, subtask);
        epics.get(epicsid).getSubtaskIds().add(id);
        updateEpicStatus(epicsid);
        return id;
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
    }
    @Override
    public void removeEpicById(int id) {
        for (int subtaskId : subtasks.keySet()) {
            for (int removedId : epics.get(id).getSubtaskIds()) {
                if (subtaskId == removedId) {
                    subtasks.remove(subtaskId);
                }
            }
        }
        epics.remove(id);
    }
    @Override
    public void removeSubtaskById(int id, int epicId) {
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
        iistoryManager.getHistory().add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public Epic getEpic(int id) {
        iistoryManager.getHistory().add(epics.get(id));
        return epics.get(id);
    }
    @Override
    public Subtask getSubtask(int id) {
        iistoryManager.getHistory().add(subtasks.get(id));
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
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() ==
                            Status.TaskStatus.NEW) {
                        epics.get(id).setStatus(Status.TaskStatus.NEW);
                    } else {
                        epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                }
            } else if (subtasks.get(epics.get(id).getSubtaskIds().get(i)).getStatus() == Status.TaskStatus.DONE) {
                if (i + 1 < epics.get(id).getSubtaskIds().size()) {
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() ==
                            Status.TaskStatus.DONE) {
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
    public<T  extends Task> ArrayList<T> getHistory(){
        ArrayList<T> history = new ArrayList<>();
        for(int i = 0; i< iistoryManager.getHistory().size(); i++){
            history.add((T) iistoryManager.getHistory().get(i));
        }
        return  history;
    }
    @Override
    public String toString(){
        return "controllers.InMemoryTaskManager";
    }
}