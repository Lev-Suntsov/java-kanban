package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements  HistoryManager{
    private ArrayList<Integer> tasks = new ArrayList<>();
    List tasksLinkedList = new LinkedList();
    HashMap<Integer, Node> taskPlaysInLinkedList = new HashMap<>();
    @Override
    public void add(int id){
        if (id == 0){
            return;
        }
        if(tasks.size() > 10){
            ArrayList<Integer> tasks2 = new ArrayList<>();
            for(int i = tasks.size() - 10; i < tasks.size(); i++){
                tasks2.add(tasks.get(i));
            }
            tasks = tasks2;
        }
        linkLast(id);
    }
    @Override
    public void remove(int id) {
        removeNode(taskPlaysInLinkedList.get(id));
    }
    @Override
    public ArrayList getHistory(){
        return tasks;
    }
    @Override
    public String toString(){
        return "Класс HistoryManager";
    }
    public void linkLast(int id){
        if(taskPlaysInLinkedList.containsKey(id)){
            removeNode(taskPlaysInLinkedList.get(id));
        }
        tasksLinkedList.addLast(id);
        Node node = new Node(tasksLinkedList.size() - 1);
        add(id, node);
        getTasks();
    }
    public void getTasks(){
        ArrayList tasksLinkedListToArrayList = new ArrayList<>();
        for (int i = 0; i < tasksLinkedList.size(); i++){
            tasksLinkedListToArrayList.add(tasksLinkedList.get(i));
        }
        tasks = tasksLinkedListToArrayList;
    }
    public void add(int id, Node node){
        taskPlaysInLinkedList.put(id, node);
    }
    public void removeNode (Node node){
        tasks.remove(tasksLinkedList.get(node.place));
        taskPlaysInLinkedList.remove(tasksLinkedList.get(node.place));
        tasksLinkedList.remove(node.place);
    }
}
