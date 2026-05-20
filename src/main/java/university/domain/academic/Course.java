package university.domain.academic;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/// A university course with a code, title, credits, and a list of lessons.
///
/// Comparable by `courseCode` so you can sort them alphabetically.
public final class Course implements Comparable<Course>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String courseCode;
    private final List<Lesson> lessons = new ArrayList<>();
    private String title;
    private int credits;

    /// Creates a course. `courseCode` and `title` can't be blank,
    /// and `credits` must be positive.
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

    /// Returns a formatted syllabus string with course info and all lessons.
    public String viewSyllabus() {
        String divider = "=".repeat(48);
        String lessonSection = lessons.isEmpty()
                ? "  (no lessons scheduled)\n"
                : lessons
                  .stream()
                  .map(lesson -> "  " + lesson + "\n")
                  .collect(Collectors.joining());

        return "%s\nCourse : %s - %s\nCredits: %d\nLessons (%d):\n%s%s".formatted(
                divider,
                courseCode,
                title,
                credits,
                lessons.size(),
                lessonSection,
                divider
        );
    }

    /// Adds a `lesson` to this course's schedule.
    public void addLesson(Lesson lesson) {
        if (lesson == null) throw new IllegalArgumentException(
                "lesson must not be null"
        );
        boolean duplicateId = lessons
                .stream()
                .anyMatch(l -> l.getId().equals(lesson.getId()));
        if (!duplicateId) {
            lessons.add(lesson);
        }
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

    /// Renames the course.
    public void setTitle(String title) {
        if (
                title == null || title.isBlank()
        ) throw new IllegalArgumentException("title must not be blank");
        this.title = title;
    }

    public int getCredits() {
        return credits;
    }

    /// Changes the credit count (must stay positive).
    public void setCredits(int credits) {
        if (credits <= 0) throw new IllegalArgumentException(
                "credits must be greater than zero, got: " + credits
        );
        this.credits = credits;
    }

    @Override
    /// Sorts by `courseCode` alphabetically.
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
