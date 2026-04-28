package university.enums;

public enum CitationFormat {
    BIBTEX("BibTeX"),
    PLAIN_TEXT("Plain Text"),
    APA("APA"),
    IEEE("IEEE");
    private final String displayName;
    CitationFormat(String displayName) {
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
