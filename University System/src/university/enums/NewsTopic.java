package university.enums;

public enum NewsTopic {
    ACADEMIC("Academic"),
    RESEARCH("Research"),
    SOCIAL("Social");
    private final String displayName;
    NewsTopic(String displayName) {
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
