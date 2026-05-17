package university.domain.academic;

import university.domain.user.Student;
import university.domain.user.Teacher;
import university.enums.AttendanceStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/// Tracks whether a student showed up for a specific lesson.
///
/// Stores who recorded it, when, and what the status was
/// (present, late, absent, etc.).
public class AttendanceRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Student student;
    private final Lesson lesson;
    private final LocalDateTime recordedAt;
    private final Teacher recordedBy;
    private AttendanceStatus status;

    /// Creates a record marking a `student`'s attendance for a `lesson`.
    ///
    /// @param student  the student whose attendance is logged
    /// @param lesson   the lesson they attended (or didn't)
    /// @param status   `Present`, `Absent`, `Late`, etc.
    /// @param recordedBy the teacher who took attendance
    public AttendanceRecord(
            Student student,
            Lesson lesson,
            AttendanceStatus status,
            Teacher recordedBy
    ) {
        if (student == null) throw new IllegalArgumentException(
                "student must not be null"
        );
        if (lesson == null) throw new IllegalArgumentException(
                "lesson must not be null"
        );
        if (status == null) throw new IllegalArgumentException(
                "status must not be null"
        );
        if (recordedBy == null) throw new IllegalArgumentException(
                "recordedBy must not be null"
        );
        this.student = student;
        this.lesson = lesson;
        this.status = status;
        this.recordedAt = LocalDateTime.now();
        this.recordedBy = recordedBy;
    }

    public Student getStudent() {
        return student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    /// Updates the attendance status (e.g. correcting a mistake).
    public void setStatus(AttendanceStatus status) {
        if (status == null) throw new IllegalArgumentException(
                "status must not be null"
        );
        this.status = status;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public Teacher getRecordedBy() {
        return recordedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceRecord other)) return false;
        return (
                Objects.equals(student, other.student) &&
                        Objects.equals(lesson, other.lesson)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, lesson);
    }

    @Override
    public String toString() {
        return "AttendanceRecord{student='%s', lesson='%s', status=%s, recordedAt=%s, recordedBy='%s'}".formatted(
                student.getName(),
                lesson.getId(),
                status,
                recordedAt.format(DISPLAY_FORMATTER),
                recordedBy.getName()
        );
    }
}
