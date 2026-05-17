package university.exception;

/// Thrown when a student hits the maximum allowed retakes for a course.
///
/// University policy limits how many times you can retake
/// the same course.
public class RetakeLimitExceededException extends RuntimeException {

    public RetakeLimitExceededException(String message) {
        super(message);
    }

    public RetakeLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
