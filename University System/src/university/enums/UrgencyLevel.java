package university.enums;

public enum UrgencyLevel {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");
    private final String displayName;
    UrgencyLevel(String displayName) {
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
