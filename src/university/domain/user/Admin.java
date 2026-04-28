package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.support.LogEntry;
import university.enums.Language;

public class Admin extends Employee {

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

    public void addUser(User user) {
        // actual storage is delegated to UniversitySystem in the service layer
    }

    public void removeUser(User user) {
        // actual removal is delegated to UniversitySystem in the service layer
    }

    public void updateUser(User user) {
        // actual update is delegated to UniversitySystem in the service layer
    }

    public List<LogEntry> viewLogs() {
        return List.copyOf(viewedLogs);
    }

    public void addLog(LogEntry entry) {
        viewedLogs.add(entry);
    }

        @Override
    public String toString() {
        return "Admin{id='%s', name='%s', logs=%d}".formatted(getId(), getName(), viewedLogs.size());
    }
}
