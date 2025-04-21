package test.controllers;

import controllers.InMemoryTaskManager;
import controllers.Managers;
import org.junit.jupiter.api.Test;
import  model.Task;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagersTest {

    @Test
    void checkGetDefault() {
        Managers managers = new Managers();
        InMemoryTaskManager test = new InMemoryTaskManager();
        assertEquals( test.toString(), managers.getDefault().toString(),
                "Проверьте метод getDefault()");
    }

    @Test
    void checkGetDefaultHistory() {
        Managers managers = new Managers();
        ArrayList<Task> test = new ArrayList<>();
        assertEquals(test.toString(), managers.getDefaultHistory().toString(),
                "Проверьте метод getDefaultHistory()");
    }
}