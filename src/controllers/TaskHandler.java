package controllers;
import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.io.IOException;
import java.util.List;
public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(Managers manager){
        super(manager);
    }
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try{
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            if(method.equals("GET") && path.equals("/tasks")){
                List<Task> tasks = manager.getDefault().getTasksValues();
                sendText(exchange, gson.toJson(tasks), 200);
            } else if (method.equals("POST") && path.equals("/tasks")) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Task task = gson.fromJson(body, Task.class);
                if(task.getId() != 0){
                    manager.getDefault().updateTask(task.getId(), task.getName(), task.getDescription(), task.getStatus());
                    sendText(exchange, "Задача обновлена", 201);
                }else {
                    if (!manager.getDefault().intersectionStartTime(task)) {
                        manager.getDefault().addNewTask(task);
                        sendText(exchange, "OK", 201);
                    } else {
                        sendHasInteractions(exchange, "Задача пересекается по времени с другой");
                    }
                }
            }else if(method.equals("GET") && path.matches("/tasks/\\d+")){
                int id = Integer.parseInt(path.split("/")[2]);
                Task task = manager.getDefault().getTask(id);
                if(task == null){
                    sendNotFound(exchange, "задача не найдена");
                } else {
                    sendText(exchange, gson.toJson(task), 200);
                }
            } else if (method.equals("DELETE") && path.matches("/task/\\d+")) {
                int id = Integer.parseInt(path.split("/")[2]);
                manager.getDefault().removeTaskById(id);
                sendText(exchange, "задача " + id + " удалена", 200);
            } else{
                sendNotFound(exchange, "Неизвестный эндпоинт");
            }
        } catch (Exception e){
            sendInternalError(exchange, e.getMessage());
        }
    }
}
