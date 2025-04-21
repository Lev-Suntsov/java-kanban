package test.model;

import model.Epic;
import model.Subtask;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import  controllers.InMemoryTaskManager;
import java.util.ArrayList;

class EpicTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    Subtask subtask;
    Epic epic;
    @BeforeEach
    public void creatEpic(){
        epic = new Epic("Уборка дома", "Уборка");
        taskManager.addNewEpic(epic);
        subtask = new Subtask("Помыть посуду", "Беру губку", epic.getId());
        taskManager.addNewSubtask(subtask, epic.getId());
    }
    @Test
    public  void checkGetSubtaskIds(){
        ArrayList<Integer> testSubtaskIds= new ArrayList<>();
        testSubtaskIds.add(subtask.getId());
        assertEquals(testSubtaskIds, epic.getSubtaskIds(), "id не совпадают");
    }
    @Test
    public  void checkCleanSubtaskIds(){
        ArrayList<Integer> testSubtaskIds= new ArrayList<>();
        epic.cleanSubtaskIds();
        assertEquals(testSubtaskIds, epic.getSubtaskIds(), "Списки не совпадают");
    }
    @Test
    public void checkId(){
        Epic epic2 = taskManager.getEpicsValues().get(epic.getId() - 1);
        assertEquals(epic, epic2, "Объекты не совпадают");
    }

}