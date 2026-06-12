package university.exceptions;

public class NonResearcherJoinProjectException extends Exception {
    private final String userName;
    public NonResearcherJoinProjectException(String userName){
        super(String.format(
            "User '%s' cannot join a research project: they do not implement the Researcher interface.", userName));
        this.userName = userName;
    }
    public String getUserName() { return userName; }
}
