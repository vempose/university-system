package university.domain.academic;

import university.enums.CourseCategory;

import java.io.Serial;
import java.io.Serializable;

/// Links a `Course` to a `Major`'s curriculum for a given year.
///
/// A record that says "in year X of major Y, students must take this
/// course as a required/elective/etc."
public record CourseRequirement(Course course, Major major, int yearOfStudy,
                                CourseCategory category) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /// Compact constructor — validates nothing is null and year ≥ 1.
    public CourseRequirement {
        if (course == null) throw new IllegalArgumentException(
                "Course must not be null."
        );
        if (major == null) throw new IllegalArgumentException(
                "Major must not be null."
        );
        if (yearOfStudy < 1) throw new IllegalArgumentException(
                "Year of study must be >= 1, got: " + yearOfStudy
        );
        if (category == null) throw new IllegalArgumentException(
                "CourseCategory must not be null."
        );
    }

    @Override
    public String toString() {
        return "CourseRequirement{course=%s, major='%s', yearOfStudy=%d, category=%s}".formatted(
                course.getCourseCode(),
                major.getName(),
                yearOfStudy,
                category
        );
    }
}
