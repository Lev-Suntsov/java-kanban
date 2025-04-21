package test.model;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import  controllers.InMemoryTaskManager;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

class TaskTest {
    Task task;
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    @BeforeEach
    void creatTestTask(){
        task = new Task("testTaskName", "testTaskDescription");
        taskManager.addNewTask(task);
    }

    @Test
    void checkGetName() {
        String testName = "testTaskName";
        assertEquals(testName, task.getName(), "Имя не совпадает");
    }

    @Test
    void checkGetDescription() {
        String testDescription = "testTaskDescription";
        assertEquals(testDescription, task.getDescription(), "Описание не совпадает");
    }

    @Test
    void checkGetStatus() {
        Status.TaskStatus testStatus = Status.TaskStatus.NEW;
        assertEquals(testStatus, task.getStatus(), "Статусы не совпадают");
    }

    @Test
    void checkSetName() {
        String testName = "testTaskName2";
        task.setName(testName);
        assertEquals(testName, task.getName(), "Метод setName работает неправильно");
    }

    @Test
    void checkSetDescription() {
        String testDescription = "testTaskDescription2";
        task.setDescription(testDescription);
        assertEquals(testDescription, task.getDescription(), "Метод setDescription" +
                " работает некорректно");
    }

    @Test
    void checkSetId() {
        int testId = 1234;
        task.setId(testId);
        assertEquals(testId, task.getId(), "В методе setId допущена ошибка");
    }

    @Test
    void checkSetStatus() {
        Status.TaskStatus testStatus = Status.TaskStatus.DONE;
        task.setStatus(testStatus);
        assertEquals(testStatus, task.getStatus(), "Проверь метод setStatus");
    }
    @Test
    public void checkId(){
        Task task2 = taskManager.getTask(task.getId());
        assertEquals(task, task2, "Объекты не совпадают");
    }

}