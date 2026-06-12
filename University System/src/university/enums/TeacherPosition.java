package university.enums;

public enum TeacherPosition {
    TUTOR("Tutor"),
    LECTOR("Lector"),
    SENIOR_LECTOR("Senior Lector"),
    PROFESSOR("Professor");

    private final String displayName;
    TeacherPosition(String displayName) {
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
