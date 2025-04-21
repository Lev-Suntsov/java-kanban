package test.controllers;

import controllers.InMemoryHistoryManager;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach ;

import java.util.ArrayList;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    Task task;
    @BeforeEach
    void createInMemoryHistoryManager(){
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("tetTaskName", "TeskTaskDescription");
    }
    @Test
    void checkAdd() {
        inMemoryHistoryManager.add(task);
        ArrayList<Task> testHistory = new ArrayList<>();
        testHistory.add(task);
        assertEquals(testHistory, inMemoryHistoryManager.getHistory());
    }

    @Test
    void checkGetHistory() {
        inMemoryHistoryManager.add(task);
        ArrayList<Task> testHistory = new ArrayList<>();
        testHistory.add(task);
        assertEquals(testHistory, inMemoryHistoryManager.getHistory());
    }
}