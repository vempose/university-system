package university.enums;

/// Categories that determine how a course counts toward a degree.
///
/// Used by `Major` and `Enrollment` to decide if a course
/// fits the student's program.
public enum CourseCategory {
    /// Core course for the student's major
    MAJOR,
    /// Secondary focus area
    MINOR,
    /// Extra course not tied to major/minor requirements
    FREE_ELECTIVE,
}
