package university.enums;

public enum LessonType {
    LECTURE("Lecture"),
    PRACTICE("Practice");

    private final String displayName;
    LessonType(String displayName) {
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
