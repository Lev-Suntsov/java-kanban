package controllers;

import java.util.Comparator;

import model.*;

public class StartTimeComporator implements Comparator<Task> {
    public StartTimeComporator() {

    }

    @Override
    public int compare(Task o1, Task o2) {
        return o1.startTime.compareTo(o2.startTime);
    }
}
