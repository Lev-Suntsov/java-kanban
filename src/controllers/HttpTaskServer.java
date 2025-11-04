package controllers;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpTaskServer {
    HttpExchange httpExchange;
    public HttpTaskServer(HttpExchange httpExchange){
        this.httpExchange = httpExchange;
    }
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.start();
    }

    public void orprocessingUriAndMetods() throws InterruptedException, IOException {
        Managers managers = new Managers();
        Task task;
        Subtask subtask;
        Gson gson = new Gson();
        Epic epic;
        BaseHttpHandler baseHttpHandler = new BaseHttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {

            }
        };
        if (httpExchange.getRequestURI().toString().equals("/tasks") && httpExchange.getRequestMethod().toString().equals("GET")) {
            managers.getDefault().getTasksValues();
            baseHttpHandler.sendHasInteractions(httpExchange, "Запрос отработал корректно");
        } else if (httpExchange.getRequestURI().toString().equals("/tasks/{id}") && httpExchange.getRequestMethod().toString().equals("GET")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/tasks/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            Task unswer = managers.getDefault().getTask(id);
            if (unswer == null) {
                baseHttpHandler.sendNotFound(httpExchange, "Задача не найдена");
            } else {
                gson = new Gson();
                String jsonObject = gson.toJson(unswer.toString());
                baseHttpHandler.sendText(httpExchange, jsonObject);
            }
        } else if (httpExchange.getRequestURI().toString().equals("/tasks") && httpExchange.getRequestURI().toString().equals("POST")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/tasks/(.*)");
            Matcher matcher = pattern.matcher(uri);
            String body = httpExchange.getRequestBody().toString();
            task = gson.fromJson(body, Task.class);
            if (managers.getDefault().getTasksValues().contains(task.getId())) {
                if (matcher.find()) {
                    managers.getDefault().updateTask(task.getId(), task.getName(), task.getDescription(), task.getStatus());
                    byte[] resp = "Задача успешно обновлена".getBytes(StandardCharsets.UTF_8);
                    httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    httpExchange.sendResponseHeaders(201, resp.length);
                    httpExchange.getResponseBody().write(resp);
                    httpExchange.close();
                } else {
                    managers.getDefault().addNewTask(task);
                    byte[] resp = gson.toJson(task).getBytes(StandardCharsets.UTF_8);
                    httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    httpExchange.sendResponseHeaders(201, resp.length);
                    httpExchange.getResponseBody().write(resp);
                    httpExchange.close();
                }
            } else {
                baseHttpHandler.sendHasInteractions(httpExchange, "Задача пересекается по времени с другой");
            }
        } else if (httpExchange.getRequestURI().toString().equals("/tasks/{id}") && httpExchange.getRequestMethod().toString().equals("DELETE")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/tasks/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            managers.getDefault().removeTaskById(id);
            baseHttpHandler.sendText(httpExchange, "Задача " + id + "  удалена");
        }
        else if(httpExchange.getRequestURI().toString().equals("/subtasks") && httpExchange.getRequestMethod().toString().equals("GET")){
            baseHttpHandler.sendText(httpExchange, gson.toJson(managers.getDefault().getSubtasksValues().toString()));
        } else if(httpExchange.getRequestURI().toString().equals("subtasks{id}") && httpExchange.getRequestMethod().toString().equals("GET")){
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/subtask/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            subtask =  managers.getDefault().getSubtask(id);
            if(subtask == null){
                baseHttpHandler.sendNotFound(httpExchange, "данной подзадачи нет");
            } else {
                baseHttpHandler.sendText(httpExchange, gson.toJson(subtask));
            }
        } else if (httpExchange.getRequestURI().toString().equals("/subtasks") && httpExchange.getRequestMethod().toString().equals("Post")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/subtask/(.*)");
            Matcher matcher = pattern.matcher(uri);
            String body = httpExchange.getRequestBody().toString();
            subtask = gson.fromJson(body, Subtask.class);
            if (managers.getDefault().getSubtasksValues().contains(subtask.getId())) {
                if (matcher.find()) {
                    managers.getDefault().updateTask(subtask.getId(), subtask.getName(), subtask.getDescription(), subtask.getStatus());
                    byte[] resp = "Подзадача успешно обновлена".getBytes(StandardCharsets.UTF_8);
                    httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    httpExchange.sendResponseHeaders(201, resp.length);
                    httpExchange.getResponseBody().write(resp);
                    httpExchange.close();
                } else {
                    managers.getDefault().addNewTask(subtask);
                    byte[] resp = gson.toJson(subtask).getBytes(StandardCharsets.UTF_8);
                    httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    httpExchange.sendResponseHeaders(201, resp.length);
                    httpExchange.getResponseBody().write(resp);
                    httpExchange.close();
                }
            } else {
                baseHttpHandler.sendHasInteractions(httpExchange, "Подзадача пересекается пео времени с другой");
            }
        } else if (httpExchange.getRequestURI().toString().equals("/subtasks/{id}") && httpExchange.getRequestMethod().toString().equals("GET")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/subtask/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            baseHttpHandler.sendText(httpExchange, gson.toJson(managers.getDefault().getSubtask(id).toString()));
        } else if(httpExchange.getRequestURI().toString().equals("/subtasks/{id}") && httpExchange.getRequestMethod().toString().equals("DELETE")){
            String body = httpExchange.getRequestBody().toString();
            subtask = gson.fromJson(body, Subtask.class);
            managers.getDefault().removeSubtaskById(subtask.getId(), subtask.getEpicId());
            baseHttpHandler.sendHasInteractions(httpExchange, "подзадача " + subtask.getId() + " удалена");
        } else if (httpExchange.getRequestURI().toString().equals("/epics/") && httpExchange.getRequestMethod().toString().equals("GET")) {
            baseHttpHandler.sendText(httpExchange, gson.toJson(managers.getDefault().getEpicsValues().toString()));
        } else if (httpExchange.getRequestURI().toString().equals("/epics/{id}") && httpExchange.getRequestMethod().toString().equals("GET")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/epics/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            epic = managers.getDefault().getEpic(id);
            if(!managers.getDefault().getEpicsValues().contains(epic.getId())){
                baseHttpHandler.sendNotFound(httpExchange, "Данного эпика нет");
            } else{
                baseHttpHandler.sendText(httpExchange, gson.toJson(epic));
            }
        } else if (httpExchange.getRequestURI().toString().equals("/epics{id}/subtask") && httpExchange.getRequestMethod().toString().
                equals("GET")) {
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/epics/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            epic = managers.getDefault().getEpic(id);
            if(!managers.getDefault().getEpicsValues().contains(epic.getId())){
                baseHttpHandler.sendNotFound(httpExchange, "Данного эпика нет");
            } else{
                baseHttpHandler.sendText(httpExchange, gson.toJson(epic));
            }
        } else if(httpExchange.getRequestURI().toString().equals("/epics") && httpExchange.getResponseBody().toString().equals("POST")){
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/subtask/(.*)");
            Matcher matcher = pattern.matcher(uri);
            String body = httpExchange.getRequestBody().toString();
            epic = gson.fromJson(body, Epic.class);
            if (matcher.find()) {
                managers.getDefault().updateTask(epic.getId(), epic.getName(), epic.getDescription(), epic.getStatus());
                byte[] resp = "Подзадача успешно обновлена".getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                httpExchange.sendResponseHeaders(201, resp.length);
                httpExchange.getResponseBody().write(resp);
                httpExchange.close();
            } else {
                managers.getDefault().addNewEpic(epic);
                byte[] resp = gson.toJson(epic).getBytes(StandardCharsets.UTF_8);
                httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                httpExchange.sendResponseHeaders(201, resp.length);
                httpExchange.getResponseBody().write(resp);
                httpExchange.close();
            }
        } else if(httpExchange.getRequestURI().toString().equals("/epics/{id}")&& httpExchange.getResponseBody().toString().equals("DELETE")){
            String uri = httpExchange.getRequestURI().toString();
            Pattern pattern = Pattern.compile("/epics/(.*)");
            Matcher matcher = pattern.matcher(uri);
            int id = Integer.getInteger(matcher.toString());
            managers.getDefault().removeEpicById(id);
            baseHttpHandler.sendText(httpExchange, "Эпик " + id + " удалён");
        } else if(httpExchange.getRequestURI().equals("/history") && httpExchange.getRequestMethod().toString().equals("GET")){
            baseHttpHandler.sendText(httpExchange, gson.toJson(managers.getDefault().getHistory()));
        } else if (httpExchange.getRequestURI().equals("/prioritized") && httpExchange.getRequestMethod().equals("GET")) {
            InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
            baseHttpHandler.sendText(httpExchange, gson.toJson(inMemoryTaskManager.getPrioritizedTasks()));
        }
    }
}