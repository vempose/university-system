package university.exception;

/// Thrown when someone who isn't authorized tries to sign a request.
///
/// Only certain roles (deans, department heads) are allowed
/// to approve or sign off on requests.
public class UnauthorizedRequestSigningException extends RuntimeException {

    public UnauthorizedRequestSigningException(String message) {
        super(message);
    }

    public UnauthorizedRequestSigningException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
