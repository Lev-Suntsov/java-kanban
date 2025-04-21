package test.controllers;

import controllers.InMemoryHistoryManager;
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
        InMemoryHistoryManager test = new InMemoryHistoryManager();
        assertEquals(test.getClass(), managers.getDefaultHistory().getClass(),
                "Проверьте метод getDefaultHistory()");
    }
}