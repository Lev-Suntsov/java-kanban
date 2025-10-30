package controllers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import model.Epic;
import model.Status;
import model.Task;
import model.Subtask;

public interface TaskManager {
    void addNewTask(Task task) throws IOException;

    void addNewEpic(Epic epic) throws IOException;

    void addNewSubtask(Subtask subtask) throws IOException;


    ArrayList<Task> getTasksValues();

    ArrayList<Epic> getEpicsValues();

    ArrayList<Subtask> getSubtasksValues();


    void removeTaskById(int id) throws IOException;

    void removeEpicById(int id) throws IOException;

    void removeSubtaskById(int id, int epicId) throws IOException;


    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);


    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();


    void updateTask(int id, String name, String description, Status.TaskStatus status);

    void updateSubtask(int id, String name, String description, int epicId, Status.TaskStatus status);

    void updateEpic(int id, String name, String decdription);

    Status.TaskStatus updateEpicStatus(int id);

    ArrayList<Task> getHistory();
}




