package university.enums;

/// Levels of management in the university hierarchy.
///
/// Determines what a `Manager` can do — OR handles
/// organizational requests, department leads manage
/// their unit, deans oversee entire schools.
public enum ManagerType {
    /// Organizational requests (OR — student orgs, clubs)
    OR,
    /// Department-level manager
    DEPARTMENT,
    /// Dean of a school
    DEAN,
}
