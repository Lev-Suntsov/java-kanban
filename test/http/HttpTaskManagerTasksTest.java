package http;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import controllers.Managers;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class HttpTaskManagerTasksTest {
    private HttpTaskServer server;
    int epicId;
    Managers manager = new Managers();
    int subtaskId;
    int taskId;

    @BeforeAll
    void startServer() throws IOException, InterruptedException {
        server = new HttpTaskServer(manager);
        server.start();
        HttpClient client = HttpClient.newHttpClient();
        String epicJson = "{\"name\":\"EpicForSubtask\",\"description\":\"test description\"}";
        HttpRequest epicReq = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        client.send(epicReq, HttpResponse.BodyHandlers.ofString());

        HttpRequest getEpics = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).GET().build();
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
    void testAddEpic() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String json = "{\"name\":\"Эпик1\",\"description\":\"Эпик для тестов\",\"startTime\":null,\\\"duration\\\":null,\"id\":0 }";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Ошибка в добавлении эпика");
        epicId = manager.getDefault().getEpicsValues().getLast().getId();
    }

    @Test
    void testGetEpicById() throws Exception {
        Assertions.assertTrue(epicId > 0, "Id эпика должен быть сохранён из предыдущих тестов");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/" + epicId)).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Epic не найден");
        Assertions.assertTrue(response.body().contains("EpicTest"));
    }

    @Test
    void testDeleteEpic() throws Exception {
        Assertions.assertTrue(epicId > 0, "Id эпика должен быть сохранён из предыдущих тестов");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epicId))
                .DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(200, response.statusCode(), "Epic не удалён");
    }

    @Test
    void testEpicNotFound() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/99999"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(404, response.statusCode(), "Не найденный epic не дал ошибку 404");
    }
    @Test
    void testGetHistory() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime st = LocalDateTime.of(2025, 10, 20, 10, 00);
        String json = String.format("{\"name\":\"TaskForHistory\",\"description\":\"Desc\",\"id\":0,\"startTime\":\"%s\"}", st.toString());
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(req, HttpResponse.BodyHandlers.ofString());
        client = HttpClient.newHttpClient();
        req = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/history")).GET().build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        HttpRequest getTasks = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();
        HttpResponse<String> resp = client.send(getTasks, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();
        int idx = body.indexOf("\"id\":") + 5;
        int commaIdx = body.indexOf(",", idx);
        String idStr = body.substring(idx, commaIdx).trim();    // <- здесь ломается!
        int taskId = Integer.parseInt(idStr);
        Assertions.assertEquals(200, response.statusCode(), "Не удалось получить историю");
        Assertions.assertTrue(response.body().contains("" + taskId), "История должна содержать id просмотренной задачи");
    }

    @Test
    void testPrioritizedTasks() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Добавим две задачи с разным временем начала
        String json1 = String.format("{\"name\":\"TaskA\",\"description\":\"DescA\",\"id\":0,\"startTime\":\"%s\"}", LocalDateTime.of(2025, 10, 10, 10, 0));
        String json2 = String.format("{\"name\":\"TaskB\",\"description\":\"DescB\",\"id\":0,\"startTime\":\"%s\"}", LocalDateTime.of(2025, 10, 11, 10, 0));

        HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json1)).build();
        HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json2)).build();

        client.send(request1, HttpResponse.BodyHandlers.ofString());
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        // Получим приоритетный список
        HttpRequest getPrioritized = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/prioritized")).GET().build();
        HttpResponse<String> resp = client.send(getPrioritized, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, resp.statusCode(), "Должен быть статус 200");

        // Проверим, что список содержит обе задачи и они идут в правильном порядке
        JsonArray arr = JsonParser.parseString(resp.body()).getAsJsonArray();
        Assertions.assertTrue(arr.size() >= 2, "Должно быть как минимум две задачи");

        String firstName = arr.get(0).getAsJsonObject().get("name").getAsString();
        String secondName = arr.get(1).getAsJsonObject().get("name").getAsString();
        Assertions.assertEquals("TaskA", firstName, "Первая задача должна быть TaskA (самая ранняя)");
        Assertions.assertEquals("TaskB", secondName, "Вторая задача должна быть TaskB");
    }
    @Test
    void testAddSubtask() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime st = LocalDateTime.of(2025, 10, 17, 14, 25);
        String json = String.format("{\"name\":\"Subtask1\",\"description\":\"SubtaskDesc\",\"id\":0,\"epicId\":%d,\"startTime\":\"%s\",\"duration\":\"PT1H\"}", epicId, st.toString());

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

    @Test
    void testAddTask() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime start = LocalDateTime.of(2025, 10, 16, 12, 4, 35);
        String json = String.format("{\"name\":\"Task1\",\"description\":\"Desc1\",\"id\":0,\"startTime\":\"%s\"}", start.toString());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
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
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(201, response.statusCode(), "Task should be updated successfully");

    }

    @Test
    void testDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/" + taskId)).DELETE().build();
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
