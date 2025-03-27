package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import model.Epic;
import  model.Status;
import model.Task;
import model.Subtask;

public class TaskManager {
     private HashMap<Integer, Epic> epics = new HashMap<>();
     private HashMap<Integer, Task> tasks = new HashMap<>();
     private HashMap<Integer, Subtask> subtasks = new HashMap<>();
     private int generatorId = 0;
     public TaskManager(){

     }
    public int addNewTask(Task task){
        final int id = ++generatorId;
        task.setId(id);
        tasks.put(id, task);
        return id;
     }
     public int addNewEpic(Epic epic){
        final int id = ++generatorId;
        epic.setId(id);
        epics.put(id, epic);
        return id;
     }
     public int addNewSubtask(Subtask subtask, int epicsid){
        final int id = ++generatorId;
        subtask.setId(id);
        subtask.setStatus(Status.TaskStatus.NEW);
        subtasks.put(id, subtask);
        epics.get(epicsid).getSubtaskIds().add(id);
         updateEpicStatus(epicsid);
        return id;
     }


     public ArrayList<Task>getTasksValues(){
        return new ArrayList<>(tasks.values());
     }
     public ArrayList<Epic>getEpicsValues(){
        return new ArrayList<>(epics.values());
     }
     public ArrayList<Subtask>getSubtasksValues(){
        return new ArrayList<>(subtasks.values());
     }


     public void removeTaskById(int id){
        tasks.remove(id);
     }
     public void removeEpicById(int id){
        epics.remove(id);
     }
     public void removeSubtaskById(int id){
        subtasks.remove(id);
     }


     public Task getTask(int id){
        return tasks.get(id);
     }
     public Epic getEpic(int id){
        return epics.get(id);
     }
     public Subtask getSubtask(int id){
        return subtasks.get(id);
     }


     public void deleteTasks(){
        tasks.clear();
     }
     public void deleteSubtasks(){
        for(Epic epic : epics.values()){
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
     }
     public void deleteEpics(){
        epics.clear();
     }


     public void updateTask(int id,String name, String description, Status.TaskStatus status){
        tasks.get(id).setStatus(status);
         tasks.get(id).setName(name);
         tasks.get(id).setDescription(description);
     }

     public void updateSubtask(int id, String name, String description, int epicId, Status.TaskStatus status){
        subtasks.get(id).setName(name);
        subtasks.get(id).setDescription(description);
        subtasks.get(id).setStatus(status);
        updateEpicStatus(epicId);
     }

     public void updateEpic(int id, String name,String decdription){
        epics.get(id).setName(name);
         epics.get(id).setDescription(decdription);
     }

     public Status.TaskStatus updateEpicStatus(int id){
        for( int i = 0; i< epics.get(id).getSubtaskIds().size(); i ++){
            if(subtasks.get(epics.get(id).getSubtaskIds().get(i)).getStatus() == Status.TaskStatus.NEW){
                epics.get(id).setStatus(Status.TaskStatus.NEW);
                if (i + 1 < epics.get(id).getSubtaskIds().size()) {
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() ==
                            Status.TaskStatus.NEW) {
                        epics.get(id).setStatus(Status.TaskStatus.NEW);
                    } else {
                        epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                }
            } else if(subtasks.get(epics.get(id).getSubtaskIds().get(i)).getStatus() == Status.TaskStatus.DONE){
                if (i + 1 < epics.get(id).getSubtaskIds().size()) {
                    if (subtasks.get(epics.get(id).getSubtaskIds().get(i + 1)).getStatus() ==
                            Status.TaskStatus.DONE) {
                        epics.get(id).setStatus(Status.TaskStatus.DONE);
                    } else {
                        epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                }
            } else{
                epics.get(id).setStatus(Status.TaskStatus.IN_PROGRESS);
                break;
            }
            }
        return epics.get(id).getStatus();
     }
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }
}




