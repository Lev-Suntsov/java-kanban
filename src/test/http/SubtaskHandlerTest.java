package test.http;

import controllers.Managers;
import http.HttpTaskServer;
import org.junit.jupiter.api.*;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class SubtaskHandlerTest {
    private HttpTaskServer server;
    private int epicId = -1;
    private int subtaskId = -1;
    Managers managers = new Managers();

    @BeforeAll
    void startServer() throws IOException, InterruptedException {
        server = new HttpTaskServer(managers);
        server.start();

        // Сначала создаём epic, иначе подзадачу нельзя будет создать!
        HttpClient client = HttpClient.newHttpClient();
        String epicJson = "{\"name\":\"EpicForSubtask\",\"description\":\"test description\"}";
        HttpRequest epicReq = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        client.send(epicReq, HttpResponse.BodyHandlers.ofString());

        HttpRequest getEpics = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET().build();
        HttpResponse<String> epicResp = client.send(getEpics, HttpResponse.BodyHandlers.ofString());
        String responseBody = epicResp.body();
        int firstIdIdx = responseBody.indexOf("\"id\":") + 5;
        int commaIdx = responseBody.indexOf(",", firstIdIdx);
        String idString = responseBody.substring(firstIdIdx, commaIdx).trim();
        epicId = Integer.parseInt(idString);
    }

    @AfterAll
    void stopServer() {
        server.stop();
    }

    @Test
    void testAddSubtask() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime st = LocalDateTime.of(2025, 10, 17, 14, 25);
        String json = String.format(
                "{\"name\":\"Subtask1\",\"description\":\"SubtaskDesc\",\"id\":0,\"epicId\":%d,\"startTime\":\"%s\",\"duration\":\"PT1H\"}",
                epicId, st.toString());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Не удалось добавить подзадачу");

        HttpRequest getAll = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET().build();
        HttpResponse<String> allResp = client.send(getAll, HttpResponse.BodyHandlers.ofString());
        String subtasksList = allResp.body();
        int idIdx = subtasksList.indexOf("\"id\":") + 5;
        int commaIdx = subtasksList.indexOf(",", idIdx);
        String idStr = subtasksList.substring(idIdx, commaIdx).trim();
        subtaskId = Integer.parseInt(idStr);
    }

    @Test
    void testGetAllSubtasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest getAll = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET().build();
        HttpResponse<String> response = client.send(getAll, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().contains("Subtask1"));
    }

    @Test
    void testGetSubtaskById() throws Exception {
        Assumptions.assumeTrue(subtaskId > 0);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtaskId))
                .GET().build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertTrue(response.body().contains("Subtask1"));
    }

    @Test
    void testUpdateSubtask() throws Exception {
        Assumptions.assumeTrue(subtaskId > 0);
        HttpClient client = HttpClient.newHttpClient();
        String json = String.format(
                "{\"name\":\"Subtask1Updated\",\"description\":\"UpdateDesc\",\"id\":%d,\"epicId\":%d}", subtaskId, epicId);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode());
    }

    @Test
    void testDeleteSubtask() throws Exception {
        Assumptions.assumeTrue(subtaskId > 0);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtaskId))
                .DELETE().build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Не удалось удалить подзадачу");
    }

    @Test
    void testSubtaskNotFound() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/99999"))
                .GET().build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode());
    }
}
