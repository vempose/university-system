package university.domain.user;

import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.academic.*;
import university.domain.student.OrganizationMembership;
import university.domain.student.TeacherRating;
import university.enums.DegreeType;
import university.enums.Language;
import university.exception.CreditLimitExceededException;
import university.exception.RetakeLimitExceededException;

/// A student enrolled in the university.
///
/// Tracks GPA, major, year of study, courses they're taking,
/// teacher ratings, and club memberships.
public class Student extends User {

    public static final int MAX_CREDITS = 21;
    public static final int MAX_ATTEMPTS = 3;

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Enrollment> enrollments = new ArrayList<>();
    private final List<TeacherRating> givenRatings = new ArrayList<>();
    private final List<OrganizationMembership> memberships = new ArrayList<>();
    private double gpa;
    private int yearOfStudy;
    private int totalCredits;
    private int failCount;
    private DegreeType degreeType;
    private Major major;
    private School school;

    /// Creates a student with a degree type and major.
    ///
    /// Starts with 0 credits, GPA 0.0, and no enrollments.
    public Student(
        String id,
        String name,
        String email,
        String password,
        Language language,
        DegreeType degreeType,
        Major major
    ) {
        super(id, name, email, password, language);
        this.degreeType = degreeType;
        this.major = major;
    }

    /// Registers the student for a course.
    ///
    /// Throws if the credit limit (21) or retry limit (3) is exceeded.
    public Enrollment registerForCourse(Course course)
        throws CreditLimitExceededException, RetakeLimitExceededException {
        if (totalCredits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException(
                "Credit limit exceeded: cannot register for " +
                    course.getCourseCode()
            );
        }
        long previousAttempts = enrollments
            .stream()
            .filter(e -> e.getCourse().equals(course))
            .count();
        if (previousAttempts >= MAX_ATTEMPTS) {
            throw new RetakeLimitExceededException(
                "Retake limit exceeded for course: " + course.getCourseCode()
            );
        }
        int attemptNo = (int) previousAttempts + 1;
        Enrollment enrollment = new Enrollment(
            this,
            course,
            "current-semester",
            attemptNo
        );
        enrollments.add(enrollment);
        totalCredits += course.getCredits();
        return enrollment;
    }

    /// Rates a teacher from 1-5 with an optional comment.
    public TeacherRating rateTeacher(
        Teacher teacher,
        int score,
        String comment
    ) {
        if (score < 1 || score > 5) throw new IllegalArgumentException(
            "Score must be between 1 and 5, got: " + score
        );
        TeacherRating rating = new TeacherRating(this, teacher, score, comment);
        givenRatings.add(rating);
        teacher.addRating(rating);
        return rating;
    }

    public List<Course> viewCourses() {
        return enrollments
            .stream()
            .map(Enrollment::getCourse)
            .distinct()
            .toList();
    }

    public String viewMyAttendance(List<Lesson> lessons) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String divider = "-".repeat(60);
        StringBuilder sb = new StringBuilder();
        sb.append("Attendance record for %s\n".formatted(getName()));
        sb.append(divider).append("\n");

        for (Lesson lesson : lessons) {
            String statusLabel = lesson
                .getAttendanceRecord(this)
                .map(r -> r.getStatus().name())
                .orElse("NOT RECORDED");
            sb.append(
                "  [%s] %-8s %s  → %s\n".formatted(
                    lesson.getId(),
                    lesson.getType(),
                    lesson.getTime().format(fmt),
                    statusLabel
                )
            );
        }
        sb.append(divider);
        return sb.toString();
    }

    public List<Mark> viewMarks() {
        return enrollments
            .stream()
            .map(e -> e.getMark().orElse(null))
            .filter(Objects::nonNull)
            .toList();
    }

    /// Returns a formatted transcript showing all enrollments with marks.
    public String getTranscript() {
        String divider = "-".repeat(50);
        List<String> lines = new ArrayList<>();
        for (Enrollment e : enrollments) {
            lines.add(
                "%s | %s | Attempt: %s | Status: %s | Mark: %s".formatted(
                    e.getCourse().getCourseCode(),
                    e.getCourse().getTitle(),
                    e.getAttemptNo(),
                    e.getStatus(),
                    e
                        .getMark()
                        .map(m -> String.valueOf(m.getTotal()))
                        .orElse("N/A")
                )
            );
        }
        String enrollmentSection = lines.isEmpty()
            ? ""
            : String.join("\n", lines) + "\n";

        return "Transcript for %s (%s)\nDegree: %s, Major: %s\nGPA: %s, Total Credits: %d\n%s\n%s%s".formatted(
            getName(),
            getId(),
            degreeType,
            major.getName(),
            gpa,
            totalCredits,
            divider,
            enrollmentSection,
            divider
        );
    }

    void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void addMembership(OrganizationMembership membership) {
        memberships.add(membership);
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Enrollment> getEnrollments() {
        return List.copyOf(enrollments);
    }

    public List<TeacherRating> getGivenRatings() {
        return List.copyOf(givenRatings);
    }

    public List<OrganizationMembership> getMemberships() {
        return List.copyOf(memberships);
    }

    @Override
    public String toString() {
        return "Student{id='%s', name='%s'}".formatted(getId(), getName());
    }
}
