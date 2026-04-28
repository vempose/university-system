package university.enums;

public enum EmployeeRequestStatus {
    PENDING("Pending"),
    SIGNED("Signed"),
    REJECTED("Rejected");
    private final String displayName;
    EmployeeRequestStatus(String displayName) {
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
