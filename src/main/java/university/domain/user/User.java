package university.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.domain.research.ResearchProfile;
import university.enums.Language;

/// Base class for everyone in the system.
///
/// Stores the basics — name, email, hashed password, and a unique ID.
/// Two users with the same ID are considered equal.
public abstract class User implements Comparable<User>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String email;
    private String password;
    private Language language;
    private ResearchProfile researchProfile;
    private final List<String> pendingNotifications = new ArrayList<>();

    /// Creates a user with all the required fields.
    ///
    /// @param id  unique identifier (used for equality & comparison)
    protected User(
        String id,
        String name,
        String email,
        String password,
        Language language
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.language = language;
    }

    /// Checks if the given email + password match what's stored.
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    /// Logs the user out — no-op for now but here for future use.
    public void logout() {}

    /// Switches the system language for this user.
    public void changeLanguage(Language language) {
        this.language = language;
    }

    @Override
    public int compareTo(User other) {
        return this.id.compareTo(other.id);
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
        return "%s{id='%s', name='%s'}".formatted(
            getClass().getSimpleName(),
            id,
            name
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public ResearchProfile getResearchProfile() {
        return researchProfile;
    }

    public void setResearchProfile(ResearchProfile researchProfile) {
        this.researchProfile = researchProfile;
    }

    public void addNotification(String notification) {
        pendingNotifications.add(notification);
    }

    public List<String> getAndClearNotifications() {
        var copy = List.copyOf(pendingNotifications);
        pendingNotifications.clear();
        return copy;
    }
}
