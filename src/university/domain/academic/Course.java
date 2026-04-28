package university.domain.academic;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Course implements Comparable<Course>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String courseCode;
    private String title;
    private int credits;
    private final List<Lesson> lessons = new ArrayList<>();

    public Course(String courseCode, String title, int credits) {
        if (
            courseCode == null || courseCode.isBlank()
        ) throw new IllegalArgumentException("courseCode must not be blank");
        if (
            title == null || title.isBlank()
        ) throw new IllegalArgumentException("title must not be blank");
        if (credits <= 0) throw new IllegalArgumentException(
            "credits must be greater than zero, got: " + credits
        );
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
    }

    public String viewSyllabus() {
        StringBuilder sb = new StringBuilder();
        String divider = "=".repeat(48);

        sb.append(divider).append('\n');
        sb.append(String.format("Course : %s — %s%n", courseCode, title));
        sb.append(String.format("Credits: %d%n", credits));
        sb.append(String.format("Lessons (%d):%n", lessons.size()));

        if (lessons.isEmpty()) {
            sb.append("  (no lessons scheduled)\n");
        } else {
            lessons.forEach(lesson ->
                sb.append("  ").append(lesson).append('\n')
            );
        }

        sb.append(divider);
        return sb.toString();
    }

    public void addLesson(Lesson lesson) {
        if (lesson == null) throw new IllegalArgumentException(
            "lesson must not be null"
        );
        lessons.add(lesson);
    }

    public List<Lesson> getLessons() {
        return List.copyOf(lessons);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (
            title == null || title.isBlank()
        ) throw new IllegalArgumentException("title must not be blank");
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (credits <= 0) throw new IllegalArgumentException(
            "credits must be greater than zero, got: " + credits
        );
        this.credits = credits;
    }

    @Override
    public int compareTo(Course other) {
        return this.courseCode.compareTo(other.courseCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course other)) return false;
        return Objects.equals(courseCode, other.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(courseCode);
    }

    @Override
    public String toString() {
        return String.format(
            "Course{courseCode='%s', title='%s', credits=%d, lessons=%d}",
            courseCode,
            title,
            credits,
            lessons.size()
        );
    }
}
