package university.domain.communication;

import java.time.LocalDate;
import java.util.UUID;
import university.domain.user.Employee;
import university.domain.user.Manager;
import university.enums.RequestStatus;

public class EmployeeRequest {

    private final String id;
    private final Employee sender;
    private final String description;
    private RequestStatus status;
    private Manager signedBy;
    private final LocalDate createdDate;

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

    public void view() {
        requireStatus(RequestStatus.NEW, "view");
        this.status = RequestStatus.VIEWED;
    }

    public void accept() {
        requireStatus(RequestStatus.VIEWED, "accept");
        this.status = RequestStatus.ACCEPTED;
    }

    public void reject() {
        requireStatus(RequestStatus.VIEWED, "reject");
        this.status = RequestStatus.REJECTED;
    }

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
                "Cannot '" +
                    operation +
                    "' request in status " +
                    this.status +
                    "; expected " +
                    expected
            );
        }
    }

    @Override
    public String toString() {
        return "EmployeeRequest{id='%s', sender=%s, status=%s, signedBy=%s, createdDate=%s, description='%s'}".formatted(
                id, sender, status, (signedBy != null) ? signedBy : "unsigned", createdDate, description
        );
    }
}
