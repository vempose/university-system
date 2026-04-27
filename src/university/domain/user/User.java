package university.domain.user;

import java.util.Objects;
import university.domain.research.ResearchProfile;
import university.enums.Language;

public abstract class User implements Comparable<User> {

    private final String id;
    private String name;
    private String email;
    private String passwordHash;
    private Language language;
    private ResearchProfile researchProfile;

    protected User(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.passwordHash = Objects.requireNonNull(
            passwordHash,
            "passwordHash must not be null"
        );
        this.language = Objects.requireNonNull(
            language,
            "language must not be null"
        );
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.passwordHash.equals(password);
    }

    public void logout() {
        // session teardown hook — override in service layer if needed
    }

    public void changeLanguage(Language language) {
        this.language = Objects.requireNonNull(
            language,
            "language must not be null"
        );
    }

    @Override
    public int compareTo(User other) {
        return this.id.compareTo(
            Objects.requireNonNull(other, "other must not be null").id
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Objects.requireNonNull(email, "email must not be null");
    }

    // package-private — not part of the public API
    String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = Objects.requireNonNull(
            passwordHash,
            "passwordHash must not be null"
        );
    }

    public Language getLanguage() {
        return language;
    }

    public ResearchProfile getResearchProfile() {
        return researchProfile;
    }

    public void setResearchProfile(ResearchProfile researchProfile) {
        this.researchProfile = researchProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

        @Override
    public String toString() {
        return (
            getClass().getSimpleName() +
            "{id='" +
            id +
            '\'' +
            ", name='" +
            name +
            '\'' +
            '}'
        );
    }
}
