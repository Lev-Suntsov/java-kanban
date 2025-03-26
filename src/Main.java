import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя задачи");
        String name = scanner.nextLine();
        System.out.println("Введите описание");
        String description = scanner.nextLine();
        Task task = new Task(name, description);
        taskManager.addNewTask(task);
        System.out.println(taskManager.getTasksValues().toString());


        System.out.println("Введите имя эпика");
        name = scanner.nextLine();
        System.out.println("Введите описание");
        description = scanner.nextLine();
        Epic epic = new Epic(name, description);
        taskManager.addNewEpic(epic);
        System.out.println(taskManager.getEpicsValues().toString());


        System.out.println("Введите имя подзадачи");
        name = scanner.nextLine();
        System.out.println("Введите описание");
        description = scanner.nextLine();
        System.out.println("Введите id Эпика");
        int epicsId = scanner.nextInt();
        Subtask subtask = new Subtask(name, description, epicsId, taskManager.getEpics());
        taskManager.addNewSubtask(subtask, epicsId);
        System.out.println(taskManager.getSubtasksValues().toString());

        System.out.println("Введите id изменяемой задачи");
        int id = scanner.nextInt();
        System.out.println("Введите новое имя");
        name = scanner.nextLine();
        System.out.println("Введите новое описание");
        description = scanner.nextLine();
        System.out.println("Введите статус задачи");
        String statusIn = scanner.nextLine();
        Status.TaskStatus status = Status.TaskStatus.NEW;
        for(Status.TaskStatus value : Status.TaskStatus.values()){
            if(value.equals(statusIn)){
                status = value;
            }
        }
        taskManager.updateTask(id, name, description, status);
        System.out.println(taskManager.getTasksValues().toString());


        System.out.println("Введите имя новой подзадачи");
        name = scanner.nextLine();
        System.out.println("Введите описание новой подзадачи");
        description = scanner.nextLine();
        Subtask subtask2 = new Subtask(name, description, epicsId, taskManager.getEpics());


        System.out.println("Введите id подзадачи, которую хотите изменить");
        id = scanner.nextInt();
        System.out.println("Введите новое имя");
        name = scanner.nextLine();
        System.out.println("Введите описание");
        description = scanner.nextLine();

        System.out.println("Сейчас у одной подзадачи статус новый, у второй статус поменяем на progress." +
                "Сейчас статус эпика =" + taskManager.getEpics().get(epicsId).getStatus().toString());
        taskManager.updateSubtask(id,name,description,epicsId,Status.TaskStatus.IN_PROGRESS);
        System.out.println("Теперь статус = " + taskManager.getEpics().get(epicsId).getStatus().toString());
        System.out.println("Поменяем статусы обеих подзадачь на DONE");
        taskManager.updateSubtask(id,name,description,epicsId,Status.TaskStatus.IN_PROGRESS);
        System.out.println("Введите новое id");
        id = scanner.nextInt();
        System.out.println("Введите новое имя");
        name = scanner.nextLine();
        System.out.println("Введите описание");
        description = scanner.nextLine();
        taskManager.updateSubtask(id, name, description, epicsId, Status.TaskStatus.DONE);
        System.out.println("Теперь статус эпика = " + taskManager.getEpics().get(id).getStatus().toString());


        System.out.println("Введите id удаляемой задачи");
        id = scanner.nextInt();
        System.out.println("Сейчас список = " + taskManager.getTasksValues().toString());
        taskManager.removeTaskById(id);
        System.out.println("Теперь - " +taskManager.getTasksValues().toString());

        System.out.println("Теперь введите id удаляемой подзадачи");
        id = scanner.nextInt();
        System.out.println("Сейчас список = " + taskManager.getSubtasksValues().toString());
        taskManager.removeSubtaskById(id);
        System.out.println("Теперь список = " + taskManager.getSubtasksValues().toString());

        System.out.println("Теперь очистим одной командой весь список подзадач");
        System.out.println("Список до очищения " + taskManager.getSubtasksValues().toString());
        taskManager.deleteSubtasks();
        System.out.println("Список после очищения " + taskManager.getSubtasksValues().toString());

        System.out.println("И наконец очистим список эпиков");
        System.out.println("До очищения: " + taskManager.getEpicsValues().toString());
        taskManager.deleteEpics();
        System.out.println("И после: " + taskManager.getEpicsValues().toString());
        System.out.println("Тесты закончены!!");
    }
}
