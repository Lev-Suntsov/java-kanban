package http;

import com.sun.net.httpserver.HttpExchange;
import controllers.Managers;
import model.Subtask;

import java.io.IOException;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler {
    public SubtasksHandler(Managers manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String pach = exchange.getRequestURI().getPath();

            if (method.equals("GET") && pach.equals("/subtasks")) {
                List<Subtask> list = manager.getDefault().getSubtasksValues();
                sendText(exchange, gson.toJson(list), 200);
            } else if (method.equals("GET") && pach.matches("//subtasks/\\d=")) {
                int id = Integer.parseInt(pach.split("/")[2]);
                Subtask subtask = manager.getDefault().getSubtask(id);
                if (subtask != null) {
                    sendText(exchange, gson.toJson(subtask), 200);
                } else {
                    sendNotFound(exchange, "Подзадача не найдена");
                }
            } else if (method.equals("POST") && pach.equals("subtasks")) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (subtask.getId() != 0) {
                    manager.getDefault().updateSubtask(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getEpicId(), subtask.getStatus());
                    sendText(exchange, "Подзадача обновлена", 201);
                } else {
                    if (!manager.getDefault().intersectionStartTime(subtask)) {
                        manager.getDefault().addNewSubtask(subtask);
                        sendText(exchange, "Подзадача добавлена", 200);
                    } else {
                        sendHasInteractions(exchange, "Подзадача пересекается по времени с другой");
                    }
                }
            } else if (method.equals("DELETE") && pach.matches("/subtasks/\\d+")) {
                int id = Integer.parseInt(pach.split("/")[2]);
                Subtask subtask = manager.getDefault().getSubtask(id);
                manager.getDefault().removeSubtaskById(id, subtask.getEpicId());
                sendText(exchange, "Подзадача " + id + " удалена", 200);
            } else {
                sendNotFound(exchange, "Неизвестный эндпоинт");
            }
        } catch (Exception e) {
            sendInternalError(exchange, e.getMessage());
        }
    }
}
