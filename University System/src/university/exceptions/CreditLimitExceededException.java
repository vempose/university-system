package university.exceptions;

public class CreditLimitExceededException extends Exception {
    private final int currentCredits;
    private final int requestedCredits;
    private static final int MAX_CREDITS =21;
    public CreditLimitExceededException(int currentCredits, int requestedCredits){
        super(String.format(
            "Credit limit exceeded: cannot add %d credits (current: %d, max: %d).",requestedCredits, currentCredits, MAX_CREDITS));
        this.currentCredits = currentCredits;
        this.requestedCredits = requestedCredits;
    }
    public int getCurrentCredits() { 
    	return currentCredits; }
    public int getRequestedCredits() { 
    	return requestedCredits; }
}
