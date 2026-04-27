package university.exception;

public class UnauthorizedRequestSigningException extends RuntimeException {

    public UnauthorizedRequestSigningException(String message) {
        super(message);
    }

    public UnauthorizedRequestSigningException(String message, Throwable cause) {
        super(message, cause);
    }
}
