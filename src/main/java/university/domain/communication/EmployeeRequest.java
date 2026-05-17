package university.domain.communication;

import university.domain.user.Employee;
import university.domain.user.Manager;
import university.enums.RequestStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/// A request from an employee to their manager.
///
/// Goes through a lifecycle: NEW → VIEWED → ACCEPTED/REJECTED → DONE.
/// A manager can sign off on it along the way.
public class EmployeeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final Employee sender;
    private final String description;
    private final LocalDate createdDate;
    private RequestStatus status;
    private Manager signedBy;

    /// Creates a new request. Starts with status `NEW` and no signatory.
    public EmployeeRequest(Employee sender, String description) {
        if (sender == null) throw new IllegalArgumentException(
                "sender must not be null"
        );
        if (description == null) throw new IllegalArgumentException(
                "description must not be null"
        );

        this.id = UUID.randomUUID().toString();
        this.sender = sender;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.signedBy = null;
        this.createdDate = LocalDate.now();
    }

    /// Marks the request as viewed (`NEW` → `VIEWED`).
    public void view() {
        requireStatus(RequestStatus.NEW, "view");
        this.status = RequestStatus.VIEWED;
    }

    /// Accepts the request (`VIEWED` → `ACCEPTED`).
    public void accept() {
        requireStatus(RequestStatus.VIEWED, "accept");
        this.status = RequestStatus.ACCEPTED;
    }

    /// Rejects the request (`VIEWED` → `REJECTED`).
    public void reject() {
        requireStatus(RequestStatus.VIEWED, "reject");
        this.status = RequestStatus.REJECTED;
    }

    /// Marks an accepted request as done (`ACCEPTED` → `DONE`).
    public void done() {
        requireStatus(RequestStatus.ACCEPTED, "done");
        this.status = RequestStatus.DONE;
    }

    /// Signs the request with a manager's approval.
    ///
    /// Can be called from `VIEWED` or `ACCEPTED` status.
    public void sign(Manager manager) {
        if (manager == null) throw new IllegalArgumentException(
                "signing manager must not be null"
        );
        if (
                status != RequestStatus.VIEWED && status != RequestStatus.ACCEPTED
        ) {
            throw new IllegalStateException(
                    "Cannot sign request in status " +
                            status +
                            "; must be VIEWED or ACCEPTED first"
            );
        }
        this.signedBy = manager;
        if (this.status != RequestStatus.ACCEPTED) this.status =
                RequestStatus.ACCEPTED;
    }

    public String getId() {
        return id;
    }

    public Employee getSender() {
        return sender;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public Manager getSignedBy() {
        return signedBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    private void requireStatus(RequestStatus expected, String operation) {
        if (this.status != expected) {
            throw new IllegalStateException(
                    "Cannot '%s' request in status %s; expected %s"
                            .formatted(operation, this.status, expected)
            );
        }
    }

    @Override
    public String toString() {
        return "EmployeeRequest{id='%s', sender=%s, status=%s, signedBy=%s, createdDate=%s, description='%s'}".formatted(
                id,
                sender,
                status,
                (signedBy != null) ? signedBy : "unsigned",
                createdDate,
                description
        );
    }
}
