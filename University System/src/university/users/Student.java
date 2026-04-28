package university.users;

import university.courses.Course;
import university.courses.Enrollment;
import university.courses.Mark;
import university.courses.TeacherRating;
import university.enums.DegreeType;
import university.exceptions.CreditLimitExceededException;
import university.exceptions.MaxAttemptsReachedException;

import java.util.ArrayList;
import java.util.List;

public class Student extends User{
    private static final int MAX_CREDITS =21;
    private static final int MAX_FAILURES =3;
    private double gpa;
    private int yearOfStudy;
    private int totalCredits;
    private int failCount;
    private DegreeType degreeType;
    private List<Enrollment> enrollments;
    private List<TeacherRating> givenRatings;
    private List<OrganizationMembership> memberships;
    public Student(String name, String email, String password, int yearOfStudy, DegreeType degreeType){
        super(name, email, password);
        this.yearOfStudy = yearOfStudy;
        this.degreeType = degreeType;
        this.gpa = 0.0;
        this.totalCredits = 0;
        this.failCount = 0;
        this.enrollments = new ArrayList<>();
        this.givenRatings = new ArrayList<>();
        this.memberships = new ArrayList<>();
    }
    public Enrollment registerForCourse(Course course) throws CreditLimitExceededException, MaxAttemptsReachedException {
        if (totalCredits + course.getCredits() > MAX_CREDITS) {
            throw new CreditLimitExceededException(totalCredits, course.getCredits());
        }
        long prevFails = enrollments.stream()
                .filter(e -> e.getCourse().equals(course) && e.getMark() != null && !e.getMark().isPassed())
                .count();
        if (prevFails >= MAX_FAILURES) {
            throw new MaxAttemptsReachedException(course.getCourseCode());
        }
        Enrollment enrollment = new Enrollment(course, "Spring-2026");
        int attempt = (int) enrollments.stream().filter(e -> e.getCourse().equals(course)).count() +1;
        enrollment.setAttempt(attempt);
        enrollments.add(enrollment);
        totalCredits += course.getCredits();
        return enrollment;
    }

    public List<Course> viewCourses() {
        List<Course> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.isActive()) result.add(e.getCourse());
        }
        return result;
    }
    public List<Mark> viewMarks() {
        List<Mark> result = new ArrayList<>();
        for (Enrollment e : enrollments) {
            if (e.getMark() != null) result.add(e.getMark());
        }
        return result;
    }
    public String viewTranscript() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transcript of ").append(getName()).append("\n");
        sb.append("ID:").append(getId()).append("\n");
        sb.append("Degree: ").append(degreeType).append("\n");
        sb.append("GPA: ").append(gpa).append("\n");
        for (Enrollment e : enrollments) {
            if (e.getMark() != null) {
                sb.append(e.getCourse().getTitle()).append(" - ")
                  .append(e.getMark().getLetterGrade()).append("\n");
            }
        }
        return sb.toString();
    }

    public TeacherRating rateTeacher(Teacher teacher, double score, String comment) {
        TeacherRating rating = new TeacherRating(teacher, score, comment);
        givenRatings.add(rating);
        return rating;
    }

    public void recalculateGpa() {
        double total = 0;
        int count = 0;
        for (Enrollment e : enrollments) {
            if (e.getMark() != null) {
                total += e.getMark().getDigitGrade();
                count++;
            }
        }
        this.gpa = count > 0 ? total / count : 0.0;
    }

    public void addMembership(OrganizationMembership membership) {
        memberships.add(membership);
    }

    public List<OrganizationMembership> getMemberships() { return new ArrayList<>(memberships); }
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }
    public int getYearOfStudy() { return yearOfStudy; }
    public int getTotalCredits() { return totalCredits; }
    public int getFailCount() { return failCount; }
    public void incrementFailCount() { this.failCount++; }
    public DegreeType getDegreeType() { return degreeType; }
    public List<Enrollment> getEnrollments() { return new ArrayList<>(enrollments); }

    @Override
    public String toString() {
        return "Student{id=" + getId() + ", name=" + getName() + ", degree=" + degreeType + ", year=" + yearOfStudy + ", gpa=" + gpa + ", credits=" + totalCredits + "}";
    }
}
