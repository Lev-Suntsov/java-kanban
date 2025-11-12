package test.model;

import model.Epic;
import model.Status;
import model.Subtask;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import  controllers.InMemoryTaskManager;

import java.io.IOException;
import java.io.ObjectInputFilter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

class EpicTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Subtask subtask;
    Epic epic;
    Subtask subtask2;
    @BeforeEach
    public void creatEpic()throws IOException {
        Duration duration = Duration.ZERO;
        LocalDateTime startTime = LocalDateTime.now();
        epic = new Epic("Уборка дома", "Уборка", startTime, duration);
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Помыть посуду", "Беру губку", epic.getId(),
                startTime, duration.plusHours(1));
        subtask2 = new Subtask("тест", "Подзадача2", epic.getId(),
                LocalDateTime.now(),Duration.ZERO.plusSeconds(LocalDateTime.now().getSecond()));
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);
    }

    @Test
    public  void checkGetSubtaskIds() throws IOException{
        ArrayList<Integer> testSubtaskIds= new ArrayList<>();
        testSubtaskIds.add(subtask.getId());
        testSubtaskIds.add(subtask2.getId());
        assertEquals(testSubtaskIds, epic.getSubtaskIds(), "id не совпадают");
    }

    @Test
    public  void checkCleanSubtaskIds() {
        ArrayList<Integer> testSubtaskIds= new ArrayList<>();
        epic.cleanSubtaskIds();
        assertEquals(testSubtaskIds, epic.getSubtaskIds(), "Списки не совпадают");
    }

    @Test
    public void checkId() {
        Epic epic2 = taskManager.getEpicsValues().get(epic.getId() - 1);
        assertEquals(epic, epic2, "Объекты не совпадают");
        taskManager.deleteSubtasks();
    }

    @Test
    public void checkStatusNEW() {
        assertEquals(epic.getStatus().toString(), "NEW");
        taskManager.deleteSubtasks();
    }

    @Test
    public void checkStatusDone() {
        taskManager.updateSubtask(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getEpicId(),
                Status.TaskStatus.DONE);
        taskManager.updateSubtask(subtask2.getId(), subtask2.getName(), subtask2.getDescription(),
                subtask2.getEpicId(), Status.TaskStatus.DONE);
        assertEquals(epic.getStatus(), Status.TaskStatus.DONE);
        taskManager.deleteSubtasks();
    }

    @Test
    public  void checkStatusInprogres() {
        taskManager.updateSubtask(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getEpicId(),
                Status.TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2.getId(), subtask2.getName(), subtask2.getDescription(),
                subtask2.getEpicId(), Status.TaskStatus.IN_PROGRESS);
        assertEquals(epic.getStatus(), Status.TaskStatus.IN_PROGRESS);
        taskManager.deleteSubtasks();
    }

    @Test
    public void checkStatusNewAndDone() {
        taskManager.updateSubtask(subtask2.getId(), subtask2.getName(), subtask2.getDescription(),
                subtask2.getEpicId(), Status.TaskStatus.DONE);
        assertEquals(epic.getStatus(), Status.TaskStatus.IN_PROGRESS);
        taskManager.deleteSubtasks();
    }
}