package test.controllers;

import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import javax.imageio.IIOException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager inMemoryHistoryManager;
    Task task;
    Epic epic = new Epic("testEpicName", "testEpicDescriptionTask", LocalDateTime.now(),
            Duration.ZERO);
    Subtask subtask;

    @BeforeEach
    void createInMemoryHistoryManager() {
        inMemoryHistoryManager = new InMemoryHistoryManager();
        task = new Task("tetTaskName", "TeskTaskDescription", LocalDateTime.now(),
                Duration.ZERO);
        subtask = new Subtask("", "", epic.getId(), LocalDateTime.now(), Duration.ZERO);
    }

    @Test
    void checkAdd() {
        inMemoryHistoryManager.add(task);
        ArrayList<Integer> testHistory = new ArrayList<>();
        testHistory.add(task.getId());
        assertEquals(testHistory, inMemoryHistoryManager.getHistory());
    }

    @Test
    void checkGetHistory() {
        inMemoryHistoryManager.add(task);
        ArrayList<Integer> testHistory = new ArrayList<>();
        testHistory.add(task.getId());
        assertEquals(testHistory, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void delliteFromHistory() {
        inMemoryHistoryManager.add(epic);
        Task task2 = new Task("tetTaskName", "TeskTaskDescription", LocalDateTime.now(),
                Duration.ZERO);
        inMemoryHistoryManager.add(task2);
        ArrayList test = new ArrayList();
        inMemoryHistoryManager.remove(epic.getId());
        test.add(task2.getId());
        assertEquals(test, inMemoryHistoryManager.getHistory());
    }

    @Test
    public void delliteFromHistoryTest2() {
        inMemoryHistoryManager.add(subtask);
        inMemoryHistoryManager.add(epic);
        inMemoryHistoryManager.add(task);
        inMemoryHistoryManager.remove(epic.getId());
        ArrayList testHistory = new ArrayList();
        testHistory.add(subtask.getId());
        testHistory.add(task.getId());
        assertEquals(testHistory, inMemoryHistoryManager.getHistory());
    }

}