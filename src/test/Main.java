package test;

import controllers.FileBackedTaskManager;
import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import model.Epic;
import model.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    static FileBackedTaskManager manager;

    public static void main(String[] args) throws IOException {
        System.out.println("Приветствую");
        manager = new FileBackedTaskManager();
        File file = new File("SavedTask");
        manager = FileBackedTaskManager.loadFromFile(file);
    }
}
