package university.domain.support;

import university.domain.user.TechSupportSpecialist;
import university.domain.user.User;
import university.enums.RequestStatus;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public final class TechSupportRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String description;
    private final User requester;
    private final LocalDateTime createdDate;
    private RequestStatus status;
    private TechSupportSpecialist assignedSpecialist;

    public TechSupportRequest(User requester, String description) {
        if (requester == null) {
            throw new IllegalArgumentException("Requester must not be null.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Description must not be null or blank."
            );
        }
        this.id = UUID.randomUUID().toString();
        this.requester = requester;
        this.description = description;
        this.status = RequestStatus.NEW;
        this.assignedSpecialist = null;
        this.createdDate = LocalDateTime.now();
    }

    public void view() {
        requireStatus(RequestStatus.NEW, "view");
        this.status = RequestStatus.VIEWED;
    }

    public void accept(TechSupportSpecialist specialist) {
        if (specialist == null) {
            throw new IllegalArgumentException(
                    "Assigned specialist must not be null."
            );
        }
        requireStatus(RequestStatus.VIEWED, "accept");
        this.assignedSpecialist = specialist;
        this.status = RequestStatus.ACCEPTED;
    }

    public void reject() {
        requireStatus(RequestStatus.VIEWED, "reject");
        this.status = RequestStatus.REJECTED;
    }

    public void complete() {
        requireStatus(RequestStatus.ACCEPTED, "complete");
        this.status = RequestStatus.DONE;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public User getRequester() {
        return requester;
    }

    public TechSupportSpecialist getAssignedSpecialist() {
        return assignedSpecialist;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    private void requireStatus(RequestStatus required, String operation) {
        if (this.status != required) {
            throw new IllegalStateException(
                    "Cannot %s ticket '%s': expected status %s but was %s.".formatted(
                            operation,
                            id,
                            required,
                            status
                    )
            );
        }
    }

    @Override
    public String toString() {
        return "TechSupportRequest{id='%s', status=%s, requester='%s', assignedSpecialist='%s', createdDate=%s, description='%s'}".formatted(
                id,
                status,
                requester.getName(),
                assignedSpecialist != null
                        ? assignedSpecialist.getName()
                        : "unassigned",
                createdDate,
                description
        );
    }
}
