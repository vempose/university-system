package university.exception;

public class NonResearcherJoinProjectException extends Exception {

    public NonResearcherJoinProjectException(String message) {
        super(message);
    }

    public NonResearcherJoinProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
