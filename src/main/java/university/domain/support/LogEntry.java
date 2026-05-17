package university.domain.support;

import university.domain.user.User;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/// An entry in the system activity log.
///
/// Records who did what and when.
public final class LogEntry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final User actor;
    private final String action;
    private final LocalDateTime timestamp;

    /// Creates a log entry for an action performed by a user.
    ///
    /// @param actor the user who performed the action (must not be null)
    /// @param action description of what was done (must not be blank)
    public LogEntry(User actor, String action) {
        if (actor == null) {
            throw new IllegalArgumentException(
                    "LogEntry actor must not be null."
            );
        }
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException(
                    "LogEntry action must not be null or blank."
            );
        }
        this.id = UUID.randomUUID().toString();
        this.actor = actor;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public User getActor() {
        return actor;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /// Two entries are equal if they have the same ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /// Returns a detailed string of the entry.
    @Override
    public String toString() {
        return "LogEntry[id='%s', timestamp=%s, actor=%s(id=%s), action='%s']".formatted(
                id,
                timestamp,
                actor.getClass().getSimpleName(),
                actor.getId(),
                action
        );
    }
}
