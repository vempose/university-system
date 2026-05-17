package university.enums;

/// Lifecycle status for employee and student requests.
///
/// Tracks where a `Request` is in the review process
/// from submission to completion.
public enum RequestStatus {
    /// Just submitted, nobody has looked yet
    NEW,
    /// Admin has seen it but hasn't decided
    VIEWED,
    /// Approved — being processed
    ACCEPTED,
    /// Denied
    REJECTED,
    /// Fully processed and closed
    DONE,
}
