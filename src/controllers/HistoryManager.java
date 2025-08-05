package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.Map;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Integer> getHistory();

    void remove(int id);
}
