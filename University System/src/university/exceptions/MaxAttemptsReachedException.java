package university.exceptions;

public class MaxAttemptsReachedException extends Exception {
    private final String courseCode;
    private static final int MAX_ATTEMPTS = 3;
    public MaxAttemptsReachedException(String courseCode){
        super(String.format(
            "Max attempts reached for course '%s': a student may not fail more than %d times.", courseCode, MAX_ATTEMPTS));
        this.courseCode = courseCode;
    }

    public String getCourseCode() { 
    	return courseCode; }
}
