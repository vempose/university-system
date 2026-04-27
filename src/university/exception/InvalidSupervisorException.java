package university.exception;

public class InvalidSupervisorException extends RuntimeException {

    public InvalidSupervisorException(String message) {
        super(message);
    }

    public InvalidSupervisorException(String message, Throwable cause) {
        super(message, cause);
    }
}
