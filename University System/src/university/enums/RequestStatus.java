package university.enums;

public enum RequestStatus {
    PENDING("Pending"),
    SIGNED("Signed"),
    REJECTED("Rejected"),
    NEW("New"),
    VIEWED("Viewed"),
    ACCEPTED("Accepted"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String displayName;
    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}
