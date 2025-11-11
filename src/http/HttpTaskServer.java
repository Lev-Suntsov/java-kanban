package http;

import com.sun.net.httpserver.HttpServer;
import controllers.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;

    private final HttpServer server;
    private final Managers managers;

    public HttpTaskServer(Managers managers) throws IOException {
        this.managers = managers;
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(managers));
        server.createContext("/tasks", new TaskHandler(managers));
        server.createContext("/subtasks", new SubtasksHandler(managers));
        server.createContext("/epics", new EpicHandler(managers));
        server.createContext("/history", new HistoryHandler(managers));
        server.createContext("/prioritized", new PrioritizedHandler(managers));
    }

    public void start() {
        server.start();
        System.out.println("HTTP server started on port " + PORT);
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTP server stopped");
    }

    public static void main(String[] args) throws IOException {
        Managers manager = new Managers();
        new HttpTaskServer(manager).start();
    }
}
