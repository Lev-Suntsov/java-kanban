package test.http;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import controllers.Managers;
import http.HttpTaskServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrioritizedHandlerTest {
    private HttpTaskServer server;
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
}
