package university.enums;

public enum Language {
    EN("English"),
    KZ("Kazakh"),
    RU("Russian");
    private final String displayName;
    Language(String displayName) {
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
