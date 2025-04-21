package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import model.Epic;
import  model.Status;
import model.Task;
import model.Subtask;

public interface TaskManager {
    public int addNewTask(Task task);
     public int addNewEpic(Epic epic);
     public int addNewSubtask(Subtask subtask, int epicsid);


     public ArrayList<Task>getTasksValues();
     public ArrayList<Epic>getEpicsValues();
     public ArrayList<Subtask>getSubtasksValues();


     public void removeTaskById(int id);
     public void removeEpicById(int id);
     public void removeSubtaskById(int id, int epicId);


     public Task getTask(int id);
     public Epic getEpic(int id);
     public Subtask getSubtask(int id);


     public void deleteTasks();
     public void deleteSubtasks();
     public void deleteEpics();


     public void updateTask(int id,String name, String description, Status.TaskStatus status);

     public void updateSubtask(int id, String name, String description, int epicId, Status.TaskStatus status);

     public void updateEpic(int id, String name,String decdription);

     public Status.TaskStatus updateEpicStatus(int id);
    public<T  extends Task> ArrayList<T> getHistory();

}




