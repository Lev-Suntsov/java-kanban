package test.http;

import controllers.Managers;
import http.HttpTaskServer;
import org.junit.jupiter.api.*;

import java.net.URI;
import java.net.http.*;
import java.io.IOException;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class TaskHandlerTest {
    private HttpTaskServer server;
    int taskId;
    Managers managers = new Managers();

    @BeforeAll
    void startServer() throws IOException {
        server = new HttpTaskServer(managers);
        server.start();
    }

    @AfterAll
    void stopServer() {
        server.stop();
    }

    @Test
    void testAddTask() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime start = LocalDateTime.of(2025, 10, 16, 12, 4, 35);
        String json = String.format(
                "{\"name\":\"Task1\",\"description\":\"Desc1\",\"id\":0,\"startTime\":\"%s\"}", start.toString());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).
                header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Ошибка в добавлении задачи");
        HttpRequest getEpics = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).GET().build();
        HttpResponse<String> getResponse = client.send(getEpics, HttpResponse.BodyHandlers.ofString());
        String responseBody = getResponse.body();
        int firstIdIdx = responseBody.indexOf("\"id\":") + 5;
        int commaIdx = responseBody.indexOf(",", firstIdIdx);
        String idString = responseBody.substring(firstIdIdx, commaIdx).trim();
        taskId = Integer.parseInt(idString);
    }

    @Test
    void testGetTasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Ошибка в получении задачи");
    }

    @Test
    void testUpdateTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String json = "{\"name\":\"Task1Updated\",\"description\":\"Desc1Updated\",\"id\":1}";
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").
                POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Task should be updated successfully");

    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + taskId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Ошибка в удалении задачи");
    }

    @Test
    void testGetNonExistentTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/999")).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode(), "Тест работает неправильно");
    }
}
