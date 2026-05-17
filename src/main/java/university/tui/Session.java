package university.tui;

import university.domain.user.User;
import university.system.UniversitySystem;

public final class Session {

    private User currentUser;
    private final UniversitySystem system;

    public Session(UniversitySystem system) {
        this.system = system;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public UniversitySystem getSystem() {
        return system;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }

    public void logout() {
        if (currentUser != null) {
            currentUser.logout();
            currentUser = null;
        }
    }
}
