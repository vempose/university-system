package university.enums;

public enum CourseCategory {
    MAJOR("Major"),
    MINOR("Minor"),
    FREE_ELECTIVE("Free Elective");
    private final String displayName;
    CourseCategory(String displayName) {
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
