package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import university.domain.support.LogEntry;
import university.enums.Language;
import university.system.UniversitySystem;

/// System administrator with full user CRUD access.
///
/// Can add, remove, and update users, plus view system logs.
public class Admin extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<LogEntry> viewedLogs = new ArrayList<>();

    /// Creates an admin with a salary (same params as any employee).
    public Admin(
        String id,
        String name,
        String email,
        String password,
        Language language,
        double salary
    ) {
        super(id, name, email, password, language, salary);
    }

    /// Adds a user to the system and logs the action.
    public void addUser(User user, UniversitySystem system) {
        system.addUser(user);
        system.addLog(new LogEntry(this, "Added user: " + user.getId()));
    }

    /// Removes a user from the system.
    public void removeUser(User user, UniversitySystem system) {
        system.removeUser(user);
    }

    /// Updates a user's name and email in-place.
    public void updateUser(User user, String newName, String newEmail) {
        user.setName(newName);
        user.setEmail(newEmail);
    }

    /// Returns an immutable copy of the system's log entries.
    public List<LogEntry> viewLogs(UniversitySystem system) {
        return List.copyOf(system.getLogs());
    }

    public void addLog(LogEntry entry) {
        viewedLogs.add(entry);
    }

    @Override
    public String toString() {
        return "Admin{id='%s', name='%s'}".formatted(getId(), getName());
    }
}
