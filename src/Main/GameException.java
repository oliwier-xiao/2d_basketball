package Main;

public class GameException extends RuntimeException {
    public GameException(String message) {
        super("Game Error: " + message);
    }

    public GameException(String message, Throwable cause) {
        super("Game Error: " + message, cause);
    }
}