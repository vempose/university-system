package university.domain.academic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import university.domain.user.Teacher;
import university.enums.LessonType;

public class Lesson {

    private static final DateTimeFormatter DISPLAY_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String id;
    private final LessonType type;
    private String room;
    private LocalDateTime time;
    private Teacher instructor;

    public Lesson(
        String id,
        LessonType type,
        String room,
        LocalDateTime time,
        Teacher instructor
    ) {
        Objects.requireNonNull(id, "Lesson id must not be null");
        Objects.requireNonNull(type, "LessonType must not be null");
        Objects.requireNonNull(room, "Room must not be null");
        Objects.requireNonNull(time, "Time must not be null");
        Objects.requireNonNull(instructor, "Instructor must not be null");

        if (id.isBlank()) throw new IllegalArgumentException(
            "Lesson id must not be blank"
        );
        if (room.isBlank()) throw new IllegalArgumentException(
            "Room must not be blank"
        );

        this.id = id;
        this.type = type;
        this.room = room;
        this.time = time;
        this.instructor = instructor;
    }

    public String getId() {
        return id;
    }

    public LessonType getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Teacher getInstructor() {
        return instructor;
    }

    public void setRoom(String room) {
        Objects.requireNonNull(room, "Room must not be null");
        if (room.isBlank()) throw new IllegalArgumentException(
            "Room must not be blank"
        );
        this.room = room;
    }

    public void setTime(LocalDateTime time) {
        this.time = Objects.requireNonNull(time, "Time must not be null");
    }

    public void setInstructor(Teacher instructor) {
        this.instructor = Objects.requireNonNull(
            instructor,
            "Instructor must not be null"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lesson[id=%s, type=%s, room=%s, time=%s, instructor=%s]".formatted(
                id, type, room, time.format(DISPLAY_FORMATTER), instructor.getName()
        );
    }
}
