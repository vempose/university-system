package university.enums;

/// Formats a lesson can take in the schedule.
///
/// Helps `Lesson` distinguish between theory-heavy lectures
/// and hands-on practice sessions.
public enum LessonType {
    /// Theory delivered to a large group
    LECTURE,
    /// Smaller group working on exercises
    PRACTICE,
}
