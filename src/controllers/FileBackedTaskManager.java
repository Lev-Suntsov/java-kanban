package controllers;

import model.Subtask;
import model.Task;
import  model.Epic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
public class FileBackedTaskManager  extends InMemoryTaskManager {
    int reterned;
    static int taskNumber = 0;
    static HashMap<Integer, Task> enumList = new HashMap<>();
    static Writer savedTask;


    public static void save() throws IOException {
        savedTask = new FileWriter("taskFile.txt");
        if (taskNumber - 1 > 0 && enumList.containsKey(taskNumber - 1)) {
            savedTask.write(enumList.get(taskNumber - 1).toString() + " ");
        } else {
            // Логирование или вывод сообщения об ошибке
            System.out.println("Элемент с индексом " + (taskNumber - 1) + " не найден в HashMap.");
        }
    }

    @Override
    public int addNewSubtask(Subtask subtask) throws IOException {
        enumList.put(taskNumber, subtask);
        reterned = super.addNewSubtask(subtask);
        taskNumber++;
        save();
        return reterned;
    }

    @Override
    public int addNewEpic(Epic epic) throws IOException {
        enumList.put(taskNumber, epic);
        reterned = super.addNewEpic(epic);
        taskNumber ++;
        save();
        return reterned;
    }

    @Override
    public int addNewTask(Task task) throws IOException {
        enumList.put(taskNumber, task);
        reterned = super.addNewTask(task);
        taskNumber++;
        save();
        return reterned;
    }

    public static Reader FileBackedTaskManager() throws IOException {
        Reader fileReader = new FileReader("taskFile.txt");
        return fileReader;
    }
}