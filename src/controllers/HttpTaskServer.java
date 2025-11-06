package controllers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static final int PORT = 8080;

    private final HttpServer server;
    private final Managers manager = new Managers();

    public HttpTaskServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtasksHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
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
        new HttpTaskServer().start();
    }
}
