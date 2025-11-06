package controllers;

import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler{

    public PrioritizedHandler(Managers manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String pach = exchange.getRequestURI().getPath();
        if(method.equals("GET") && pach.equals("/prioritized")){
            TreeSet<Task> listc = manager.getDefault().getPrioritizedTasks();
            sendText(exchange, gson.toJson(listc), 200);
        }
    }
}
