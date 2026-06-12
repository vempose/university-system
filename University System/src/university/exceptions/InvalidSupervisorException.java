package university.exceptions;

public class InvalidSupervisorException extends Exception{
    private final int hIndex;
    private static final int MIN_H_INDEX = 3;
    public InvalidSupervisorException(String supervisorName, int hIndex){
        super(String.format(
            "Cannot assign '%s' as supervisor: h-index is %d (minimum required: %d).",supervisorName, hIndex, MIN_H_INDEX));
        this.hIndex = hIndex;
    }

    public int getHIndex() { 
    	return hIndex;}
}
