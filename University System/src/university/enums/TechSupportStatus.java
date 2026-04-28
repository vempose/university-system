package university.enums;

public enum TechSupportStatus {
    NEW("New"),
    VIEWED("Viewed"),
    ACCEPTED("Accepted"),
    IN_PROGRESS("In Progress"),
    DONE("Done"),
    REJECTED("Rejected");
	
    private final String displayName;
    TechSupportStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
