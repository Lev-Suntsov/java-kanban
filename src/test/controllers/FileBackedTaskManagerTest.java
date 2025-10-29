package test.controllers;
import controllers.FileBackedTaskManager;
import exceptions.ManagerSaveExeption;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public  class FileBackedTaskManagerTest extends TaskMenegerTest {
    static FileBackedTaskManager manager;
    Epic testEpic;

    @BeforeEach
    void creatreFileBackedTaskManager() {
        manager = new FileBackedTaskManager();
        testEpic = new Epic("Тестовый эпик", "Всего лишь тест ", LocalDateTime.now(), Duration.ZERO);
    }
    @Test
    public void testException() {
        Assertions.assertThrows(ManagerSaveExeption.class, () -> {
            manager.addNewSubtask(subtask);
        }, "Извините, список подзадач пустой");
    }

    @Test
    public void checkAddNewTask() throws IOException {
        Task task = new Task("Привет", "Это тестовое описание", LocalDateTime.now(),
                Duration.ZERO);
        manager.addNewTask(task);
        File tempFile = File.createTempFile("testTask", "txt");
        try (Writer fileWrite = new FileWriter(tempFile)) {
            fileWrite.write(task.toString());
        }
        try (Reader fileReader = new FileReader(tempFile)) {
            StringBuilder fileContent = new StringBuilder();
            int ch;
            while ((ch = fileReader.read()) != -1) {
                fileContent.append((char) ch);
            }
            assertEquals(task.toString(), fileContent.toString());
        }
    }

    @Test
    public void checkAddNewEpic() throws IOException {
        manager.addNewEpic(testEpic);
        File testEpicFile = File.createTempFile("testEpic", "txt");
        try (Writer fileWriter = new FileWriter(testEpicFile)) {
            fileWriter.write(testEpic.toString());
        }
        try (Reader fileReader = new FileReader(testEpicFile)) {
            StringBuilder stringFromtestEpicFile = new StringBuilder();
            int ch;
            while ((ch = fileReader.read()) != -1) {
                stringFromtestEpicFile.append((char) ch);
            }
            assertEquals(testEpic.toString(), stringFromtestEpicFile.toString());
        }

    }

    @Test
    public void checkAddNewSubtask() throws IOException {
        manager.addNewEpic(testEpic);
        Subtask testSubtask = new Subtask("Тестовая подзадача", "Проводится тест добавления подзадачи",
                testEpic.getId(), LocalDateTime.now(), Duration.ZERO);
        manager.addNewSubtask(testSubtask);
        File testSubtaskFile = File.createTempFile("testSubtask", "txt");
        try(Writer writer = new FileWriter(testSubtaskFile)){
           writer.write(testSubtask.toString());
        }
        try(Reader fileReader = new FileReader(testSubtaskFile)){
            StringBuilder stringFromTestSubtaskFile = new StringBuilder();
            int ch;
            while((ch = fileReader.read()) != -1){
                stringFromTestSubtaskFile.append((char) ch);
            }
            assertEquals(testSubtask.toString(), stringFromTestSubtaskFile.toString());
        }
    }
}



