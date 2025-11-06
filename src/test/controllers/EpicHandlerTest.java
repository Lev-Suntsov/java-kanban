package test.controllers;

import controllers.HttpTaskServer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class EpicHandlerTest {
        private HttpTaskServer server;
        int epicId;
        @BeforeAll
        void startServer() throws IOException {
            server = new HttpTaskServer();
            server.start();
        }
        @AfterAll
        void stopServer(){
            server.stop();
        }

        @Test
        void testAddEpic() throws IOException, InterruptedException {
            HttpClient client = HttpClient.newHttpClient();
            String json = "{\"name\":\"Эпик1\",\"description\":\"Эпик для тестов\",\"startTime\":null,\\\"duration\\\":null,\"id\":0 }";

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).
                    header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Assertions.assertEquals(201, response.statusCode(), "Ошибка в добавлении эпика");
            HttpRequest getEpics = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics")).GET().build();
            HttpResponse<String> getResponse = client.send(getEpics, HttpResponse.BodyHandlers.ofString());
            String responseBody = getResponse.body();
            int firstIdIdx = responseBody.indexOf("\"id\":") + 5;
            int commaIdx = responseBody.indexOf(",", firstIdIdx);
            String idString = responseBody.substring(firstIdIdx, commaIdx).trim();
            epicId = Integer.parseInt(idString);
        }
        @Test
        void testGetEpicById() throws Exception {
            Assertions.assertTrue(epicId > 0, "Id эпика должен быть сохранён из предыдущих тестов");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/epics/" + epicId))
                    .GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());Assertions.assertEquals(200, response.statusCode(), "Epic не найден");
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
    }

