package test.http;

import controllers.Managers;
import http.HttpTaskServer;
import org.junit.jupiter.api.*;

import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.time.LocalDateTime;



@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HistoryHandlerTest {
    private HttpTaskServer server;
    private int taskId = -1;
    Managers managers = new Managers();

    @BeforeAll
    void startServer() throws IOException, InterruptedException {
        server = new HttpTaskServer(managers);
        server.start();

        // Добавляем задачу, чтобы потом попасть в историю
        HttpClient client = HttpClient.newHttpClient();
        LocalDateTime st = LocalDateTime.of(2025, 10, 20, 10, 00);
        String json = String.format("{\"name\":\"TaskForHistory\",\"description\":\"Desc\",\"id\":0,\"startTime\":\"%s\"}", st.toString());
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        client.send(req, HttpResponse.BodyHandlers.ofString());

        // Получаем id задачи
        HttpRequest getTasks = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks")).GET().build();
        HttpResponse<String> resp = client.send(getTasks, HttpResponse.BodyHandlers.ofString());
        String body = resp.body();
        int idx = body.indexOf("\"id\":") + 5;
        int commaIdx = body.indexOf(",", idx);
        String idStr = body.substring(idx, commaIdx).trim();    // <- здесь ломается!
        taskId = Integer.parseInt(idStr);

        // Осуществляем просмотр задачи
        HttpRequest getTask = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/" + taskId)).GET().build();
        client.send(getTask, HttpResponse.BodyHandlers.ofString());
    }

    @AfterAll
    void stopServer() {
        server.stop();
    }

    @Test
    void testGetHistory() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/history")).GET().build();
        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());

        Assertions.assertEquals(200, response.statusCode(), "Не удалось получить историю");
        Assertions.assertTrue(response.body().contains("" + taskId), "История должна содержать id просмотренной задачи");
    }
}
