package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.academic.Course;
import university.domain.academic.Enrollment;
import university.domain.academic.Mark;
import university.domain.communication.Complaint;
import university.domain.student.TeacherRating;
import university.enums.Language;
import university.enums.TeacherPosition;
import university.enums.UrgencyLevel;

public class Teacher extends Employee {

    private TeacherPosition position;
    private final List<TeacherRating> receivedRatings = new ArrayList<>();
    private final List<Complaint> submittedComplaints = new ArrayList<>();

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
        // Full implementation requires system-level access — delegated to service layer
        return List.of();
    }

    public void manageCourse(Course course) {
        // management logic delegated to service layer
    }

    public void putMark(Enrollment enrollment, Mark mark) {
        enrollment.setMark(mark);
    }

    public List<Student> viewStudents(Course course) {
        // Full implementation requires system-level access — delegated to service layer
        return List.of();
    }

    public Complaint sendComplaint(
        List<Student> targets,
        UrgencyLevel urgency,
        String text,
        Manager receiver
    ) {
        Complaint complaint = new Complaint(this, targets, urgency, text, receiver);
        submittedComplaints.add(complaint);
        return complaint;
    }

    public void addRating(TeacherRating rating) {
        receivedRatings.add(rating);
    }

    public double getAverageRating() {
        if (receivedRatings.isEmpty()) {
            return 0.0;
        }
        return receivedRatings
            .stream()
            .mapToInt(TeacherRating::getScore)
            .average()
            .orElse(0.0);
    }

    public TeacherPosition getPosition() {
        return position;
    }

    // promoting to PROFESSOR requires a ResearchProfile — enforced by service layer
    public void setPosition(TeacherPosition position) {
        this.position = position;
    }

    public List<TeacherRating> getReceivedRatings() {
        return List.copyOf(receivedRatings);
    }

    public List<Complaint> getSubmittedComplaints() {
        return List.copyOf(submittedComplaints);
    }

        @Override
    public String toString() {
        return "Teacher{id='%s', name='%s', position=%s, avgRating=%.2f, ratings=%d, complaints=%d}".formatted(
                getId(), getName(), position, getAverageRating(), receivedRatings.size(), submittedComplaints.size()
        );
    }
}
