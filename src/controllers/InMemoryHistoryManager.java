package controllers;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager{
    private ArrayList<Task> tasks = new ArrayList<>();
    @Override
    public void add(Task task){
        if (task == null){
            return;
        }
        if(tasks.size() > 10){
            ArrayList<Task> tasks2 = new ArrayList<>();
            for(int i = tasks.size() - 10; i < tasks.size(); i++){
                tasks2.add(tasks.get(i));
            }
            tasks = tasks2;
        }
        tasks.add(task);
    }
    @Override
    public ArrayList<Task> getHistory(){
        return tasks;
    }
    @Override
    public String toString(){
        return "Класс HistoryManager";
    }
}
