package university.enums;

/// Stages a enrollment request goes through.
///
/// Tracks the lifecycle from when a student applies to a course
/// all the way to being officially registered.
public enum EnrollmentStatus {
    /// Waiting for admin approval
    PENDING,
    /// Enrollment approved — not yet registered
    APPROVED,
    /// Application denied
    REJECTED,
    /// Fully registered in the system
    REGISTERED,
}
