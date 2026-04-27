package university.domain.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import university.domain.support.TechSupportRequest;
import university.enums.Language;
import university.enums.RequestStatus;

public class TechSupportSpecialist extends Employee {

    private final List<TechSupportRequest> assignedRequests = new ArrayList<>();

    public TechSupportSpecialist(
        String id,
        String name,
        String email,
        String passwordHash,
        Language language,
        double salary
    ) {
        super(id, name, email, passwordHash, language, salary);
    }

    public List<TechSupportRequest> viewNewRequests() {
        return assignedRequests
            .stream()
            .filter(r -> r.getStatus() == RequestStatus.NEW)
            .toList();
    }

    public void acceptRequest(TechSupportRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        request.accept(this);
    }

    public void rejectRequest(TechSupportRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        request.reject();
    }

    public void completeRequest(TechSupportRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        request.complete();
    }

    public void assignRequest(TechSupportRequest request) {
        assignedRequests.add(
            Objects.requireNonNull(request, "request must not be null")
        );
    }

    public List<TechSupportRequest> getAssignedRequests() {
        return List.copyOf(assignedRequests);
    }

        @Override
    public String toString() {
        return (
            "TechSupportSpecialist{" +
            "id='" +
            getId() +
            '\'' +
            ", name='" +
            getName() +
            '\'' +
            ", assignedTickets=" +
            assignedRequests.size() +
            '}'
        );
    }
}
