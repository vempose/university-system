package university.exception;

public class RetakeLimitExceededException extends RuntimeException {

    public RetakeLimitExceededException(String message) {
        super(message);
    }

    public RetakeLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
