package university.domain.academic;

import university.domain.user.Student;
import university.enums.EnrollmentStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

/// Links a student to a course they enrolled in.
///
/// Tracks the attempt number, mark, and enrollment status through
/// the semester (PENDING → APPROVED → REGISTERED).
public class Enrollment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Student student;
    private final Course course;
    private final int attemptNo;
    private String semesterLabel;
    private EnrollmentStatus status;
    private Mark mark;

    /// Creates an enrollment for `student` in `course` with a starting
    /// status of `PENDING`. `attemptNo` starts at 1.
    public Enrollment(
            Student student,
            Course course,
            String semesterLabel,
            int attemptNo
    ) {
        this.student = student;
        this.course = course;
        this.semesterLabel = semesterLabel;
        if (attemptNo < 1) throw new IllegalArgumentException(
                "attemptNo must be >= 1, got: " + attemptNo
        );
        this.attemptNo = attemptNo;
        this.status = EnrollmentStatus.PENDING;
    }

    /// Approves the enrollment — moves from `PENDING` to `APPROVED`.
    public void approve() {
        if (status != EnrollmentStatus.PENDING) throw new IllegalStateException(
                "Can only approve a PENDING enrollment, current: " + status
        );
        status = EnrollmentStatus.APPROVED;
    }

    /// Rejects the enrollment — moves from `PENDING` to `REJECTED`.
    public void reject() {
        if (status != EnrollmentStatus.PENDING) throw new IllegalStateException(
                "Can only reject a PENDING enrollment, current: " + status
        );
        status = EnrollmentStatus.REJECTED;
    }

    /// Registers a previously approved enrollment (`APPROVED` → `REGISTERED`).
    public void register() {
        if (
                status != EnrollmentStatus.APPROVED
        ) throw new IllegalStateException(
                "Can only register an APPROVED enrollment, current: " + status
        );
        status = EnrollmentStatus.REGISTERED;
    }

    /// Returns the mark if one has been set (wrapped in `Optional`).
    public Optional<Mark> getMark() {
        return Optional.ofNullable(mark);
    }

    /// Assigns a mark — only allowed when status is `REGISTERED`.
    public void setMark(Mark mark) {
        if (
                status != EnrollmentStatus.REGISTERED
        ) throw new IllegalStateException(
                "Mark can only be set on a REGISTERED enrollment, current: " +
                        status
        );
        this.mark = mark;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public String getSemesterLabel() {
        return semesterLabel;
    }

    public void setSemesterLabel(String semesterLabel) {
        this.semesterLabel = semesterLabel;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public int getAttemptNo() {
        return attemptNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enrollment other)) return false;
        return attemptNo == other.attemptNo
                && Objects.equals(student, other.student)
                && Objects.equals(course, other.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course, attemptNo);
    }

    @Override
    public String toString() {
        return "Enrollment{student=%s, course=%s, semester='%s', attempt=%d, status=%s, mark=%s}".formatted(
                student.getName(),
                course.getCourseCode(),
                semesterLabel,
                attemptNo,
                status,
                mark != null ? mark.getTotal() : "N/A"
        );
    }
}
