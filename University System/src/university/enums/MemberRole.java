package university.enums;

public enum MemberRole {
    MEMBER("Member"),
    HEAD("Head");

    private final String displayName;
    MemberRole(String displayName) {
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
