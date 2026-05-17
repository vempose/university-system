package university.domain.academic;

import university.domain.user.Student;
import university.domain.user.Teacher;
import university.enums.AttendanceStatus;
import university.enums.LessonType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/// A single lesson session within a course.
///
/// Has a room, time, instructor, and attendance records for every student.
public class Lesson implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter DISPLAY_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String id;
    private final LessonType type;
    private final Map<Student, AttendanceRecord> attendanceRecords =
            new LinkedHashMap<>();
    private String room;
    private LocalDateTime time;
    private Teacher instructor;

    /// Creates a lesson. `id` and `room` must not be blank.
    public Lesson(
            String id,
            LessonType type,
            String room,
            LocalDateTime time,
            Teacher instructor
    ) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException(
                "Lesson id must not be blank"
        );
        if (room == null || room.isBlank()) throw new IllegalArgumentException(
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

    public void setRoom(String room) {
        if (room == null || room.isBlank()) throw new IllegalArgumentException(
                "Room must not be blank"
        );
        this.room = room;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Teacher getInstructor() {
        return instructor;
    }

    public void setInstructor(Teacher instructor) {
        this.instructor = instructor;
    }

    /// Records or updates a student's attendance for this lesson.
    ///
    /// If the student already has a record the status is updated;
    /// otherwise a new `AttendanceRecord` is created.
    public void markAttendance(
            Student student,
            AttendanceStatus status,
            Teacher recordedBy
    ) {
        if (student == null) throw new IllegalArgumentException(
                "student must not be null"
        );
        if (status == null) throw new IllegalArgumentException(
                "status must not be null"
        );
        if (recordedBy == null) throw new IllegalArgumentException(
                "recordedBy must not be null"
        );
        AttendanceRecord existing = attendanceRecords.get(student);
        if (existing != null) {
            existing.setStatus(status);
        } else {
            attendanceRecords.put(
                    student,
                    new AttendanceRecord(student, this, status, recordedBy)
            );
        }
    }

    /// Looks up one student's attendance record (if they have one).
    public Optional<AttendanceRecord> getAttendanceRecord(Student student) {
        return Optional.ofNullable(attendanceRecords.get(student));
    }

    /// Returns an unmodifiable view of all attendance records.
    public Map<Student, AttendanceRecord> getAttendanceRecords() {
        return Collections.unmodifiableMap(attendanceRecords);
    }

    /// Returns all students whose attendance matches a given `status`.
    public List<Student> getStudentsByStatus(AttendanceStatus status) {
        return attendanceRecords
                .values()
                .stream()
                .filter(r -> r.getStatus() == status)
                .map(AttendanceRecord::getStudent)
                .toList();
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
                id,
                type,
                room,
                time.format(DISPLAY_FORMATTER),
                instructor.getName()
        );
    }
}
