import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class TaskManager {
    public static void main(String[] args) {
        HashMap<Integer, Epic> epics = new HashMap<>();
        HashMap<Integer, Task> tasks = new HashMap<>();
        HashMap<Integer, Subtask> subtasks = new HashMap<>();
        int id = 1;
        while (true) {
            System.out.println("Введите номер команды, которую хотите выполнить:\n1 - добавить задачу\n2 - увидеть список задач\n3 - удалить задачу по id" +
                    "\n4 - найти задачу по id\n5 - Удалить все задачи\n6 - Обновить задачу \n0 - Выход");
            Scanner scanner = new Scanner(System.in);
            int command = scanner.nextInt();
            switch (command) {
                case 1:
                    System.out.println("Введите тип задачи - Таск, Эпик, или Подзадача");
                    String type = scanner.nextLine();
                    type = scanner.nextLine();
                    switch (type) {
                        case "Таск":
                            System.out.println("Введите имя задачи");
                            String tasksName = scanner.nextLine();

                            System.out.println("Введите описание задачи");
                            String tasksDescription = scanner.nextLine();
                            Task task = new Task(tasksName, tasksDescription);
                            tasks.put(task.getId(), task);
                            break;
                        case "Эпик":
                            System.out.println("Введите название Эпика");
                            String epicsName = scanner.nextLine();
                            System.out.println("Введите описание");
                            String epicsDescription = scanner.nextLine();
                            Epic epic = new Epic(epicsName, epicsDescription);
                            epics.put(epic.getId(), epic);
                            break;
                        case "Подзадача":
                            System.out.println("Введите название подзадачи");
                            String name = scanner.nextLine();
                            System.out.println("Введите описание");
                            String description = scanner.nextLine();
                            System.out.println("Введите id эпика");
                            int epicId = scanner.nextInt();
                            Subtask subtask = new Subtask(name, description, epicId, epics);
                            subtasks.put(subtask.getId(), subtask);
                            if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.NEW) {
                                for (int i = 0; i < epics.get(epicId).getIdSubtask().size(); i++) {
                                    if (subtasks.get(epics.get(epicId).getIdSubtask().get(i)).getStatus() == Status.TaskStatus.NEW) {
                                        epics.get(epicId).setStatus(Status.TaskStatus.NEW);
                                    } else {
                                        epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                                        break;
                                    }
                                }
                            } else if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.DONE) {
                                for (int i = 0; i < epics.get(epicId).getIdSubtask().size(); i++) {
                                    if (subtasks.get(epics.get(epicId).getIdSubtask().get(i)).getStatus() == Status.TaskStatus.DONE) {
                                        epics.get(epicId).setStatus(Status.TaskStatus.DONE);
                                    } else {
                                        epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                                        break;
                                    }
                                }
                            } else {
                                epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                            }
                            break;
                    }
                    break;
                case 2:
                    System.out.println("Какой тип вы хотите увидеть? Task, Epic или Subtask?");
                    type = scanner.next();
                    switch (type) {
                        case "Task":
                            for (Integer object : tasks.keySet()) {
                                System.out.println(tasks.get(object).toString());
                            }
                            break;
                        case "Epic":
                            for (Integer object : epics.keySet()) {
                                System.out.println(epics.get(object).toString());
                            }
                            break;
                        case "Subtask":
                            for (Integer object : subtasks.keySet()) {
                                System.out.println(subtasks.get(object).toString());
                            }
                            break;
                    }
                    break;
                case 3:
                    System.out.println("Введите тип задачи - Таск, Эпик, или Подзадача");
                    type = scanner.next();
                    switch (type) {
                        case "Таск":
                            System.out.println("Введите id");
                            Integer tasksId = scanner.nextInt();
                            for (Integer object : tasks.keySet()) {
                                if (object == tasksId) {
                                    tasks.remove(object);
                                    System.out.println("Задача удаленв");
                                }
                                break;
                            }
                        case "Эпик":
                            System.out.println("Введите id");
                            int epicId = scanner.nextInt();
                            for (Integer object : epics.keySet()) {
                                if (object == epicId) {
                                    epics.remove(object);
                                    System.out.println("Эпик удалён");
                                }
                            }
                            break;
                        case "Подзадача":
                            System.out.println("Введите id");
                            int subtaskId = scanner.nextInt();
                            for (Integer object : subtasks.keySet()) {
                                if (object == subtaskId) {
                                    subtasks.remove(object);
                                }
                            }
                            break;
                    }
                    break;
                case 4:
                    System.out.println("Какой тип задачи вы хотите найти Таск, Эпик или Подзадача?");
                    type = scanner.next();
                    switch (type) {
                        case "Таск":
                            System.out.println("Введите id задачи");
                            int taskId = scanner.nextInt();
                            for (Integer object : tasks.keySet()) {
                                if (object == taskId) {
                                    System.out.println(tasks.get(object).toString());
                                }
                            }
                            break;
                        case "Эпик":
                            System.out.println("Введите id");
                            int epicId = scanner.nextInt();
                            System.out.println(epics.get(epicId).toString());
                            break;
                        case "Подзадача":
                            System.out.println("Введите id");
                            int subtaskId = scanner.nextInt();
                            System.out.println(subtasks.get(subtaskId).toString());
                            break;
                    }
                    break;
                case 5:
                    System.out.println("Список задач какого типа вы хотите очистить Task, Epic, или subtask?");
                    type = scanner.next();
                    switch (type) {
                        case "Task":
                            tasks.clear();
                            System.out.println("Список очищен");
                            break;
                        case "Epic":
                            epics.clear();
                            System.out.println("Список очищен");
                            break;
                        case "Subtask":
                            subtasks.clear();
                            System.out.println("Список очищен");
                            break;
                    }
                    break;
                case 6:
                    System.out.println("Введите тип задачи, которые нужно обновить: Task, Epic, или Subtask");
                    type = scanner.next();
                    switch (type) {
                        case "Task":
                            System.out.println("что вы хотите изменить: Имя, описание, или статус?");
                            String overridable = scanner.next();
                            switch (overridable) {
                                case "Имя":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое имя");
                                    String name = scanner.nextLine();
                                    name = scanner.nextLine();
                                    tasks.get(id).setName(name);
                                    break;
                                case "описание":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое описание");
                                    String description = scanner.nextLine();
                                    description = scanner.nextLine();
                                    tasks.get(id).setDescription(description);
                                    break;
                                case "статус":
                                    Status.TaskStatus status;
                                    System.out.println("Введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Доступные статусы:" + "/n" + Status.TaskStatus.NEW.toString() + "/n" + Status.TaskStatus.DONE.toString()
                                            + "/n" + Status.TaskStatus.IN_PROGRESS.toString() + "/nВыберите нужный");
                                    String statusType = scanner.nextLine();
                                    statusType = scanner.nextLine();
                                    if (statusType.equals(Status.TaskStatus.NEW.toString())) {
                                        status = Status.TaskStatus.NEW;
                                    } else if (statusType.equals(Status.TaskStatus.DONE.toString())) {
                                        status = Status.TaskStatus.DONE;
                                    } else {
                                        status = Status.TaskStatus.IN_PROGRESS;
                                    }
                                    tasks.get(id).setStatus(status);
                                    break;
                            }
                            break;
                        case "Subtask":
                            System.out.println("что вы хотите изменить: Имя, описание, или статус?");
                            overridable = scanner.next();
                            switch (overridable) {
                                case "Имя":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое имя");
                                    String name = scanner.nextLine();
                                    name = scanner.nextLine();
                                    subtasks.get(id).setName(name);
                                    break;
                                case "описание":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое описание");
                                    String description = scanner.nextLine();
                                    description = scanner.nextLine();
                                    subtasks.get(id).setDescription(description);
                                    break;
                                case "статус":
                                    Status.TaskStatus status;
                                    System.out.println("Введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Доступные статусы:" + "\n" + Status.TaskStatus.NEW.toString() + "\n" + Status.TaskStatus.DONE.toString()
                                            + "\n" + Status.TaskStatus.IN_PROGRESS.toString() + "\nВыберите нужный");
                                    String statusType = scanner.next();
                                    if (statusType.equals(Status.TaskStatus.NEW.toString())) {
                                        status = Status.TaskStatus.NEW;
                                    } else if (statusType.equals(Status.TaskStatus.DONE.toString())) {
                                        status = Status.TaskStatus.DONE;
                                    } else {
                                        status = Status.TaskStatus.IN_PROGRESS;
                                    }
                                    subtasks.get(id).setStatus(status);
                                    System.out.println("Введите id эпика такой подзадачей");
                                    int epicId = scanner.nextInt();
                                    if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.NEW) {
                                        for (int i = 1; i < epics.get(epicId).getIdSubtask().size(); i++) {
                                            if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.NEW) {
                                                epics.get(epicId).setStatus(Status.TaskStatus.NEW);
                                            } else {
                                                epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                                                break;
                                            }
                                        }
                                    } else if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.DONE) {
                                        for (int i = 1; i < epics.get(epicId).getIdSubtask().size(); i++) {
                                            if (subtasks.get(epics.get(epicId).getIdSubtask().get(0)).getStatus() == Status.TaskStatus.DONE) {
                                                epics.get(epicId).setStatus(Status.TaskStatus.DONE);
                                            } else {
                                                epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                                                break;
                                            }
                                        }
                                    } else {
                                        epics.get(epicId).setStatus(Status.TaskStatus.IN_PROGRESS);
                                    }
                                    break;
                            }
                            break;
                        case "Epic":
                            System.out.println("что вы хотите изменить: Имя или описание?");
                            overridable = scanner.next();
                            switch (overridable) {
                                case "Имя":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое имя");
                                    String name = scanner.nextLine();
                                    name = scanner.nextLine();
                                    epics.get(id).setName(name);
                                    break;
                                case "описание":
                                    System.out.println("введите id");
                                    id = scanner.nextInt();
                                    System.out.println("Введите новое описание");
                                    String description = scanner.nextLine();
                                    description = scanner.nextLine();
                                    epics.get(id).setDescription(description);
                                    break;
                            }
                            break;
                    }
                    break;
                case 0:
                    return;
            }
        }
    }
}




