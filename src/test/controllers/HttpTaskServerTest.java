package test.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import controllers.HttpTaskServer;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;

public class HttpTaskServerTest {
    static HttpExchange httpExchange;
    Managers manager = new Managers();
    public HttpTaskServerTest(HttpExchange httpExchange){
        this.httpExchange = httpExchange;
    }
    static HttpTaskServer httpTaskServer = new HttpTaskServer(httpExchange);
    @BeforeEach
    public void startServer()throws IOException {
        String[] args = new String[5];
        httpTaskServer.main(args);
    }
    @Test
    public  void orprocessingUriAndMetodsAddTaskCheck() throws  IOException{
        if(httpExchange.getRequestURI().toString().equals("/tasks") && httpExchange.getRequestMethod().toString().equals("GET")){
            JsonElement jsonElement = JsonParser.parseString(httpExchange.getRequestBody().toString());
            if(jsonElement.isJsonArray()){
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                ArrayList<Task> unswer = manager.getDefault().getTasksValues();
                assertEquals(unswer, jsonArray);
            }
        }
    }
}
