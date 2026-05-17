package university.exception;

/// Thrown when an invalid supervisor is assigned to a student or project.
///
/// For example, a teacher who doesn't supervise in that field
/// or doesn't have the right qualifications.
public class InvalidSupervisorException extends RuntimeException {

    public InvalidSupervisorException(String message) {
        super(message);
    }

    public InvalidSupervisorException(String message, Throwable cause) {
        super(message, cause);
    }
}
