package http;

import com.sun.net.httpserver.HttpExchange;
import controllers.Managers;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(Managers manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int id;
        try {
            String method = exchange.getRequestMethod();
            String pach = exchange.getRequestURI().getPath();
            final String[] idFinded = pach.split("/");
            if (method.equals("GET") && pach.equals("/epics")) {
                List<Epic> list = manager.getDefault().getEpicsValues();
                sendText(exchange, gson.toJson(list), 200);
            } else if (method.equals("GET") && idFinded.length >= 3) {
                id = Integer.parseInt(idFinded[2]);
                Epic epic = manager.getDefault().getEpic(id);
                if (epic != null) {
                    sendText(exchange, gson.toJson(epic), 200);
                } else {
                    sendNotFound(exchange, "Подзадача не найдена");
                }
            } else if (method.equals("POST") && pach.equals("/epics")) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                Epic epic = gson.fromJson(body, Epic.class);
                if (idFinded.length >= 33) {
                    manager.getDefault().updateEpic(epic.getId(), epic.getName(), epic.getDescription());
                    sendText(exchange, "Подзадача обновлена", 201);
                } else {
                    if (!manager.getDefault().intersectionStartTime(epic)) {
                        manager.getDefault().addNewEpic(epic);
                        sendText(exchange, "Эпик добавлен", 200);
                    } else {
                        sendHasInteractions(exchange, "Эпик пересекается по времени с другой");
                    }
                }
            } else if (method.equals("DELETE") && idFinded.length >= 3) {
                id = Integer.parseInt(idFinded[2]);
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
