package university.domain.academic;

import university.enums.CourseCategory;

public class CourseRequirement {

    private final Course course;
    private final Major major;
    private final int yearOfStudy;
    private final CourseCategory category;

    public CourseRequirement(
        Course course,
        Major major,
        int yearOfStudy,
        CourseCategory category
    ) {
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
        this.course = course;
        this.major = major;
        this.yearOfStudy = yearOfStudy;
        this.category = category;
    }

    public Course getCourse() {
        return course;
    }

    public Major getMajor() {
        return major;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public CourseCategory getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return (
            "CourseRequirement{" +
            "course=" +
            course.getCourseCode() +
            ", major='" +
            major.getName() +
            '\'' +
            ", yearOfStudy=" +
            yearOfStudy +
            ", category=" +
            category +
            '}'
        );
    }
}
