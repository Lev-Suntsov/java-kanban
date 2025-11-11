package http;

import com.sun.net.httpserver.HttpExchange;
import controllers.Managers;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(Managers manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String pach = exchange.getRequestURI().getPath();
        if(method.equals("GET")&& pach.equals("/history")){
            List<Integer> list = manager.getDefaultHistory().getHistory();
            sendText(exchange, gson.toJson(list), 200);
        }
    }
}
