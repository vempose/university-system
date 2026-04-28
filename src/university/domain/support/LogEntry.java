package university.domain.support;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import university.domain.user.User;

public final class LogEntry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final User actor;
    private final String action;
    private final LocalDateTime timestamp;

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
