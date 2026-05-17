package university.exception;

/// Thrown when a non-researcher tries to join a research project.
///
/// Only users with a `ResearchProfile` can be added
/// as members of `ResearchProject`.
public class NonResearcherJoinProjectException extends RuntimeException {

    public NonResearcherJoinProjectException(String message) {
        super(message);
    }

    public NonResearcherJoinProjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
