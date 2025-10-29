package test.model;

import controllers.InMemoryTaskManager;
import model.Epic;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Subtask subtask;
    Epic epic;

    @BeforeEach
    public void createSubtaskForeTest() throws IOException {
        epic = new Epic("Уборка дома", "Уборка", LocalDateTime.now(), Duration.ZERO.plusSeconds(
                LocalDateTime.now().getSecond()));
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Помыть посуду", "Беру губку", epic.getId(), LocalDateTime.now(),
                Duration.ZERO.plusSeconds(LocalDateTime.now().getSecond()));
        taskManager.addNewSubtask(subtask);
        taskManager.deleteSubtasks();
        taskManager.deleteEpics();
    }

    @Test
    void checkGetEpicId() {
        int testGetEpicId = epic.getId();
        assertEquals(testGetEpicId, subtask.getEpicId(), "Списки не совпадают");
    }

    @Test
    public void chheckId(){
        Subtask subtask2 = taskManager.getSubtask(epic.getSubtaskIds().get(subtask.getId() - 2));
        assertEquals(subtask, subtask2, "Объекты не совпадают");
        taskManager.deleteSubtasks();
    }
}