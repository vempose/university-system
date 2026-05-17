package university.enums;

/// Available attendance statuses for tracking lesson participation.
///
/// Used by `AttendanceRecord` to mark whether a student showed up,
/// was missing, or had a valid reason not to.
public enum AttendanceStatus {
    /// Student was there
    PRESENT,
    /// Student didn't show
    ABSENT,
    /// Absence was approved (medical, family, etc.)
    EXCUSED,
}
