package university.enums;

/// How urgent a request or ticket is.
///
/// Used in `TechSupportRequest` and `EmployeeRequest`
/// to prioritize what gets handled first.
public enum UrgencyLevel {
    /// Can wait — no pressure
    LOW,
    /// Should be handled reasonably soon
    MEDIUM,
    /// Needs immediate attention
    HIGH,
}
