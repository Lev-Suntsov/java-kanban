package controllers;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements  HistoryManager{
    ArrayList<Task> tasks = new ArrayList<>();
    @Override
    public void add(Task task){
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
