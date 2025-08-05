package controllers;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Integer> tasks = new ArrayList<>();
    HashMap<Integer, Node> nodeMap = new HashMap<>();
    Node last;
    Node first;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (nodeMap.containsValue(task)) {
            removeNode(task.getId());
        }
        linkLast(task);
        nodeMap.put(task.getId(), last);
    }

    @Override
    public void remove(int id) {
        removeNode(id);
    }

    @Override
    public ArrayList<Integer> getHistory() {
        getTasks();
        return tasks;
    }

    @Override
    public String toString() {
        return "Класс HistoryManager";
    }

    private void linkLast(Task task) {
        final Node node = new Node(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    private void getTasks() {
        ArrayList<Integer> tasksLinkedListToArrayList = new ArrayList<>();
        for (Integer i : nodeMap.keySet()) {
            tasksLinkedListToArrayList.add(i);
        }
        tasks = tasksLinkedListToArrayList;
    }

    private void removeNode(int id) {
        tasks.remove(nodeMap.get(id));
        nodeMap.remove(id);

    }
}
