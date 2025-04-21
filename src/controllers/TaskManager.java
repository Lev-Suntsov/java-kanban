package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import model.Epic;
import  model.Status;
import model.Task;
import model.Subtask;

public interface TaskManager {
 int addNewTask(Task task);
     int addNewEpic(Epic epic);
     int addNewSubtask(Subtask subtask);


     ArrayList<Task>getTasksValues();
     ArrayList<Epic>getEpicsValues();
     ArrayList<Subtask>getSubtasksValues();


     void removeTaskById(int id);
     void removeEpicById(int id);
     void removeSubtaskById(int id, int epicId);


     Task getTask(int id);
     Epic getEpic(int id);
     Subtask getSubtask(int id);


     void deleteTasks();
     void deleteSubtasks();
     void deleteEpics();


     void updateTask(int id,String name, String description, Status.TaskStatus status);

     void updateSubtask(int id, String name, String description, int epicId, Status.TaskStatus status);

     void updateEpic(int id, String name,String decdription);

     Status.TaskStatus updateEpicStatus(int id);
     ArrayList<Task> getHistory();

}




