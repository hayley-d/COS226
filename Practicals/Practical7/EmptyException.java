public class EmptyException extends Exception {
    public EmptyException(String message) {
        super(message);
    }

    public EmptyException() {
        super("Stack is empty.");
    }
}
