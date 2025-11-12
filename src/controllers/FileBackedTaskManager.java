package controllers;

import exceptions.ManagerSaveExeption;
import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private ArrayList<String> typeOfTask = new ArrayList<>();

    protected void save() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("SavedTask"))) {
            switch (typeOfTask.getLast()) {
                case "Task":
                    if (super.getTasksValues() == null) {
                        new ManagerSaveExeption("извините, список задач пустой", null);
                    } else {
                        writer.write(getTasksValues().getLast().toString());
                    }
                    break;
                case "Epic":
                    if (super.getEpicsValues().isEmpty()) {
                        throw new ManagerSaveExeption("Извините, список Эпиков пустой", null);
                    } else {
                        writer.write(getEpicsValues().getLast().toString());
                    }

                    break;
                case "Subtask":
                    if (super.getSubtasksValues().isEmpty()) {
                        new ManagerSaveExeption("Извините, список подзадач пустой", null);
                    } else {
                        writer.write(getSubtasksValues().getLast().toString());
                    }
            }
        } catch (ManagerSaveExeption e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addNewSubtask(Subtask subtask) throws IOException {
        if(subtask != null) {
            typeOfTask.add("Subtask");
            super.addNewSubtask(subtask);
            save();
        } else {
            throw new IllegalArgumentException("subtask не должен быть null");
        }

    }

    @Override
    public void addNewEpic(Epic epic) throws IOException {
        if(epic != null) {
            typeOfTask.add("Epic");
            super.addNewEpic(epic);
            save();
        }else {
            throw new IllegalArgumentException("epic не должен быть null");
        }
    }

    @Override
    public void addNewTask(Task task) throws IOException {
        if (task != null) {
            typeOfTask.add("Task");
            super.addNewTask(task);
            save();
        } else {
            throw new IllegalArgumentException("epic не должен быть null");
        }
    }

    @Override
    public void removeEpicById(int id) throws IOException {
        typeOfTask.remove("Epic");
        super.removeEpicById(id);
        save();
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parch = line.split(": ");
                String[] time = parch[6].split(".");
                String[] duriator = parch[6].split(".");
                if (parch[0].equals("Task")) {
                    Task task = new Task(parch[1], parch[2], LocalDateTime.of(Integer.getInteger(time[0]), Integer.getInteger(time[1]), Integer.getInteger(time[2]), Integer.getInteger(time[3]), Integer.getInteger(time[4])), Duration.ofDays(Integer.getInteger(duriator[0])).plusMinutes(Integer.getInteger(duriator[2])).plusHours(Integer.getInteger(duriator[1])));
                    if (parch[4].equals("IN_PROGRESS")) {
                        task.setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                    if (parch[4].equals("Done")) {
                        task.setStatus(Status.TaskStatus.DONE);
                    }
                    task.setId(Integer.getInteger(parch[3]));
                    manager.addNewTask(task);
                }
                if (parch[0].equals("Epic")) {
                    Epic epic = new Epic(parch[1], parch[2], LocalDateTime.of(Integer.getInteger(time[0]), Integer.getInteger(time[1]), Integer.getInteger(time[2]), Integer.getInteger(time[3]), Integer.getInteger(time[4])), Duration.ofDays(Integer.getInteger(duriator[0])).plusMinutes(Integer.getInteger(duriator[2])).plusHours(Integer.getInteger(duriator[1])));
                    if (parch[4].equals("IN_PROGRESS")) {
                        epic.setStatus(Status.TaskStatus.IN_PROGRESS);
                    }
                    if (parch[4].equals("Done")) {
                        epic.setStatus(Status.TaskStatus.DONE);
                    }
                    epic.setId(Integer.getInteger(parch[3]));
                    manager.addNewEpic(epic);
                }
                if (parch[0].equals("Subtask")) {
                    for (Subtask element : manager.getSubtasksValues()) {
                        if (Integer.getInteger(parch[5]) == element.getEpicId()) {
                            Subtask subtask = new Subtask(parch[1], parch[2], Integer.getInteger(parch[5]),
                                    LocalDateTime.of(Integer.getInteger(time[0]), Integer.getInteger(time[1]), Integer.getInteger(time[2]), Integer.getInteger(time[3]), Integer.getInteger(time[4])), Duration.ofDays(Integer.getInteger(duriator[0])).plusMinutes(Integer.getInteger(duriator[2])).plusHours(Integer.getInteger(duriator[1])));
                            if (parch[4].equals("IN_PROGRESS")) {
                                subtask.setStatus(Status.TaskStatus.IN_PROGRESS);
                            }
                            if (parch[4].equals("Done")) {
                                subtask.setStatus(Status.TaskStatus.DONE);
                            }
                            subtask.setId(Integer.getInteger(parch[3]));
                            manager.addNewSubtask(subtask);
                            break;
                        } else if (element == manager.getSubtasksValues().getLast()) {
                            System.out.println("К сожалению данный эпик не существует");
                        }
                    }
                }
            }
        }
        return manager;
    }
}