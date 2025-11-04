package controllers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;
public class TaskHandler extends BaseHttpHandler{
    public TaskHandler(TaskManager manager){
        super(manager);
    }
}
