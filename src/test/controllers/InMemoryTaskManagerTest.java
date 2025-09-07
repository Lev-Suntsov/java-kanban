package test.controllers;

import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import model.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;

class InMemoryTaskManagerTest {
    Task task;
    Epic epic;
    Subtask subtask;
    InMemoryTaskManager taskManager;
    ArrayList<Task> testTasks = new ArrayList<>();
    ArrayList<Epic> testEpics = new ArrayList<>();
    ArrayList<Subtask> testSubtasks = new ArrayList<>();

    @BeforeEach
    void createAll() {
        task = new Task("testTaskName", "testTaskDescriptionTask");
        epic = new Epic("testEpicName", "testEpicDescriptionTask");
        subtask = new Subtask("testSubtaskName", "testSubtaskDescriptionTask",
                epic.getId());
        taskManager = new InMemoryTaskManager();
        testTasks.clear();
        testEpics.clear();
        testSubtasks.clear();
    }

    @Test
    void checkAddNewTask() throws IOException {
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTask(task.getId()), "В методе добавления" +
                " задачи допущена ошибка");
    }

    @Test
    void checkAddNewEpic() throws IOException {
        taskManager.addNewEpic(epic);
        assertEquals(epic, taskManager.getEpic(epic.getId()), "В методе добавления" +
                " Эпика допущена ошибка");
    }

    @Test
    void checkAddNewSubtask() throws IOException {
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()),
                "В методе добавления подзадачи допущена ошибка");
    }

    @Test
    void checkGetTasksValues() throws IOException{
        taskManager.addNewTask(task);
        testTasks.add(task);
        assertEquals(testTasks, taskManager.getTasksValues(), "Метод получения" +
                " списка задач работает некорректно");
    }

    @Test
    void checkGetEpicsValues() throws  IOException{
        taskManager.addNewEpic(epic);
        testEpics.add(epic);
        assertEquals(testEpics, taskManager.getEpicsValues(), "Метод получения" +
                " списка эпика работает некорректно");
    }

    @Test
    void checkGetSubtasksValues() throws  IOException{
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        testSubtasks.add(subtask);
        assertEquals(testSubtasks, taskManager.getSubtasksValues(), "Метод получения" +
                " подзадачи эпиков работает некорректно");
    }

    @Test
    void checkRemoveTaskById() throws  IOException{
        taskManager.addNewTask(task);
        taskManager.removeTaskById(task.getId());
        assertEquals(testTasks, taskManager.getTasksValues(), "Метод удаления" +
                " задачи работает некорректно");
    }

    @Test
    void checkRemoveEpicById() throws  IOException{
        taskManager.addNewEpic(epic);
        taskManager.removeEpicById(epic.getId());
        assertEquals(testEpics, taskManager.getEpicsValues(), "Метод удаления" +
                " эпика работает некорректно");
    }

    @Test
    void chekRemoveSubtaskById() throws  IOException{
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.removeSubtaskById(subtask.getId(), epic.getId());
        assertEquals(testSubtasks, taskManager.getSubtasksValues(), "Метод удаления" +
                " подзадачи работает некорректно");
        assertEquals(testSubtasks, epic.getSubtaskIds(), "Подзадача не " +
                "удаляется внутри эпика");
    }

    @Test
    void checkGetTask() throws IOException{
        taskManager.addNewTask(task);
        assertEquals(task, taskManager.getTask(task.getId()), "Метод получения " +
                "задачи работает некорректно");
    }

    @Test
    void checkGetEpic() throws  IOException
    {
        taskManager.addNewEpic(epic);
        assertEquals(epic, taskManager.getEpic(epic.getId()), "Метод получения " +
                "эпика работает некорректно");
    }

    @Test
    void checkGetSubtask() throws  IOException{
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void checkDeleteTasks() throws  IOException{
        taskManager.addNewTask(task);
        taskManager.deleteTasks();
        assertEquals(testTasks, taskManager.getTasksValues(), "Метод очищения списка" +
                " задач работает некорректно");
    }

    @Test
    void checkDeleteSubtasks() throws  IOException{
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.deleteSubtasks();
        assertEquals(testSubtasks, taskManager.getSubtasksValues(), "Метод очищения списка" +
                " подзадач работает некорректно");
    }

    @Test
    void checkDeleteEpics() throws  IOException{
        taskManager.addNewEpic(epic);
        taskManager.deleteEpics();
        assertEquals(testEpics, taskManager.getEpicsValues(), "Метод очищения списка" +
                " эпиков работает некорректно");
    }

    @Test
    void checkUpdateTask() throws  IOException{
        taskManager.addNewTask(task);
        taskManager.updateTask(task.getId(), "testTaskName2", "testTaskDescriptionTask2",
                Status.TaskStatus.DONE);
        assertEquals(taskManager.getTask(task.getId()), task, "Ошибка в " +
                "обновлении задачи");
    }

    @Test
    void checkUpdateSubtask() throws  IOException{
        taskManager.addNewEpic(epic);
        subtask.setEpicId(epic.getId());
        taskManager.addNewSubtask(subtask);
        taskManager.updateSubtask(subtask.getId(), "testSubtaskName2", "testSubtaskDescriptionTask2",
                epic.getId(), Status.TaskStatus.DONE);
        assertEquals(taskManager.getSubtask(subtask.getId()), subtask, "Проблема" +
                " с обновлением подзадачи");
    }

    @Test
    void checkUpdateEpic() throws  IOException{
        Epic testEpic = new Epic("testEpicName2", "testEpicDescriptionTask2");
        taskManager.addNewEpic(epic);
        taskManager.updateEpic(epic.getId(), testEpic.getName(), testEpic.getDescription());
        assertEquals(epic, taskManager.getEpic(epic.getId()), "Проблема" +
                " с обновлением эпика");
    }

    @Test
    <T extends Task> void checkGetHistory() throws IOException{
        taskManager.addNewTask(task);
        taskManager.getTask(task.getId());
        ArrayList<Task> testHistory = new ArrayList<>();
        testHistory.add(task);
        assertEquals(testHistory, taskManager.getHistory(), "Ошибка в сохранении " +
                "истории просмотра");


    }
}