package university.tui;

import university.domain.user.User;
import university.system.UniversitySystem;

/// Holds the currently authenticated user and a reference
/// to the university system for the duration of a session.
public final class Session {

    private User currentUser;
    private final UniversitySystem system;

    /// Creates a new session tied to the given system.
    public Session(UniversitySystem system) {
        this.system = system;
    }

    /// Returns the logged-in user, or null if not authenticated.
    public User getCurrentUser() {
        return currentUser;
    }

    /// Sets the current user after a successful login.
    void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /// Returns the university system reference.
    public UniversitySystem getSystem() {
        return system;
    }

    /// Returns true if a user is currently logged in.
    public boolean isAuthenticated() {
        return currentUser != null;
    }

    /// Logs the user out and clears the session.
    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }
}
