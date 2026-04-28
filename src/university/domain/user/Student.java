package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import university.domain.academic.Course;
import university.domain.academic.Enrollment;
import university.domain.academic.Major;
import university.domain.academic.Mark;
import university.domain.academic.School;
import university.domain.student.TeacherRating;
import university.enums.DegreeType;
import university.enums.Language;
import university.exception.CreditLimitExceededException;
import university.exception.RetakeLimitExceededException;

public class Student extends User {

    public static final int MAX_CREDITS = 21;
    public static final int MAX_ATTEMPTS = 3;

    private double gpa;
    private int yearOfStudy;
    private int totalCredits;
    private int failCount;
    private DegreeType degreeType;
    private Major major;
    private School school;
    private final List<Enrollment> enrollments = new ArrayList<>();
    private final List<TeacherRating> givenRatings = new ArrayList<>();

    public Student(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        DegreeType degreeType,
        Major major
    ) {
        super(id, name, email, passwordHash, language);
        this.degreeType = degreeType;
        this.major = major;
    }

    public List<Course> viewCourses() {
        return enrollments
            .stream()
            .map(Enrollment::getCourse)
            .distinct()
            .toList();
    }

    public Enrollment registerForCourse(Course course)
        throws CreditLimitExceededException, RetakeLimitExceededException {
            throw new CreditLimitExceededException(
                "Registering for \"" +
                    course.getTitle() +
                    "\" would exceed the " +
                    MAX_CREDITS +
                    "-credit limit. Current enrolled credits: " +
                    totalCredits +
                    ", course credits: " +
                    course.getCredits() +
                    "."
            );
        }

        long previousAttempts = enrollments
            .stream()
            .filter(e -> e.getCourse().equals(course))
            .count();

        if (previousAttempts >= MAX_ATTEMPTS) {
            throw new RetakeLimitExceededException(
                "Student '" +
                    getId() +
                    "' has already attempted course \"" +
                    course.getTitle() +
                    "\" " +
                    previousAttempts +
                    " time(s). " +
                    "Maximum allowed is " +
                    MAX_ATTEMPTS +
                    "."
            );
        }

        int attemptNo = (int) previousAttempts + 1;
        var enrollment = new Enrollment(
            this,
            course,
            "current-semester",
            attemptNo
        );
        enrollments.add(enrollment);
        totalCredits += course.getCredits();
        return enrollment;
    }

    public List<Mark> viewMarks() {
        return enrollments
            .stream()
            .map(Enrollment::getMark)
            .flatMap(Optional::stream)
            .toList();
    }

    public String viewTranscript() {
        return getTranscript();
    }

    public String getTranscript() {
        var sb = new StringBuilder();

        sb.append(
            "╔══════════════════════════════════════════════════════════════════════╗\n"
        );
        sb.append(
            "║                       ACADEMIC TRANSCRIPT                           ║\n"
        );
        sb.append(
            "╚══════════════════════════════════════════════════════════════════════╝\n"
        );
        sb.append(
            String.format("Student  : %-30s  (ID: %s)%n", getName(), getId())
        );
        sb.append(
            String.format(
                "Degree   : %-15s  Year of Study: %d%n",
                degreeType,
                yearOfStudy
            )
        );
        if (major != null) {
            sb.append(String.format("Major    : %s%n", major));
        }
        if (school != null) {
            sb.append(String.format("School   : %s%n", school));
        }
        sb.append("─".repeat(72)).append("\n");
        sb.append(
            String.format(
                "%-35s  %-18s  %s  %s%n",
                "Course",
                "Semester",
                "Att",
                "Result"
            )
        );
        sb.append("─".repeat(72)).append("\n");

        for (var enrollment : enrollments) {
            var course = enrollment.getCourse();
            var markText = enrollment
                .getMark()
                .<String>map(m ->
                    String.format(
                        "%5.1f  %-11s",
                        m.getTotal(),
                        m.isPassed() ? "PASSED" : "FAILED"
                    )
                )
                .orElse("  N/A  IN PROGRESS");

            sb.append(
                String.format(
                    "%-35s  %-18s   %d   %s%n",
                    course.getTitle(),
                    enrollment.getSemesterLabel(),
                    enrollment.getAttemptNo(),
                    markText
                )
            );
        }

        sb.append("─".repeat(72)).append("\n");
        sb.append(String.format("Cumulative GPA   : %.2f%n", gpa));
        sb.append(String.format("Total Enrollments: %d%n", enrollments.size()));
        sb.append(String.format("Fail Count       : %d%n", failCount));

        return sb.toString();
    }

    public TeacherRating rateTeacher(
        Teacher teacher,
        int score,
        String comment
    ) {
            throw new IllegalArgumentException(
                "Rating score must be between 1 and 5, got: " + score
            );
        }
        var rating = new TeacherRating(this, teacher, score, comment);
        givenRatings.add(rating);
        teacher.addRating(rating);
        return rating;
    }

    // package-private — for loading persisted data without going through registerForCourse
    void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
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

    // bypasses the credit-limit check enforced by registerForCourse — use with care
    public void setTotalCredits(int totalCredits) {
        if (totalCredits < 0) {
            throw new IllegalArgumentException(
                "totalCredits must be non-negative, got: " + totalCredits
            );
        }
        this.totalCredits = totalCredits;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        if (failCount < 0) {
            throw new IllegalArgumentException(
                "failCount must be non-negative, got: " + failCount
            );
        }
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
}
