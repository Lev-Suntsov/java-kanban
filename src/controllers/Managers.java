package controllers;

import model.Task;

import java.util.ArrayList;

public class Managers {
    public <T extends InMemoryHistoryManager> T getDefault(){
        T object = (T) new InMemoryHistoryManager();
        return object;
    }
    public ArrayList<Task> getDefaultHistory(){
        InMemoryHistoryManager iistoryManager = new InMemoryHistoryManager();
        return iistoryManager.getHistory();
    }
}
