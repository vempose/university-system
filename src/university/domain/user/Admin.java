package university.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.domain.support.LogEntry;
import university.enums.Language;
import university.system.UniversitySystem;

public class Admin extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<LogEntry> viewedLogs = new ArrayList<>();

    public Admin(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        double salary
    ) {
        super(id, name, email, passwordHash, language, salary);
    }

    public void addUser(User user, UniversitySystem system) {
        system.addUser(user);
        system.addLog(new LogEntry(this, "Added user: " + user.getId()));
    }

    public void removeUser(User user, UniversitySystem system) {
        system.removeUser(user);
    }

    public void updateUser(User user, String newName, String newEmail) {
        user.setName(newName);
        user.setEmail(newEmail);
    }

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
