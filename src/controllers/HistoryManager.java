package controllers;
import model.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void add(int id);
    ArrayList<Task> getHistory();
    void remove(int id);
}
