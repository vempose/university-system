package university.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.domain.academic.Course;
import university.domain.academic.Enrollment;
import university.domain.academic.Mark;
import university.domain.academic.School;
import university.domain.communication.Complaint;
import university.domain.research.ResearchProfile;
import university.domain.student.TeacherRating;
import university.enums.Language;
import university.enums.TeacherPosition;
import university.enums.UrgencyLevel;

public class Teacher extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private TeacherPosition position;
    private final List<TeacherRating> receivedRatings = new ArrayList<>();
    private final List<Complaint> submittedComplaints = new ArrayList<>();
    private final List<Course> assignedCourses = new ArrayList<>();

    public Teacher(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        double salary,
        TeacherPosition position
    ) {
        super(id, name, email, passwordHash, language, salary);
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
