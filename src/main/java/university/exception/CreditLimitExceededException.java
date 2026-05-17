package university.exception;

/// Thrown when a student tries to exceed their allowed credit load.
///
/// Credits are capped per semester — this stops over-enrollment.
public class CreditLimitExceededException extends RuntimeException {

    public CreditLimitExceededException(String message) {
        super(message);
    }

    public CreditLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
