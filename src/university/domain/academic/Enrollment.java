package university.domain.academic;

import java.util.Objects;
import java.util.Optional;
import university.domain.user.Student;
import university.enums.EnrollmentStatus;

public class Enrollment {

    private final Student student;
    private final Course course;
    private String semesterLabel;
    private EnrollmentStatus status;
    private final int attemptNo;
    private Mark mark;

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

    // State machine: PENDING → APPROVED → REGISTERED, or PENDING → REJECTED
    public void approve() {
        if (status != EnrollmentStatus.PENDING) throw new IllegalStateException(
            "Can only approve a PENDING enrollment, current: " + status
        );
        status = EnrollmentStatus.APPROVED;
    }

    public void reject() {
        if (status != EnrollmentStatus.PENDING) throw new IllegalStateException(
            "Can only reject a PENDING enrollment, current: " + status
        );
        status = EnrollmentStatus.REJECTED;
    }

    public void register() {
        if (
            status != EnrollmentStatus.APPROVED
        ) throw new IllegalStateException(
            "Can only register an APPROVED enrollment, current: " + status
        );
        status = EnrollmentStatus.REGISTERED;
    }

    public Optional<Mark> getMark() {
        return Optional.ofNullable(mark);
    }

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
        return (
            attemptNo == other.attemptNo &&
            Objects.equals(student, other.student) &&
            Objects.equals(course, other.course) &&
            Objects.equals(semesterLabel, other.semesterLabel)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, course, semesterLabel, attemptNo);
    }

    @Override
    public String toString() {
        return "Enrollment{student=%s, course=%s, semester='%s', attempt=%d, status=%s, mark=%s}".formatted(
                student.getName(), course.getCourseCode(), semesterLabel, attemptNo, status, mark != null ? mark.getTotal() : "N/A"
        );
    }
}
