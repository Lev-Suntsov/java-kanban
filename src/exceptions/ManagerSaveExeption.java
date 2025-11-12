package exceptions;

public class ManagerSaveExeption extends RuntimeException {
    public ManagerSaveExeption(String message, Exception cause) {
        super(message, cause != null ? cause : new Exception("Unknown cause"));
    }

    public ManagerSaveExeption(String message) {
        super(message);
    }
}
