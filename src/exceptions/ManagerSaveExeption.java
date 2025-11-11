package exceptions;

public class ManagerSaveExeption extends RuntimeException {
    public ManagerSaveExeption(String message, Exception cause) {
        super(message, cause);
    }
}
