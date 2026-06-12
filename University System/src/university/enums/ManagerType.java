package university.enums;

public enum ManagerType {
    OR("OR"),
    DEPARTMENT("Department"),
    DEAN("Dean");
    private final String displayName;

    ManagerType(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}
