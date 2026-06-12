package university.enums;

public enum DegreeType {
    BACHELOR("Bachelor"),
    MASTER("Master"),
    PHD("PhD");
    private final String displayName;
    DegreeType(String displayName) {
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
