package university.domain.user;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import university.domain.academic.*;
import university.domain.communication.Complaint;
import university.domain.research.ResearchProfile;
import university.domain.student.TeacherRating;
import university.enums.AttendanceStatus;
import university.enums.Language;
import university.enums.TeacherPosition;
import university.enums.UrgencyLevel;

public class Teacher extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<TeacherRating> receivedRatings = new ArrayList<>();
    private final List<Complaint> submittedComplaints = new ArrayList<>();
    private final List<Course> assignedCourses = new ArrayList<>();
    private TeacherPosition position;

    public Teacher(
        String id,
        String name,
        String email,
        String password,
        Language language,
        double salary,
        TeacherPosition position
    ) {
        super(id, name, email, password, language, salary);
        this.position = position;
    }

    public List<Course> viewCourses() {
        return List.copyOf(assignedCourses);
    }

    public void addAssignedCourse(Course course) {
        if (!assignedCourses.contains(course)) {
            assignedCourses.add(course);
        }
    }

    public void manageCourse(Course course) {}

    public void markAttendance(
        Lesson lesson,
        Student student,
        AttendanceStatus status
    ) {
        lesson.markAttendance(student, status, this);
    }

    public void markAttendanceBulk(
        Lesson lesson,
        Map<Student, AttendanceStatus> attendanceMap
    ) {
        attendanceMap.forEach((student, status) ->
            lesson.markAttendance(student, status, this)
        );
    }

    public List<AttendanceRecord> viewAttendanceForLesson(Lesson lesson) {
        return List.copyOf(lesson.getAttendanceRecords().values());
    }

    public String getAttendanceSummary(Course course, List<Student> students) {
        List<Lesson> lessons = course.getLessons();
        String divider = "-".repeat(68);
        StringBuilder sb = new StringBuilder();
        sb.append(
            "Attendance Summary ─ %s (%s)\n".formatted(
                course.getTitle(),
                course.getCourseCode()
            )
        );
        sb.append(divider).append("\n");
        sb.append(
            "  %-24s %8s %8s %8s %8s\n".formatted(
                "Student",
                "Present",
                "Excused",
                "Absent",
                "Total"
            )
        );
        sb.append(divider).append("\n");

        for (Student student : students) {
            long present = lessons
                .stream()
                .flatMap(l -> l.getAttendanceRecord(student).stream())
                .filter(r -> r.getStatus() == AttendanceStatus.PRESENT)
                .count();
            long excused = lessons
                .stream()
                .flatMap(l -> l.getAttendanceRecord(student).stream())
                .filter(r -> r.getStatus() == AttendanceStatus.EXCUSED)
                .count();
            long absent = lessons
                .stream()
                .flatMap(l -> l.getAttendanceRecord(student).stream())
                .filter(r -> r.getStatus() == AttendanceStatus.ABSENT)
                .count();
            long recorded = present + excused + absent;

            sb.append(
                "  %-24s %8d %8d %8d %8d\n".formatted(
                    student.getName(),
                    present,
                    excused,
                    absent,
                    recorded
                )
            );
        }
        sb.append(divider);
        return sb.toString();
    }

    public void putMark(Enrollment enrollment, Mark mark) {
        enrollment.setMark(mark);
    }

    public List<Student> viewStudents(
        Course course,
        List<Enrollment> enrollments
    ) {
        return enrollments
            .stream()
            .filter(e -> e.getCourse().equals(course))
            .map(Enrollment::getStudent)
            .toList();
    }

    public Complaint sendComplaint(
        List<Student> targets,
        UrgencyLevel urgency,
        String text,
        Manager receiver
    ) {
        Complaint complaint = new Complaint(
            this,
            targets,
            urgency,
            text,
            receiver
        );
        submittedComplaints.add(complaint);
        return complaint;
    }

    public void addRating(TeacherRating rating) {
        receivedRatings.add(rating);
    }

    public double getAverageRating() {
        if (receivedRatings.isEmpty()) return 0.0;
        return receivedRatings
            .stream()
            .mapToInt(TeacherRating::getScore)
            .average()
            .orElse(0.0);
    }

    public TeacherPosition getPosition() {
        return position;
    }

    public void setPosition(TeacherPosition position) {
        this.position = position;
        if (
            position == TeacherPosition.PROFESSOR &&
            getResearchProfile() == null
        ) {
            setResearchProfile(new ResearchProfile());
        }
    }

    public List<TeacherRating> getReceivedRatings() {
        return List.copyOf(receivedRatings);
    }

    public List<Complaint> getSubmittedComplaints() {
        return List.copyOf(submittedComplaints);
    }

    @Override
    public String toString() {
        return "Teacher{id='%s', name='%s'}".formatted(getId(), getName());
    }
}
