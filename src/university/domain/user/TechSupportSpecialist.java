package university.domain.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import university.domain.support.TechSupportRequest;
import university.enums.Language;
import university.enums.RequestStatus;

public class TechSupportSpecialist extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

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
        request.view();
        request.accept(this);
    }

    public void rejectRequest(TechSupportRequest request) {
        request.view();
        request.reject();
    }

    public void completeRequest(TechSupportRequest request) {
        request.complete();
    }

    public void assignRequest(TechSupportRequest request) {
        assignedRequests.add(request);
    }

    public List<TechSupportRequest> getAssignedRequests() {
        return List.copyOf(assignedRequests);
    }

    @Override
    public String toString() {
        return "TechSupportSpecialist{id='%s', name='%s'}".formatted(
            getId(),
            getName()
        );
    }
}
