package test.controllers;


import org.junit.jupiter.api.Test;

import model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

class InMemoryTaskManagerTest extends TaskMenegerTest {

    @Test
    public void cheskStartAndEndTimeintersection() throws IOException {
        taskManager.deleteSubtasks();
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        epic.startTime = LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 4, 36);
        taskManager.addNewSubtask(new Subtask("testSubtaskName", "testSubtaskDescriptionTask", epic.getId(), LocalDateTime.of(2025, Month.OCTOBER, 17, 10, 4, 36), Duration.ZERO));
        taskManager.addNewSubtask(new Subtask("тест", "подзадача 2", epic.getId(), LocalDateTime.of(2025, Month.OCTOBER, 16, 12, 4, 35), Duration.ZERO));
        ArrayList testArrayList = new ArrayList<>();
        testArrayList.add(subtask.toString());
        assertEquals(taskManager.getSubtasksValues(), testArrayList);
    }
}